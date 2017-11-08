/**
 * Created by USER on 2017/11/8.
 */
public class Template {


    private static String GRAMMAR_PRODUCTIONS = "";
    private static String HEAD_OF_PRODUCTION = "";
    private static String SYMBOL_NUM_OF_PRODUCTION = "";
    //终结符的字符串字面值(ACTION表的表头)
    private static String TOKEN_NAMES = "";
    private static String ACTIONS = "";
    //非终结符的字符串的字面值(GOTO表的表头)
    private static String NON_TERMINAL = "";
    private static String GOTOS = "";

    public static void ADD_PRODUCTIONS(ProductionRule productionRule){

        Template.GRAMMAR_PRODUCTIONS += "\"" + productionRule.toString() + "\", ";
        Template.HEAD_OF_PRODUCTION += "\"" + productionRule.getProductionHead().getValue() + "\", ";
        int num = productionRule.getProductionBody().size();
        Template.SYMBOL_NUM_OF_PRODUCTION += num +", ";
    }

    public static void ADD_TOKEN_NAME(GrammarSymbol terminal){
        Template.TOKEN_NAMES += "\"" + terminal.getValue() + "\", ";
    }

    public static void ADD_NON_TERMINAL(GrammarSymbol nonTerminal){
        Template.NON_TERMINAL += "\"" + nonTerminal.getValue() + "\", ";
    }

    public static void ADD_ACTIONS(String s){
        Template.ACTIONS += s;
    }

    public static void ADD_GOTOS(String s){
        Template.GOTOS += s;
    }

    public static String GET_ANALYER_CLASS(){

        return
            "import java.util.HashMap;\n"+
            "import java.util.Map;\n"+
            "import java.util.Stack;\n"+

            "public class GrammarAnalyer {\n"+

            "private String[] grammarProductions = { " + Template.GRAMMAR_PRODUCTIONS + " };\n"+
            "private String[] headOfProduction = {" + Template.HEAD_OF_PRODUCTION + "};\n"+
            "private Integer[] symbolNumOfProduction = {" + Template.SYMBOL_NUM_OF_PRODUCTION + "};\n"+
            "private String[] tokenNames = {" + Template.TOKEN_NAMES + "};\n"+
            "private Integer[][] actions = {" + Template.ACTIONS + "};\n"+
            "private String[] nonTerminal = {" + Template.NON_TERMINAL + "};\n"+
            "private Integer[][] gotos = {" + Template.GOTOS + "};\n"+

            "private Map<String, Integer> tokenNameMap = new HashMap<>();\n"+
            "private Map<String, Integer> nonTerminalMap = new HashMap<>();\n"+

            "public static void main(String[] args){\n"+

                "String[][] sampleToken = {{\"c\",\"1\"},{\"c\",\"1\"}, {\"d\",\"1\"},{\"c\",\"1\"},{\"d\",\"1\"},{\"$\",\"1\"}};\n"+

                "GrammarAnalyer grammarAnalyer = new GrammarAnalyer();\n"+
                "grammarAnalyer.analyze(sampleToken);\n"+
            "}\n"+

            "public void analyze(String[][] tokens){\n"+
                "int tokenIndex = 0;\n"+

                "Stack<Integer> states = new Stack<>();\n"+
                "states.push(new Integer(0));\n"+

                "while(true){\n"+
                    "int state = states.peek();\n"+
                    "int actionIndex = getIndexOfTokenName(tokens[tokenIndex][0]);\n"+

                    "Integer action = actions[state][actionIndex];\n"+

                    "if(action==null){\n"+
                        "System.out.println();\n"+
                        "System.out.println(\"grammar error in token: \"+tokenIndex);\n"+
                        "return;\n"+
                    "}\n"+
                    "else if(action.equals(Integer.MAX_VALUE)){\n"+
                        "System.out.println();\n"+
                        "System.out.println(\"grammar analyze completed!\");\n"+
                        "return;\n"+
                    "}\n"+
                    "else if(action>=0){\n"+
                        "tokenIndex++;\n"+
                        "states.push(action);\n"+
                    "}\n"+
                    "else {\n"+
                        "int shouldPopNum = symbolNumOfProduction[-action];\n"+
                        "for(int i=0; i<shouldPopNum; i++){\n"+
                            "states.pop();\n"+
                        "}\n"+

                        "int newState = gotos[states.peek()][getIndexOfNonTerminal(headOfProduction[-action])];\n"+
                        "System.out.println(grammarProductions[-action]);\n"+

                        "states.push(newState);\n"+
                    "}\n"+

                "}\n"+


            "}\n"+

            "private int getIndexOfTokenName(String tokenName){\n"+
                "Integer result = tokenNameMap.get(tokenName);\n"+
                "if(result!=null){\n"+
                    "return result;\n"+
                "}\n"+
                "else {\n"+
                    "for(int i=0; i<tokenNames.length; i++){\n"+
                        "if(tokenNames[i].equals(tokenName)){\n"+
                            "tokenNameMap.put(tokenName, i);\n"+
                            "return i;\n"+
                        "}\n"+
                    "}\n"+

                    "return -1;\n"+
                "}\n"+
            "}\n"+

            "private int getIndexOfNonTerminal(String terminal){\n"+
                "Integer result = nonTerminalMap.get(terminal);\n"+
                "if(result!=null){\n"+
                    "return result;\n"+
                "}\n"+
                "else {\n"+
                    "for(int i=0; i<tokenNames.length; i++){\n"+
                        "if(nonTerminal[i].equals(terminal)){\n"+
                            "nonTerminalMap.put(terminal, i);\n"+
                            "return i;\n"+
                        "}\n"+
                    "}\n"+

                    "return -2;\n"+
                "}\n"+
            "}\n"+


        "}\n";


    }

}
