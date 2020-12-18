package dialogos.frame.nodes;

import com.clt.diamant.*;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.diamant.gui.NodePropertiesDialog;
import com.clt.gui.GUI;
import com.clt.gui.Passpartout;
import com.clt.script.exp.Type;
import com.clt.script.exp.Value;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StringInputNode extends Node implements FrameInput
{
    private String input;
    private Slot variable;
    private JComboBox<String> varCombo;

    public StringInputNode()
    {
        super();
        addEdge();
        setTitle(Resources.getString("String Input"));
    }

    @Override
    public Node execute(WozInterface comm, InputCenter input, ExecutionLogger logger)
    {
        graphicallyRecognize(comm.getLayeredPane());
        Node target = this.getEdge(0).getTarget();
        comm.transition(this, target, 0, null);
        return target;
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        List<Slot> all = this.getGraph().getAllVariables(Graph.LOCAL);
        String[] varNames = new String[all.size()];
        for (int inx = 0; inx < all.size(); inx++)
        {
            if (all.get(inx).getType().equals(Type.String))
            {
                varNames[inx] = all.get(inx).getName();
            }
        }

        constraints.gridy = 0;
        panel.add(new JLabel("Select variable to receive the input"), constraints);

        varCombo = new JComboBox<>(varNames);
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(varCombo, constraints);

        return panel;
    }

    @Override
    public void writeVoiceXML(XMLWriter w, IdMap uid_map) throws IOException
    {

    }

    @Override
    protected void writeAttributes(XMLWriter out, IdMap uid_map)
    {
        super.writeAttributes(out, uid_map);
    }

    @Override
    protected void readAttribute(XMLReader r, String name, String value, IdMap uid_map) throws SAXException
    {
        super.readAttribute(r, name, value, uid_map);
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
            for (Slot slot : this.getGraph().getAllVariables(Graph.LOCAL))
            {
                if (slot.getName().equals(varCombo.getItemAt(varCombo.getSelectedIndex())))
                {
                    variable = slot;
                    break;
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    //
    // Allow for entering of string, and continue on Apply or "Enter" keypress
    //
    private void graphicallyRecognize(JLayeredPane layer)
    {
        final JTextField result = new JTextField(30);
        result.setPreferredSize(new Dimension(300, 75));

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setOpaque(false);
        panel.add(result, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);

        final JButton apply = new JButton("Apply");
        apply.addActionListener(e ->
                                {
                                    synchronized (apply)
                                    {
                                        input = result.getText();
                                        apply.notifyAll();
                                    }
                                });

        final JButton stop = new JButton(GUI.getString("Cancel"));
        bottom.add(stop);
        bottom.add(apply);

        panel.add(bottom, BorderLayout.SOUTH);
        JComponent popup = null;
        if (layer != null)
        {
            popup = new Passpartout(panel, panel.getBackground(), false);
            popup.setSize(layer.getWidth(), layer.getHeight());
            layer.add(popup, JLayeredPane.POPUP_LAYER);
            layer.revalidate();
        }
        try
        {
            synchronized (apply)
            {
                apply.wait();
            }
        } catch (InterruptedException exp)
        {
            exp.printStackTrace();
        } finally
        {
            if (layer != null)
            {
                layer.remove(popup);
                if (layer.getParent() != null)
                {
                    layer.getParent().repaint();
                }
            }

            List<Slot> slotList = this.getGraph().getAllVariables(Graph.LOCAL);
            for (Slot slot : slotList)
            {
                if (slot.equals(variable))
                {
                    slot.setValue(Value.of(input));
                    break;
                }
            }
        }
    }

    @Override
    public Slot getVariable()
    {
        return variable;
    }

    @Override
    public void setVariable(Slot slot)
    {
        variable = slot;
    }
}

