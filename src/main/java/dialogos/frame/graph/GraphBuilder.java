package dialogos.frame.graph;

import com.clt.diamant.graph.Comment;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.VisualGraphElement;
import com.clt.diamant.graph.nodes.ConditionalNode;

import java.util.ArrayList;

/**
 * Class to be used for the programmatic modification of dialog graphs.
 */
public class GraphBuilder
{
    private static final double X_UNIT = 1.25;
    private static final double Y_UNIT = 1.25;
    private static final int baseHeight = 55;
    private static final int baseWidth = 100;

    /**
     * Calculate half the width of a element.
     *
     * @param element The element for which the width will be calculated.
     * @return The width of half the element floored.
     */
    private static int getHalvedWidth(VisualGraphElement element)
    {
        return (int) Math.floor((float) element.getWidth() / 2);
    }

    /**
     * Calculate the X coordinate of the middle of a element.
     *
     * @param element The element for which the position will be calculated.
     * @return The X coordinate of the middle of the element.
     */
    private static int getMiddle(VisualGraphElement element)
    {
        return element.getX() + getHalvedWidth(element);
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
        setEdge(startNode, targetNode, 0);
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

    public static void setConditionalEdges(ConditionalNode node, Node exprTrue, Node exprFalse)
    {
        setEdge(node, exprTrue, 0);
        setEdge(node, exprFalse, 1);
    }

    public static void placeBottomLeft(VisualGraphElement relativeTo, VisualGraphElement element, double xScale, double yScale)
    {
        element.setLocation((int) Math
                                    .floor(getMiddle(relativeTo) - getHalvedWidth(element) - (baseWidth * X_UNIT * xScale)),
                            (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT * yScale));
    }

    /**
     * Change the position of the element to be bottom of relativeTo
     *
     * @param relativeTo The element that is used as the reference.
     * @param element    The element whose position will be changed.
     */
    public static void placeBottom(VisualGraphElement relativeTo, VisualGraphElement element)
    {
        placeBottom(relativeTo, element, 1);
    }

    public static void placeBottomAbs(VisualGraphElement relativeTo, VisualGraphElement element, int yUnits)
    {
        element.setLocation(getMiddle(relativeTo) - getHalvedWidth(element),
                            (int) Math.floor(relativeTo.getY() + yUnits));
    }

    public static void placeBottom(VisualGraphElement relativeTo, VisualGraphElement element, double scale)
    {
        element.setLocation(getMiddle(relativeTo) - getHalvedWidth(element),
                            (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT * scale));
    }

    public static void placeBottomRight(VisualGraphElement relativeTo, VisualGraphElement element, double xScale, double yScale)
    {
        element.setLocation((int) Math
                                    .floor(getMiddle(relativeTo) - getHalvedWidth(element) + (baseWidth * X_UNIT * xScale)),
                            (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT * yScale));
    }

    /**
     * Change the position of the element to be left of relativeTo
     *
     * @param relativeTo The element that is used as the reference.
     * @param element    The element whose position will be changed.
     */
    public static void placeLeft(VisualGraphElement relativeTo, VisualGraphElement element)
    {
        placeLeft(relativeTo, element, 1);
    }

    public static void placeLeft(VisualGraphElement relativeTo, VisualGraphElement element, double scale)
    {
        element.setLocation((int) Math
                                    .floor(getMiddle(relativeTo) - getHalvedWidth(element) - (baseWidth * X_UNIT * scale)),
                            relativeTo.getY());
    }

    /**
     * Change the position of the element to be right of relativeTo
     *
     * @param relativeTo The element that is used as the reference.
     * @param element    The element whose position will be changed.
     */
    public static void placeRight(VisualGraphElement relativeTo, VisualGraphElement element)
    {
        placeRight(relativeTo, element, 1);
    }

    public static void placeRight(VisualGraphElement relativeTo, VisualGraphElement element, double scale)
    {
        element.setLocation((int) Math
                                    .floor(getMiddle(relativeTo) - getHalvedWidth(element) + (baseWidth * X_UNIT * scale)),
                            relativeTo.getY());
    }

    public static void placeTopLeft(VisualGraphElement relativeTo, VisualGraphElement element, double xScale, double yScale)
    {
        element.setLocation((int) Math
                                    .floor(getMiddle(relativeTo) - getHalvedWidth(element) - (baseWidth * X_UNIT * xScale)),
                            (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT * yScale));
    }

    /**
     * Change the position of the element to be top of relativeTo
     *
     * @param relativeTo The element that is used as the reference.
     * @param element    The element whose position will be changed.
     */
    public static void placeTop(VisualGraphElement relativeTo, VisualGraphElement element)
    {
        placeTop(relativeTo, element, 1);
    }

    public static void placeTop(VisualGraphElement relativeTo, VisualGraphElement element, double scale)
    {
        element.setLocation(getMiddle(relativeTo) - getHalvedWidth(element),
                            (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT * scale));
    }

    /**
     * Change the position of the element to be top right of relativeTo
     *
     * @param relativeTo The element that is used as the reference.
     * @param element    The element whose position will be changed.
     */
    public static void placeTopRight(VisualGraphElement relativeTo, VisualGraphElement element)
    {
        placeTopRight(relativeTo, element, 1, 1);
    }

    public static void placeTopRight(VisualGraphElement relativeTo, VisualGraphElement element, double xScale, double yScale)
    {
        element.setLocation((int) Math
                                    .floor(getMiddle(relativeTo) - getHalvedWidth(element) + (baseWidth * X_UNIT * xScale)),
                            (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT * yScale));
    }

    public static void removeAllNodes(Graph graph)
    {
        for (Node node : new ArrayList<>(graph.getNodes()))
        {
            graph.remove(node);
        }
    }

    public static void removeAllComments(Graph graph)
    {
        for (Comment comment : new ArrayList<>(graph.getComments()))
        {
            graph.removeComment(comment);
        }
    }
}
