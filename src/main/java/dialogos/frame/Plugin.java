package dialogos.frame;

import com.clt.dialogos.plugin.PluginRuntime;
import com.clt.dialogos.plugin.PluginSettings;
import com.clt.diamant.IdMap;
import com.clt.diamant.graph.Graph;
import com.clt.diamant.graph.Node;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Arrays;
import java.util.Set;

public class Plugin implements com.clt.dialogos.plugin.Plugin
{

    @Override
    public void initialize()
    {
        Node.registerNodeTypes(com.clt.speech.Resources.getResources().createLocalizedString("ScriptNode"),
                               Arrays.asList(new Class<?>[]{FrameNode.class}));
    }

    @Override
    public String getId()
    {
        return "dialogos-plugin-frame";
    }

    @Override
    public String getName()
    {
        return "Frame-based Dialogsystems";
    }

    @Override
    public Icon getIcon()
    {
        return null;
    }

    @Override
    public String getVersion()
    {
        return "1.0";
    }   // DO NOT EDIT - This line is updated automatically by the make-release script.

    @Override
    public PluginSettings createDefaultSettings()
    {
        return new FramePluginSettings();
    }

    public static class FramePluginSettings extends PluginSettings
    {
        public Integer maxTokenWords = null;

        @Override
        public void writeAttributes(XMLWriter xmlWriter, IdMap idMap)
        {
            if (maxTokenWords != null)
            {
                Graph.printAtt(xmlWriter, "maxTokenWords", maxTokenWords);
            }
        }

        @Override
        protected void readAttribute(XMLReader xmlReader, String name, String value, IdMap idMap)
        {
            if (name.equals("maxTokenWords"))
            {
                maxTokenWords = Integer.parseInt(value);
            }
        }

        @Override
        public boolean isRelevantForNodes(Set<Class<? extends Node>> nodeTypes)
        {
            return nodeTypes.contains(FrameNode.class);
        }

        @Override
        public JComponent createEditor()
        {
            JPanel topPanel = new JPanel(new BorderLayout());
            JPanel inputPanel = new JPanel(new GridBagLayout());
            inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            GridBagConstraints constraints = new GridBagConstraints();

            JLabel lengthLabel = new JLabel("Token word count:");

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            inputPanel.add(lengthLabel, constraints);

            JTextField lengthText = new JTextField();
            lengthText.setColumns(5);

            if (maxTokenWords != null)
            {
                lengthText.setText(String.valueOf(maxTokenWords));
            }

            lengthText.getDocument().addDocumentListener(new DocumentListener()
            {
                public void changedUpdate(DocumentEvent e)
                {
                    warn();
                }

                public void removeUpdate(DocumentEvent e)
                {
                    warn();
                }

                public void insertUpdate(DocumentEvent e)
                {
                    warn();
                }

                public void warn()
                {
                    if (!(lengthText.getText().isEmpty() || lengthText.getText().isBlank()))
                    {
                        if (!lengthText.getText().matches("[0-9]+"))
                        {
                            maxTokenWords = null;
                            JOptionPane.showMessageDialog(null,
                                                          "Error: Please only enter numbers",
                                                          "Error Message",
                                                          JOptionPane.ERROR_MESSAGE);
                        }
                        else
                        {
                            maxTokenWords = Integer.parseInt(lengthText.getText());
                        }
                    }
                    else
                    {
                        maxTokenWords = null;
                    }
                }
            });

            constraints.gridx = 1;
            constraints.gridwidth = 3;
            constraints.anchor = GridBagConstraints.LINE_END;
            inputPanel.add(lengthText, constraints);

            topPanel.add(inputPanel, BorderLayout.NORTH);
            return topPanel;
        }

        @Override
        protected PluginRuntime createRuntime(Component component)
        {
            return new FramePluginRuntime(FramePluginSettings.this);
        }
    }

    static class FramePluginRuntime implements PluginRuntime
    {
        final FramePluginSettings settings;

        FramePluginRuntime(FramePluginSettings settings)
        {
            this.settings = settings;
        }

        Integer getMaxTokenWords()
        {
            return settings.maxTokenWords;
        }

        @Override
        public void dispose()
        {
        }
    }

}
