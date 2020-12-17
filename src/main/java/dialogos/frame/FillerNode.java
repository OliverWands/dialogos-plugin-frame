package dialogos.frame;

import com.clt.diamant.*;
import com.clt.diamant.graph.Node;
import com.clt.script.exp.Value;
import com.clt.xml.XMLWriter;
import dialogos.frame.utils.graph.NodeBuilder;
import dialogos.frame.utils.tags.GrammarIO;
import dialogos.frame.utils.tags.Token;
import dialogos.frame.utils.tags.TokenList;

import javax.swing.*;
import java.util.Map;

/**
 * Take string from previous node, tokenize it, tag it with the grammars stored in the frame plugin settings
 * Then get the minimal, tagged list and find out which slots of the frame in the frame plugin settings has already
 * been filled with this one. Then return the frame to the graph/node.
 */
public class FillerNode extends Node
{
    private final Slot variable;
    private final FrameNode frameNode;
    private int expectedSlotInput;

    public FillerNode(Slot variable, FrameNode frameNode)
    {
        this(variable, frameNode, -1);
    }

    public FillerNode(Slot variable, FrameNode frameNode, int expectedSlotInput)
    {
        super();

        this.variable = variable;
        this.frameNode = frameNode;
        this.expectedSlotInput = expectedSlotInput;

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
    public void writeVoiceXML(XMLWriter w, IdMap uid_map)
    {

    }

    private void fillSlots()
    {
        String inputString = variable.getValue().toString();

        TokenList tokens = GrammarIO.tagTokenList(frameNode.frameStruct.getUsedGrammars(), inputString);

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
                        frameNode.getVariable(NodeBuilder.filledVariableName(slotStruct)).setValue(Value.of(true));
                        tokens.remove(token);
                        break;
                    }
                }
            }
        }

        for (SlotStruct slotStruct : frameNode.frameStruct.getSlots())
        {
            System.out.printf("%s %s\n", slotStruct.getName(), slotStruct.getValue());
        }
    }
}
