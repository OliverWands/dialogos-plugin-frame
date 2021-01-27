package dialogos.frame.graph;

import com.clt.diamant.graph.Comment;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ConditionalNode;
import com.clt.diamant.graph.nodes.ReturnNode;
import com.clt.diamant.graph.nodes.StartNode;
import com.clt.script.exp.Type;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.FrameNode;
import dialogos.frame.nodes.FillerNode;
import dialogos.frame.nodes.StringInputNode;
import dialogos.frame.struct.SlotStruct;

import java.awt.*;
import java.util.List;

public class FrameGraph
{
    private final StartNode startNode;
    private final FrameNode frameNode;
    private final NodeBuilder nodeBuilder;
    private final String INV_ID = "INPUT_VAR_ID";

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

            startNode.setLocation(58, startNode.getY());

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
            inputNode.setVariableID(INV_ID);
            frameNode.add(inputNode);

            FillerNode fillerNode = new FillerNode();
            fillerNode.setVariable(frameNode.getVariable(INV_ID));
            fillerNode.setFrameNode(frameNode);
            frameNode.add(fillerNode);

            GraphBuilder.placeBottom(startNode, helpPrompt);
            GraphBuilder.placeBottom(helpPrompt, inputNode);
            GraphBuilder.placeBottom(inputNode, fillerNode);

            GraphBuilder.connectNodes(new Node[]{startNode, helpPrompt, inputNode, fillerNode});

            int maxX = buildBottomUp(frameNode.frameStruct.getSlots(), fillerNode, returnNode);

            frameNode.getOwnedGraph().setSize(maxX + 258,
                                              returnNode.getLocation().y + 100);
        }
    }

    /**
     * Collection of variables that needs to be added for the graph.
     */
    private void assignAllVariables()
    {

        frameNode.addVariable(INV_ID, "INPUT_VAR", Type.String, null);

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
    private int buildBottomUp(List<SlotStruct> slots, Node start, Node end)
    {
        int maxWidth = 0;
        int columns;
        if (slots.size() < 5)
        {
            columns = 1;
        }
        else if (slots.size() < 9)
        {
            columns = 2;
        }
        else if (slots.size() < 13)
        {
            columns = 3;
        }
        else
        {
            columns = 4;
        }

        int rows = (int) (Math.ceil((double) slots.size() / columns));

        frameNode.getOwnedGraph().setSize(frameNode.getOwnedGraph().getWidth() * columns,
                                          frameNode.getOwnedGraph().getHeight() * rows);

        end.setLocation(150, end.getY());

        GraphBuilder.placeBottom(start, end, rows * 3 + 4);
        Node previous = end;

        int index = slots.size() - 1;
        for (int row = rows - 1; row >= 0; row--)
        {
            for (int column = index % columns; column >= 0; column--, index--)
            {
                SlotStruct slotStruct = slots.get(index);

                FillerNode fillerNode = new FillerNode();
                fillerNode.setVariable(frameNode.getVariable(INV_ID));
                fillerNode.setFrameNode(frameNode);
                fillerNode.setExpectedSlotInput(index);
                frameNode.add(fillerNode);

                StringInputNode inputNode = new StringInputNode();
                inputNode.setVariableID(INV_ID);
                frameNode.add(inputNode);

                TTSNode queryNode = new TTSNode();
                nodeBuilder.assignTTSNode(queryNode, String.format("Query %s", slotStruct.getName()), slotStruct.getQuery());
                frameNode.add(queryNode);

                Comment comment = new Comment();
                comment.setComment(slotStruct.getName());
                frameNode.addComment(comment);

                ConditionalNode checkEmpty = new ConditionalNode();
                nodeBuilder.assignConditionalNode(checkEmpty,
                                                  "Check Empty " + index + 1,
                                                  NodeBuilder.filledVariableName(slotStruct));
                frameNode.add(checkEmpty);
                GraphBuilder.setConditionalEdges(checkEmpty, previous, queryNode);
                GraphBuilder.connectNodes(new Node[]{queryNode, inputNode, fillerNode, checkEmpty});

                GraphBuilder.placeTopRight(end, fillerNode, column * 4 + 1, 2.75);
                if (maxWidth < fillerNode.getX())
                {
                    maxWidth = fillerNode.getX();
                }
                GraphBuilder.placeTopRight(fillerNode, inputNode);
                GraphBuilder.placeLeft(inputNode, queryNode);
                GraphBuilder.placeLeft(queryNode, checkEmpty);
                GraphBuilder.placeBottom(inputNode, comment);

                previous = checkEmpty;
                if (column == 0)
                {
                    end = checkEmpty;
                }
            }
        }

        GraphBuilder.setEdge(start, end);

        return maxWidth;
    }
}
