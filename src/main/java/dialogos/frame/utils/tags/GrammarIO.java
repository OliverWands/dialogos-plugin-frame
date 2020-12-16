package dialogos.frame.utils.tags;

import com.clt.diamant.Grammar;
import com.clt.xml.AbstractHandler;
import com.clt.xml.XMLReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.Attributes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GrammarIO
{
    /**
     * Extracts the grammar and defined tags from a JSON-File into a list.
     *
     * @param file The json file that contains the tags.
     */
    public static List<Grammar> jsonToGrammars(File file)
    {
        List<Grammar> grammarList = new ArrayList<>();
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
            return null;
        }

        JSONObject jsonObject = new JSONObject(content);
        for (String key : jsonObject.keySet())
        {
            Object value = jsonObject.get(key);
            if (value instanceof String)
            {
                Grammar grammar = new Grammar(key, jsonObject.getString(key));
                grammar.setId(UUID.randomUUID().toString());
                grammarList.add(grammar);
                continue;
            }

            //
            // Generates a simple grammar from alternatives given as a JSONArray
            //
            if (value instanceof JSONArray)
            {
                JSONArray array = jsonObject.getJSONArray(key);
                grammarList.add(new Grammar(key, buildGrammarVariable(key, array)));
            }
        }

        return grammarList;
    }

    public static List<Grammar> xmlToGrammars(File file)
    {
        if (file != null)
        {
            List<Grammar> grammarList = new ArrayList<>();
            XMLReader reader = new XMLReader(false);

            try
            {
                reader.parse(file, new AbstractHandler("grammars")
                {
                    @Override
                    protected void start(String name, Attributes atts)
                    {
                        final Grammar grammar = new Grammar("grammar");
                        grammar.setId(atts.getValue(UUID.randomUUID().toString()));
                        grammarList.add(grammar);
                        reader.setHandler(new AbstractHandler("grammar")
                        {
                            @Override
                            protected void end(String grammarElem)
                            {
                                if (grammarElem.equals("name"))
                                {
                                    grammar.setName(this.getValue());
                                }
                                else if (grammarElem.equals("value"))
                                {
                                    grammar.setGrammar(this.getValue());
                                }
                            }
                        });
                    }
                });

                return grammarList;
            } catch (IOException exp)
            {
                exp.printStackTrace();
            }
        }

        return null;
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
     * @return Formatted string that gives information about the composition of the tags in the file. -1 if error.
     */
    public static String fileToGrammarInfo(File tagFile)
    {
        List<Grammar> grammars = null;
        if (tagFile.getName().endsWith("json"))
        {
            grammars = GrammarIO.jsonToGrammars(tagFile);
        }
        else if (tagFile.getName().endsWith("xml"))
        {
            grammars = GrammarIO.xmlToGrammars(tagFile);
        }

        return String.format("Contains %d grammars.", grammars != null ? grammars.size() : -1);
    }

    public static TokenList tagTokenList(List<Grammar> grammars, String input)
    {
        TokenList tokenList = tokenize(input);
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

    public static TokenList cleanupTokens(TokenList tokens)
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

    public static TokenList tokenize(String input)
    {
        input = input.replaceAll("\\p{Punct}", "");
        String[] tokens = input.split(" ");
        TokenList tokenList = new TokenList();

        for (int inx = 1; inx <= input.length(); inx++)
        {
            for (int jnx = 0; jnx < tokens.length; jnx++)
            {
                if ((inx + (jnx - 1)) < tokens.length)
                {
                    StringBuilder content = new StringBuilder();
                    String prefix = "";
                    for (int knx = 0; knx < inx; knx++)
                    {
                        content.append(prefix);
                        prefix = " ";
                        content.append(tokens[jnx + knx]);
                    }

                    tokenList.add(new Token(content.toString(), jnx, jnx + inx));
                }
            }
        }

        return tokenList;
    }
}
