package dialogos.frame.utils.tags;

import com.clt.diamant.Grammar;
import dialogos.frame.utils.tokens.FrameTokenizer;
import dialogos.frame.utils.tokens.Token;
import dialogos.frame.utils.tokens.TokenList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagIO
{
    /**
     * Extracts the grammar and defined tags from a JSON-File into maps that contain the string value as key and the
     * tag as value.
     *
     * @param file       The json file that contains the tags.
     * @param grammarMap This map will be filled with the grammars for each tag.
     */
    public static boolean jsonToTags(File file, Map<String, String> grammarMap)
    {
        String content = null;
        try
        {
            content = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset().name());
            content = content.toLowerCase();
            content = content.replaceAll("\\n", "");
            content = content.replaceAll(" {2,}", " ");
        } catch (IOException exp)
        {
            exp.printStackTrace();
        }

        if (content == null)
        {
            return false;
        }

        JSONObject jsonObject = new JSONObject(content);
        for (String key : jsonObject.keySet())
        {
            Object value = jsonObject.get(key);
            if (value instanceof String)
            {
                String grammarString = jsonObject.getString(key);
                grammarMap.put(key, grammarString);
                continue;
            }

            //
            // Generates a simple grammar from alternatives given as a JSONArray
            //
            if (value instanceof JSONArray)
            {
                JSONArray array = jsonObject.getJSONArray(key);
                grammarMap.put(key, buildGrammarVariable(key, array));
            }
        }

        return true;
    }

    public static boolean xmlToTags(File file, Map<String, String> grammarMap)
    {
        return false;
    }

    /**
     * To be used when the JSON-Tag-File contains a json array of alternatives.
     * This method will format them to be an OR-separated grammar.
     *
     * @param key   The name of the non-terminal.
     * @param array The Json-Array that contains the alternatives.
     * @return A properly formatted OR-separated grammar.
     */
    private static String buildGrammarVariable(String key, JSONArray array)
    {
        StringBuilder builder = new StringBuilder(String.format("root $%s;$%s = ", key, key));
        for (int inx = 1; inx < array.length() - 1; inx++)
        {
            builder.append(array.getString(inx));
            builder.append(" | ");
        }
        builder.append(array.getString(array.length() - 1).toLowerCase());
        builder.append(";");
        return builder.toString();
    }

    /**
     * Get formatted string that gives information about the grammars.
     *
     * @param tagFile File that contains the tags
     * @return Formatted string that gives information about the composition of the tags in the file.
     */
    public static String fileToGrammarInfo(File tagFile)
    {
        HashMap<String, String> tags = new HashMap<>();
        TagIO.jsonToTags(tagFile, tags);

        return String.format("Contains %d grammars.", tags.size());
    }

    public TokenList tagTokenList(List<Grammar> grammars, String input)
    {
        TokenList tokenList = FrameTokenizer.tokenize(input);
        for (Grammar grammar : grammars)
        {
            try
            {
                com.clt.srgf.Grammar testGrammar = com.clt.srgf.Grammar.create(grammar.getGrammar());

                for (Token token : tokenList)
                {
                    if (testGrammar.match(token.getLower(), null) != null)
                    {
                        token.addTag(grammar.getName());
                    }
                }
            } catch (Exception exp)
            {
                exp.printStackTrace();
            }
        }

        return tokenList;
    }

    public TokenList cleanupTokens(TokenList tokens)
    {
        TokenList cleaned = new TokenList();
        tokens.removeIf(token -> token.getTags().isEmpty());

        for (int inx = 0; inx < tokens.size(); inx++)
        {
            Token token = tokens.get(inx);
            for (int jnx = 0; jnx < token.size(); jnx++)
            {
                if (inx == jnx)
                {
                    continue;
                }
                Token compareTkn = tokens.get(jnx);

                if (token.getStartIndex() == compareTkn.getStartIndex())
                {
                    if (token.containsSomeTags(compareTkn))
                    {
                        if (token.size() == compareTkn.size())
                        {
                            cleaned.add(token);
                            cleaned.add(compareTkn);
                        }
                        else if (token.size() > compareTkn.size())
                        {
                            cleaned.add(token);
                        }
                        else
                        {
                            cleaned.add(compareTkn);
                        }
                    }
                }
            }
        }

        return cleaned;
    }
}
