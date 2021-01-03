package dialogos.frame.nodes;

import com.clt.diamant.*;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.script.exp.Value;
import com.clt.script.exp.values.StructValue;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import dialogos.frame.FrameNode;
import dialogos.frame.graph.NodeBuilder;
import dialogos.frame.struct.SlotStruct;
import dialogos.frame.utils.GrammarIO;
import dialogos.frame.utils.Token;
import dialogos.frame.utils.TokenList;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.util.Map;

/**
 * Take string from previous node, tokenize it, tag it with the grammars stored in the frame plugin settings
 * Then get the minimal, tagged list and find out which slots of the frame in the frame plugin settings has already
 * been filled with this one. Then return the frame to the graph/node.
 */
public class FillerNode extends Node
{
    private String frameNodeID = null;
    private String varID = null;

    private FrameNode frameNode = null;
    private Slot variable = null;
    private int expectedSlotInput = -1;

    public FillerNode()
    {
        super();
        addEdge();
        setTitle(Resources.getString("Filler"));
    }

    @Override
    public Node execute(WozInterface comm, InputCenter input, ExecutionLogger logger)
    {
        fillSlots();

        Node target = this.getEdge(0).getTarget();
        comm.transition(this, target, 0, null);
        return target;
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
        return null;
    }

    @Override
    protected void writeAttributes(XMLWriter out, IdMap uid_map)
    {
        super.writeAttributes(out, uid_map);

        Graph.printAtt(out, "slotIndex", expectedSlotInput);
        Graph.printAtt(out, "varID", variable.getId());
        Graph.printAtt(out, "frameNodeID", frameNode.getId());
    }

    @Override
    protected void readAttribute(XMLReader r, String name, String value, IdMap uid_map) throws SAXException
    {
        super.readAttribute(r, name, value, uid_map);

        switch (name)
        {
            case "slotIndex":
                this.expectedSlotInput = Integer.parseInt(value);
                break;
            case "varID":
                varID = value;
                break;
            case "frameNodeID":
                frameNodeID = value;
                r.addCompletionRoutine((XMLReader.CompletionRoutine) () ->
                {
                    Node node = uid_map.nodes.get(frameNodeID);
                    if (node instanceof FrameNode)
                    {
                        frameNode = (FrameNode) node;
                    }
                });
                break;
        }
    }

    @Override
    public void writeVoiceXML(XMLWriter w, IdMap uid_map)
    {

    }

    public void setVariable(Slot variable)
    {
        this.variable = variable;
    }

    public void setFrameNode(FrameNode frameNode)
    {
        this.frameNode = frameNode;

    }

    public void setExpectedSlotInput(int expectedSlotInput)
    {
        this.expectedSlotInput = expectedSlotInput;
    }

    private void fillSlots()
    {
        if (variable == null)
        {
            variable = frameNode.getVariable(varID);
        }

        String inputString = variable.getValue().toString();

        TokenList tokens = GrammarIO.tagTokenList(frameNode.frameStruct.getEmptySlotGrammars(),
                                                  inputString,
                                                  frameNode.getMaxTokenLength());

        for (Token token : tokens)
        {
            System.out.println(token.getLower());
        }

        GrammarIO.cleanupTokens(tokens);

        expectedSlotInput = expectedSlotInput != -1 ? expectedSlotInput : 0;
        for (int inx = 0; inx < frameNode.frameStruct.size(); inx++)
        {
            SlotStruct slotStruct =
                    frameNode.frameStruct.getSlot((expectedSlotInput + inx) % frameNode.frameStruct.size());

            if (!slotStruct.isFilled())
            {
                for (Token token : tokens)
                {
                    if (token.hasTag(slotStruct.getGrammarName()))
                    {
                        slotStruct.setValue(token.getLower());
                        frameNode.getVariable(NodeBuilder.filledVariableID(frameNode.frameStruct, slotStruct))
                                .setValue(Value.of(true));
                        tokens.remove(token);
                        break;
                    }
                }
            }
        }

        Slot structVar = null;

        for (Slot slot : frameNode.getSuperGraph().getVariables())
        {
            if (slot.getId().equals(frameNode.frameStruct.getResultVariableID()))
            {
                structVar = slot;
            }
        }

        if (structVar != null)
        {
            String[] names = new String[frameNode.frameStruct.getSlots().size()];
            Value[] values = new Value[frameNode.frameStruct.getSlots().size()];
            for (int inx = 0; inx < frameNode.frameStruct.size(); inx++)
            {
                SlotStruct slotStruct = frameNode.frameStruct.getSlot(inx);
                names[inx] = slotStruct.getName();
                values[inx] = Value.of(slotStruct.getValue());
            }

            structVar.setValue(new StructValue(names, values));
        }
    }
}
