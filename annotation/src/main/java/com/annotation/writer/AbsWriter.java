package com.annotation.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

/**
 * Created by renxl
 * On 2017/7/22 17:23.
 */

public abstract class AbsWriter implements AdapterWriter {

    ProcessingEnvironment mProcessingEnvironment;
    Filer mFiler;

    public AbsWriter(ProcessingEnvironment processingEnvironment) {
        this.mProcessingEnvironment = processingEnvironment;
        mFiler = processingEnvironment.getFiler();
    }

    /**
     * AbsWriter 的 generate 方法中使用 模板方法 模式，封装了一些生成辅助类的通用逻辑
     * <p>
     * 1. 写入 package、import、class 以及 findViews 函数等代码段
     * 2. 写入该类中的所有字段到 findViews 方法中
     * 3. 写入 findViews 函数的大括号以及类的大括号
     *
     * @param map key 为类名，value 为该类中使用了注解的变量集合
     */
    @Override
    public void generate(Map<String, List<VariableElement>> map) {
        for (Map.Entry<String, List<VariableElement>> entry : map.entrySet()) {
            // 获取某一类中所有的被注解的成员
            List<VariableElement> variableElements = entry.getValue();
            if (variableElements == null || variableElements.isEmpty()) continue;

            // 取第一个元素来构造注入信息,因为一个 key 对应的 value 中所有 variableElements 都是在一个类中的，得到所在类型的信息是一样的，所以取第一个元素来构造注入信息就可以
            InjectorInfo info = createInjectorInfo(variableElements.get(0));
            Writer writer = null;

            // JavaFileObject 用于生产新的辅助类
            JavaFileObject javaFileObject;

            try {
                javaFileObject = mFiler.createSourceFile(info.getClassFullPath());
                writer = javaFileObject.openWriter();

                // 1. 写入 package、import、class 以及 findViews 函数等代码段
                generateImport(writer, info);
                // 2. 写入该类中的所有字段到 findViews 方法中
                for (VariableElement variableElement : variableElements) {
                    writeFiled(writer, variableElement, info);
                }
                // 3. 写入 findViews 函数的大括号以及类的大括号
                writerEnd(writer);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null)
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
    }

    protected abstract void generateImport(Writer writer, InjectorInfo injectorInfo) throws IOException;

    protected abstract void writeFiled(Writer writer, VariableElement variableElement, InjectorInfo info) throws IOException;

    protected abstract void writerEnd(Writer writer) throws IOException;

    private InjectorInfo createInjectorInfo(VariableElement element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String packageName = mProcessingEnvironment.getElementUtils().getPackageOf(typeElement)/* 获取包元素 */.getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        return new InjectorInfo(packageName, className);
    }

    /**
     * 注解相关的信息实体类
     */
    protected static class InjectorInfo {
        /**
         * 被注解的成员所在类的包名
         */
        public String packageName;
        /**
         * 被注解的成员所在类的类名
         */
        public String className;
        /**
         * 要创建的 InjectAdapter 辅助类的完整路径，新类的名字为被注解的变量所之类的类名 + "$InjectAdapter" ，与被注解的类在同一个包下
         * 加 $ 表示是原来类的子类，可以在被注解的成员所在类中通过反射得到辅助类
         */
        public String newClassName;

        public InjectorInfo(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
            newClassName = className + "$InjectAdapter";
        }

        public String getClassFullPath() {
            // File.separator 系统默认的分隔符, Android 默认的分隔符为 .
            return packageName + "." + newClassName;
        }
    }
}
