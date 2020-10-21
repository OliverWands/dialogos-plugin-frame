package dialogos.frame;

import dialogos.frame.utils.Token;

import java.util.List;

public class Slot
{
    private final String name;
    private String value;
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
