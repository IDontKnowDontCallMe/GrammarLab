import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
public class GrammarAnalyer {
private String[] grammarProductions = { "T -> S ", "S -> C C ", "C -> c C ", "C -> d ",  };
private String[] headOfProduction = {"T", "S", "C", "C", };
private Integer[] symbolNumOfProduction = {1, 2, 2, 1, };
private String[] tokenNames = {"c", "d", "$", };
private Integer[][] actions = {{3, 4, null, },
{null, null, 2147483647, },
{6, 7, null, },
{3, 4, null, },
{-3, -3, null, },
{null, null, -1, },
{6, 7, null, },
{null, null, -3, },
{-2, -2, null, },
{null, null, -2, },
};
private String[] nonTerminal = {"S", "C", };
private Integer[][] gotos = {{1, 2, },
{null, null, },
{null, 5, },
{null, 8, },
{null, null, },
{null, null, },
{null, 9, },
{null, null, },
{null, null, },
{null, null, },
};
private Map<String, Integer> tokenNameMap = new HashMap<>();
private Map<String, Integer> nonTerminalMap = new HashMap<>();
public static void main(String[] args){
String[][] sampleToken = {{"c","1"},{"c","1"}, {"d","1"},{"c","1"},{"d","1"},{"$","1"}};
GrammarAnalyer grammarAnalyer = new GrammarAnalyer();
grammarAnalyer.analyze(sampleToken);
}
public void analyze(String[][] tokens){
int tokenIndex = 0;
Stack<Integer> states = new Stack<>();
states.push(new Integer(0));
while(true){
int state = states.peek();
int actionIndex = getIndexOfTokenName(tokens[tokenIndex][0]);
Integer action = actions[state][actionIndex];
if(action==null){
System.out.println();
System.out.println("grammar error in token: "+tokenIndex);
return;
}
else if(action.equals(Integer.MAX_VALUE)){
System.out.println();
System.out.println("grammar analyze completed!");
return;
}
else if(action>=0){
tokenIndex++;
states.push(action);
}
else {
int shouldPopNum = symbolNumOfProduction[-action];
for(int i=0; i<shouldPopNum; i++){
states.pop();
}
int newState = gotos[states.peek()][getIndexOfNonTerminal(headOfProduction[-action])];
System.out.println(grammarProductions[-action]);
states.push(newState);
}
}
}
private int getIndexOfTokenName(String tokenName){
Integer result = tokenNameMap.get(tokenName);
if(result!=null){
return result;
}
else {
for(int i=0; i<tokenNames.length; i++){
if(tokenNames[i].equals(tokenName)){
tokenNameMap.put(tokenName, i);
return i;
}
}
return -1;
}
}
private int getIndexOfNonTerminal(String terminal){
Integer result = nonTerminalMap.get(terminal);
if(result!=null){
return result;
}
else {
for(int i=0; i<tokenNames.length; i++){
if(nonTerminal[i].equals(terminal)){
nonTerminalMap.put(terminal, i);
return i;
}
}
return -2;
}
}
}
