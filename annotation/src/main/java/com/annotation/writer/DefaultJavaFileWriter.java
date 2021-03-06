package com.annotation.writer;

import com.annotation.ViewInjector;

import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

/**
 * Created by renxl
 * On 2017/7/22 16:07.
 */

public class DefaultJavaFileWriter extends AbsWriter {

    public DefaultJavaFileWriter(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    /**
     * 写入 package、import、class 以及 findViews 函数等代码段
     *
     * @param writer       Writer
     * @param injectorInfo InjectorInfo
     */
    @Override
    protected void generateImport(Writer writer, InjectorInfo injectorInfo) throws IOException {
        // 写入包名
        writer.write("package " + injectorInfo.packageName + ";");
        writer.write("\n\n");

        // 写入 import 的类， InjectAdapter 和 ViewFinder 都是辅助新生成的辅助类完成任务的类
        // ViewFinder 辅助完成 findViewById 的过程
        writer.write("import com.annotation.adapter.InjectAdapter;"); // 注解类
        writer.write("\n");
        writer.write("import com.renxl.butterknife.compile.ViewFinder;");

        // 写入类名 --> 第一个方法声明部分
        // InjectAdapter 接口中声明了所有生成的辅助类实现注入的入口方法 bind，在 ButterKnife 类中得到辅助类的实例后，通过该入口方法实现注入
        writer.write("\n\n\n");
        writer.write("/* This class is generated by Simple ViewInjector, please don't modify! */");
        writer.write("\n");
        writer.write("public class " + injectorInfo.newClassName + " implements InjectAdapter<" + injectorInfo.className + ">{ ");
        writer.write("\n");
        writer.write("\n");

        // 绑定方法，为什么要有该方法？因为在 ButterKnife 的 bind 方法中，就是调用了为当前 Activity 生成的辅助类的 bind 方法也就是当前类的 bind 方法实现注入
        // 调用该方法为被注解的变量赋值
        writer.write("    public void bind(" + injectorInfo.className + " target) { ");
        writer.write("\n");
    }

    /**
     * 写入该类中的所有字段到 findViews 方法中
     *
     * @param writer          Writer
     * @param variableElement VariableElement
     * @param info            InjectorInfo
     */
    @Override
    protected void writeFiled(Writer writer, VariableElement variableElement, InjectorInfo info) throws IOException {
        // 获取被注解的元素中的注解
        ViewInjector injector = variableElement.getAnnotation(ViewInjector.class);
        // 获取被注解的元素的引用名
        String fieldName = variableElement.getSimpleName().toString();

        // 调用 ViewFinder 的方法，通过 target(被注解的元素所在的类的实例) 和 注解中的值 完成 findViewById 的过程
        writer.write("        target." + fieldName + " = ViewFinder.findRequiredView(target, " + injector.value() + ");");
        writer.write("\n");
    }

    /**
     * 写入 findViews 函数的大括号以及类的大括号
     *
     * @param writer Writer
     */
    @Override
    protected void writerEnd(Writer writer) throws IOException {
        writer.write("    }");
        writer.write("\n\n");
        writer.write("}");
    }


}
