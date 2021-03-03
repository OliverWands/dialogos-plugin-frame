package dialogos.frame.utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Token
{
    private final int size;
    private final int startIndex;
    private final int endIndex;
    private final List<String> tags;
    private String content;

    public Token(String content, int startIndex, int endIndex)
    {
        this.content = content;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.size = endIndex - startIndex;
        tags = new ArrayList<>();
    }

    public void addTag(String tag)
    {
        tags.add(tag);
    }

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

    @Override
    public String toString()
    {
        return toString(0);
    }
}

