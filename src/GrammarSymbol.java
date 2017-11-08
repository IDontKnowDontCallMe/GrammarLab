import java.util.Objects;

/**
 * Created by USER on 2017/11/7.
 */
public class GrammarSymbol {

    //是否是终结符
    private boolean isTerminal;
    //是否是epsilon
    private boolean isEpsilon;
    //字面值
    private String value;

    public GrammarSymbol(String value, boolean isTerminal){

        this.isTerminal = isTerminal;
        this.value = value;
        this.isEpsilon = false;
    }

    public GrammarSymbol(String value, boolean isTerminal, boolean isEpsilon){

        this.isTerminal = isTerminal;
        this.value = value;
        this.isEpsilon = isEpsilon;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEpsilon() {
        return isEpsilon;
    }

    public void setEpsilon(boolean epsilon) {
        isEpsilon = epsilon;
    }

    @Override
    public boolean equals(Object o){
        if(o==null) return false;
        if(o==this) return true;
        if(!(o instanceof GrammarSymbol)) return false;

        GrammarSymbol gs = (GrammarSymbol) o;

        if(this.isEpsilon){
            if(gs.isEpsilon){
                return true;
            }
        }

        return this.isTerminal==gs.isTerminal && this.value.equals(gs.value);
    }

    @Override
    public int hashCode(){
        return 31*Objects.hashCode(this.value)+Objects.hashCode(this.isTerminal);
    }

    @Override
    public String toString(){
        return this.value;
    }

}
