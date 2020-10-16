package dialogos.frame;

import com.clt.dialogos.plugin.PluginRuntime;
import com.clt.dialogos.plugin.PluginSettings;
import com.clt.diamant.IdMap;
import com.clt.diamant.graph.Node;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import dialogos.frame.nodes.FrameNode;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        return "dialogos.plugin.frame";
    }

    @Override
    public String getName()
    {
        return "Frame dialogos.frame.Plugin";
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
        return new SqlPluginSettings();
    }

    static class SqlPluginSettings extends PluginSettings
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
            JPanel p = new JPanel();
            JTextField urlField = new JTextField("No Text", 45);
            AbstractAction getSlot = new AbstractAction("Enter something")
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("## act -> " + urlField.getText());
                }
            };
            urlField.addActionListener((ActionEvent e) ->
            {
                System.out.println("## url -> " + urlField.getText());
            });
            p.add(new JButton(getSlot));
            p.add(urlField);
            return p;
        }

        // @Override
        // public JComponent createEditor()
        // {
        //     JPanel p = new JPanel();
        //     JTextField urlField = new JTextField(db.jdbcURL, 40);
        //     AbstractAction openSqliteFile = new AbstractAction("Open SQLite...", Images.load("OpenFile.png"))
        //     {
        //         @Override
        //         public void actionPerformed(ActionEvent e)
        //         {
        //             FileChooser fc = new FileChooser(new FileExtensionFilter("db", "Sqlite database file"));
        //             //fc.setFileFilter();
        //             File file = fc.standardGetFile(p);
        //             if (file != null)
        //             {
        //                 try
        //                 {
        //                     urlField.setText("jdbc:sqlite:" + file.getCanonicalPath());
        //                     db.setDatabaseURL(urlField.getText());
        //                 } catch (IOException e1)
        //                 {
        //                     e1.printStackTrace();
        //                 }
        //             }
        //         }
        //     };
        //     urlField.addActionListener((ActionEvent e) ->
        //     {
        //         db.setDatabaseURL(urlField.getText());
        //     });
        //     p.add(new JButton(openSqliteFile));
        //     p.add(urlField);
        //     // ability to select a file
        //     return p;
        // }

        @Override
        protected PluginRuntime createRuntime(Component component) throws Exception
        {
            return new FramePluginRuntime(SqlPluginSettings.this);
        }
    }

    static class FramePluginRuntime implements PluginRuntime
    {

        SqlPluginSettings settings;

        FramePluginRuntime(SqlPluginSettings settings)
        {
            this.settings = settings;
        }

        // dialogos.frame.Database getDatabase()
        // {
        //     return settings.db;
        // }

        @Override
        public void dispose()
        {
        }
    }

}
