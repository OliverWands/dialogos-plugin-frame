package dialogos.frame;

import com.clt.diamant.*;
import com.clt.diamant.graph.Comment;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.diamant.graph.nodes.CallNode;
import com.clt.diamant.graph.nodes.ProcNode;
import com.clt.diamant.gui.GraphEditorFactory;
import com.clt.diamant.gui.NodePropertiesDialog;
import com.clt.script.exp.Type;
import com.clt.script.exp.types.StructType;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import dialogos.frame.graph.FrameGraph;
import dialogos.frame.graph.GraphBuilder;
import dialogos.frame.gui.FrameNodeMenu;
import dialogos.frame.struct.FrameStruct;
import dialogos.frame.struct.SlotStruct;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FrameNode extends CallNode
{
    private static final Color nodeColor = new Color(255, 187, 0);
    private Integer maxTokenLength = null;

    public FrameStruct frameStruct = new FrameStruct();
    private ProcNode procNode = null;

    public FrameNode()
    {
    }

    public static Color getDefaultColor()
    {
        return nodeColor;
    }

    public Integer getMaxTokenLength()
    {
        return maxTokenLength;
    }

    @Override
    public Node execute(WozInterface wozInterface, InputCenter input, ExecutionLogger logger)
    {
        Plugin.FramePluginRuntime runtime = (Plugin.FramePluginRuntime) this.getPluginRuntime(Plugin.class, wozInterface);
        maxTokenLength = runtime.getMaxTokenWords();

        if (procNode == null)
        {
            for (Node node : getSuperGraph().getNodes())
            {
                if (node instanceof ProcNode && node.getId().equals(frameStruct.getId()))
                {
                    procNode = (ProcNode) node;
                    break;
                }
            }
        }

        Node node = super.execute(wozInterface, input, logger);

        for (SlotStruct slotStruct : frameStruct.getSlots())
        {
            slotStruct.removeValue();
        }

        return node;
    }

    @Override
    protected void writeAttributes(XMLWriter out, IdMap uid_map)
    {
        super.writeAttributes(out, uid_map);

        frameStruct.writeToXML(out);
    }

    @Override
    protected void readAttribute(XMLReader r, String name, String value, IdMap uid_map) throws SAXException
    {
        super.readAttribute(r, name, value, uid_map);

        frameStruct.readFromXML(r);

        setUsedGrammars();
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
//        return super.createEditorComponent(properties);
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

            //
            // Open a view that shows the corresponding graph
            //
            if (!frameStruct.isEmpty())
            {
                if (procNode == null)
                {
                    procNode = new ProcNode();
                    procNode.setTitle(frameStruct.getName());
                    procNode.setId(frameStruct.getId());
                    procNode.setColor(nodeColor);
                    getSuperGraph().add(procNode);
                    GraphBuilder.placeLeft(this, procNode);

                    super.setProperty("procedure", procNode);
                }

                // If the graph is created  for the first time
                if (getVariable(frameStruct.getResultVariableID()) == null)
                {
                    Slot slot = new Slot();
                    slot.setId(frameStruct.getResultVariableID());
                    slot.setName(frameStruct.getResultVariableName());
                    slot.setType(new StructType());
                    getSuperGraph().getVariables().add(slot);
                }

                //
                // Add all used grammars from the file and super graph.
                // Delete the list beforehand to avoid duplicate and outdated grammars.
                //
                List<Grammar> owned = getOwnedGraph().getGrammars();
                owned.clear();
                setUsedGrammars();
                owned.addAll(frameStruct.getUsedGrammars());

                GraphEditorFactory.show(procNode);
                new FrameGraph(this).buildFrameInterpretationAlgo();
                GraphEditorFactory.get(procNode).closeEditor();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Add a node to the procedures graph.
     *
     * @param node The node that will be added.
     */
    public void add(Node node)
    {
        getOwnedGraph().add(node);
    }

    /**
     * Add multiple nodes to the procedures graph.
     *
     * @param nodes The nodes that will be added.
     */
    public void add(Node[] nodes)
    {
        for (Node node : nodes)
        {
            getOwnedGraph().add(node);
        }
    }

    /**
     * Add a comment to the procedures graph.
     *
     * @param comment The comment to be added.
     */
    public void addComment(Comment comment)
    {
        getOwnedGraph().addComment(comment);
    }

    /**
     * Creates a slot/variable with the given configuration and adds it to the graph of the FrameNode.
     *
     * @param name      The name of the variable.
     * @param type      The type of the variable.
     * @param initValue The initial value of the variable.
     */
    public void addVariable(String id, String name, Type type, String initValue)
    {
        Slot slot = new Slot();
        slot.setId(id);
        slot.setName(name);
        slot.setType(type);
        if (initValue != null)
        {
            slot.setInitValue(initValue);
        }

        addVariable(slot);
    }

    /**
     * Used to add a new variable to the graph of the FrameNode.
     *
     * @param variable The variable that will be added.
     */
    public void addVariable(Slot variable)
    {
        List<Slot> variables = getOwnedGraph().getVariables();

        if (variables == null)
        {
            variables = new ArrayList<>();
        }

        variables.add(variable);
    }

    public Slot getVariable(String id)
    {
        for (Slot slot : getOwnedGraph().getVariables())
        {
            if (slot.getId().equals(id))
            {
                return slot;
            }
        }
        return null;
    }

    /**
     * Create a new grammar using the parameters and adding it to the graph of the procedure.
     *
     * @param id             The grammar id.
     * @param name           The grammar name.
     * @param grammarContent The grammar content.
     */
    public void addGrammar(String id, String name, String grammarContent)
    {
        List<Grammar> grammars = getOwnedGraph().getGrammars();
        Grammar grammar = new Grammar(name, grammarContent);
        grammar.setId(id);
        grammars.add(grammar);
    }

    /**
     * Get a grammar from the graph of the procedure by name.
     * Can be null if a grammar with this name doesn't exist.
     *
     * @param name The name of the grammar.
     * @return The grammar or null.
     */
    public Grammar getGrammar(String name)
    {
        List<Grammar> grammars = getAllGrammars();
        for (Grammar grammar : grammars)
        {
            if (grammar != null && grammar.getName().equals(name))
            {
                return grammar;
            }
        }

        return null;
    }

    public List<Grammar> getAllGrammars()
    {
        List<Grammar> all = new ArrayList<>(getSuperGraph().getGrammars());
        all.addAll(frameStruct.getUsedGrammars());
        all.addAll(frameStruct.getLoadedGrammars());
        return all;
    }

    public void setUsedGrammars()
    {
        List<Grammar> grammars = new ArrayList<>();
        frameStruct.getSlots().forEach(slot -> grammars.add(getGrammar(slot.getGrammarName())));
        frameStruct.getUsedGrammars().clear();
        frameStruct.getUsedGrammars().addAll(grammars);
    }

    /**
     * Get the owned graph of the procedure.
     *
     * @return The graph.
     */
    public Graph getOwnedGraph()
    {
        return procNode.getOwnedGraph();
    }

    /**
     * Get the graph that contains this node.
     *
     * @return The graph.
     */
    public Graph getSuperGraph()
    {
        return getMainOwner().getOwnedGraph();
    }
}