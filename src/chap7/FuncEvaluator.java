package chap7;
import java.util.List;
import javassist.gluonj.*;
import stone.StoneException;
import stone.ast.*;
import chap6.BasicEvaluator;
import chap6.Environment;
import chap6.BasicEvaluator.ASTreeEx;
import chap6.BasicEvaluator.BlockEx;

@Require(BasicEvaluator.class)
@Reviser public class FuncEvaluator {
    @Reviser public static interface EnvEx extends Environment {
        void putNew(String name, Object value);
        Environment where(String name);
        void setOuter(Environment e);
    }
    @Reviser public static class DefStmntEx extends DefStmnt {
        public DefStmntEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            ((EnvEx)env).putNew(name(), new Function(parameters(), body(), env));
            return name();
        }
    }
    @Reviser public static class PrimaryEx extends PrimaryExpr {
        public PrimaryEx(List<ASTree> c) { super(c); }
        public ASTree operand() { return child(0); }

        /**
         * 返回实参序列（后缀 形如->fun( 1 ) 中的 1 就是一个Number） (或者在类型中
         * 的 instance.new 中的new)后面用作可以返回数组等
         * @param nest
         * @return
         */
        public Postfix postfix(int nest) {
            //反着取 比如 children是 123 那 0取出来的是3  这是因为加入的时候是321这样放进去的
            //就是push进去了，顺序反向
            return (Postfix)child(numChildren() - nest - 1);
        }
        public boolean hasPostfix(int nest) { return numChildren() - nest > 1; } 
        public Object eval(Environment env) {
            return evalSubExpr(env, 0);
        }

        /**
         * 如果 postfix 返回了多个对象 就递归此求值
         * @param env
         * @param nest 表示env从外层数起的第几层     * @return
         */
        public Object evalSubExpr(Environment env, int nest) {
            if (hasPostfix(nest)) {
                Object target = evalSubExpr(env, nest + 1);
                return ((PostfixEx)postfix(nest)).eval(env, target);
            }
            else
                return ((ASTreeEx)operand()).eval(env);
        }
    }
    @Reviser public static abstract class PostfixEx extends Postfix {
        public PostfixEx(List<ASTree> c) { super(c); }
        public abstract Object eval(Environment env, Object value);
    }
    @Reviser public static class ArgumentsEx extends Arguments {
        public ArgumentsEx(List<ASTree> c) { super(c); }

        /**
         * 变量可以是局部的 也可以是外部的，将会从最内部依次向外查找变量
         * @param callerEnv
         * @param value 想要被调用的函数
         * @return
         */
        public Object eval(Environment callerEnv, Object value) {
            if (!(value instanceof Function))
                throw new StoneException("bad function", this);
            Function func = (Function)value;
            ParameterList params = func.parameters();
            if (size() != params.size())
                throw new StoneException("bad number of arguments", this);
            Environment newEnv = func.makeEnv();
            int num = 0;
            //每个参数都调用，然后将值储存再newEnv里面
            for (ASTree a: this)
                ((ParamsEx)params).eval(newEnv, num++,
                                        ((ASTreeEx)a).eval(callerEnv));
            return ((BlockEx)func.body()).eval(newEnv);
        }
    }
    @Reviser public static class ParamsEx extends ParameterList {
        public ParamsEx(List<ASTree> c) { super(c); }

        public void eval(Environment env, int index, Object value) {
            ((EnvEx)env).putNew(name(index), value);
        }
    }
}
