package dialogos.frame.utils.graph;

import com.clt.diamant.Slot;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.nodes.SetVariableNode;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that allows for easier programmatic modification of a selection of nodes.
 */
public class NodeBuilder
{
    Graph ownedGraph;

    public NodeBuilder(Graph ownedGraph)
    {
        this.ownedGraph = ownedGraph;
    }

    public void assignSetVariableNode(SetVariableNode node, String varID, String assingVal)
    {
        if (node == null)
        {
            node = new SetVariableNode();
        }

        SetVariableNode variableNode = node;
        variableNode.setProperty(SetVariableNode.VAR_ID, varID);

        List<SetVariableNode.VarAssignment> assignments =
                (List<SetVariableNode.VarAssignment>) variableNode.getProperty(SetVariableNode.ASSIGNMENTS);

        if (assignments == null)
        {
            assignments = new ArrayList<>();
        }

        SetVariableNode.VarAssignment assignment = new SetVariableNode.VarAssignment();

        List<Slot> slots = ownedGraph.getVariables();

        for (Slot slot : slots)
        {
            if (slot.getId().equals(varID))
            {
                assignment.setVariable(slot);
                variableNode.setTitle(slot.getName() + " " + assingVal);
            }
        }

        assignment.setValue(assingVal);

        assignments.add(assignment);

        variableNode.setProperty(SetVariableNode.ASSIGNMENTS, assignments);
    }

    public void assignTTSNode(TTSNode node, String title, String prompt)
    {
        if (node == null)
        {
            node = new TTSNode();
        }

        TTSNode ttsNode = node;
        ttsNode.setTitle(title);
        ttsNode.setProperty("prompt", prompt);
    }
}
