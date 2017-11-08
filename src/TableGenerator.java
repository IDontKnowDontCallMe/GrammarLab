import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 2017/11/8.
 */
public class TableGenerator {

    private List<LR1ItemSet> itemSetList = null;
    private Grammars grammars = null;

    private List<GrammarSymbol> terminalSymbolList;
    private List<GrammarSymbol> nonTerminalSymbolList;
    private Map<GrammarSymbol, Integer> terminalIndexMap = new HashMap<>();
    private Map<GrammarSymbol, Integer> nonTerminalIndexMap = new HashMap<>();

    public TableGenerator(List<LR1ItemSet> itemSetList, Grammars grammars){
        this.itemSetList = itemSetList;
        this.grammars = grammars;
    }

    public void constructTableTemplate(){
        terminalSymbolList = grammars.getTerminalSymbol();
        nonTerminalSymbolList = grammars.getNonTerminalSymbol();
        for(GrammarSymbol terminalSymbol: terminalSymbolList){
            Template.ADD_TOKEN_NAME(terminalSymbol);
        }
        for(GrammarSymbol nonTerminalSymbol: nonTerminalSymbolList){
            Template.ADD_NON_TERMINAL(nonTerminalSymbol);
        }

        for(ProductionRule productionRule: grammars.getProductionList()){
            Template.ADD_PRODUCTIONS(productionRule);
        }


        for(LR1ItemSet lr1ItemSet: itemSetList){
            List<Integer> actions = getNullList(terminalSymbolList.size());
            List<Integer> gotos = getNullList(nonTerminalSymbolList.size());
            for(LR1Item lr1Item: lr1ItemSet.getItemList()){

                GrammarSymbol grammarSymbolBehindPoint = lr1Item.getGrammarSymbolBehindPoint();
                if(lr1Item.isTerminalItem()){

                    actions.set(getIndexOfTerminal(new GrammarSymbol("$", true)), Integer.MAX_VALUE);

                }
                else if(grammarSymbolBehindPoint!=null && grammarSymbolBehindPoint.isTerminal()){
                    LR1ItemSet tempSet = lr1ItemSet.getGoto(grammarSymbolBehindPoint);
                    if(tempSet!=null){
                        //System.out.println(grammarSymbolBehindPoint);
                        actions.set(getIndexOfTerminal(grammarSymbolBehindPoint), tempSet.getItemIdex());
                    }

                }
                else if(grammarSymbolBehindPoint==null){
                    actions.set(getIndexOfTerminal(lr1Item.getPredictSymbol()), -grammars.getIndexOfProductionRule(lr1Item.getProductionRule()));
                }

            }

            for(Map.Entry<GrammarSymbol, LR1ItemSet> entry: lr1ItemSet.getGotoMap().entrySet()){
                if(!entry.getKey().isTerminal()){
                    //System.out.println(entry.getKey());
                    gotos.set(getIndexOfNonTerminal(entry.getKey()), entry.getValue().getItemIdex());
                }
            }

            addTempleteStringOfActionAndGoto(actions, gotos);
        }

    }

    private void addTempleteStringOfActionAndGoto(List<Integer> actions, List<Integer> gotos){

        String actionString = "{";
        String gotoString = "{";

        for(Integer integer: actions){
            if(integer.equals(Integer.MIN_VALUE)){
                actionString += "null, ";
            }
            else {
                actionString += integer+", ";
            }
        }
        for(Integer integer: gotos){
            if(integer.equals(Integer.MIN_VALUE)){
                gotoString += "null, ";
            }
            else {
                gotoString += integer+", ";
            }
        }
        actionString += "},\n";
        gotoString += "},\n";

        Template.ADD_ACTIONS(actionString);
        Template.ADD_GOTOS(gotoString);
    }

    private int getIndexOfTerminal(GrammarSymbol gs){
        Integer result = terminalIndexMap.get(gs);
        if(result!=null){
            return result;
        }
        else {
            for(int i=0; i<terminalSymbolList.size(); i++){
                if(terminalSymbolList.get(i).equals(gs)){
                    terminalIndexMap.put(gs, i);
                    return i;
                }
            }

            return -1;
        }
    }

    private int getIndexOfNonTerminal(GrammarSymbol gs){
        Integer result = nonTerminalIndexMap.get(gs);
        if(result!=null){
            return result;
        }
        else {
            for(int i=0; i<nonTerminalSymbolList.size(); i++){
                if(nonTerminalSymbolList.get(i).equals(gs)){
                    nonTerminalIndexMap.put(gs, i);
                    return i;
                }
            }

            return -2;
        }
    }

    private List<Integer> getNullList(int length){
        List<Integer> result = new ArrayList<>();
        for(int i=0; i<length; i++){
            result.add(Integer.MIN_VALUE);
        }
        return result;
    }

}
