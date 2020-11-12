package dialogos.frame;

import dialogos.frame.utils.tokens.Token;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlotStruct implements Marshalling
{
    private String name;
    private String value;
    private String query;
    private List<String> matchedTags;
    private boolean isAdditional;
    private boolean isFilled;

    public SlotStruct()
    {
    }

    public SlotStruct(String name)
    {
        this.name = name;
    }

    public void removeValue()
    {
        value = null;
        isFilled = false;
    }

    public SlotStruct setIsAdditional(boolean additional)
    {
        isAdditional = additional;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public SlotStruct setValue(Token token)
    {
        for (String tag : matchedTags)
        {
            if (token.getTags().contains(tag))
            {
                value = token.getLower();
                isFilled = true;
                return this;
            }
        }
        isFilled = false;
        return this;
    }

    public String getQuery()
    {
        return query;
    }

    public SlotStruct setQuery(String query)
    {
        this.query = query;
        return this;
    }

    public List<String> getMatchedTags()
    {
        return matchedTags;
    }

    public SlotStruct setMatchedTags(String[] matchedTags)
    {
        this.matchedTags = Arrays.asList(matchedTags);
        return this;
    }

    public SlotStruct setMatchedTags(List<String> matchedTags)
    {
        this.matchedTags = matchedTags;
        return this;
    }

    public boolean isAdditional()
    {
        return isAdditional;
    }

    public boolean isFilled()
    {
        return isFilled;
    }

    public boolean isNotFilled()
    {
        return !isFilled;
    }

    public boolean isComplete()
    {
        return isFilled || isAdditional;
    }

    public String[] getContent()
    {
        StringBuilder builder = new StringBuilder();
        for (String tag : matchedTags)
        {
            builder.append(tag);
            builder.append(", ");
        }
        return new String[]{name, builder.toString(), isAdditional ? "Yes" : "No", query};
    }

    @Override
    public JSONObject marshal()
    {
        JSONObject jsonObject = marshalStruct();
        jsonObject.put("VALUE", value);
        jsonObject.put("IS_FILLED", isFilled);

        return jsonObject;
    }

    @Override
    public boolean unmarshal(JSONObject jsonObject)
    {
        return false;
    }

    @Override
    public JSONObject marshalStruct()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("NAME", name);
        jsonObject.put("QUERY", query);
        jsonObject.put("IS_ADDITIONAL", isAdditional);
        jsonObject.put("MATCHED_TAGS", new JSONArray(matchedTags));

        return jsonObject;
    }

    @Override
    public boolean unmarshalStruct(JSONObject jsonObject)
    {
        for (String key : new String[]{"NAME", "QUERY", "IS_ADDITIONAL", "MATCHED_TAGS"})
        {
            if (!jsonObject.has(key))
            {
                return false;
            }
        }

        name = jsonObject.getString("NAME");
        query = jsonObject.getString("QUERY");
        isAdditional = jsonObject.getBoolean("IS_ADDITIONAL");
        JSONArray jsonArray = jsonObject.getJSONArray("MATCHED_TAGS");
        matchedTags = new ArrayList<>();
        for (int inx = 0; inx < jsonArray.length(); inx++)
        {
            matchedTags.add(inx, jsonArray.getString(inx));
        }

        return true;
    }
}
