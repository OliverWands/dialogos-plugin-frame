package dialogos.frame;

import com.clt.dialogos.plugin.PluginRuntime;
import com.clt.dialogos.plugin.PluginSettings;
import com.clt.diamant.IdMap;
import com.clt.diamant.graph.Node;
import com.clt.gui.Images;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import dialogos.frame.nodes.FrameNode;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
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
        return "Frame";
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
            JPanel panel = new JPanel();
            JLabel label = new JLabel();
            JFrame frame = new JFrame();
            JButton selectFile = new JButton();
            JFileChooser fileChooser = new JFileChooser();

            label.setText("Select global tags:");

            selectFile.setIcon(Images.load("OpenFile.png"));

            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON and XML files.", "json", "xml"));
            fileChooser.addActionListener(e ->
            {
                switch (e.getActionCommand())
                {
                    case JFileChooser.APPROVE_SELECTION:
                        System.out.printf("Chose %s\n", fileChooser.getSelectedFile());
                    case JFileChooser.CANCEL_SELECTION:
                        fileChooser.setEnabled(false);
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                        frame.setVisible(false);
                        frame.dispose();
                        break;
                }
            });

            selectFile.addActionListener(e ->
            {
                frame.setLayout(new BorderLayout());
                frame.add(fileChooser, BorderLayout.CENTER);

                fileChooser.setEnabled(true);
                fileChooser.showOpenDialog(frame);

                frame.pack();
                frame.setVisible(true);
                frame.repaint();
            });

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(label);
            panel.add(selectFile);

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
