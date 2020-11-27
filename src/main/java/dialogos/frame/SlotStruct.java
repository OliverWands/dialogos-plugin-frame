package dialogos.frame;

import dialogos.frame.utils.tokens.Token;
import org.json.JSONObject;

import java.util.UUID;

public class SlotStruct implements Marshalling
{
    public final String ID = UUID.randomUUID().toString();
    private String name;
    private String value;
    private String query;
    private String grammarName;
    private boolean isAdditional;
    private boolean isFilled;

    public SlotStruct()
    {
    }

    public SlotStruct(String name)
    {
        this.name = name;
    }

    public SlotStruct setIsAdditional(boolean additional)
    {
        isAdditional = additional;
        return this;
    }

    public String getGrammarName()
    {
        return grammarName;
    }

    public SlotStruct setGrammarName(String grammarName)
    {
        this.grammarName = grammarName;
        return this;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public SlotStruct setQuery(String query)
    {
        this.query = query;
        return this;
    }

    public SlotStruct setValue(Token token)
    {
        if (token.getTags().contains(grammarName))
        {
            value = token.getLower();
            isFilled = true;
            return this;
        }
        isFilled = false;
        return this;
    }

    public String[] getContent()
    {
        return new String[]{name, grammarName, isAdditional ? "Yes" : "No", query};
    }

    public String getName()
    {
        return name;
    }

    public String getQuery()
    {
        return query;
    }

    public String getValue()
    {
        return value;
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

    public void removeValue()
    {
        value = null;
        isFilled = false;
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
        jsonObject.put("GRAMMAR_NAME", grammarName);

        return jsonObject;
    }

    @Override
    public boolean unmarshalStruct(JSONObject jsonObject)
    {
        for (String key : new String[]{"NAME", "QUERY", "IS_ADDITIONAL", "GRAMMAR_NAME"})
        {
            if (!jsonObject.has(key))
            {
                return false;
            }
        }

        name = jsonObject.getString("NAME");
        query = jsonObject.getString("QUERY");
        isAdditional = jsonObject.getBoolean("IS_ADDITIONAL");
        grammarName = jsonObject.getString("GRAMMAR_NAME");

        return true;
    }
}
