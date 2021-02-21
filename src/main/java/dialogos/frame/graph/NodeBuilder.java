package dialogos.frame.graph;

import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.nodes.ConditionalNode;
import com.clt.diamant.graph.nodes.GotoNode;
import com.clt.diamant.graph.nodes.LabelNode;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.struct.FrameStruct;
import dialogos.frame.struct.SlotStruct;

import java.awt.*;

/**
 * Class that allows for easier programmatic modification of a selection of nodes.
 */
public class NodeBuilder
{
    final Graph ownedGraph;
    public static final String FILLED = "FILLED";
    public static final String INPUT = "INPUT";

    public NodeBuilder(Graph ownedGraph)
    {
        this.ownedGraph = ownedGraph;
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

    public GotoNode createGotoNode(String title, LabelNode target, Color color)
    {
        GotoNode gotoNode = new GotoNode();
        gotoNode.setTitle(title);
        gotoNode.setColor(color);
        gotoNode.setProperty("target", target);

        return gotoNode;
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
