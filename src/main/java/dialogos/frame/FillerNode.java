package dialogos.frame;

import com.clt.diamant.*;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.SetVariableNode;
import com.clt.xml.XMLWriter;

import javax.swing.*;
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

                    if (slot.getId().equals("INPUT_VAR_ID"))
                    {
                        if (slot.getValue() != null)
                        {
//                        TokenList tokens = tagTokenList(slot.getValue().toString(), null);
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
}
