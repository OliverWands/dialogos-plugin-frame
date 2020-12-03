package dialogos.frame.utils.graph;

import com.clt.diamant.Grammar;
import com.clt.diamant.Slot;
import com.clt.diamant.graph.Edge;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ConditionalNode;
import com.clt.diamant.graph.nodes.SetVariableNode;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import edu.cmu.lti.dialogos.sphinx.plugin.SphinxNode;

import java.awt.*;
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

    /**
     * Calculates a color for the node, using the hash of its title.
     *
     * @param node The node whose color will be changed.
     */
    public void changeColor(Node node)
    {
        Color color = new Color((int) Math.floor(((double) node.getTitle().hashCode() * 16777215.0) / 2147483647.0));
        node.setColor(color);
    }

    /**
     * @param node
     * @param varID
     * @param assingVal
     */
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

    /**
     * @param node
     * @param title
     * @param prompt
     */
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

    public void assignSphinxNode(SphinxNode node, String grammarID)
    {
        List<Grammar> grammars = ownedGraph.getGrammars();

        if (grammars != null)
        {
            for (Grammar grammar : grammars)
            {
                if (grammar.getId() != null && grammar.getId().equals(grammarID))
                {
                    node.setProperty("grammar", grammar);
                    break;
                }
            }
        }
    }

    public void assignSlotSphinx(SphinxNode node, Grammar grammar, String edgeCondition)
    {
        node.setTitle(grammar.getName());
        changeColor(node);
        node.setProperty("grammar", grammar);
        addEdgeCondition(node, edgeCondition);
    }

    public Integer addEdgeCondition(SphinxNode node, String condition)
    {
        Edge edge = node.addEdge(condition);
        return node.getOutEdges().indexOf(edge);
    }

    /**
     * @param node
     * @param title
     * @param expression
     */
    public void assignConditionalNode(ConditionalNode node, String title, String expression)
    {
        if (node == null)
        {
            node = new ConditionalNode();
        }

        ConditionalNode conditionalNode = node;
        conditionalNode.setTitle(title);

        conditionalNode.setProperty(ConditionalNode.EXPRESSION, expression);
    }
}
