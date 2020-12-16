package dialogos.frame;

import com.clt.dialogos.plugin.PluginRuntime;
import com.clt.dialogos.plugin.PluginSettings;
import com.clt.diamant.IdMap;
import com.clt.diamant.graph.Node;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import org.xml.sax.SAXException;

import javax.swing.*;
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
            return new JPanel();
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
