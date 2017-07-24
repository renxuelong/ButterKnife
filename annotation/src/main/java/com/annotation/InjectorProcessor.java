package com.annotation;

import com.annotation.handler.AnnotationHandler;
import com.annotation.handler.ViewInjectHandler;
import com.annotation.writer.AdapterWriter;
import com.annotation.writer.DefaultJavaFileWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Java 的 Mirror 模块，可以在 Java -> class 过程中访问类
 * <p>
 * Mirror 中的 APT 是命令行工具，在 java -> class 过程中可以访问类的成员，Android 和 Java 都可以通过一定的配置更方便的使用 APT 工具
 * <p>
 * 1. APT 通过 AnnotationProcessor 确定访问规则，即访问被哪个注解修饰的类
 * <p>
 * 2. APT 获取到指定类的信息之后将获取到的内容交给 AnnotationProcessor 的 process 方法处理
 */

// 指定要处理的注解
@SupportedAnnotationTypes("com.annotation.ViewInjector")
// 指定源码版本，必须高于 6
@SupportedSourceVersion(SourceVersion.RELEASE_7)
// 自动生成 resources/META-INF/services/javax.annotation.processing.Processor 文件，目的是告诉 Mirror 模块中的 APT 工具应该通过哪个 AbstractProcessor 类确定要访问的类
// @AutoService(Processor.class)
public class InjectorProcessor extends AbstractProcessor {

    // 处理注解信息的 Handler 集合，每一个 handler 处理一种注解
    List<AnnotationHandler> mHandlers = new ArrayList<>();

    // 所有类及其对应的被注解成员集合
    Map<String, List<VariableElement>> map = new HashMap<>();

    // 用于将注解信息写到新类文件中
    AdapterWriter mWriter;

    /**
     * 初始化方法
     * 1. 初始化所有需要处理注解的 Handler
     * 2. 初始化将注解信息写入到类文件中的 Writer
     *
     * @param processingEnvironment 解析过程中使用到的工具
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        registerHandlers();
        mWriter = new DefaultJavaFileWriter(processingEnvironment);
    }

    private void registerHandlers() {
        mHandlers.add(new ViewInjectHandler());
    }

    /**
     * APT 获取到指定类的信息之后将获取到的内容交给 AnnotationProcessor 的 process 方法处理
     *
     * @param set              set
     * @param roundEnvironment 所有注解信息
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // 迭代所有的注解处理器，使的每一个注解都有一个处理器来处理

        for (AnnotationHandler handler : mHandlers) {
            // 为 handler 关联 ProcessingEnvironment
            handler.attachProcessingEnv(processingEnv);
            // 解析注解相关信息，并添加到 map 集合中
            map.putAll(handler.handleAnnotation(roundEnvironment));
        }

        // 将解析到的注解信息写入到具体的类型中
        mWriter.generate(map);
        return true;
    }
}
