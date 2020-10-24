package dialogos.frame;

import com.clt.dialogos.plugin.PluginRuntime;
import com.clt.dialogos.plugin.PluginSettings;
import com.clt.diamant.IdMap;
import com.clt.diamant.graph.Node;
import com.clt.gui.Images;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
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
        return "Frame-based Dialogs";
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
        private static File globalTags = null;

        public File getGlobalTagFile()
        {
            return globalTags;
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
            textField.addActionListener(e ->
            {
                if (e.getActionCommand().equals(JTextField.notifyAction))
                {
                    System.out.println(textField.getText());
                }
            });

            JLabel label = new JLabel();
            label.setText("Select the plugin-wide available tags:");

            JButton helpDialog = new JButton();
            helpDialog.setIcon(Images.load("Info16.png"));
            helpDialog.addActionListener(e ->
            {
                System.out.println("HELP!");
            });

            JFrame frame = new JFrame();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON and XML files.", "json", "xml"));
            fileChooser.addActionListener(e ->
            {
                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
                {
                    globalTags = fileChooser.getSelectedFile();
                    textField.setText(globalTags.getAbsolutePath());
                }
                fileChooser.setEnabled(false);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                frame.setVisible(false);
                frame.dispose();
            });

            JButton selectFile = new JButton();
            selectFile.setIcon(Images.load("OpenFile.png"));
            selectFile.addActionListener(e ->
            {
                fileChooser.setEnabled(true);
                fileChooser.showOpenDialog(frame);

                frame.setLayout(new BorderLayout());
                frame.add(fileChooser, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
                frame.repaint();
            });

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.LINE_START;

            //
            // Label
            //
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 0.8;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            gridPanel.add(label, constraints);

            //
            // Help Button
            //
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 0.2;
            gridPanel.add(helpDialog, constraints);

            //
            // TextField
            //
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 0.8;
            constraints.ipady = 0;
            gridPanel.add(textField, constraints);

            //
            // Select file button
            //
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.weightx = 0.2;
            gridPanel.add(selectFile, constraints);

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
