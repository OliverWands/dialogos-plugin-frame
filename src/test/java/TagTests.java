import dialogos.frame.utils.tags.TagIO;
import dialogos.frame.utils.tokens.FrameTokenizer;
import dialogos.frame.utils.tokens.TokenList;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class TagTests
{
    @Test
    public void testGrammarTags()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL tagFile = classLoader.getResource("tagging.json");
        assert tagFile != null;

        File file = new File(tagFile.getFile());
        HashMap<String, String> grammarTags = new HashMap<>();

        assert TagIO.jsonToTags(file, grammarTags);

        assert grammarTags.size() == 4;

        System.out.println(new JSONObject(grammarTags).toString(4));
    }

    @Test
    public void testTokenizer()
    {
        String phrase = "Ich möchte von Stade zu den Landungsbrücken";
        TokenList tokenList = FrameTokenizer.tokenize(phrase);
        int length = phrase.split(" ").length;

        assert tokenList.size() == ((length * length + length) / 2);
    }
}
