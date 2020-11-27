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
    private StartNode startNode;
    private final FrameNode frameNode;
    private final NodeBuilder nodeBuilder;
    private int scale = 1;

    public FrameGraph(FrameNode frameNode)
    {
        this.frameNode = frameNode;
        for (Node node : frameNode.getOwnedGraph().getNodes())
        {
            if (node.getClassName().equals(StartNode.class.getName()))
            {
                startNode = (StartNode) node;
                break;
            }
        }

        nodeBuilder = new NodeBuilder(this.frameNode.getOwnedGraph());
    }

    /**
     * This method creates the graph that represents the frame-interpretation-algorithm.
     */
    public void buildFrameInterpretationAlgo()
    {
        if (startNode != null)
        {
            assignAllVariables();

            ReturnNode returnNode = new ReturnNode();
            returnNode.setTitle("Return");
            returnNode.setColor(Color.BLACK);
            frameNode.add(returnNode);

            // Todo Replace with speech recognition
            SetVariableNode inputVariable = new SetVariableNode();
            nodeBuilder.assignSetVariableNode(inputVariable, FrameNode.INPUT_VAR_ID, "Ich will von Stade nach Veddel");
            frameNode.add(inputVariable);

            FillerNode fillerNode = new FillerNode();
            frameNode.add(fillerNode);

            GraphBuilder.placeBottom(startNode, inputVariable);
            GraphBuilder.placeBottom(inputVariable, fillerNode);

            buildBottomUp(frameNode.frameStruct.getSlots(), fillerNode, returnNode);
        }
    }

    /**
     *
     */
    private void assignAllVariables()
    {
        for (int inx = 0; inx < frameNode.frameStruct.size(); inx++)
        {
            SlotStruct slot = frameNode.frameStruct.getSlot(inx);
            frameNode.addVariable("FILLED" + slot.ID, "FILLED" + slot.ID, Type.Bool, "false");
            frameNode.addVariable("INPUT" + slot.ID, slot.ID + "INPUT", Type.String, "");
        }
    }

    /**
     * @param slots
     * @param start
     * @param end
     */
    private void buildBottomUp(List<SlotStruct> slots, Node start, Node end)
    {
        GraphBuilder.placeBottom(start, end, slots.size() * 3 + 1);

        for (int inx = slots.size() - 1; inx >= 0; inx--)
        {
            SlotStruct slotStruct = slots.get(inx);

            SetVariableNode setNotEmpty = new SetVariableNode();
            nodeBuilder.assignSetVariableNode(setNotEmpty, "FILLED" + slotStruct.ID, "true");
            frameNode.add(setNotEmpty);

            // TODO
            SetVariableNode filler = new SetVariableNode();
            filler.setTitle("Filler");
            frameNode.add(filler);

            SphinxNode recogniser = new SphinxNode();
            Grammar slotGrammar0 = frameNode.getGrammar(slotStruct.getGrammarName());
            nodeBuilder.assignSlotSphinx(recogniser, slotGrammar0, "INPUT" + slotStruct.ID);
            frameNode.add(recogniser);

            TTSNode queryNode = new TTSNode();
            nodeBuilder.assignTTSNode(queryNode, String.format("Query %s", slotStruct.getName()), slotStruct.getQuery());
            frameNode.add(queryNode);

            Comment comment = new Comment();
            comment.setComment(slotStruct.getName());
            frameNode.addComment(comment);

            ConditionalNode checkEmpty = new ConditionalNode();
            nodeBuilder.assignConditionalNode(checkEmpty, "Check Empty", "FILLED" + slotStruct.ID);
            frameNode.add(checkEmpty);

            GraphBuilder.placeTopRight(end, setNotEmpty, 1, 2);
            GraphBuilder.placeRight(setNotEmpty, filler);
            GraphBuilder.placeTop(filler, recogniser);
            GraphBuilder.placeLeft(recogniser, queryNode);
            GraphBuilder.placeLeft(queryNode, checkEmpty);
            GraphBuilder.placeLeft(checkEmpty, comment);

            GraphBuilder.setConditionalEdges(checkEmpty, end, queryNode);
            GraphBuilder.connectNodes(new Node[]{queryNode, recogniser, filler, setNotEmpty});

            end = checkEmpty;
        }
        GraphBuilder.setEdge(start, end);
    }

    public void buildGraph()
    {
        frameNode.addVariable("FRAMESIZE", "Size", Type.Int, Integer.toString(frameNode.frameStruct.size()));
        frameNode.addVariable(FrameNode.INPUT_VAR_ID, FrameNode.INPUT_VAR_NAME, Type.String, "");

        if (startNode != null)
        {
            SetVariableNode inputVariable = new SetVariableNode();
            nodeBuilder.assignSetVariableNode(inputVariable, FrameNode.INPUT_VAR_ID, "Ich will von Stade nach Veddel");

            SetVariableNode variableNode = new SetVariableNode();
            nodeBuilder.assignSetVariableNode(variableNode, "FRAMESIZE", "111");

            frameNode.addGrammar("firstGrammar", "grammar", "root $Input;\n$Input = .*;\n");

            Comment comment = new Comment();
            comment.setComment("This is a comment!");
            GraphBuilder.placeLeft(startNode, comment);

            frameNode.getOwnedGraph().addComment(comment);

            FillerNode fillerNode = new FillerNode();

            SphinxNode sphinxNode = new SphinxNode();
            sphinxNode.setTitle("Haltestelle");
            nodeBuilder.changeColor(sphinxNode);
            nodeBuilder.assignSphinxNode(sphinxNode, "firstGrammar");
            nodeBuilder.addEdgeCondition(sphinxNode, FrameNode.INPUT_VAR_NAME);

            TTSNode speechSynthesis0 = new TTSNode();
            nodeBuilder.assignTTSNode(speechSynthesis0, "TTS 0", "null");

            TTSNode speechSynthesis1 = new TTSNode();
            nodeBuilder.assignTTSNode(speechSynthesis1, "TTS 1", "eins");

            ConditionalNode conditionalNode = new ConditionalNode();
            nodeBuilder.assignConditionalNode(conditionalNode, "Condition", "Size > 10");

            ReturnNode returnNode = new ReturnNode();
            returnNode.setTitle("Return");
            returnNode.setColor(Color.BLACK);

            frameNode.add(new Node[]{inputVariable, fillerNode, sphinxNode, variableNode,
                    conditionalNode, speechSynthesis0, speechSynthesis1, returnNode});

            GraphBuilder.connectNodes(new Node[]{startNode, inputVariable, sphinxNode,
                    fillerNode, variableNode, conditionalNode});

            GraphBuilder.setEdge(speechSynthesis0, returnNode);
            GraphBuilder.setEdge(speechSynthesis1, returnNode);
            GraphBuilder.setConditionalEdges(conditionalNode, speechSynthesis0, speechSynthesis1);

            GraphBuilder.placeBottom(startNode, inputVariable);
            GraphBuilder.placeBottom(inputVariable, sphinxNode);
            GraphBuilder.placeBottom(sphinxNode, fillerNode);
            GraphBuilder.placeBottom(fillerNode, variableNode);
            GraphBuilder.placeBottom(variableNode, conditionalNode);
            GraphBuilder.placeConditional(conditionalNode, speechSynthesis0, speechSynthesis1);
            GraphBuilder.placeBottomLeft(speechSynthesis1, returnNode);
        }
    }
}
