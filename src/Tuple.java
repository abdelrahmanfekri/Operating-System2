public class Tuple {

    private String var, value;

    public Tuple(String var, String value) {
        this.var = var;
        this.value = value;
    }

    public String getVar() {
        return var;
    }

    public String getValue() {
        return value;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return var + " " + value;
    }
}
