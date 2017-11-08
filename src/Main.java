import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class Main {

    private static String[] grammars = {
                                "T->S",
                                "S->CC",
                                "C->`c`C",
                                "C->`d`",
                                };


    public static void main(String[] args) throws IOException{
        Grammars grammar = new Grammars(Main.grammars);
        //grammar.print();
        System.out.println();

        CollectionGenerator collectionGenerator = new CollectionGenerator(grammar);
        List<LR1ItemSet> itemSetList =  collectionGenerator.constructCollection();

        TableGenerator tableGenerator = new TableGenerator(itemSetList, grammar);
        tableGenerator.constructTableTemplate();


        FileOutputStream writerStream = new FileOutputStream("GrammarAnalyer.java");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));


        writer.write(Template.GET_ANALYER_CLASS());
        writer.flush();
        writer.close();

    }
}
