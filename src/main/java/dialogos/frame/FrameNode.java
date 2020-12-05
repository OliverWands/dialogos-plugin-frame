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
import com.clt.xml.XMLWriter;
import dialogos.frame.gui.FrameNodeMenu;
import dialogos.frame.utils.graph.FrameGraph;
import dialogos.frame.utils.graph.GraphBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FrameNode extends CallNode
{
    public FrameStruct frameStruct = new FrameStruct();
    private final ProcNode procNode = new ProcNode();
    private static final Color nodeColor = new Color(255, 187, 0);

    public FrameNode()
    {
    }

    public static Color getDefaultColor()
    {
        return nodeColor;
    }

    @Override
    public Node execute(WozInterface wozInterface, InputCenter input, ExecutionLogger logger)
    {
        Node node = super.execute(wozInterface, input, logger);

        List<Slot> superVariables = getSuperGraph().getVariables();

        for (Slot variable : getOwnedGraph().getVariables())
        {
            String varName = variable.getName();
            if (!varName.matches("\\d_.+_.+"))
            {
                continue;
            }

            String[] varParts = variable.getName().split("_");
            int index = Integer.getInteger(varParts[0]);
            String type = varParts[1];
            if (type.equals(FrameGraph.INPUT))
            {
                SlotStruct slotStruct = frameStruct.getSlot(index);
                slotStruct.setValue(variable.getValue().toString());

                Slot slot = new Slot();
                slot.setName(slotStruct.getName());
//                slot.setType(StructType.String);
                slot.setType(Type.String);
                slot.setValue(variable.getValue());

                superVariables.add(slot);
            }
        }

        return node;
    }

    @Override
    public void writeVoiceXML(XMLWriter w, IdMap uid_map)
    {
        super.writeVoiceXML(w, uid_map);
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
        // TODO
        //  return super.createEditorComponent(properties);
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

//        Plugin.FramePluginSettings settings = (Plugin.FramePluginSettings) this.getPluginSettings(Plugin.class);

        // Pass the global tags to the frame struct when it will be executed.
//        if (settings != null)
//        {
//            frameStruct.setFromSettings(settings);
//        }

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
            // TODO Remove
            if (!frameStruct.isEmpty() || true)
            {
                procNode.setTitle("Frame");
                procNode.setId("FRAMEPROCEDURE");
                procNode.setColor(nodeColor);

                super.setProperty("procedure", procNode);

                getSuperGraph().add(procNode);
                GraphBuilder.placeLeft(this, procNode);

                GraphBuilder.setGraphSize(getOwnedGraph(), 2, 2);

                // For testing
//                String id = "Test_ID";
//                List<SlotStruct> slots = new ArrayList<>();
//                slots.add(new SlotStruct("Slot0").setGrammarName("tag1").setQuery("Please enter Slot0"));
//                slots.add(new SlotStruct("Slot1").setGrammarName("tag2").setQuery("Please enter Slot1"));
//                slots.add(new SlotStruct("Slot2").setGrammarName("tag3").setQuery("Please enter Slot2"));
//
//                frameStruct = new FrameStruct(id, slots);
//                frameStruct.setTagsFromFile(new File("/Users/oliverwandschneider/develop/IdeaProjects/dialogos-plugin-frame/src/test/resources/tagging.json"));

                //
                // Add all grammars from the tag file to the graph.
                //
                List<Grammar> grammars = getOwnedGraph().getGrammars();
                for (String key : frameStruct.getGrammars().keySet())
                {
                    grammars.add(new Grammar(key, frameStruct.getGrammars().get(key)));
                }

                GraphEditorFactory.show(procNode);

                FrameGraph frameGraphBuilder = new FrameGraph(this);
                frameGraphBuilder.buildFrameInterpretationAlgo();
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

    // TODO Keep track of all added variables, grammars etc. to delete all of them once the node gets removed

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

    /**
     * Add a grammar to the graph of the FrameNode.
     *
     * @param name          The name of the grammar.
     * @param grammarString The content of the grammar.
     * @return The id of the newly created grammar.
     */
    public String addGrammar(String name, String grammarString)
    {
        List<Grammar> grammars = getOwnedGraph().getGrammars();
        Grammar grammar = new Grammar(name, grammarString);
        grammar.setId(UUID.randomUUID().toString());
        grammars.add(grammar);

        return grammar.getId();
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
        List<Grammar> grammars = getOwnedGraph().getGrammars();
        for (Grammar grammar : grammars)
        {
            if (grammar.getName().equals(name))
            {
                return grammar;
            }
        }

        return null;
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