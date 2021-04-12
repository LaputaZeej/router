package com.laputa.compiler;

import com.google.auto.service.AutoService;
import com.laputa.annotations.HRouter;
import com.laputa.annotations.RouterBean;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.laputa.annotations.HRouter"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({Constant.MODULE_ID, Constant.PACKAGE_NAME_FOR_APT})
public class HRounterCompiler extends AbstractProcessor {
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    private String moduleId;
    private String packageNameForAPT;
    /* group to paths */
    private Map<String, List<RouterBean>> pathMap = new HashMap<>();
    /* group to class */
    private Map<String, String> groupMap = new HashMap<>();
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        moduleId = processingEnv.getOptions().get(Constant.MODULE_ID);
        packageNameForAPT = processingEnv.getOptions().get(Constant.PACKAGE_NAME_FOR_APT);
        println(" xxxxxxxxxxz MODULE_ID = " + moduleId);
        println(" xxxxxxxxxxz PACKAGE_NAME_FOR_APT = " + packageNameForAPT);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        println("size  =" + set.size());
        if (set.isEmpty()) {
            println("注解 is empty");
            return false;
        }
        testJavaPoet();
        testPoet2(roundEnvironment);
        TypeElement activityTypeElement = elementUtils.getTypeElement(Constant.ACTIVITY_PACKAGE_NAME);
        TypeMirror activityTypeMirror = activityTypeElement.asType();
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(HRouter.class);

        // 扫描所有注解信息，然后加入pathMap，等待生成对应的RouterPath
        // key      ----->          List<RouterBean>
        // group    -1------n--->   path
        for (Element element : elements) {
            HRouter annotation = element.getAnnotation(HRouter.class);
            RouterBean routerBean;
            // 判断继承Activity
            TypeMirror typeMirror = element.asType();
            if (typeUtils.isSubtype(typeMirror, activityTypeMirror)) {
                routerBean = new RouterBean();
                routerBean.setPath(annotation.path());
                routerBean.setGroup(annotation.group());
                routerBean.setElement(element);
                routerBean.setType(RouterBean.Type.ACTIVITY);
                // routerBean.setAnnotationClass(ClassName.get((TypeElement)element)); // todo
            } else {
                throw new RuntimeException("注解目前必须用在activity之上");
            }

            if (checkBean(routerBean)) {
                List<RouterBean> routerBeans = pathMap.get(routerBean.getGroup());
                if (routerBeans == null) {
                    routerBeans = new ArrayList<>();
                    routerBeans.add(routerBean);
                    pathMap.put(routerBean.getGroup(), routerBeans);
                }else{
                    routerBeans.add(routerBean);
                }
            }
        }//end

        TypeElement groupTypeElement = elementUtils.getTypeElement(Constant.RouterGroup_PACKAGE_NAME);
        TypeElement pathTypeElement = elementUtils.getTypeElement(Constant.RouterPath_PACKAGE_NAME);

        try {
            createPath(pathTypeElement);
        } catch (Exception e) {
            e.printStackTrace();
            error("createPath error " + e.getLocalizedMessage());
        }

        try {
            createGroup(groupTypeElement, pathTypeElement);
        } catch (Exception e) {
            e.printStackTrace();
            error("createGroup error " + e.getLocalizedMessage());
        }

        display();

        return true;
    }

    private void display() {
        println("======================display=====================");
        println("pathMap.size = " + pathMap.size());
        for (Map.Entry<String, List<RouterBean>> entry : pathMap.entrySet()) {
            println("key --> " + entry.getKey());
        }
        println("groupMap.size = " + groupMap.size());
        for (Map.Entry<String, String> entry2 : groupMap.entrySet()) {
            println("key --> " + entry2.getKey());
        }
        println("======================display=====================z");
    }

    /**
     * @param pathTypeElement
     * @throws Exception
     */
    private void createPath(TypeElement pathTypeElement) throws Exception {
        if (pathMap.isEmpty()) {
            error("========pathMap is empty===========");
            return;
        }
        println("========createPath===========");
        // Map<String,RouterBean>
        ParameterizedTypeName returnType = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(RouterBean.class));

        for (Map.Entry<String, List<RouterBean>> entry : pathMap.entrySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getPathMap")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(returnType)
                    //  Map<String,RouterBean> pathMap = new HashMap<>();
                    .addStatement("$T<$T,$T> $N = new $T<>()"
                            , ClassName.get(Map.class),  // Map
                            ClassName.get(String.class) // String
                            , ClassName.get(RouterBean.class)// RouterBean
                            , "pathMap"
                            , ClassName.get(HashMap.class)
                    );

            List<RouterBean> value = entry.getValue();
            int index = 0;
            for (RouterBean routerBean : value) {
                String beanName = "bean" + index;
                // RouterBean bean = new RouterBean();
                methodBuilder.addStatement("$T $N = new $T()",
                        ClassName.get(RouterBean.class),
                        beanName,
                        ClassName.get(RouterBean.class)
                );
                // bean.setPath(path);
                methodBuilder.addStatement("$N.setPath($S)", beanName, routerBean.getPath());
                // bean.setGroup(group);
                methodBuilder.addStatement("$N.setGroup($S)", beanName, routerBean.getGroup());
                // bean.setType(Type.ACTIVITY);
                methodBuilder.addStatement("$N.setType($T.$L)", beanName, RouterBean.Type.class, routerBean.getType());
                // bean.setAnnotationClass(cls);
                methodBuilder.addStatement("$N.setAnnotationClass($T.class)", beanName, ClassName.get((TypeElement) routerBean.getElement()));
                // bean.setElement(element.getElement());
                // methodBuilder.addStatement("$N.setElement($L)", beanName, routerBean.getElement());
                // pathMap.put("/app/MainActivity",routerBean)
                methodBuilder.addStatement("$N.put($S,$N)", "pathMap", routerBean.getPath(), beanName);
                index++;
            }

            // return pathMap
            methodBuilder.addStatement("return $N", "pathMap");

            String finaleClassName = Constant.RouterPath_PATH_NAME + entry.getKey();
            JavaFile.builder(packageNameForAPT,
                    TypeSpec.classBuilder(finaleClassName)
                            .addSuperinterface(ClassName.get(pathTypeElement))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(methodBuilder.build())
                            .build()
            ).build().writeTo(filer);
            groupMap.put(entry.getKey(), finaleClassName);
        }

        println("========createPath======end=====");
    }

    private void createGroup(TypeElement groupTypeElement, TypeElement pathTypeElement) throws Exception {
        //  Map<String,Class<? extends RouterPath>>
        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get(Map.class), // Map
                ClassName.get(String.class), // String
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathTypeElement)) // ? extends RouterPath
                ) // Class<?  extends RouterPath
        );
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("getGroupPath")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType);
        // Map<String,Class<? extends RouterPath> map = new HashMap<>();
        methodSpecBuilder.addStatement(
                "$T<$T,$T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(ClassName.get(pathTypeElement))),
                "map",
                ClassName.get(HashMap.class)
        );
        //map.put(key,value);
        for (Map.Entry<String, String> entry : groupMap.entrySet()) {
            methodSpecBuilder.addStatement(
                    "$N.put($S,$T.class)",
                    "map",
                    entry.getKey(),
                    ClassName.get(packageNameForAPT, entry.getValue())
            );
            // return map
            methodSpecBuilder.addStatement("return $N", "map");
            String finalClassName = Constant.RouterPath_Group_NAME + moduleId;
            JavaFile.builder(packageNameForAPT,
                    TypeSpec.classBuilder(finalClassName)
                            .addSuperinterface(ClassName.get(groupTypeElement))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(methodSpecBuilder.build())
                            .build()
            ).build().writeTo(filer);
        }
    }

    private boolean checkBean(RouterBean routerBean) {
        String path = routerBean.getPath();
        try {
            // todo 校验
            String group = path.substring(1, path.indexOf("/", 1));
            routerBean.setGroup(group);
            return true;
        } catch (Exception e) {
            error(e.getLocalizedMessage());
        }
        return false;
    }

    private void testPoet2(RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(HRouter.class);
        for (Element element : elements) {
            Name packageName = elementUtils.getPackageOf(element).getQualifiedName();
            Name className = element.getSimpleName();
            HRouter annotation = element.getAnnotation(HRouter.class);
            println("packageName = " + packageName + " ,className = " + className);
            MethodSpec methodSpec = MethodSpec.methodBuilder("createClass")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(Class.class)
                    .addParameter(String.class, "path")
                    .addStatement("return path.equals($S) ? $T.class : null"
                            , annotation.path(),
                            ClassName.get((TypeElement) element)
                    )
                    .build();
            TypeSpec typeSpec = TypeSpec.classBuilder(className.toString() + "HRouter")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(methodSpec)
                    .build();
            JavaFile javaFile = JavaFile.builder(packageName.toString(), typeSpec).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void testJavaPoet() {
        // method
        MethodSpec methodSpec = MethodSpec.methodBuilder("hello")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "names")
                .addStatement("$T.out.println($S)", System.class, "Hello Poet!!!")
                .build();
        // type
        TypeSpec classSpec = TypeSpec.classBuilder("JavaPoetDemo")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodSpec)
                .build();
        // package
        JavaFile javaFile = JavaFile.builder("com.bugull.javapoet", classSpec).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            error("poet fail !");
        }
    }

    private void println(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, "\n" + msg + "\n");
    }

    private void error(String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, "\n" + msg + "\n");
    }
}