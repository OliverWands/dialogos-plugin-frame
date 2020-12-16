package dialogos.frame.utils.graph;

import com.clt.diamant.Grammar;
import com.clt.diamant.graph.Comment;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ConditionalNode;
import com.clt.diamant.graph.nodes.ReturnNode;
import com.clt.diamant.graph.nodes.SetVariableNode;
import com.clt.diamant.graph.nodes.StartNode;
import com.clt.script.exp.Type;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.FillerNode;
import dialogos.frame.FrameNode;
import dialogos.frame.SlotStruct;
import edu.cmu.lti.dialogos.sphinx.plugin.SphinxNode;

import java.awt.*;
import java.util.List;

public class FrameGraph
{
    public static String FILLED = "FILLED";
    public static String INPUT = "INPUT";
    private final StartNode startNode;
    private final FrameNode frameNode;
    private final NodeBuilder nodeBuilder;

    /**
     * Constructor for the FrameGraph
     *
     * @param frameNode The FrameNode for which the graph is to be created.
     */
    public FrameGraph(FrameNode frameNode)
    {
        startNode = frameNode.getOwnedGraph().getStartNode();

        GraphBuilder.removeAllNodes(frameNode.getOwnedGraph());
        GraphBuilder.removeAllComments(frameNode.getOwnedGraph());

        nodeBuilder = new NodeBuilder(frameNode.getOwnedGraph());

        this.frameNode = frameNode;
    }

    /**
     * This method creates the graph that represents the frame-interpretation-algorithm.
     */
    public void buildFrameInterpretationAlgo()
    {
        if (startNode != null)
        {
            assignAllVariables();

            frameNode.getOwnedGraph().setSize(frameNode.getOwnedGraph().getWidth(),
                                              (frameNode.frameStruct.size() + 3) * 200);

            ReturnNode returnNode = new ReturnNode();
            returnNode.setTitle("Return");
            returnNode.setColor(Color.BLACK);
            frameNode.add(returnNode);

            SetVariableNode inputVariable = new SetVariableNode();
            nodeBuilder.assignSetVariableNode(inputVariable, "INPUT_VAR_ID", "");
            frameNode.add(inputVariable);

            FillerNode fillerNode = new FillerNode();
            frameNode.add(fillerNode);

            GraphBuilder.placeBottom(startNode, inputVariable);
            GraphBuilder.placeBottom(inputVariable, fillerNode);

            GraphBuilder.connectNodes(new Node[]{startNode, inputVariable, fillerNode});

            buildBottomUp(frameNode.frameStruct.getSlots(), fillerNode, returnNode);

            frameNode.getOwnedGraph().setSize(frameNode.getOwnedGraph().getWidth(),
                                              returnNode.getLocation().y + 100);
        }
    }

    /**
     * Collection of variables that needs to be added for the graph.
     */
    private void assignAllVariables()
    {
        frameNode.addVariable("INPUT_VAR_ID", "INPUT_VAR_ID", Type.String, null);

        for (int inx = 0; inx < frameNode.frameStruct.size(); inx++)
        {
            SlotStruct slot = frameNode.frameStruct.getSlot(inx);
            frameNode.addVariable(filledVariableID(slot), filledVariableName(slot), Type.Bool, "false");
            frameNode.addVariable(inputVariableID(slot), inputVariableName(slot), Type.String, null);
        }
    }

    /**
     * Create the graph structure that allows to fill all the slots given in the list.
     * The graph will be created starting from the bottom "end" node and finally connect
     * to the start node, when every slot has been built.
     *
     * @param slots The slots that should be filled using the graph.
     * @param start The top node that connects the slot filling.
     * @param end   The last node that marks the end of the slot filling.
     */
    private void buildBottomUp(List<SlotStruct> slots, Node start, Node end)
    {
        GraphBuilder.placeBottom(start, end, slots.size() * 3 + 2);

        for (int inx = slots.size() - 1; inx >= 0; inx--)
        {
            SlotStruct slotStruct = slots.get(inx);

            SetVariableNode setNotEmpty = new SetVariableNode();
            nodeBuilder.assignSetVariableNode(setNotEmpty, filledVariableID(slotStruct), "true");
            frameNode.add(setNotEmpty);

            // TODO
            SetVariableNode filler = new SetVariableNode();
            filler.setTitle("Filler");
            nodeBuilder.assignSetVariableNode(filler, inputVariableID(slotStruct), "TestInput" + numbersToLetters(inx + 1));
            frameNode.add(filler);

            SphinxNode recogniser = new SphinxNode();
            Grammar slotGrammar0 = frameNode.getGrammar(slotStruct.getGrammarName());
            nodeBuilder.assignSlotSphinx(recogniser, slotGrammar0, inputVariableName(slotStruct));
            frameNode.add(recogniser);

            TTSNode queryNode = new TTSNode();
            nodeBuilder.assignTTSNode(queryNode, String.format("Query %s", slotStruct.getName()), slotStruct.getQuery());
            frameNode.add(queryNode);

            Comment comment = new Comment();
            comment.setComment(slotStruct.getName());
            frameNode.addComment(comment);

            ConditionalNode checkEmpty = new ConditionalNode();
            nodeBuilder.assignConditionalNode(checkEmpty, "Check Empty " + inx + 1, filledVariableName(slotStruct));
            frameNode.add(checkEmpty);
            GraphBuilder.setConditionalEdges(checkEmpty, end, queryNode);
            GraphBuilder.connectNodes(new Node[]{queryNode, recogniser, filler, setNotEmpty, end});

            GraphBuilder.placeTopRight(end, setNotEmpty, 1, 2);
            GraphBuilder.placeRight(setNotEmpty, filler);
            GraphBuilder.placeTop(filler, recogniser);
            GraphBuilder.placeLeft(recogniser, queryNode);
            GraphBuilder.placeLeft(queryNode, checkEmpty);
            GraphBuilder.placeLeft(checkEmpty, comment);

            end = checkEmpty;
        }

        GraphBuilder.setEdge(start, end);
    }

    private String filledVariableName(SlotStruct slot)
    {
        return String.format("%s%s%s",
                             numbersToLetters(frameNode.frameStruct.getIndex(slot)),
                             FILLED,
                             replaceAllDigits(slot.getName()));
    }

    private String inputVariableName(SlotStruct slot)
    {
        return String.format("%s%s%s",
                             numbersToLetters(frameNode.frameStruct.getIndex(slot)),
                             INPUT,
                             replaceAllDigits(slot.getName()));
    }

    private String filledVariableID(SlotStruct slot)
    {
        return String.format("%d_%s_%s", frameNode.frameStruct.getIndex(slot), FILLED, slot.getId());
    }

    private String inputVariableID(SlotStruct slot)
    {
        return String.format("%d_%s_%s", frameNode.frameStruct.getIndex(slot), INPUT, slot.getId());
    }

    //
    // Think of more elegant solution
    //
    private String replaceAllDigits(String input)
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

    private String numbersToLetters(int number)
    {
        return numbersToLetters(Integer.toString(number));
    }

    private String numbersToLetters(String number)
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
