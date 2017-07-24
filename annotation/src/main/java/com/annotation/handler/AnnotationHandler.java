package com.annotation.handler;

import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.VariableElement;

/**
 * Created by renxl
 * On 2017/7/22 16:01.
 * <p>
 * AnnotationHandler 负责处理表示了注解的元素，每一种 Handler 负责处理一种类型的注解
 */

public interface AnnotationHandler {
    /**
     * 绑定注解处理工具
     *
     * @param processingEnvironment 注解处理工具
     */
    public void attachProcessingEnv(ProcessingEnvironment processingEnvironment);

    /**
     * 解析注解信息
     *
     * @param roundEnvironment 所有未经整理的注解信息
     * @return
     */
    public Map<? extends String, ? extends List<VariableElement>> handleAnnotation(RoundEnvironment roundEnvironment);
}
