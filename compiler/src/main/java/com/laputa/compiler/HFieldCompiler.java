package com.laputa.compiler;

import com.google.auto.service.AutoService;
import com.laputa.annotations.HField;
import com.laputa.annotations.HRouter;
import com.laputa.annotations.RouterBean;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static javax.lang.model.type.TypeKind.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.laputa.annotations.HField"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({Constant.MODULE_ID, Constant.PACKAGE_NAME_FOR_APT})
public class HFieldCompiler extends AbstractProcessor {
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Types typeUtils;

    private String packageNameForAPT;
    private Map<TypeElement, List<Element>> fieldMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        packageNameForAPT = processingEnv.getOptions().get(Constant.PACKAGE_NAME_FOR_APT);
        println(" xxxxxxxxxxz PACKAGE_NAME_FOR_APT = " + packageNameForAPT);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        println("size  =" + set.size());
        if (set.isEmpty()) {
            println("注解 is empty");
            return false;
        }
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(HField.class);
        if (elements != null && !elements.isEmpty()) {


            TypeElement activityTypeElement = elementUtils.getTypeElement(Constant.ACTIVITY_PACKAGE_NAME);
            TypeElement fieldTypeElement = elementUtils.getTypeElement(Constant.RouterFIELD_PACKAGE_NAME);
            for (Element element : elements) {


                // 当前节点的父节点-》Activity
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                if (!typeUtils.isSubtype(enclosingElement.asType(), activityTypeElement.asType())) {
                    throw new RuntimeException("HField当前只能用在Activity中");
                }
                if (fieldMap.containsKey(enclosingElement)) {
                    fieldMap.get(enclosingElement).add(element);
                } else {
                    List<Element> fields = new ArrayList<>();
                    fields.add(element);
                    fieldMap.put(enclosingElement, fields);
                }
            }//end
            if (fieldMap.isEmpty()) {
                return false;
            }


            // Object target
            ParameterSpec target = ParameterSpec.builder(TypeName.OBJECT, "target").build();

            for (Map.Entry<TypeElement, List<Element>> entry : fieldMap.entrySet()) {
                TypeElement key = entry.getKey();
                // MainActivity
                ClassName className = ClassName.get(entry.getKey());
                // MainActivity
                MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("loadField")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(target);
                // MainActivity activity = (MainActivity)target
                methodBuilder.addStatement("$T $N = ($T)$N", className, "activity", className, "target");
                // activity.getIntent().getStringExtra("name")
                List<Element> fields = entry.getValue();
                if (fields == null || fields.isEmpty()) {
                    continue;
                }
                // Bundle bundle = activity.getIntent().getExtras();
                methodBuilder.addStatement("$T $N = $N.getIntent().getExtras()",
                        elementUtils.getTypeElement(Constant.BUNDLE_PACKAGE_NAME),
                        "bundle",
                        "activity"
                );
                // if(bundle!=null)
                methodBuilder.beginControlFlow("if($N!=null)", "bundle");
                // activity.name = activity.getIntent().getExtras().getString("name1");
                // todo 可以使用反射？防止获取不到属性？
                for (Element field : fields) {
                    TypeMirror typeMirror = field.asType();
                    TypeKind kind = typeMirror.getKind();
                    String fieldName = field.getSimpleName().toString();
                    String annotationValue = field.getAnnotation(HField.class).name();
                    String finalString = "$N.$N = $N.";

                    annotationValue = (annotationValue == null || annotationValue.trim().isEmpty()) ? fieldName : annotationValue;
                    switch (kind) {
                        case INT:
                            finalString += "getInt($S,0)";
                            break;
                        case BOOLEAN:
                            finalString += "getBoolean($S,false)";
                            break;
                        case LONG:
                            finalString += "getLong($S,0)";
                            break;
                        case FLOAT:
                            finalString += "getFloat($S,0F)";
                            break;
                        default:
                            // todo 更多情况
                            if (typeMirror.toString().equalsIgnoreCase("java.lang.String")) {
                                finalString += "getString($S)";
                            }
                            break;
                    }
                    methodBuilder.addStatement(finalString , "activity", fieldName, "bundle", annotationValue);

                }//end
                methodBuilder.endControlFlow();
                String finalClassName = key.getSimpleName() + Constant.RouterFiled_NAME;
                try {
                    JavaFile.builder(packageNameForAPT,
                            TypeSpec.classBuilder(finalClassName)
                                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                    .addSuperinterface(ClassName.get(fieldTypeElement))
                                    .addMethod(methodBuilder.build())
                                    .build()
                    ).build().writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }//end


            return true;
        }
        return false;
    }

    private void println(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, "\n" + msg + "\n");
    }

    private void error(String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, "\n" + msg + "\n");
    }
}