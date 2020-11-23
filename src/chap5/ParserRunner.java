package chap5;
import stone.ast.ASTree;
import stone.*;

public class ParserRunner {
    public static void main(String[] args) throws ParseException {
        
        var text="a=0;b=2 \r\n"+
        "if a <1 { \r\n"+
        "b=1;+\r\n"+
        "}";
        
        var dialog=new CodeDialog(text);
        Lexer l = new Lexer(dialog);
        BasicParser bp = new BasicParser();
        while (l.peek(0) != Token.EOF) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }
}
