package dialogos.frame.utils;

import com.clt.diamant.graph.Node;

public class GraphBuilder
{
    private static final double X_UNIT = 1.25;
    private static final double Y_UNIT = 1.25;
    private static final int baseHeight = 55;
    private static final int baseWidth = 66;

    private static int getMiddle(Node node)
    {
        return (int) Math.floor((float) node.getWidth() / 2);
    }

    private static int getMiddleX(Node node)
    {
        return node.getX() + getMiddle(node);
    }

    public static void setEdge(Node startNode, Node targetNode)
    {
        if (targetNode == null)
        {
            return;
        }

        startNode.getEdge(0).setTarget(targetNode);
    }

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

    public static void placeBottomLeft(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddleX(relativeTo) - getMiddle(node) - (baseWidth * X_UNIT)),
                (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT));
    }

    public static void placeBottom(Node relativeTo, Node node)
    {
        node.setLocation(getMiddleX(relativeTo) - getMiddle(node),
                (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT));
    }

    public static void placeBottomRight(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddleX(relativeTo) - getMiddle(node) + (baseWidth * X_UNIT)),
                (int) Math.floor(relativeTo.getY() + baseHeight * Y_UNIT));
    }

    public static void placeLeft(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddleX(relativeTo) - getMiddle(node) - (baseWidth * X_UNIT)), relativeTo.getY());
    }

    public static void placeRight(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddleX(relativeTo) - getMiddle(node) + (baseWidth * X_UNIT)), relativeTo.getY());
    }

    public static void placeTopLeft(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddleX(relativeTo) - getMiddle(node) - (baseWidth * X_UNIT)),
                (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT));
    }

    public static void placeTop(Node relativeTo, Node node)
    {
        node.setLocation(getMiddleX(relativeTo) - getMiddle(node), (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT));
    }

    public static void placeTopRight(Node relativeTo, Node node)
    {
        node.setLocation((int) Math.floor(getMiddleX(relativeTo) - getMiddle(node) + (baseWidth * X_UNIT)),
                (int) Math.floor(relativeTo.getY() - baseHeight * Y_UNIT));
    }
}
