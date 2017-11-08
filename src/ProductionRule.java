import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by USER on 2017/11/7.
 */
public class ProductionRule {

    //产生式头, 是一个文法符号
    private GrammarSymbol productionHead;
    //产生式体, 是一个文法符号队列
    private List<GrammarSymbol> productionBody = new ArrayList<>();

    public ProductionRule(){}

    public ProductionRule(GrammarSymbol head){
        this.productionHead = head;
    }

    public void addBody(GrammarSymbol symbol){
        this.productionBody.add(symbol);
    }

    public void setHead(GrammarSymbol symbol){
        this.productionHead = symbol;
    }

    public List<GrammarSymbol> getProductionBody(){
        return this.productionBody;
    }

    public GrammarSymbol getProductionHead(){
        return this.productionHead;
    }

    public boolean isEpsilonProduction(){
        return this.getProductionBody().size()==1 && this.getProductionBody().get(0).isEpsilon();
    }

    @Override
    public String toString(){
        String result =  productionHead.getValue()+" -> ";
        for(GrammarSymbol g: productionBody){
            result += g.getValue()+" ";
        }

        return result;
    }

    @Override
    public boolean equals(Object o){

        if(o==null) return false;
        if(o==this) return true;
        if(!(o instanceof ProductionRule)) return false;

        ProductionRule pr = (ProductionRule) o;

        return this.productionHead.equals(pr.productionHead) && this.productionBody.equals(pr.productionBody);

    }

    @Override
    public int hashCode(){

        return 31* Objects.hashCode(this.productionHead.getValue())+ Arrays.hashCode(this.productionBody.toArray());
    }

}
