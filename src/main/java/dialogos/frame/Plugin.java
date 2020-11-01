package dialogos.frame;

import com.clt.dialogos.plugin.PluginRuntime;
import com.clt.dialogos.plugin.PluginSettings;
import com.clt.diamant.IdMap;
import com.clt.diamant.graph.Node;
import com.clt.gui.Images;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import dialogos.frame.utils.TagIO;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        return "Frame-based Dialogsystem";
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

    static class FramePluginSettings extends PluginSettings
    {
        public Map<String, String> definedMap = new HashMap<>();
        public Map<String, String> regexMap = new HashMap<>();
        private File globalTags = null;
        private String test = "HELLO TEST";

        public String getTest()
        {
            return test;
        }

        // TODO decide if necessary
        public File getGlobalTagFile()
        {
            return globalTags;
        }

        public Map<String, String> getDefinedMap()
        {
            return definedMap;
        }

        public Map<String, String> getRegexMap()
        {
            return regexMap;
        }

        @Override
        public void writeAttributes(XMLWriter xmlWriter, IdMap idMap)
        {
        }

        @Override
        protected void readAttribute(XMLReader xmlReader, String name, String value, IdMap idMap) throws SAXException
        {
        }

        @Override
        public boolean isRelevantForNodes(Set<Class<? extends Node>> nodeTypes)
        {
            return nodeTypes.contains(FrameNode.class);
        }

        @Override
        public JComponent createEditor()
        {
            JPanel panel = new JPanel(new BorderLayout());
            JPanel gridPanel = new JPanel(new GridBagLayout());
            JTextField textField = new JTextField();

            //
            // TODO Handle path input
            //
            textField.addActionListener(e -> System.out.println(textField.getText()));

            JLabel instruction = new JLabel();
            instruction.setText("Select the plugin-wide available tags:");

            JButton helpDialog = new JButton();
            helpDialog.setToolTipText("The file must be either xml or json.");
            helpDialog.setIcon(Images.load("Info16.png"));

            JLabel tagInfo = new JLabel();
            tagInfo.setFont(new Font(tagInfo.getFont().getName(), Font.PLAIN, 14));

            JFrame fileChooserFrame = new JFrame();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON and XML files.", "json", "xml"));

            JButton selectFile = new JButton();
            selectFile.setIcon(Images.load("OpenFile.png"));
            selectFile.addActionListener(e ->
            {
                fileChooser.setEnabled(true);
                int val = fileChooser.showOpenDialog(fileChooserFrame);
                if (val == JFileChooser.APPROVE_OPTION)
                {
                    globalTags = fileChooser.getSelectedFile();
                    textField.setText(globalTags.getAbsolutePath());

                    TagIO.jsonToTags(globalTags, definedMap, regexMap);
                    int counts = TagIO.countTags(definedMap, regexMap);

                    tagInfo.setText(String.format("Contains %d tags,\n%d words and %d regex patterns.",
                            counts, definedMap.size(), regexMap.size()));
                }
            });

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.LINE_START;

            //
            // Label
            //
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 0.8;
            constraints.gridwidth = 2;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            gridPanel.add(instruction, constraints);

            //
            // Help Button
            //
            constraints.anchor = GridBagConstraints.LINE_END;
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 0.2;
            constraints.fill = GridBagConstraints.NONE;
            gridPanel.add(helpDialog, constraints);

            //
            // TextField
            //
            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 0.0;
            constraints.gridwidth = 3;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            gridPanel.add(textField, constraints);

            //
            // Select file button
            //
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.weightx = 0.0;
            constraints.gridwidth = 2;
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.CENTER;
            gridPanel.add(selectFile, constraints);

            //
            // Info label
            //
            constraints.gridx = 0;
            constraints.gridy = 3;
            constraints.weightx = 0.0;
            constraints.gridwidth = 2;
            constraints.fill = GridBagConstraints.NONE;
            constraints.anchor = GridBagConstraints.CENTER;
            gridPanel.add(tagInfo, constraints);

            panel.add(gridPanel, BorderLayout.NORTH);
            return panel;
        }

        @Override
        protected PluginRuntime createRuntime(Component component) throws Exception
        {
            return new FramePluginRuntime(FramePluginSettings.this);
        }
    }

    static class FramePluginRuntime implements PluginRuntime
    {
        FramePluginSettings settings;

        FramePluginRuntime(FramePluginSettings settings)
        {
            this.settings = settings;
        }

        @Override
        public void dispose()
        {
        }
    }

}
