/**
 * Author by xpl, Date on 2021/4/15.
 */
public class Test {
    public static void main(String[] args) {
        Visitor visitor = new VisitorImpl();
        Element girl = new Girl();
        Element boy = new Boy();
        girl.accept(visitor);
        boy.accept(visitor);

    }
}
