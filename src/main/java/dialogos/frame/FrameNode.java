package dialogos.frame;

import com.clt.diamant.ExecutionLogger;
import com.clt.diamant.IdMap;
import com.clt.diamant.InputCenter;
import com.clt.diamant.WozInterface;
import com.clt.diamant.graph.*;
import com.clt.diamant.graph.nodes.EndNode;
import com.clt.diamant.graph.nodes.OwnerNode;
import com.clt.diamant.gui.GraphEditorFactory;
import com.clt.diamant.gui.NodePropertiesDialog;
import com.clt.xml.XMLWriter;
import dialogos.frame.gui.FrameNodeMenu;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class FrameNode extends OwnerNode
{
    public FrameStruct frameStruct = new FrameStruct();
    // To be used to decide the instance and the corresponding tags in the cache
    private final String FRAME_UUID = UUID.randomUUID().toString().replace("-", "");

    public FrameNode()
    {
        this(new SubGraph(null));
    }

    public FrameNode(SubGraph ownedGraph)
    {
        super(ownedGraph);
        for (EndNode n : this.getEndNodes())
        {
            this.addEdge(n.getTitle());
        }

        this.addEdgeListener(new EdgeListener()
        {
            public void edgeAdded(EdgeEvent e)
            {
                final Node node = FrameNode.this.getEndNode(e.getIndex());
                FrameNode.this.addEdge(node.getTitle());
            }

            public void edgeRemoved(EdgeEvent e)
            {
                FrameNode.this.removeEdge(e.getIndex());
            }

            public void edgeUpdated(EdgeEvent e)
            {
                Edge edge = FrameNode.this.getEdge(e.getIndex());
                edge.setCondition(FrameNode.this.getPortName(e.getIndex()));
                edge.setColor(FrameNode.this.getPortColor(e.getIndex()));
            }
        });
    }

    public static Color getDefaultColor()
    {
        return new Color(255, 187, 0);
    }

    @Override
    public Node execute(WozInterface wozInterface, InputCenter input, ExecutionLogger logger)
    {
        logNode(logger);
        Node target;
        try
        {
            wozInterface.subgraph(this, true);
            target = this.getOwnedGraph().execute(wozInterface, input, logger);
        } finally
        {
            wozInterface.subgraph(this, false);
            this.setActive(false);
        }

        int index = this.getEndNodeIndex(target);
        if (index >= 0)
        {
            target = this.getEdge(index).getTarget();
            wozInterface.transition(this, target, index, null);
        }

        return target;
    }

    @Override
    public void writeVoiceXML(XMLWriter w, IdMap uid_map) throws IOException
    {
        w.printElement("subdialog", new String[]{"name"}, new String[]{"graph" + uid_map.graphs.put(this.getOwnedGraph())});
        this.getOwnedGraph().exportVoiceXML(w, uid_map);
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
        return new FrameNodeMenu(this);
    }

    @Override
    public boolean editProperties(Component parent)
    {
        Map<String, Object> props = (Map<String, Object>) this.deep_copy(this.properties);

        NodePropertiesDialog dialog = new NodePropertiesDialog(this, parent, props, this.createEditorComponent(props));
        dialog.setVisible(true);

        this.setProperty(NodePropertiesDialog.LAST_TAB, props.get(NodePropertiesDialog.LAST_TAB));
        this.setProperty(NodePropertiesDialog.LAST_SIZE, props.get(NodePropertiesDialog.LAST_SIZE));
        this.setProperty(NodePropertiesDialog.LAST_POSITION, props.get(NodePropertiesDialog.LAST_POSITION));

        if (frameStruct == null)
        {
            frameStruct = new FrameStruct();
        }

        Plugin.FramePluginSettings settings = (Plugin.FramePluginSettings) this.getPluginSettings(Plugin.class);

        // Pass the global tags to the framestruct when it will be executed.
        if (settings != null)
        {
            frameStruct.setFromSettings(settings);
        }

        if (dialog.approved())
        {
            for (String key : props.keySet())
            {
                if (!key.equals("numEdges"))
                {
                    this.setProperty(key, props.get(key));
                }
            }

            this.properties.keySet().removeIf(key -> !props.containsKey(key));

            if (!frameStruct.isEmpty())
            {
                GraphEditorFactory.show(this);
                Collection<Node> mainGraphNodes = this.getGraph().getNodes();
                for (Node node : mainGraphNodes)
                {
                    if (node.getClassName().equals(FrameNode.class.getName()))
                    {
                        FrameNode fNode = (FrameNode) node;
                        FrameGraph frameGraphBuilder = new FrameGraph(fNode);
                        frameGraphBuilder.buildGraph();
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

//    private void frameTest()
//    {
//        FrameStruct frame = new FrameStruct("HVV");
//        SlotStruct startOrt = new SlotStruct("Start");
//        startOrt.setMatchedTags(new String[]{"haltestelle"});
//
//        SlotStruct zielOrt = new SlotStruct("Ziel");
//        zielOrt.setMatchedTags(new String[]{"haltestelle"});
//
//        SlotStruct zeitpunkt = new SlotStruct("Zeit");
//        zeitpunkt.setMatchedTags(new String[]{"zeit"});
//
//        frame.addSlot(startOrt);
//        frame.addSlot(zielOrt);
//        frame.addSlot(zeitpunkt);
//
//        TagIO.jsonToTags(new File("/Users/oliverwandschneider/develop/tagging.json"),
//                frameStruct.getAllDefinedTags(),
//                frameStruct.getAllRegexTags());
//
//        FrameTokenizer tokenizer = new FrameTokenizer(2);
//        Tagger tagger = new Tagger(frameStruct.getAllDefinedTags(), frameStruct.getAllRegexTags());
//        TokenList testList = tokenizer.tokenize("Ich will von Stade bis Hauptbahnhof");
//        tagger.tagTokenList(testList);
//    }
}