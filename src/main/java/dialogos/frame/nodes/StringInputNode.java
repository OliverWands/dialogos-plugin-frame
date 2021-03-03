package dialogos.frame.nodes;

import com.clt.diamant.*;
import com.clt.diamant.graph.Edge;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StringInputNode extends Node
{
    private String input;
    private String varID;
    private JComboBox<String> varCombo;
    private Edge targetEdge;

    public StringInputNode()
    {
        super();
        addEdge();
        addEdge();
        setTitle(Resources.getString("String Input"));
    }

    @Override
    public Node execute(WozInterface comm, InputCenter input, ExecutionLogger logger)
    {
        handleInput(comm.getLayeredPane());
        Node target = targetEdge.getTarget();
        comm.transition(this, target, 0, null);
        return target;
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        List<Slot> stringVars = new ArrayList<>();
        this.getGraph().getAllVariables(Graph.LOCAL).forEach(var -> {
            if (var.getType().equals(Type.String))
            {
                stringVars.add(var);
            }
        });

        String[] varNames = new String[stringVars.size()];
        for (int inx = 0; inx < stringVars.size(); inx++)
        {
            varNames[inx] = stringVars.get(inx).getName();
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
    public void writeVoiceXML(XMLWriter w, IdMap uid_map)
    {

    }

    @Override
    protected void writeAttributes(XMLWriter out, IdMap uid_map)
    {
        super.writeAttributes(out, uid_map);

        Graph.printAtt(out, "varID", varID);
    }

    @Override
    protected void readAttribute(XMLReader r, String name, String value, IdMap uid_map) throws SAXException
    {
        super.readAttribute(r, name, value, uid_map);

        if ("varID".equals(name))
        {
            varID = value;
        }
    }

    @Override
    public boolean editProperties(Component parent)
    {
        Map<String, Object> props = (Map<String, Object>) this.deep_copy(this.properties);

        NodePropertiesDialog dialog =
                new NodePropertiesDialog(this, parent, props, this.createEditorComponent(props));
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
                    varID = slot.getId();
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
    private void handleInput(JLayeredPane layer)
    {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        top.add(new JLabel("Enter text:"));

        final JTextField result = new JTextField(30);
        result.setPreferredSize(new Dimension(300, 75));

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setOpaque(false);
        panel.add(result, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);

        final JButton apply = new JButton("Apply");
        apply.addActionListener(e -> {
            synchronized (apply)
            {
                input = result.getText();
                apply.notifyAll();
                targetEdge = getOutEdges().get(0);
            }
        });

        final JButton stop = new JButton(GUI.getString("Cancel"));
        stop.addActionListener(e -> {
            synchronized (apply)
            {
                apply.notifyAll();
                targetEdge = getOutEdges().get(1);
            }
        });

        bottom.add(stop);
        bottom.add(apply);

        panel.add(top, BorderLayout.NORTH);
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

            for (Slot slot : this.getGraph().getAllVariables(Graph.LOCAL))
            {
                if (varID != null && slot.getId().equals(varID))
                {
                    slot.setValue(Value.of(input));
                    break;
                }
            }
        }
    }

    public void setVariableID(String id)
    {
        varID = id;
    }

    @Override
    public Color getPortColor(int portNumber)
    {
        if (portNumber < this.numEdges() - 1)
        {
            return new Color(89, 168, 105);
        }
        else
        {
            return new Color(204, 96, 99);
        }
    }
}

