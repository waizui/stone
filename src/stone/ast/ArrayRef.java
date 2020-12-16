package stone.ast;
import java.util.List;

public class ArrayRef extends Postfix {
    public ArrayRef(List<ASTree> c) { super(c); }

    /**
     * 返回第一个 postfix child
     * @return first child
     */
    public ASTree index() { return child(0); }
    public String toString() { return "[" + index() + "]"; }
}
