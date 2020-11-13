package dialogos.frame;

import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ReturnNode;
import com.clt.diamant.graph.nodes.SetVariableNode;
import com.clt.diamant.graph.nodes.StartNode;
import com.clt.script.exp.Type;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.utils.graph.GraphBuilder;
import dialogos.frame.utils.graph.NodeBuilder;

import java.awt.*;

public class FrameGraph
{
    private final FrameNode frameNode;
    private StartNode startNode;
    private NodeBuilder nodeBuilder;

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

//        frameNode.setGraphName(frameNode.frameStruct.getID());
    }

    public void buildGraph()
    {
        frameNode.addVariable("FRAMESIZE", "Size", Type.Int, Integer.toString(frameNode.frameStruct.size()));

        if (startNode != null)
        {
            SetVariableNode variableNode = new SetVariableNode();
            nodeBuilder.assignSetVariableNode(variableNode, "FRAMESIZE", "= 111");

//            ConditionalNode conditionalNode = new ConditionalNode();
//            conditionalNode.setTitle("Condition 1");
//            conditionalNode.setProperty(ConditionalNode.EXPRESSION, "CONDITION");

            TTSNode speechSynthesis0 = new TTSNode();
            nodeBuilder.assignTTSNode(speechSynthesis0, "TTS 0", "null");

            ReturnNode returnNode = new ReturnNode();
            returnNode.setTitle("Return");
            returnNode.setColor(Color.BLACK);

            frameNode.add(new Node[]{returnNode, speechSynthesis0, variableNode});

            GraphBuilder.connectNodes(new Node[]{startNode, speechSynthesis0, variableNode, returnNode});

            GraphBuilder.placeBottom(startNode, speechSynthesis0);
            GraphBuilder.placeBottomLeft(speechSynthesis0, variableNode);
            GraphBuilder.placeBottomRight(variableNode, returnNode);

        }
    }

}
