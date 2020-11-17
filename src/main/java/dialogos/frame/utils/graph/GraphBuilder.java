package dialogos.frame.utils.graph;

import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ConditionalNode;

/**
 * Class to be used for the programmatic modification of dialog graphs.
 */
public class GraphBuilder
{
    private static final double X_UNIT = 1.25;
    private static final double Y_UNIT = 1.25;
    private static final int baseHeight = 55;
    private static final int baseWidth = 66;

    /**
     * Calculate half the width of a node.
     *
     * @param node The node for which the width will be calculated.
     * @return The width of half the node floored.
     */
    private static int getHalvedWidth(Node node)
    {
        return (int) Math.floor((float) node.getWidth() / 2);
    }

    /**
     * Calculate the X coordinate of the middle of a node.
     *
     * @param node The node for which the position will be calculated.
     * @return The X coordinate of the middle of the node.
     */
    private static int getMiddle(Node node)
    {
        return node.getX() + getHalvedWidth(node);
    }

    /**
     * Create edges between the nodes.
     * A edge is set between every two nodes.
     * E.g nodes[0] -> nodes[1], nodes[1] -> nodes[2]...
     *
     * @param nodes The nodes that will be connected.
     */
    public static void connectNodes(Node[] nodes)
    {
        for (int inx = 0; inx < nodes.length - 1; inx++)
        {
            setEdge(nodes[inx], nodes[inx + 1]);
        }
    }

    /**
     * Create a new edge from the startNode to the targetNode
     *
     * @param startNode  The edge will start from this node.
     * @param targetNode The edge will end at this node.
     */
    public static void setEdge(Node startNode, Node targetNode)
    {
        if (targetNode == null)
        {
            return;
        }

        startNode.getEdge(0).setTarget(targetNode);
    }

    /**
     * Create a new edge from the startNode to the targetNode
     *
     * @param startNode  The edge will start from this node.
     * @param targetNode The edge will end at this node.
     * @param startIndex The index of the edge at the startNode.
     */
    public static void setEdge(Node startNode, Node targetNode, int startIndex)
    {
        if (targetNode == null)
        {
            return;
        }

        startNode.getEdge(startIndex).setTarget(targetNode);
    }

    /**
     * Create multiple edges from the startNode to each of the targetNodes.
     *
     * @param startNode   The edges will start from this node.
     * @param targetNodes The nodes that connect to the startNode.
     */
    public static void setEdges(Node startNode, Node[] targetNodes)
    {
        if (targetNodes == null)
        {
            return;
        }

        for (Node targetNode : targetNodes)
        {
            setEdge(startNode, targetNode);
        }
    }

    /**
     * @param node
     * @param exprTrue
     * @param exprFalse
     */
    public static void setConditionalEdges(ConditionalNode node, Node exprTrue, Node exprFalse)
    {
        setEdge(node, exprTrue, 0);
        setEdge(node, exprFalse, 1);
    }

    /**
     * @param node
     * @param exprTrue
     * @param exprFalse
     */
    public static void placeConditional(ConditionalNode node, Node exprTrue, Node exprFalse)
    {
        placeBottomLeft(node, exprTrue);
        placeBottomRight(node, exprFalse);
    }

    /**
     * Change the position of the node to be bottom left of relativeTo
     *
     * @param relativeTo The node that is used as the reference.
     * @param node       The node whose position will be changed.
     */
    public static void placeBottomLeft(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddle(relativeTo) - getHalvedWidth(node) - (baseWidth * X_UNIT)),
                (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT));
    }

    /**
     * Change the position of the node to be bottom of relativeTo
     *
     * @param relativeTo The node that is used as the reference.
     * @param node       The node whose position will be changed.
     */
    public static void placeBottom(Node relativeTo, Node node)
    {
        node.setLocation(getMiddle(relativeTo) - getHalvedWidth(node),
                (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT));
    }

    /**
     * Change the position of the node to be bottom right of relativeTo
     *
     * @param relativeTo The node that is used as the reference.
     * @param node       The node whose position will be changed.
     */
    public static void placeBottomRight(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddle(relativeTo) - getHalvedWidth(node) + (baseWidth * X_UNIT)),
                (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT));
    }

    /**
     * Change the position of the node to be left of relativeTo
     *
     * @param relativeTo The node that is used as the reference.
     * @param node       The node whose position will be changed.
     */
    public static void placeLeft(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddle(relativeTo) - getHalvedWidth(node) - (baseWidth * X_UNIT)), relativeTo.getY());
    }

    /**
     * Change the position of the node to be right of relativeTo
     *
     * @param relativeTo The node that is used as the reference.
     * @param node       The node whose position will be changed.
     */
    public static void placeRight(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddle(relativeTo) - getHalvedWidth(node) + (baseWidth * X_UNIT)), relativeTo.getY());
    }

    /**
     * Change the position of the node to be top left of relativeTo
     *
     * @param relativeTo The node that is used as the reference.
     * @param node       The node whose position will be changed.
     */
    public static void placeTopLeft(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddle(relativeTo) - getHalvedWidth(node) - (baseWidth * X_UNIT)),
                (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT));
    }

    /**
     * Change the position of the node to be top of relativeTo
     *
     * @param relativeTo The node that is used as the reference.
     * @param node       The node whose position will be changed.
     */
    public static void placeTop(Node relativeTo, Node node)
    {
        node.setLocation(getMiddle(relativeTo) - getHalvedWidth(node), (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT));
    }

    /**
     * Change the position of the node to be top right of relativeTo
     *
     * @param relativeTo The node that is used as the reference.
     * @param node       The node whose position will be changed.
     */
    public static void placeTopRight(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddle(relativeTo) - getHalvedWidth(node) + (baseWidth * X_UNIT)),
                (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT));
    }
}
