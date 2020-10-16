package dialogos.frame.nodes;

import com.clt.diamant.ExecutionLogger;
import com.clt.diamant.IdMap;
import com.clt.diamant.InputCenter;
import com.clt.diamant.WozInterface;
import com.clt.diamant.graph.Node;
import com.clt.xml.XMLWriter;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class RecognizerNode extends Node
{
    @Override
    public Node execute(WozInterface comm, InputCenter input, ExecutionLogger logger)
    {
        return null;
    }

    @Override
    public JComponent createEditorComponent(Map<String, Object> properties)
    {
        return null;
    }

    @Override
    public void writeVoiceXML(XMLWriter w, IdMap uid_map) throws IOException
    {

    }
}
