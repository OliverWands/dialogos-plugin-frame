package dialogos.frame.struct;

import com.clt.diamant.IdentityObject;
import com.clt.xml.XMLWriter;
import dialogos.frame.utils.Token;

import java.util.UUID;

public class SlotStruct implements IdentityObject
{
    private String id;
    private String name;
    private String value;
    private String query;
    private String grammarName;
    private boolean isFilled;

    public SlotStruct()
    {
        setId(UUID.randomUUID().toString());
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
            value = token.getContent().toLowerCase();
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

    public boolean isEmpty()
    {
        return !isFilled;
    }

    public String removeValue()
    {
        String val = value;
        value = null;
        isFilled = false;
        return val;
    }

    public void writeToXML(XMLWriter writer)
    {
        writer.printElement("slotStruct",
                            new String[]{"uid", "name", "query", "grammarName"},
                            new Object[]{getId(), getName(), getQuery(), getGrammarName()},
                            null);
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
