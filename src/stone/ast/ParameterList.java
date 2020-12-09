package stone.ast;
import java.util.List;

/**储存得有 参参数列表，每个参数都是一个AStree节点
 *
 */
public class ParameterList extends ASTList {
    public ParameterList(List<ASTree> c) { super(c); }
    public String name(int i) { return ((ASTLeaf)child(i)).token().getText(); }

    /**
     * 返回 子节点数量
     * @return
     */
    public int size() { return numChildren(); }
}
