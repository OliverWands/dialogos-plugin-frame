package dialogos.frame.utils.graph;

import com.clt.diamant.Slot;
import com.clt.diamant.graph.Comment;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.*;
import com.clt.script.exp.Type;
import de.saar.coli.dialogos.marytts.plugin.TTSNode;
import dialogos.frame.FrameNode;
import edu.cmu.lti.dialogos.sphinx.plugin.SphinxNode;

import java.awt.*;
import java.util.HashMap;

public class FrameGraph
{
    private StartNode startNode;
    private final FrameNode frameNode;
    private final NodeBuilder nodeBuilder;

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

    public void setVariables()
    {
        frameNode.addVariable("FRAMESIZE", "Size", Type.Int, Integer.toString(frameNode.frameStruct.size()));
        //frameNode.addVariable("GRAMMAR", "grammar", Type.String, "root $Input;\n$Input = hello;\n");

        HashMap<String, String> grammarMap = frameNode.frameStruct.getAllGrammars();
        if (grammarMap != null && !grammarMap.isEmpty())
        {
            for (String key : grammarMap.keySet())
            {
                frameNode.addGrammar(key, key, grammarMap.get(key));
            }
            // Calculate color out of key for the recognizer node.
        }
    }

    public void buildGraph()
    {
        setVariables();
        if (startNode != null)
        {
            SetVariableNode variableNode = new SetVariableNode();
            // ONLY ASSIGN WHEN IT EXISTS
            nodeBuilder.assignSetVariableNode(variableNode, "FRAMESIZE", "111");

            frameNode.addGrammar("firstGrammar", "grammar", "root $Input;\n$Input = hello | test;\n");

            ProcNode procedure = new ProcNode();
            procedure.setId("xyz");


            for (Slot slot : frameNode.getOwnedGraph().getVariables())
            {
                if (slot.getId().equals("FRAMESIZE"))
                {
                    procedure.getReturnVariables().add(slot);
                }
            }

            CallNode callNode = new CallNode();
            callNode.setProperty("procedure", procedure);

            frameNode.getMainOwner().getOwnedGraph().add(procedure);
            frameNode.getMainOwner().getOwnedGraph().add(callNode);

            Comment comment = new Comment();
            comment.setComment("This is a comment!");

            frameNode.getOwnedGraph().addComment(comment);

            SphinxNode sphinxNode = new SphinxNode();
            nodeBuilder.assignSphinxNode(sphinxNode, "firstGrammar");

            TTSNode speechSynthesis0 = new TTSNode();
            nodeBuilder.assignTTSNode(speechSynthesis0, "TTS 0", "null");

            TTSNode speechSynthesis1 = new TTSNode();
            nodeBuilder.assignTTSNode(speechSynthesis1, "TTS 1", "eins");

            ConditionalNode conditionalNode = new ConditionalNode();
            nodeBuilder.assignConditionalNode(conditionalNode, "Condition", "Size > 10");

            GraphBuilder.setConditionalEdges(conditionalNode, speechSynthesis0, speechSynthesis1);

            ReturnNode returnNode = new ReturnNode();
            returnNode.setTitle("Return");
            returnNode.setColor(Color.BLACK);

            frameNode.add(new Node[]{sphinxNode, variableNode, conditionalNode, speechSynthesis0, speechSynthesis1, returnNode});

            GraphBuilder.connectNodes(new Node[]{startNode, sphinxNode, variableNode, conditionalNode});

            GraphBuilder.setEdge(speechSynthesis0, returnNode);
            GraphBuilder.setEdge(speechSynthesis1, returnNode);

            GraphBuilder.placeBottom(startNode, sphinxNode);
            GraphBuilder.placeBottom(sphinxNode, variableNode);
            GraphBuilder.placeBottom(variableNode, conditionalNode);
            GraphBuilder.placeConditional(conditionalNode, speechSynthesis0, speechSynthesis1);
            GraphBuilder.placeBottomLeft(speechSynthesis1, returnNode);

        }
    }

}
