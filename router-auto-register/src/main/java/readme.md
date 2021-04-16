 /**
     * 定义：封装某些作用于某种数据结构中各元素的操作，它可以在不改变数据结构的前提下定义作用于这些数据元素的新的操作
     * 意图：主要将数据结构和数据操作分离
     * 主要解决：稳定的数据结构和易变的操作的解耦
     * 适用场景：
     * 假如一个对象中存在着一些与本对象不相干（或者关系较弱）的操作，可以使用访问者模式把这些操作封装到访问者中去，这样便避免了这些不相干的操作污染这个对象。
     * 假如一组对象中，存在着相似的操作，可以将这些相似的操作封装到访问者中去，这样便避免了出现大量重复的代码
     * 访问者模式适用于对功能已经确定的项目进行重构的时候适用，因为功能已经确定，元素类的数据结构也基本不会变了；如果是一个新的正在开发中的项目，在访问者模式中，每一个元素类都有它对应的处理方法，每增加一个元素类都需要修改访问者类，修改起来相当麻烦。
     */
     
     
     上述代码可以分为三步：
     1. 创建一个元素类的对象
     2. 创建一个访问类的对象
     3. 元素对象通过 Element#accept(Visitor visitor) 方法传入访问者对象
     
     # ASM
     ClassReader：它将字节数组或者 class 文件读入到内存当中，并以树的数据结构表示，树中的一个节点代表着 class 文件中的某个区域。可以将 ClassReader 看作是 Visitor 模式中的访问者的实现类
     
     ClassVisitor（抽象类）：ClassReader 对象创建之后，调用 ClassReader#accept() 方法，传入一个 ClassVisitor 对象。在 ClassReader 中遍历树结构的不同节点时会调用 ClassVisitor 对象中不同的 visit() 方法，从而实现对字节码的修改。在 ClassVisitor 中的一些访问会产生子过程，比如 visitMethod 会产生 MethodVisitor 的调用，visitField 会产生对 FieldVisitor 的调用，用户也可以对这些 Visitor 进行自己的实现，从而达到对这些子节点的字节码的访问和修改
     
     ClassWriter：ClassWriter 是 ClassVisitor 的实现类，它是生成字节码的工具类，它一般是责任链中的最后一个节点，其之前的每一个 ClassVisitor 都是致力于对原始字节码做修改，而 ClassWriter 的操作则是老实得把每一个节点修改后的字节码输出为字节数组。
       
     # ASM工作流程
     1.ClassReader 读取字节码到内存中，生成用于表示该字节码的内部表示的树，ClassReader 对应于访问者模式中的元素
     2.组装 ClassVisitor 责任链，这一系列 ClassVisitor 完成了对字节码一系列不同的字节码修改工作，对应于访问者模式中的访问者 Visitor
     3.然后调用 ClassReader#accept() 方法，传入 ClassVisitor 对象，此 ClassVisitor 是责任链的头结点，经过责任链中每一个 ClassVisitor 的对已加载进内存的字节码的树结构上的每个节点的访问和修改
     4.最后，在责任链的末端，调用 ClassWriter 这个 visitor 进行修改后的字节码的输出工作
    