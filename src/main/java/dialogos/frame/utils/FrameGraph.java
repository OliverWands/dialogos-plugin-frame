package dialogos.frame.utils;

import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.ReturnNode;
import com.clt.diamant.graph.nodes.StartNode;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.FrameNode;

import java.awt.*;

public class FrameGraph
{
    private final FrameNode frameNode;
    private StartNode startNode;

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
    }

    public void buildGraph()
    {
        if (startNode != null)
        {
            TTSNode speechSynthesis0 = new TTSNode();
            speechSynthesis0.setTitle("0");
            speechSynthesis0.setProperty("prompt", "Null");

            TTSNode speechSynthesis1 = new TTSNode();
            speechSynthesis1.setTitle("1");
            speechSynthesis1.setProperty("prompt", "Eins");

            TTSNode speechSynthesis2 = new TTSNode();
            speechSynthesis2.setTitle("2");
            speechSynthesis2.setProperty("prompt", "Zwei");

            TTSNode speechSynthesis3 = new TTSNode();
            speechSynthesis3.setTitle("3");
            speechSynthesis3.setProperty("prompt", "Drei");

            TTSNode speechSynthesis4 = new TTSNode();
            speechSynthesis4.setTitle("4");
            speechSynthesis4.setProperty("prompt", "Vier");

            TTSNode speechSynthesis5 = new TTSNode();
            speechSynthesis5.setTitle("5");
            speechSynthesis5.setProperty("prompt", "Fünf");

            TTSNode speechSynthesis6 = new TTSNode();
            speechSynthesis6.setTitle("6");
            speechSynthesis6.setProperty("prompt", "Sechs");

            TTSNode speechSynthesis7 = new TTSNode();
            speechSynthesis7.setTitle("7");
            speechSynthesis7.setProperty("prompt", "Sieben");

            TTSNode speechSynthesis8 = new TTSNode();
            speechSynthesis8.setTitle("8");
            speechSynthesis8.setProperty("prompt", "Acht");

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

            GraphBuilder.setEdge(startNode, speechSynthesis0);
            GraphBuilder.setEdge(speechSynthesis0, speechSynthesis1);
            GraphBuilder.setEdge(speechSynthesis1, speechSynthesis2);
            GraphBuilder.setEdge(speechSynthesis2, speechSynthesis3);
            GraphBuilder.setEdge(speechSynthesis3, speechSynthesis4);
            GraphBuilder.setEdge(speechSynthesis4, speechSynthesis5);
            GraphBuilder.setEdge(speechSynthesis5, speechSynthesis6);
            GraphBuilder.setEdge(speechSynthesis6, speechSynthesis7);
            GraphBuilder.setEdge(speechSynthesis7, speechSynthesis8);
            GraphBuilder.setEdge(speechSynthesis8, returnNode);

            GraphBuilder.placeBottom(startNode, speechSynthesis0);
            GraphBuilder.placeTopRight(speechSynthesis0, speechSynthesis1);
            GraphBuilder.placeBottomRight(speechSynthesis1, speechSynthesis2);
            GraphBuilder.placeBottomLeft(speechSynthesis2, speechSynthesis3);
            GraphBuilder.placeLeft(speechSynthesis3, speechSynthesis4);
            GraphBuilder.placeTopLeft(speechSynthesis4, speechSynthesis5);
            GraphBuilder.placeTop(speechSynthesis5, speechSynthesis6);
            GraphBuilder.placeLeft(speechSynthesis6, speechSynthesis7);
            GraphBuilder.placeBottomLeft(speechSynthesis7, speechSynthesis8);
            GraphBuilder.placeRight(speechSynthesis8, returnNode);

        }
    }

}
