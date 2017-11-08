import sun.awt.Symbol;

import java.util.*;

/**
 * Created by USER on 2017/11/7.
 */
public class Grammars {

    private List<ProductionRule> rules = new ArrayList<>();

    private Set<GrammarSymbol> symbols = new HashSet<>();

    private List<GrammarSymbol> symbolsList = new ArrayList<>();

    private List<GrammarSymbol> terminalSymbol = new ArrayList<>();

    private List<GrammarSymbol> nonTerminalSymbol = new ArrayList<>();

    private GrammarSymbol dollorSymbol = new GrammarSymbol("$", true);

    private Map<GrammarSymbol, Set<GrammarSymbol>> firstMap = new HashMap<>();

    public Grammars(String[] productionStrings){

        for(String string : productionStrings){
            dealOneProductionString(string);
        }

        symbols.add(dollorSymbol);
        terminalSymbol.add(dollorSymbol);
        symbolsList.add(dollorSymbol);
    }

    private void dealOneProductionString(String string){
        String[] strings = string.split("->");

        GrammarSymbol productionHead = new GrammarSymbol(strings[0], false);
        symbols.add(productionHead);
        ProductionRule productionRule = new ProductionRule(productionHead);

        int index=0;
        String bodyString = strings[1];
        while(index < bodyString.length()){
            //遇到空白忽略
            if(bodyString.charAt(index)==' '||bodyString.charAt(index)=='\t'){
                index++;
                continue;
            }
            if(bodyString.charAt(index)=='`'){//找到一个终结符并加入
                index++;
                int start = index;
                while (bodyString.charAt(index) != '`') {
                    index++;
                }
                GrammarSymbol symbol = new GrammarSymbol(bodyString.substring(start, index), true);
                productionRule.addBody(symbol);
                symbols.add(symbol);
                symbolsList.add(symbol);
                if(terminalSymbol.indexOf( symbol)==-1){
                    terminalSymbol.add(symbol);
                }
                index++;
            }
            else {
                GrammarSymbol symbol = new GrammarSymbol(bodyString.substring(index, index+1), false);
                productionRule.addBody(symbol);
                symbols.add(symbol);
                symbolsList.add(symbol);
                if(nonTerminalSymbol.indexOf( symbol)==-1){
                    nonTerminalSymbol.add(symbol);
                }
                index++;
            }
        }

        rules.add(productionRule);
    }

    public ProductionRule getProductionRule(int index){
        return rules.get(index);
    }

    public List<ProductionRule> getProductionList(){
        return this.rules;
    }

    public List<GrammarSymbol> getAllGrammarSymbols(){
        return this.symbolsList;
    }

    public List<GrammarSymbol> getTerminalSymbol(){
        return this.terminalSymbol;
    }

    public List<GrammarSymbol> getNonTerminalSymbol(){
        return this.nonTerminalSymbol;
    }

    public GrammarSymbol getDollorSymbol(){
        return this.dollorSymbol;
    }

    public void print(){

        for(ProductionRule rule: rules){
            System.out.println(rule);
        }

    }

    public int getIndexOfProductionRule(ProductionRule productionRule){
        return rules.indexOf(productionRule);
    }

    public List<ProductionRule> getProductionRulesWithHead(GrammarSymbol headSymbol){
        List<ProductionRule> result = new ArrayList<>();

        for(ProductionRule productionRule: this.rules){
            if(productionRule.getProductionHead().equals(headSymbol)){
                result.add(productionRule);
            }
        }

        return result;
    }

    public List<GrammarSymbol> getFirst(List<GrammarSymbol> symbolList){

        Set<GrammarSymbol> result = new HashSet<>();

        for(GrammarSymbol symbol: symbolList){

            Set<GrammarSymbol> tempSet = getFirst(symbol);
            result.addAll(tempSet);

            if(!tempSet.contains(new GrammarSymbol("epsilon", true, true))){
                result.remove(new GrammarSymbol("epsilon", true, true));
                break;
            }
        }

        List<GrammarSymbol> result2 = new ArrayList<>();
        result2.addAll(result);

        return result2;
    }

    public Set<GrammarSymbol> getFirst(GrammarSymbol symbol){
        if(firstMap.get(symbol)!=null){
            return firstMap.get(symbol);
        }

        Set<GrammarSymbol> result = new HashSet<>();

        if(symbol.isTerminal()){
            result.add(symbol);
            firstMap.put(symbol, result);
            return result;
        }

        List<ProductionRule> rules = getProductionRulesWithHead(symbol);

        for(ProductionRule rule: rules){
            if(rule.isEpsilonProduction()){
                result.add(new GrammarSymbol("epsilon", true, true));
                continue;
            }

            List<GrammarSymbol> bodySymbols = rule.getProductionBody();
            int index = 0;
            while(true){
                Set<GrammarSymbol> tempSymbols = getFirstOfRecursion(bodySymbols.get(index), new HashMap<>());
                result.addAll(tempSymbols);

                //如果FIRST(bodySymbols.get(index))含有epsilon, 要继续; 否则, 不用再添加, 开始下一条产生式
                if(tempSymbols.contains(new GrammarSymbol("epsilon", true, true))){
                    index++;
                    if(index<bodySymbols.size()){
                        continue;
                    }
                    else {
                        break;
                    }
                }
                else {
                    break;
                }
            }
        }

        return result;
    }

    private Set<GrammarSymbol> getFirstOfRecursion(GrammarSymbol symbol, Map<GrammarSymbol, Boolean> hasMeetedMap){

        Set<GrammarSymbol> result = new HashSet<>();

        if(symbol.isTerminal()){
            result.add(symbol);
            firstMap.put(symbol, result);
            return result;
        }

        //说明遇到过当前文法符号, 可能存在左递归或间接左递归, 返回空即可; 或者这样理解, 每个非终结符只找最多一次
        if(hasMeetedMap.get(symbol)!=null){
            return result;
        }
        else {
            hasMeetedMap.put(symbol, true);
        }

        List<ProductionRule> rules = getProductionRulesWithHead(symbol);

        for(ProductionRule rule: rules){

            GrammarSymbol symbol1 = rule.getProductionBody().get(0);
            if(symbol1.isTerminal()){
                result.add(symbol1);
            }
            else {
                result.addAll(getFirstOfRecursion(symbol1, hasMeetedMap));
            }
        }

        firstMap.put(symbol, result);
        return result;
    }

}
