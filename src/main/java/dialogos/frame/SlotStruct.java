package dialogos.frame;

import dialogos.frame.utils.Token;

import java.util.Arrays;
import java.util.List;

public class SlotStruct
{
    private final String name;
    private String value;
    private String query;
    private List<String> matchedTags;
    private boolean isAdditional;
    private boolean isFilled;

    public SlotStruct(String name)
    {
        this.name = name;
    }

    public SlotStruct setValue(Token token)
    {
        for (String tag : matchedTags)
        {
            if (!token.getTags().contains(tag))
            {
                isFilled = false;
                return this;
            }
        }
        value = token.getLower();
        isFilled = true;
        return this;
    }

    public void removeValue()
    {
        value = null;
        isFilled = false;
    }

    public SlotStruct setQuery(String query)
    {
        this.query = query;
        return this;
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

    public SlotStruct setIsAdditional(boolean additional)
    {
        isAdditional = additional;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    public String getQuery()
    {
        return query;
    }

    public List<String> getMatchedTags()
    {
        return matchedTags;
    }

    public boolean isAdditional()
    {
        return isAdditional;
    }

    public boolean isFilled()
    {
        return isFilled;
    }

    public boolean isComplete()
    {
        return isFilled || isAdditional;
    }
}
