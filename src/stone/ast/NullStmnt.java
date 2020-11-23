package stone.ast;
import java.util.List;

//用来标记空语句
public class NullStmnt extends ASTList {
    public NullStmnt(List<ASTree> c) { super(c); }
}
