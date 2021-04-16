/**
 * Author by xpl, Date on 2021/4/15.
 */
public class VisitorImpl implements Visitor {
    @Override
    public void visitor(Girl girl) {
        System.out.println("v girl ....");
    }

    @Override
    public void visitor(Boy boy) {
        System.out.println("v boy ....");
    }
}
