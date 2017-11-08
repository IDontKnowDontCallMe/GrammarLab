import java.util.*;

/**
 * Created by USER on 2017/11/7.
 */
public class LR1ItemSet {

    private List<LR1Item> itemList = new ArrayList<>();
    private Set<LR1Item> itemSet = new HashSet<>();

    private int itemIdex = -10;
    private Map<GrammarSymbol, LR1ItemSet> gotoMap = new HashMap<>();

    public LR1ItemSet(){}

    public void addLR1Item(LR1Item lr1Item){
        if(this.itemSet.add(lr1Item)){
            this.itemList.add(lr1Item);
        }
    }

    public List<LR1Item> getItemList(){
        return this.itemList;
    }

    public void setGoto(GrammarSymbol gs, LR1ItemSet lr1ItemSet){
        this.gotoMap.put(gs, lr1ItemSet);
    }

    public LR1ItemSet getGoto(GrammarSymbol gs){
        return gotoMap.get(gs);
    }

    public Map<GrammarSymbol, LR1ItemSet> getGotoMap(){
        return gotoMap;
    }

    public int getItemIdex() {
        return itemIdex;
    }

    public void setItemIdex(int itemIdex) {
        this.itemIdex = itemIdex;
    }

    @Override
    public boolean equals(Object o){
        if(o==null) return false;
        if(o==this) return true;
        if(!(o instanceof LR1ItemSet)) return false;

        LR1ItemSet ls = (LR1ItemSet) o;

        return this.itemSet.equals(ls.itemSet);
    }

    @Override
    public int hashCode(){
        return Arrays.hashCode(itemSet.toArray());
    }


}
