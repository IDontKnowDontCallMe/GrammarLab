import java.util.*;

/**
 * Created by USER on 2017/11/7.
 */
public class LR1Item {

    //LR1项的产生式
    private ProductionRule productionRule;
    //LR1项的点的位置
    private int pointIndex;
    //预测符
    private GrammarSymbol predictSymbol;





    public LR1Item(ProductionRule productionRule, int pointIndex, GrammarSymbol predictSymbol){
        this.productionRule = productionRule;
        this.pointIndex = pointIndex;
        this.predictSymbol = predictSymbol;
    }

    public int getPointIndex(){
        return this.pointIndex;
    }

    public ProductionRule getProductionRule(){
        return this.productionRule;
    }

    public boolean isPointBeforeNonTerminal(){

        if(this.pointIndex>=productionRule.getProductionBody().size()){
            return false;
        }
        if(productionRule.getProductionBody().get(this.pointIndex).isTerminal()){
            return false;
        }
        else {
            return true;
        }
    }

    public GrammarSymbol getGrammarSymbolBehindPoint(){
        if(this.pointIndex>=productionRule.getProductionBody().size()){
            return null;
        }

        return productionRule.getProductionBody().get(this.pointIndex);
    }

    public List<GrammarSymbol> getSymbolsfrom(int start, int end){

        List<GrammarSymbol> result = new ArrayList<>();

        List<GrammarSymbol> list = productionRule.getProductionBody();

        for(int i=start; i<end; i++){
            result.add(list.get(i));
        }

        return result;
    }


    public GrammarSymbol getPredictSymbol(){
        return this.predictSymbol;
    }

    public boolean isTerminalItem(){

        return this.getProductionRule().getProductionHead().getValue().equals("T")
                && this.getProductionRule().getProductionBody().size() == 1
                && this.pointIndex == 1
                && this.getProductionRule().getProductionBody().get(0).getValue().equals("S")
                && this.predictSymbol.getValue().equals("$");

    }



    @Override
    public boolean equals(Object o){
        if(o==null) return false;
        if(o==this) return true;
        if(!(o instanceof LR1Item)) return false;

        LR1Item lt = (LR1Item) o;

        return (this.productionRule.equals(lt.productionRule))&&(this.pointIndex==lt.pointIndex)&&(this.predictSymbol.equals(lt.predictSymbol));
    }



    @Override
    public int hashCode(){
        int result = 1;

        return this.productionRule.hashCode()*31*31 + this.pointIndex*31 + this.predictSymbol.hashCode();
    }

    @Override
    public String toString(){

        String result =  productionRule.getProductionHead().getValue() + " -> " ;

        for(int i=0; i<productionRule.getProductionBody().size(); i++){
            if(i==pointIndex){
                result += "* ";
            }
            GrammarSymbol gs = productionRule.getProductionBody().get(i);
            result += gs.getValue()+" ";
        }
        if(productionRule.getProductionBody().size()==pointIndex){
            result += "* ";
        }
        result += ", ";
        result += predictSymbol.getValue();

        return result;
    }

}
