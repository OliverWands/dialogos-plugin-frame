package dialogos.frame.utils.tags;

import dialogos.frame.utils.tokens.FrameTokenizer;
import dialogos.frame.utils.tokens.Token;
import dialogos.frame.utils.tokens.TokenList;

import java.util.Map;

public class Tagger
{
    Map<String, String> definedMap;
    Map<String, String> regexMap;

    public Tagger(Map<String, String> definedMap, Map<String, String> regexMap)
    {
        this.definedMap = definedMap;
        this.regexMap = regexMap;
    }

    public void tagTokenList(TokenList tokenList)
    {
        for (Token token : tokenList)
        {
            String tag = definedMap.getOrDefault(token.getLower(), null);
            if (tag != null)
            {
                token.setTag(tag);
            }

            for (String pattern : regexMap.keySet())
            {
                if (token.getLower().matches(pattern))
                {
                    token.setTag(regexMap.get(pattern));
                }
            }
        }
    }

    private void handlePhraseInput(String phrase)
    {
        FrameTokenizer tokenizer = new FrameTokenizer(2);
        TokenList tokens = tokenizer.tokenize(phrase);
    }
}
