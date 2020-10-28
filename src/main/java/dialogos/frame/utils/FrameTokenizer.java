package dialogos.frame.utils;

public class FrameTokenizer
{
    private final int maxMatchLength;

    public FrameTokenizer(int maxMatchLength)
    {
        this.maxMatchLength = maxMatchLength;
    }

    public TokenList tokenize(String input)
    {
        input = input.replaceAll("\\p{Punct}", "");
        String[] tokens = input.split(" ");
        TokenList tokenList = new TokenList();

        for (int inx = 1; inx <= maxMatchLength; inx++)
        {
            for (int jnx = 0; jnx < tokens.length; jnx++)
            {
                if ((inx + (jnx - 1)) < tokens.length)
                {
                    StringBuilder content = new StringBuilder();
                    String prefix = "";
                    for (int knx = 0; knx < inx; knx++)
                    {
                        content.append(prefix);
                        prefix = " ";
                        content.append(tokens[jnx + knx]);
                    }

                    tokenList.add(new Token(content.toString(), jnx, jnx + inx));
                }
            }
        }

        return tokenList;
    }
}
