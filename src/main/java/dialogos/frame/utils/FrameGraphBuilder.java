package dialogos.frame.utils;

import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ReturnNode;
import com.clt.diamant.graph.nodes.StartNode;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.GraphAdjacencyList;
import dialogos.frame.nodes.FrameNode;

import java.awt.*;

public class FrameGraphBuilder
{
    private FrameNode frameNode;

    public FrameGraphBuilder(FrameNode frameNode)
    {
        this.frameNode = frameNode;
    }

    public FrameGraphBuilder(FrameNode frameNode, GraphAdjacencyList graphAdjacencyList)
    {

    }

    public void buildGraph()
    {
        StartNode sNode = null;
        for (Node node : frameNode.getOwnedGraph().getNodes())
        {
            if (node.getClassName().equals("com.clt.diamant.graph.nodes.StartNode"))
            {
                sNode = (StartNode) node;
            }
        }

        if (sNode != null)
        {
            int middle = sNode.getX() + (int) Math.floor((float) sNode.getWidth() / 2);

            TTSNode speechSynthesis = new TTSNode();
            speechSynthesis.setProperty("prompt", "Adios amigos");

            ReturnNode returnNode = new ReturnNode();
            returnNode.setTitle("Adios Amigos!");
            returnNode.setColor(Color.BLACK);

            frameNode.getOwnedGraph().add(returnNode);
            frameNode.getOwnedGraph().add(speechSynthesis);

            speechSynthesis.setLocation(middle - (int) Math.floor((float) speechSynthesis.getWidth() / 2), sNode.getY() + sNode.getHeight() * 2);
            returnNode.setLocation(middle - (int) Math.floor((float) returnNode.getWidth() / 2), sNode.getY() + sNode.getHeight() * 4);

            sNode.getEdge(0).setTarget(speechSynthesis);
            speechSynthesis.getEdge(0).setTarget(returnNode);
        }
    }


    // TODO handle double edges???
    private Node setEdges(Node baseNode, Node[] targetNodes)
    {
        if (targetNodes == null)
        {
            return null;
        }

        //
        // Add the edges
        //
        int startInx = baseNode.getOutEdges().size();
        for (int inx = 0; inx < targetNodes.length; inx++)
        {
            baseNode.getEdge(startInx + inx).setTarget(targetNodes[inx]);
        }

        return baseNode;
    }
}
