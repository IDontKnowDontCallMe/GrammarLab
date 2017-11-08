import java.util.*;

/**
 * Created by USER on 2017/11/7.
 */
public class CollectionGenerator {

    private Grammars grammars = null;

    private List<LR1ItemSet> allItemList = new ArrayList<>();
    private Set<LR1ItemSet> allItemSet = new HashSet<>();


    public CollectionGenerator(Grammars grammars){
        this.grammars = grammars;
    }

    public List<LR1ItemSet> constructCollection(){

        LR1ItemSet startSet = new LR1ItemSet();
        startSet.addLR1Item(new LR1Item(grammars.getProductionRule(0), 0, grammars.getDollorSymbol()));

        startSet = getClosure(startSet);
        startSet.setItemIdex(0);
        allItemSet.add(startSet);
        allItemList.add(startSet);

//        System.out.println("---------------start items--------------------");
//        for(LR1Item lr1Item: startSet.getItemList()){
//            System.out.println(lr1Item);
//        }

        int index = 0;
        while(index < allItemList.size()){
            LR1ItemSet preItemSet = allItemList.get(index);

            for(GrammarSymbol grammarSymbol: grammars.getAllGrammarSymbols()){
                LR1ItemSet temp = getGoto(preItemSet, grammarSymbol);

                if(temp.getItemList().size()==0){
                    continue;
                }
                else if( !allItemSet.contains(temp)){
                    temp.setItemIdex(allItemList.size());
                    allItemSet.add(temp);
                    allItemList.add(temp);
                }
                else {
                    temp.setItemIdex(allItemList.indexOf(temp));
                }

                preItemSet.setGoto(grammarSymbol, temp);
                //System.out.println(preItemSet.getItemIdex() + " " + grammarSymbol + " " + temp.getItemIdex());
            }

            index++;
        }

        for(LR1ItemSet lr1ItemSet: allItemList){

            System.out.println("--------------- I"+ lr1ItemSet.getItemIdex()  +" --------------------");
            for(LR1Item lr1Item: lr1ItemSet.getItemList()){
                System.out.println(lr1Item);
            }
            System.out.println();

            Map<GrammarSymbol, LR1ItemSet> gotoMap = lr1ItemSet.getGotoMap();
            for(Map.Entry<GrammarSymbol, LR1ItemSet> entry: gotoMap.entrySet()){
                System.out.println("from "+entry.getKey()+" goto I"+entry.getValue().getItemIdex());
            }

            System.out.println("---------------------------------------");
            System.out.println();
        }

        return allItemList;

    }


    private LR1ItemSet getClosure(LR1ItemSet lr1ItemSet){

        List<LR1Item> lr1ItemList = lr1ItemSet.getItemList();

        int index = 0;
        while(index < lr1ItemList.size()){
            LR1Item lr1Item = lr1ItemList.get(index);

            GrammarSymbol grammarSymbolBehindPoint = lr1Item.getGrammarSymbolBehindPoint();
            if(grammarSymbolBehindPoint==null || grammarSymbolBehindPoint.isTerminal()){//如果点后面没有文法符号或是终结符，不考虑这个项
                index++;
                continue;
            }
            else{
                List<GrammarSymbol> symbolsBehindNonTerminal = lr1Item.getSymbolsfrom(lr1Item.getPointIndex()+1, lr1Item.getProductionRule().getProductionBody().size());
                symbolsBehindNonTerminal.add(lr1Item.getPredictSymbol());

                List<ProductionRule> rules = grammars.getProductionRulesWithHead(grammarSymbolBehindPoint);

                for(ProductionRule rule: rules){
                    for(GrammarSymbol symbol: grammars.getFirst(symbolsBehindNonTerminal)){
                        LR1Item temp = new LR1Item(rule, 0, symbol);
                        lr1ItemSet.addLR1Item(temp);

                    }
                }
            }

            index++;
        }

        return lr1ItemSet;
    }

    private LR1ItemSet getGoto(LR1ItemSet lr1ItemSet, GrammarSymbol symbol){

        LR1ItemSet result = new LR1ItemSet();

        for(LR1Item lr1Item: lr1ItemSet.getItemList()){
            if (lr1Item.getGrammarSymbolBehindPoint()!=null &&lr1Item.getGrammarSymbolBehindPoint().equals(symbol)){
                LR1Item newItem = new LR1Item(lr1Item.getProductionRule(), lr1Item.getPointIndex()+1, lr1Item.getPredictSymbol());
                result.addLR1Item(newItem);

            }
        }

        result = getClosure(result);


        return result;
    }

}
