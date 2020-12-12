package dialogos.frame;

import com.clt.diamant.IdentityObject;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import dialogos.frame.utils.tokens.Token;
import org.json.JSONObject;

import java.util.UUID;

public class SlotStruct implements Marshalling, IdentityObject
{
    private String id = UUID.randomUUID().toString();
    private String name;
    private String value;
    private String query;
    private String grammarName;
    private boolean isFilled;

    public SlotStruct()
    {
    }

    public SlotStruct(String name)
    {
        this.name = name;
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

    public SlotStruct setName(String name)
    {
        this.name = name;
        return this;
    }

    public SlotStruct setQuery(String query)
    {
        this.query = query;
        return this;
    }

    public SlotStruct setValue(String value)
    {
        this.value = value;
        isFilled = !this.value.equals("");
        return this;
    }

    @Deprecated
    public SlotStruct setTokenValue(Token token)
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
        return new String[]{name, grammarName, query};
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

    public boolean isFilled()
    {
        return isFilled;
    }

    public String removeValue()
    {
        String val = value;
        value = null;
        isFilled = false;
        return val;
    }

    @Override
    public JSONObject marshal()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("NAME", name);
        jsonObject.put("QUERY", query);
        jsonObject.put("GRAMMAR_NAME", grammarName);

        return jsonObject;
    }

    @Override
    public boolean unmarshal(JSONObject jsonObject)
    {
        for (String key : new String[]{"NAME", "QUERY", "GRAMMAR_NAME"})
        {
            if (!jsonObject.has(key))
            {
                return false;
            }
        }

        name = jsonObject.getString("NAME");
        query = jsonObject.getString("QUERY");
        grammarName = jsonObject.getString("GRAMMAR_NAME");

        return true;
    }

    @Override
    public void marshalXML(XMLWriter writer)
    {
        writer.openElement("slotStruct",
                new String[]{"uid", "name", "query", "grammarName"},
                new Object[]{getId(),
                        getName(),
                        getQuery(),
                        getGrammarName()});

        writer.closeElement("slotStruct");
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }
}
