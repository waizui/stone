package stone;
import static stone.Parser.rule;
import java.util.HashSet;
import stone.Parser.Operators;
import stone.ast.*;

public class BasicParser {
    HashSet<String> reserved = new HashSet<String>();
    Operators operators = new Operators();
    Parser expr0 = rule();
    //终结符
    Parser primary = rule(PrimaryExpr.class)
        .or(rule().sep("(").ast(expr0).sep(")"),
            rule().number(NumberLiteral.class),
            rule().identifier(Name.class, reserved),
            rule().string(StringLiteral.class));
    //因式子
    Parser factor = rule().or(rule(NegativeExpr.class).sep("-").ast(primary),
                              primary);
                              
    //内部dsl风格，其实直接使用expr而不用expr0也可以
    Parser expr = expr0.expression(BinaryExpr.class, factor, operators);
    
    Parser statement0 = rule();
    //块 再{后面执行paser完规则后，会在最后paser有没有“}” 如果没有一条符合
    //就paser出错，syntax error
    Parser block = rule(BlockStmnt.class)
        .sep("{").option(statement0)
        .repeat(rule().sep(";", Token.EOL).option(statement0))
        .sep("}");
    //简单
    Parser simple = rule(PrimaryExpr.class).ast(expr);
    Parser statement = statement0.or(
            rule(IfStmnt.class).sep("if").ast(expr).ast(block)
                               .option(rule().sep("else").ast(block)),
            rule(WhileStmnt.class).sep("while").ast(expr).ast(block),
            simple);
    Parser program = rule().or(statement, rule(NullStmnt.class))
                           .sep(";", Token.EOL);

    public BasicParser() {
        reserved.add(";");
        //左括号无需单独添加，因为语法分析算法的原因
        reserved.add("}");
        reserved.add(Token.EOL);
        //操作符，有左和右边之分
        operators.add("=", 1, Operators.RIGHT);
        operators.add("==", 2, Operators.LEFT);
        operators.add(">", 2, Operators.LEFT);
        operators.add("<", 2, Operators.LEFT);
        operators.add("+", 3, Operators.LEFT);
        operators.add("-", 3, Operators.LEFT);
        operators.add("*", 4, Operators.LEFT);
        operators.add("/", 4, Operators.LEFT);
        operators.add("%", 4, Operators.LEFT);
    }
    public ASTree parse(Lexer lexer) throws ParseException {
        //从program开始parse，遍历子parser，递归parse 每一层递归返回就检测这层对应的那个BNF是否正确，否则语法错误
        return program.parse(lexer);
    }
}
