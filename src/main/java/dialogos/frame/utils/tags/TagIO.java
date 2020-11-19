package dialogos.frame.utils.tags;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
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
    public static String fileToTagMaps(File tagFile)
    {
        HashMap<String, String> tags = new HashMap<>();
        TagIO.jsonToTags(tagFile, tags);

        return String.format("Contains %d grammars.", tags.size());
    }
}
