package dialogos.frame.utils.graph;

import com.clt.diamant.Grammar;
import com.clt.diamant.Slot;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ConditionalNode;
import com.clt.diamant.graph.nodes.SetVariableNode;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.nodes.FrameInput;
import dialogos.frame.FrameStruct;
import dialogos.frame.SlotStruct;
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
    public static String FILLED = "FILLED";
    public static String INPUT = "INPUT";

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
        node.addEdge(edgeCondition);
    }
    public void assignInputNode(Node node, String title, Slot variable)
    {
        if (node instanceof FrameInput)
        {
            ((FrameInput) node).setVariable(variable);
            node.setTitle(title);
        }
    }



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

    public static String filledVariableName(SlotStruct slot)
    {
        return String.format("%s%s", FILLED, replaceAllDigits(slot.getName()).toUpperCase());
    }

    public static String inputVariableName(SlotStruct slot)
    {
        return String.format("%s%s", INPUT, replaceAllDigits(slot.getName().toUpperCase()));
    }

    public static String filledVariableID(FrameStruct frameStruct, SlotStruct slot)
    {
        return String.format("%d_%s_%s", frameStruct.getIndex(slot), FILLED, slot.getId());
    }

    public static String inputVariableID(FrameStruct frameStruct, SlotStruct slot)
    {
        return String.format("%d_%s_%s", frameStruct.getIndex(slot), INPUT, slot.getId());
    }

    //
    // Think of more elegant solution
    //
    private static String replaceAllDigits(String input)
    {
        if (!input.matches(".*\\d+.*"))
        {
            return input;
        }

        StringBuilder builder = new StringBuilder();
        for (char digit : input.toCharArray())
        {
            String digitString = String.valueOf(digit);
            if (digitString.matches("\\d"))
            {
                digitString = numbersToLetters(digitString);
            }
            builder.append(digitString);
        }

        return builder.toString();
    }

    private static String numbersToLetters(String number)
    {
        String numberString = number;
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        for (int inx = 0; inx < letters.length; inx++)
        {
            numberString = numberString.replaceAll(Integer.toString(inx), letters[inx]);
        }

        return numberString;
    }
}
