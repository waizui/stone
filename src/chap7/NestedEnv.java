package chap7;
import java.util.HashMap;
import chap6.Environment;
import chap7.FuncEvaluator.EnvEx;

public class NestedEnv implements Environment {
    protected HashMap<String,Object> values;
    protected Environment outer;
    public NestedEnv() { this(null); }
    public NestedEnv(Environment e) {
        values = new HashMap<String,Object>();
        outer = e;
    }
    public void setOuter(Environment e) { outer = e; }
    public Object get(String name) {
        Object v = values.get(name);
        if (v == null && outer != null)
            return outer.get(name);
        else
            return v;
    }
    public void putNew(String name, Object value) { values.put(name, value); }

    /**
     * 放入一个变量到环境中，如果有就更新那个值
     * @param name
     * @param value
     */
    public void put(String name, Object value) {
        Environment e = where(name);
        if (e == null)
            e = this;
        ((EnvEx)e).putNew(name, value);
    }

    /**
     * 在嵌套环境中找到变量值
     * @param name
     * @return
     */
    public Environment where(String name) {
        if (values.get(name) != null)
            return this;
        else if (outer == null)
            return null;
        else
            return ((EnvEx)outer).where(name);
    }
}
