package dialogos.frame;

import com.clt.diamant.*;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.SetVariableNode;
import com.clt.xml.XMLWriter;
import dialogos.frame.utils.tokens.FrameTokenizer;
import dialogos.frame.utils.tokens.Token;
import dialogos.frame.utils.tokens.TokenList;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * Take string from previous node, tokenize it, tag it with the grammars stored in the frame plugin settings
 * Then get the minimal, tagged list and find out which slots of the frame in the frame plugin settings has already
 * been filled with this one. Then return the frame to the graph/node.
 */
public class FillerNode extends SetVariableNode
{
    @Override
    public Node execute(WozInterface comm, InputCenter input, ExecutionLogger logger)
    {
        Graph ownedGraph = getGraph();

        if (ownedGraph != null)
        {
            for (Slot slot : ownedGraph.getVariables())
            {
                if (slot != null)
                {
                    if (slot.getId() == null)
                    {
                        continue;
                    }

                    if (slot.getId().equals(FrameNode.INPUT_VAR_ID))
                    {
                        if (slot.getValue() != null)
                        {
//                        TokenList tokens = tagTokenList(slot.getValue().toString(), null);
                            System.out.println("########## " + slot.getValue().toString());
                        }
                    }
                }
            }
        }
        return super.execute(comm, input, logger);
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
        return super.createEditorComponent(properties);
    }

    @Override
    public void writeVoiceXML(XMLWriter w, IdMap uid_map)
    {

    }

    private TokenList tagTokenList(String input)
    {
        List<Grammar> grammars = getGraph().getGrammars();
        TokenList tokenList = FrameTokenizer.tokenize(input);
        for (Grammar grammar : grammars)
        {
            try
            {
                com.clt.srgf.Grammar testGrammar = com.clt.srgf.Grammar.create(grammar.getGrammar());

                for (Token token : tokenList)
                {
                    if (testGrammar.match(token.getLower(), null) != null)
                    {
                        token.addTag(grammar.getName());
                    }
                }
            } catch (Exception exp)
            {
                exp.printStackTrace();
            }
        }

        return tokenList;
    }
}
