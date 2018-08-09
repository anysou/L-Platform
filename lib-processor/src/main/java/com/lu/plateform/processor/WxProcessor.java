package com.lu.plateform.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * 用于生成微信支付、登录、分享等功能所需的回调activity
 */
@AutoService(Processor.class)
public class WxProcessor extends AbstractProcessor {
    private String mPackageName = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Map<String, String> options = processingEnvironment.getOptions();
        if (options.size() > 0) {
            mPackageName = options.get("appid")+".wxapi";
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> strings = new HashSet<>();
        strings.add(Override.class.getCanonicalName());
        return strings;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (mPackageName == null) return false;
        //生成支付回调WXPayEntryActivity
        TypeSpec typeSpec = TypeSpec.classBuilder("WXPayEntryActivity")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ClassName.get("com.lu.platform.app.util", "WXPayEntryDelegate"))
                .build();
        JavaFile javaFile = JavaFile.builder(mPackageName, typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        typeSpec = TypeSpec.classBuilder("WXEntryActivity")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ClassName.get("com.lu.platform.app.util", "WXEntryDelegate"))
                .build();
        javaFile = JavaFile.builder(mPackageName, typeSpec).build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
