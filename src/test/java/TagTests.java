import com.clt.diamant.Grammar;
import dialogos.frame.utils.tags.GrammarIO;
import dialogos.frame.utils.tags.Token;
import dialogos.frame.utils.tags.TokenList;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class TagTests
{
    @Test
    public void testGrammarTags()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL tagFile = classLoader.getResource("tagging.json");
        assert tagFile != null;

        File file = new File(tagFile.getFile());
        List<Grammar> grammarList = GrammarIO.jsonToGrammars(file);

        assert grammarList != null;
        assert grammarList.size() == 4;
    }

    @Test
    public void testTokenizer()
    {
        String phrase = "I would like to order a burger and fries";
        TokenList tokenList = GrammarIO.tokenize(phrase);
        int length = phrase.split(" ").length;

        assert tokenList.size() == ((length * length + length) / 2);

        File file = new File("/Users/oliverwandschneider/develop/IdeaProjects/dialogos-plugin-frame/examples/foodOrder/foodOrderGrammars.xml");
        List<Grammar> grammarList = GrammarIO.xmlToGrammars(file);

        TokenList tokens = GrammarIO.tagTokenList(grammarList, phrase);

        GrammarIO.cleanupTokens(tokens);

        for (Token token : tokens)
        {
            System.out.println(token.getStartIndex() + ", "
                                       + token.getLower() + ", "
                                       + token.getEndIndex() + ", "
                                       + Arrays.toString(token.getTags().toArray()));
        }
    }

    @Test
    public void testGrammar()
    {
        Grammar grammar = new Grammar("Test", "root $Input;\n$Input = yes+;\n");
        try
        {
            com.clt.srgf.Grammar testGrammar = com.clt.srgf.Grammar.create(grammar.getGrammar());

            assert testGrammar.match("yes yes", null) != null;

            if (testGrammar.match("yes yes", null) != null)
            {
                System.out.println(testGrammar.match("yes yes", null));
            }

        } catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }

    @Test
    public void testGrammar2()
    {
        Grammar grammar = new Grammar("Test", "root $Input;\n$Input = $base+;\n$base = $test1 | $test2 | $test3;\n$test1 = was | ist | denn;\n$test2 = ergibt | wo | welche;\n$test3 = daher | denn;");
        try
        {
            com.clt.srgf.Grammar testGrammar = com.clt.srgf.Grammar.create(grammar.getGrammar());
            String input = "was ist daher";
            assert testGrammar.match(input, null) != null;

            if (testGrammar.match(input, null) != null)
            {
                System.out.println(testGrammar.match(input, null));
            }

        } catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }

    @Test
    public void testXML()
    {
        File file = new File("/Users/oliverwandschneider/develop/IdeaProjects/dialogos-plugin-frame/examples/grammars.xml");
        List<Grammar> grammars = GrammarIO.xmlToGrammars(file);
        grammars.forEach(grammar -> System.out.println(grammar.getName()));
    }

//    @Test
//    public void testGrammarTagging()
//    {
//        ClassLoader classLoader = getClass().getClassLoader();
//        URL tagFile = classLoader.getResource("tagging.json");
//        assert tagFile != null;
//
//        File file = new File(tagFile.getFile());
//        HashMap<String, String> grammarTags = new HashMap<>();
//
//        assert TagIO.jsonToTags(file, grammarTags);
//
//        TokenList tokens = TagIO.tagTokenList("Stade ist das denn für eine tolle methode die das hier berechnet", grammarTags);
//
//        System.out.println(tokens.toPretty());
//    }
}
