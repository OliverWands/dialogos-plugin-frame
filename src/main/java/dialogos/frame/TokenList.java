package dialogos.frame;

import java.util.ArrayList;
import java.util.Comparator;

class TokenList extends ArrayList<Token>
{
    private final Comparator<Token> comparator;

    public TokenList()
    {
        comparator = new Comparator<Token>()
        {
            @Override
            public int compare(Token o1, Token o2)
            {
                return Integer.compare(o1.getStartIndex(), o2.getStartIndex());
            }
        };
    }

    @Override
    public boolean add(Token token)
    {
        boolean returnVal = super.add(token);
        sort(comparator);
        return returnVal;
    }
}
