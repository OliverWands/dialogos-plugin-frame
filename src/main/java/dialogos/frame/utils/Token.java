package dialogos.frame.utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Token
{
    private String content;
    private String lower;
    private int size;
    private int startIndex;
    private int endIndex;
    private List<String> tags;

    public Token()
    {

    }

    public Token(String content, int startIndex, int endIndex)
    {
        this.content = content;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.size = endIndex - startIndex;
        tags = new ArrayList<>();
    }

    public Token setIndices(int startIndex, int endIndex)
    {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        size = endIndex - startIndex;
        return this;
    }

    public Token addTag(String tag)
    {
        tags.add(tag);
        return this;
    }

    public Token setTags(List<String> tags)
    {
        this.tags = tags;
        return this;
    }

    // TODO remove tags methods

    public int getEndIndex()
    {
        return endIndex;
    }

    public int size()
    {
        return size;
    }

    public int getStartIndex()
    {
        return startIndex;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public String toString(int indent)
    {
        JSONObject jsonObject = new JSONObject(this);
        return jsonObject.toString(indent);
    }

    @Override
    public String toString()
    {
        return toString(0);
    }

    public boolean hasTag(String tag)
    {
        return tags.contains(tag);
    }

    public boolean containsTagsOf(Token token)
    {
        for (String tag : this.tags)
        {
            if (token.getTags().contains(tag))
            {
                return true;
            }
        }
        return false;
    }
}

