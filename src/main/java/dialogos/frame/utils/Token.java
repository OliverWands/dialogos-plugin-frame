package dialogos.frame.utils;

public class Token
{
    private String content;
    private String lower;
    private int length;
    private int startIndex;
    private int endIndex;

    public Token()
    {

    }

    public Token(String content, int startIndex, int endIndex)
    {
        this.content = content;
        this.lower = content.toLowerCase();
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public void setIndex(int startIndex, int endIndex)
    {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        length = endIndex - startIndex;
    }

    public int getEndIndex()
    {
        return endIndex;
    }

    public int getLength()
    {
        return length;
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
        this.lower = content.toLowerCase();
    }

    public String getLower()
    {
        return lower;
    }
}

