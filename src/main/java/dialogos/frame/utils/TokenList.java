package dialogos.frame.utils;

import java.util.ArrayList;
import java.util.Comparator;

public class TokenList extends ArrayList<Token>
{
    @Override
    public boolean add(Token token)
    {
        boolean returnVal = super.add(token);
        sort(Comparator.comparingInt(Token::getStartIndex));
        return returnVal;
    }
}
