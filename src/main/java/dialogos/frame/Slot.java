package dialogos.frame;

import dialogos.frame.utils.Token;

import java.util.Arrays;
import java.util.List;

public class Slot
{
    private final String name;
    private String value;
    private String query;
    private List<String> matchedTags;
    private boolean isAdditional;
    private boolean isFilled;

    public Slot(String name)
    {
        this.name = name;
    }

    public boolean setValue(Token token)
    {
        for (String tag : matchedTags)
        {
            if (!token.getTags().contains(tag))
            {
                return false;
            }
        }
        value = token.getLower();
        isFilled = true;
        return true;
    }

    public void removeValue()
    {
        value = null;
        isFilled = false;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public void setMatchedTags(String[] matchedTags)
    {
        this.matchedTags = Arrays.asList(matchedTags);
    }

    public void setMatchedTags(List<String> matchedTags)
    {
        this.matchedTags = matchedTags;
    }

    public void setIsAdditional(boolean additional)
    {
        isAdditional = additional;
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
