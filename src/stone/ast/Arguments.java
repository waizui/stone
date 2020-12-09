package stone.ast;
import java.util.List;

public class Arguments extends Postfix {
    public Arguments(List<ASTree> c) { super(c); }

    /**
     * 返回postfix类子节点astree数
     * @return
     */
    public int size() { return numChildren(); }
}
