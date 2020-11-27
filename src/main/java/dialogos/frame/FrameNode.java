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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FrameNode extends CallNode
{
    public final static String INPUT_VAR_NAME = "INPUT_VAR";
    public final static String INPUT_VAR_ID = "INPUT_VAR_ID";

    public FrameStruct frameStruct = new FrameStruct();
    private final ProcNode procNode = new ProcNode();

    public FrameNode()
    {
    }

    public static Color getDefaultColor()
    {
        return new Color(255, 187, 0);
    }

    @Override
    public Node execute(WozInterface wozInterface, InputCenter input, ExecutionLogger logger)
    {
        return super.execute(wozInterface, input, logger);
    }

    @Override
    public void writeVoiceXML(XMLWriter w, IdMap uid_map)
    {
        try
        {
            w.printElement("subdialog", new String[]{"name"}, new String[]{"graph" + uid_map.graphs.put(getOwnedGraph())});
            getOwnedGraph().exportVoiceXML(w, uid_map);
        } catch (IOException exp)
        {
            exp.printStackTrace();
        }
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

        if (frameStruct == null)
        {
            frameStruct = new FrameStruct();
        }

        Plugin.FramePluginSettings settings = (Plugin.FramePluginSettings) this.getPluginSettings(Plugin.class);

        // Pass the global tags to the frame struct when it will be executed.
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

            //
            // Open a view that shows the corresponding graph
            // TODO Remove
            if (!frameStruct.isEmpty() || true)
            {
                procNode.setTitle("Frame");
                procNode.setId("FRAMEPROCEDURE");
                super.setProperty("procedure", procNode);

                getMainOwner().getOwnedGraph().add(procNode);
                GraphBuilder.placeLeft(this, procNode);

                GraphBuilder.setGraphSize(getOwnedGraph(), 2, 2);

                frameStruct.setTagsFromFile(new File("/Users/oliverwandschneider/develop/IdeaProjects/dialogos-plugin-frame/src/test/resources/tagging.json"));

                //
                // Add all grammars from the tag file to the graph.
                //
                List<Grammar> grammars = getOwnedGraph().getGrammars();
                for (String key : frameStruct.getAllGrammars().keySet())
                {
                    grammars.add(new Grammar(key, frameStruct.getAllGrammars().get(key)));
                }

                GraphEditorFactory.show(procNode);

                FrameGraph frameGraphBuilder = new FrameGraph(this);
//                frameGraphBuilder.buildGraph();


                List<SlotStruct> slots = new ArrayList<>();
                slots.add(new SlotStruct("Slot0")
                        .setGrammarName("tag1")
                        .setIsAdditional(false)
                        .setQuery("Please enter Slot0"));
                slots.add(new SlotStruct("Slot1")
                        .setGrammarName("tag2")
                        .setIsAdditional(false)
                        .setQuery("Please enter Slot1"));
                slots.add(new SlotStruct("Slot2")
                        .setGrammarName("tag3")
                        .setIsAdditional(false)
                        .setQuery("Please enter Slot2"));

                String id = "Test_ID";
                frameStruct = new FrameStruct(id, slots);

                frameGraphBuilder.buildFrameInterpretationAlgo();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void add(Node node)
    {
        getOwnedGraph().add(node);
    }

    public void add(Node[] nodes)
    {
        for (Node node : nodes)
        {
            getOwnedGraph().add(node);
        }
    }

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
        slot.setInitValue(initValue);
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

    public void addGrammar(String id, String name, String grammarString)
    {
        List<Grammar> grammars = getOwnedGraph().getGrammars();
        Grammar grammar = new Grammar(name, grammarString);
        grammar.setId(id);
        grammars.add(grammar);
    }

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

    public Graph getOwnedGraph()
    {
        return procNode.getOwnedGraph();
    }

    public void setOwnedGraph(Graph graph)
    {
        procNode.setGraph(graph);
    }
}