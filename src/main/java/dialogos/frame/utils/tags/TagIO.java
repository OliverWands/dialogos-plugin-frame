package dialogos.frame.utils.tags;

import dialogos.frame.utils.tokens.Token;
import dialogos.frame.utils.tokens.TokenList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TagIO
{
    private static final String DEFINED = "defined";
    private static final String GRAMMAR = "grammar";

    /**
     * Extracts the grammar and defined tags from a JSON-File into maps that contain the string value as key and the
     * tag as value.
     *
     * @param file        The json file that contains the tags.
     * @param definedTags This map will be filled with the defined tags.
     * @param grammarMap  This map will be filled with the grammar.
     */
    public static void jsonToTags(File file, Map<String, String> definedTags, Map<String, String> grammarMap)
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
            return;
        }

        JSONObject jsonObject = new JSONObject(content);
        for (String key : jsonObject.keySet())
        {
            JSONObject current = jsonObject.getJSONObject(key);
            if (current.has(DEFINED))
            {
                JSONArray values = current.getJSONArray(DEFINED);
                for (int inx = 0; inx < values.length(); inx++)
                {
                    definedTags.put(values.getString(inx), key);
                }

                System.out.println(buildGrammarVariable(key, values));
            }

            if (current.has(GRAMMAR))
            {
                String grammar = current.getString(GRAMMAR);
                grammarMap.put(key, grammar);
            }
        }
    }

    private static String buildGrammarVariable(String key, JSONArray array)
    {
        String grammarVar = "$" + key + " = ";
        for (int inx = 0; inx < array.length(); inx++)
        {
            grammarVar += array.getString(inx) + " | ";
        }
        grammarVar = grammarVar.substring(0, grammarVar.length() - 3);
        grammarVar += ";";

        return grammarVar;
    }

    /**
     * Creates a map that has he tag as key and the number of occurrences of this tag as value
     *
     * @param tags The map containing the tags
     * @return A map of tags and longs
     */
    public static Map<String, Long> tagSetCount(Map<String, String> tags)
    {
        return tags.values().stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    /**
     * Calculates the total amount of individual (duplicates will not be counted) tags in two maps.
     *
     * @param tags1 The first map.
     * @param tags2 The second map.
     * @return The number of individual tags.
     */
    public static int countTags(Map<String, String> tags1, Map<String, String> tags2)
    {
        Set<String> count = new HashSet<>();
        count.addAll(tags1.values());
        count.addAll(tags2.values());
        return count.size();
    }

    /**
     * Collect information about the tags
     *
     * @param tagFile File that contains the tags
     * @return Formatted string that gives information about the composition of the tags in the file.
     */
    public static String fileToTagMaps(File tagFile, Map<String, String> tags1, Map<String, String> tags2)
    {
        TagIO.jsonToTags(tagFile, tags1, tags2);
        int counts = TagIO.countTags(tags1, tags2);

        return String.format("Contains %d tags,\n%d words and %d grammars.",
                counts, tags1.size(), tags2.size());
    }

    /**
     * @param tokenList
     * @param definedMap
     * @param grammarMap
     */
    public static void tagTokenList(TokenList tokenList, Map<String, String> definedMap, Map<String, String> grammarMap)
    {
        for (Token token : tokenList)
        {
            String tag = definedMap.getOrDefault(token.getLower(), null);
            if (tag != null)
            {
                token.setTag(tag);
            }

            for (String pattern : grammarMap.keySet())
            {
                if (token.getLower().matches(pattern))
                {
                    token.setTag(grammarMap.get(pattern));
                }
            }
        }
    }
}
