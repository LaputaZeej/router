# 涉及知识点
## 组件化
## 路由
## APT
## JavaPoet
## Gradle插件
## ASM
这是一个相关的链接
https://github.com/SusionSuc/AdvancedAndroid/blob/master/gradle/README.md

ASM 是一个 Java 字节码操控框架。它能被用来动态生成类或者增强既有类的功能。ASM 可以直接产生二进制 class 文件，
也可以在类被加载入 Java 虚拟机之前动态改变类行为。Java class 被存储在严格格式定义的 .class 文件里，这些类文件
拥有足够的元数据来解析类中的所有元素：类名称、方法、属性以及 Java 字节码（指令）。ASM 从类文件中读入信息后，能
够改变类行为，分析类信息，甚至能够根据用户要求生成新类。
https://www.jianshu.com/p/c2c1d350d245

> <<深入理解JVM字节码>>

## ARouter

# 帮助
## ARouter
https://github.com/alibaba/ARouter/blob/master/README_CN.md

## AutoRegister:一种更高效的组件自动注册方案(android组件化开发)
https://juejin.cn/post/6844903520429162509

*Q1:如何将不同的module中的组件类自动注册到ComponentManager中?*
> ARouter:扫描所有dex里的包进行反射
> ActivityRouter:appModule mapModule 维护一个注解@Module 在map里生成类调用RouterInit#init

*Gradle+ASM*
在编译时，扫描所有类，将符合条件的类收集起来，并通过修改字节码生成注册代码到指定的管理类中，从而实现编译时自动注册的功能，不用再关心项目中有哪些组件类了。
优点：不会增加新的class，不需要反射，运行时直接调用组件的构造方法；ASM自定义扫描，过滤不必要的类；
缺点：增加编译时耗时，可忽略

*AutoRegister*
在编译期扫描即将打包到apk中的所有类，并将指定接口的实现类(或指定类的子类)通过字节码操作自动注册到对应的管理类中。尤其适用于命令模式或策略模式下的映射表生成。
>在组件化开发框架中，可有助于实现分级按需加载的功能：
>在组件管理类中生成组件自动注册的代码
>在组件框架第一次被调用时加载此注册表
>若组件中有很多功能提供给外部调用，可以将这些功能包装成多个Processor，并将它们自动注册到组件中进行管理
>组件被初次调用时再加载这些Processor

*AndAop*
https://github.com/luckybilly/AndAOP
一个aop方案：通过配置aop类和方法，修改class来实现在方法的前后添加自定义逻辑。

## ARouter简介 最佳实践
https://developer.aliyun.com/article/71687

## CC
https://github.com/luckybilly/CC

## 对比
https://github.com/luckybilly/AndroidComponentizeLibs

## Gradle插件
https://blog.csdn.net/sbsujjbcy/article/details/50782830