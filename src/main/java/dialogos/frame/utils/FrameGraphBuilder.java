package dialogos.frame.utils;

import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ReturnNode;
import com.clt.diamant.graph.nodes.StartNode;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.nodes.FrameNode;

import java.awt.*;

public class FrameGraphBuilder
{
    private final double X_UNIT = 1.25;
    private final double Y_UNIT = 1.25;

    private final FrameNode frameNode;
    private StartNode startNode;
    private final int baseHeight;
    private final int baseWidth;

    public FrameGraphBuilder(FrameNode frameNode)
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

        baseHeight = startNode.getHeight();
        baseWidth = startNode.getWidth();

        buildGraph();
    }

    public void buildGraph()
    {
        if (startNode != null)
        {
            TTSNode speechSynthesis0 = new TTSNode();
            speechSynthesis0.setTitle("0");
            speechSynthesis0.setProperty("prompt", "Adios amigos");

            TTSNode speechSynthesis1 = new TTSNode();
            speechSynthesis1.setTitle("1");
            speechSynthesis1.setProperty("prompt", "Adios amigos");

            TTSNode speechSynthesis2 = new TTSNode();
            speechSynthesis2.setTitle("2");
            speechSynthesis2.setProperty("prompt", "Adios amigos");

            TTSNode speechSynthesis3 = new TTSNode();
            speechSynthesis3.setTitle("3");
            speechSynthesis3.setProperty("prompt", "Adios amigos");

            TTSNode speechSynthesis4 = new TTSNode();
            speechSynthesis4.setTitle("4");
            speechSynthesis4.setProperty("prompt", "Adios amigos");

            TTSNode speechSynthesis5 = new TTSNode();
            speechSynthesis5.setTitle("5");
            speechSynthesis5.setProperty("prompt", "Adios amigos");

            TTSNode speechSynthesis6 = new TTSNode();
            speechSynthesis6.setTitle("6");
            speechSynthesis6.setProperty("prompt", "Adios amigos");

            TTSNode speechSynthesis7 = new TTSNode();
            speechSynthesis7.setTitle("7");
            speechSynthesis7.setProperty("prompt", "Adios amigos");

            TTSNode speechSynthesis8 = new TTSNode();
            speechSynthesis8.setTitle("8");
            speechSynthesis8.setProperty("prompt", "Adios amigos");

            ReturnNode returnNode = new ReturnNode();
            returnNode.setTitle("Return");
            returnNode.setColor(Color.BLACK);

            frameNode.getOwnedGraph().add(returnNode);
            frameNode.getOwnedGraph().add(speechSynthesis0);
            frameNode.getOwnedGraph().add(speechSynthesis1);
            frameNode.getOwnedGraph().add(speechSynthesis2);
            frameNode.getOwnedGraph().add(speechSynthesis3);
            frameNode.getOwnedGraph().add(speechSynthesis4);
            frameNode.getOwnedGraph().add(speechSynthesis5);
            frameNode.getOwnedGraph().add(speechSynthesis6);
            frameNode.getOwnedGraph().add(speechSynthesis7);
            frameNode.getOwnedGraph().add(speechSynthesis8);

            setEdge(startNode, speechSynthesis0);
            setEdge(speechSynthesis0, speechSynthesis1);
            setEdge(speechSynthesis1, speechSynthesis2);
            setEdge(speechSynthesis2, speechSynthesis3);
            setEdge(speechSynthesis3, speechSynthesis4);
            setEdge(speechSynthesis4, speechSynthesis5);
            setEdge(speechSynthesis5, speechSynthesis6);
            setEdge(speechSynthesis6, speechSynthesis7);
            setEdge(speechSynthesis7, speechSynthesis8);
            setEdge(speechSynthesis8, returnNode);

            placeBottom(startNode, speechSynthesis0);
            placeTopRight(speechSynthesis0, speechSynthesis1);
            placeBottomRight(speechSynthesis1, speechSynthesis2);
            placeBottomLeft(speechSynthesis2, speechSynthesis3);
            placeLeft(speechSynthesis3, speechSynthesis4);
            placeTopLeft(speechSynthesis4, speechSynthesis5);
            placeTop(speechSynthesis5, speechSynthesis6);
            placeLeft(speechSynthesis6, speechSynthesis7);
            placeBottomLeft(speechSynthesis7, speechSynthesis8);
            placeRight(speechSynthesis8, returnNode);

        }
    }

    private Node setEdge(Node startNode, Node targetNode)
    {
        if (targetNode == null)
        {
            return null;
        }

        startNode.getEdge(0).setTarget(targetNode);

        return startNode;
    }

    private Node setEdges(Node startNode, Node[] targetNodes)
    {
        if (targetNodes == null)
        {
            return null;
        }

        for (Node targetNode : targetNodes)
        {
            setEdge(startNode, targetNode);
        }

        return startNode;
    }

    private int getMiddle(Node node)
    {
        return node.getX() + (int) Math.floor((float) node.getWidth() / 2);
    }

    private void placeBottomLeft(Node relativeTo, Node node)
    {
        int yPos = relativeTo.getY();
        int xPos = getMiddle(relativeTo);
        int midOfNode = (int) Math.floor((float) node.getWidth() / 2);

        node.setLocation((int) Math.floor(xPos - midOfNode - (baseWidth * X_UNIT)),
                (int) Math.floor(yPos + baseHeight * Y_UNIT));
    }

    private void placeBottom(Node relativeTo, Node node)
    {
        int yPos = relativeTo.getY();
        int xPos = getMiddle(relativeTo);
        int midOfNode = (int) Math.floor((float) node.getWidth() / 2);

        node.setLocation(xPos - midOfNode, (int) Math.floor(yPos + baseHeight * Y_UNIT));
    }

    private void placeBottomRight(Node relativeTo, Node node)
    {
        int yPos = relativeTo.getY();
        int xPos = getMiddle(relativeTo);
        int midOfNode = (int) Math.floor((float) node.getWidth() / 2);

        node.setLocation((int) Math.floor(xPos - midOfNode + (baseWidth * X_UNIT)),
                (int) Math.floor(yPos + baseHeight * Y_UNIT));
    }

    private void placeLeft(Node relativeTo, Node node)
    {
        int yPos = relativeTo.getY();
        int xPos = getMiddle(relativeTo);
        int midOfNode = (int) Math.floor((float) node.getWidth() / 2);

        node.setLocation((int) Math.floor(xPos - midOfNode - (baseWidth * X_UNIT)), yPos);
    }

    private void placeRight(Node relativeTo, Node node)
    {
        int yPos = relativeTo.getY();
        int xPos = getMiddle(relativeTo);
        int midOfNode = (int) Math.floor((float) node.getWidth() / 2);

        node.setLocation((int) Math.floor(xPos - midOfNode + (baseWidth * X_UNIT)), yPos);
    }

    private void placeTopLeft(Node relativeTo, Node node)
    {
        int yPos = relativeTo.getY();
        int xPos = getMiddle(relativeTo);
        int midOfNode = (int) Math.floor((float) node.getWidth() / 2);

        node.setLocation((int) Math.floor(xPos - midOfNode - (baseWidth * X_UNIT)),
                (int) Math.floor(yPos - baseHeight * Y_UNIT));
    }

    private void placeTop(Node relativeTo, Node node)
    {
        int yPos = relativeTo.getY();
        int xPos = getMiddle(relativeTo);
        int midOfNode = (int) Math.floor((float) node.getWidth() / 2);

        node.setLocation(xPos - midOfNode, (int) Math.floor(yPos - baseHeight * Y_UNIT));
    }

    private void placeTopRight(Node relativeTo, Node node)
    {
        int yPos = relativeTo.getY();
        int xPos = getMiddle(relativeTo);
        int midOfNode = (int) Math.floor((float) node.getWidth() / 2);

        node.setLocation((int) Math.floor(xPos - midOfNode + (baseWidth * X_UNIT)),
                (int) Math.floor(yPos - baseHeight * Y_UNIT));
    }
}
