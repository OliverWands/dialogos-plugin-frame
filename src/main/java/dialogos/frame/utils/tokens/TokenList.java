package dialogos.frame.utils.tokens;

import org.json.JSONArray;
import org.json.JSONObject;

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

    @Override
    public String toString()
    {
        return toJSONArray().toString();
    }

    public String toPretty()
    {
        return toJSONArray().toString(4);
    }

    private JSONArray toJSONArray()
    {
        JSONArray jsonArray = new JSONArray();
        for (Token token : this)
        {
            jsonArray.put(new JSONObject(token));
        }

        return jsonArray;
    }
}
