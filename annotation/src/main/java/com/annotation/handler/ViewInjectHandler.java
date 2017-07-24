package com.annotation.handler;

import com.annotation.ViewInjector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by renxl
 * On 2017/7/22 16:06.
 * <p>
 * ViewInjectHandler 负责处理 ViewInjector 类型的注解
 */

public class ViewInjectHandler implements AnnotationHandler {
    ProcessingEnvironment mProcessingEnvironment;

    /**
     * 绑定注解处理工具
     *
     * @param processingEnvironment 注解处理工具
     */
    public void attachProcessingEnv(ProcessingEnvironment processingEnvironment) {
        mProcessingEnvironment = processingEnvironment;
    }

    /**
     * 处理 ViewInjector 类型的注解
     *
     * @param roundEnvironment 所有未经整理的注解信息，所有使用了注解的元素信息
     * @return
     */
    public Map<? extends String, ? extends List<VariableElement>> handleAnnotation(RoundEnvironment roundEnvironment) {
        Map<String, List<VariableElement>> annotationMap = new HashMap<>();

        // 1. 获取所有使用了 ViewInjector 的注解的所有元素
        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(ViewInjector.class);

        // 迭代所有使用了 ViewInjector 注解的元素，并将其按类名分开
        for (Element element : elementSet) {
            // 2. 获取被注解的字段，由于 ViewInjector 注解的使用对象是 ElementType.FIELD ，所以可以向下转型为 VariableElement
            // 每一个被注解修饰的元素都有一个对应的 Element 类型，变量对应的为 VariableElement
            VariableElement variableElement = (VariableElement) element;
            // 3. 获取字段所在类型的完整路径名，如一个 TextView 多种的 Activity 的完整路径，也就是变量的宿主类的完整路径名
            String className = getParentClassName(variableElement);
            // 4. 在 annotationMap 中取出该路径名对应的注解元素的集合，如果为空就创建一个 ArrayList
            List<VariableElement> variableElements = annotationMap.get(className);
            if (variableElements == null)
                variableElements = new ArrayList<>();
            // 5. 将新解析到的使用了 ViewInjector 注解的元素添加到其类名所对应的集合中
            variableElements.add(variableElement);
            // 6. 将新的集合加入 Map 或者更新 Map 中已有的集合
            annotationMap.put(className, variableElements);
        }

        // 将所有使用了 ViewInjector 注解的类对应的 Map 返回，key 为类名，value 为该类中使用了该注解的元素集合
        return annotationMap;
    }

    private String getParentClassName(VariableElement variableElement) {
        // 1. 获取该元素所在类的类型
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        // 2. 获取 typeElement 的包名
        String packageName = mProcessingEnvironment.getElementUtils().getPackageOf(typeElement)/* 获取包元素 */.getQualifiedName().toString();
        return packageName + "." + typeElement.getSimpleName();
    }
}
