package dialogos.frame.nodes;

import com.clt.diamant.*;
import com.clt.diamant.graph.*;
import com.clt.diamant.graph.nodes.EndNode;
import com.clt.diamant.graph.nodes.OwnerNode;
import com.clt.diamant.gui.GraphEditorFactory;
import com.clt.diamant.gui.NodePropertiesDialog;
import com.clt.xml.XMLWriter;
import dialogos.frame.utils.FrameGraphBuilder;
import dialogos.frame.utils.Token;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class FrameNode extends OwnerNode
{
    public static final String SLOT = "frameSlot";
    Token token;

    public FrameNode()
    {
        this(new SubGraph(null));
    }

    public FrameNode(SubGraph ownedGraph)
    {
        super(ownedGraph);

        this.setProperty(SLOT, "");

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
        token.setContent("Hello");
    }

    public static Color getDefaultColor()
    {
        return Color.ORANGE;
    }

    @Override
    public Node execute(WozInterface comm, InputCenter input, ExecutionLogger logger)
    {
        logNode(logger);
        Node target;
        try
        {
            comm.subgraph(this, true);
            target = this.getOwnedGraph().execute(comm, input, logger);
        } finally
        {
            comm.subgraph(this, false);
            this.setActive(false);
        }

        int index = this.getEndNodeIndex(target);
        if (index >= 0)
        {
            target = this.getEdge(index).getTarget();
            comm.transition(this, target, index, null);
        }

        return target;
    }

    @Override
    public void writeVoiceXML(XMLWriter w, IdMap uid_map) throws IOException
    {
        w.printElement("subdialog", new String[]{"name"}, new String[]{"graph" + uid_map.graphs.put(this.getOwnedGraph())});
        this.getOwnedGraph().exportVoiceXML(w, uid_map);
    }

    /**
     * Returns a List of all Groovy-specific and non-Groovy variables
     *
     * @return list of variables
     */
    public java.util.List<AbstractVariable> getAllGlobalVariables()
    {
        List<AbstractVariable> allVariables = new ArrayList<AbstractVariable>(this.getOwnedGraph().getVariables());
        allVariables.addAll(this.getOwnedGraph().getGroovyVariables());
        return allVariables;
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel horiz = new JPanel();
        horiz.add(new JLabel("Enter Slot"));
        //set x- and y-position of JPanel in GridBagLayout
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(horiz, constraints);

        horiz = new JPanel();
        String[] columnNames = {"Slot", "Value"};
        String[][] data = {
                {"Start", "Hamburg"},
                {"End", "Rome"}
        };
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        // horiz.add(NodePropertiesDialog.createTextArea(properties, SLOT));
        // JTextField slotField = new JTextField("");
        // slotField.setColumns(30);
        // horiz.add(slotField);
        horiz.add(scrollPane);
        constraints.gridy = 1;
        panel.add(horiz, constraints);

        horiz = new JPanel();
        JButton graphButton = new JButton("Show Graph");
        graphButton.addActionListener(actionEvent ->
        {
            GraphEditorFactory.show(FrameNode.this);
            Collection<Node> nodes = FrameNode.this.getGraph().getNodes();
            for (Node node : nodes)
            {
                if (node.getClassName().equals("edu.cmu.lti.dialogos.db.sqlite.dialogos.frame.nodes.FrameNode"))
                {
                    FrameNode fNode = (FrameNode) node;

                    FrameGraphBuilder frameGraphBuilder = new FrameGraphBuilder(fNode);
                    frameGraphBuilder.buildGraph();
                }
            }
        });
        horiz.add(graphButton);
        constraints.gridy = 2;
        panel.add(horiz, constraints);

        return panel;
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

        if (dialog.approved())
        {
            for (String key : props.keySet())
            {
                if (!key.equals("numEdges"))
                {
                    this.setProperty(key, props.get(key));
                }
            }

            for (Iterator<String> it = this.properties.keySet().iterator(); it.hasNext(); )
            {
                String key = it.next();
                if (!props.containsKey(key))
                {
                    it.remove();
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
