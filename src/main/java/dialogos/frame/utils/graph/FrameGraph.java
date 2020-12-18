package dialogos.frame.utils.graph;

import com.clt.diamant.graph.Comment;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ConditionalNode;
import com.clt.diamant.graph.nodes.ReturnNode;
import com.clt.diamant.graph.nodes.StartNode;
import com.clt.script.exp.Type;
import com.clt.script.exp.types.StructType;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.nodes.FillerNode;
import dialogos.frame.FrameNode;
import dialogos.frame.SlotStruct;
import dialogos.frame.nodes.StringInputNode;

import java.awt.*;
import java.util.List;

public class FrameGraph
{
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

            TTSNode helpPrompt = new TTSNode();
            nodeBuilder.assignTTSNode(helpPrompt, "Help Prompt", frameNode.frameStruct.getHelpPrompt());
            frameNode.add(helpPrompt);

            StringInputNode inputNode = new StringInputNode();
            inputNode.setVariable(frameNode.getVariable("INPUT_VAR_ID"));
            frameNode.add(inputNode);

            FillerNode fillerNode = new FillerNode();
            fillerNode.setVariable(frameNode.getVariable("INPUT_VAR_ID"));
            fillerNode.setFrameNode(frameNode);
            frameNode.add(fillerNode);

            GraphBuilder.placeBottom(startNode, helpPrompt);
            GraphBuilder.placeBottom(helpPrompt, inputNode);
            GraphBuilder.placeBottom(inputNode, fillerNode);

            GraphBuilder.connectNodes(new Node[]{startNode, helpPrompt, inputNode, fillerNode});

            buildBottomUp(frameNode.frameStruct.getSlots(), fillerNode, returnNode);

            frameNode.getOwnedGraph().setSize(frameNode.getOwnedGraph().getWidth(),
                                              returnNode.getLocation().y + 200);
        }
    }

    /**
     * Collection of variables that needs to be added for the graph.
     */
    private void assignAllVariables()
    {

        frameNode.addVariable("INPUT_VAR_ID", "INPUT_VAR", Type.String, null);

        String[] names = new String[frameNode.frameStruct.size()];
        Type[] types = new Type[frameNode.frameStruct.size()];

        for (int inx = 0; inx < frameNode.frameStruct.size(); inx++)
        {
            SlotStruct slot = frameNode.frameStruct.getSlot(inx);
            frameNode.addVariable(NodeBuilder.filledVariableID(frameNode.frameStruct, slot),
                                  NodeBuilder.filledVariableName(slot),
                                  Type.Bool,
                                  "false");
            frameNode.addVariable(NodeBuilder.inputVariableID(frameNode.frameStruct, slot),
                                  NodeBuilder.inputVariableName(slot),
                                  Type.String,
                                  null);

            names[inx] = slot.getName();
            types[inx] = Type.String;
        }
        StructType type = new StructType(names, types, false);

        frameNode.addVariable("testID", "test", type, "");
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

            FillerNode fillerNode = new FillerNode();
            fillerNode.setVariable(frameNode.getVariable("INPUT_VAR_ID"));
            fillerNode.setFrameNode(frameNode);
            fillerNode.setExpectedSlotInput(inx);
            frameNode.add(fillerNode);

            StringInputNode inputNode = new StringInputNode();
            inputNode.setVariable(frameNode.getVariable("INPUT_VAR_ID"));
            frameNode.add(inputNode);

            TTSNode queryNode = new TTSNode();
            nodeBuilder.assignTTSNode(queryNode, String.format("Query %s", slotStruct.getName()), slotStruct.getQuery());
            frameNode.add(queryNode);

            Comment comment = new Comment();
            comment.setComment(slotStruct.getName());
            frameNode.addComment(comment);

            ConditionalNode checkEmpty = new ConditionalNode();
            nodeBuilder.assignConditionalNode(checkEmpty,
                                              "Check Empty " + inx + 1,
                                              NodeBuilder.filledVariableName(slotStruct));
            frameNode.add(checkEmpty);
            GraphBuilder.setConditionalEdges(checkEmpty, end, queryNode);
            GraphBuilder.connectNodes(new Node[]{queryNode, inputNode, fillerNode, end});

            GraphBuilder.placeTopRight(end, fillerNode, 2, 2);
            GraphBuilder.placeTop(fillerNode, inputNode);
            GraphBuilder.placeLeft(inputNode, queryNode);
            GraphBuilder.placeLeft(queryNode, checkEmpty);
            GraphBuilder.placeLeft(checkEmpty, comment);

            end = checkEmpty;
        }

        GraphBuilder.setEdge(start, end);
    }
}
