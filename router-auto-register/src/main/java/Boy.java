/**
 * Author by xpl, Date on 2021/4/15.
 */
public class Boy implements Element{
    @Override
    public void accept(Visitor visitor) {
        visitor.visitor(this);
    }
}
