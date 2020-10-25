package dialogos.frame.utils;

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
    private static final String REGEX = "regex";

    public static void jsonToTags(File file, Map<String, String> definedTags, Map<String, String> regexTags)
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
            definedTags = null;
            regexTags = null;
            return;
        }

        JSONObject jsonObject = new JSONObject(content);
        for (String key : jsonObject.keySet())
        {
            JSONObject current = jsonObject.getJSONObject(key);
            for (String arrKey : new String[]{DEFINED, REGEX})
            {
                JSONArray values = current.getJSONArray(arrKey);
                for (int inx = 0; inx < values.length(); inx++)
                {
                    if (arrKey.equals(DEFINED))
                    {
                        definedTags.put(values.getString(inx), key);
                    }
                    else if (arrKey.equals(REGEX))
                    {
                        regexTags.put(values.getString(inx), key);
                    }
                }
            }
        }
    }

    public static Map<String, Long> tagSetCount(Map<String, String> tags)
    {
        return tags.values().stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    public static int countTags(Map<String, String> tags1, Map<String, String> tags2)
    {
        Set<String> count = new HashSet<>();
        count.addAll(tags1.values());
        count.addAll(tags2.values());
        return count.size();
    }
}
