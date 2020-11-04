package dialogos.frame.nodes;

import com.clt.diamant.graph.Node;
import com.clt.diamant.gui.GraphEditorFactory;
import com.clt.gui.Images;
import dialogos.frame.FrameNode;
import dialogos.frame.utils.FrameGraph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;

public class FrameNodeMenu extends JPanel
{
    FrameNode frameNode;

    public FrameNodeMenu(FrameNode frameNode)
    {
        super(new BorderLayout());

        this.frameNode = frameNode;

        setBorder(new EmptyBorder(0, 5, 20, 5));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel title = new JLabel("Enter or create a Frame");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 14));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(title, constraints);

        JLabel name = new JLabel("Create:");

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(name, constraints);

        JButton createButton = new JButton("Create Frame");
        createButton.addActionListener(e ->
        {
            JFrame frame = new JFrame();
            NewFrameDialog frameDialog = new NewFrameDialog(frame, "Create Frame");
            frameDialog.addWindowListener(new WindowListener()
            {
                @Override
                public void windowOpened(WindowEvent e)
                {

                }

                @Override
                public void windowClosing(WindowEvent e)
                {

                }

                @Override
                public void windowClosed(WindowEvent e)
                {
                    frameNode.frameStruct = frameDialog.getFrameStruct();
                }

                @Override
                public void windowIconified(WindowEvent e)
                {

                }

                @Override
                public void windowDeiconified(WindowEvent e)
                {

                }

                @Override
                public void windowActivated(WindowEvent e)
                {

                }

                @Override
                public void windowDeactivated(WindowEvent e)
                {

                }
            });

            frame.pack();
            frame.setVisible(true);
        });

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(createButton, constraints);

        JLabel tags = new JLabel("Import from file:");

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(tags, constraints);

        JButton importButton = new JButton("Import Frame");

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(importButton, constraints);

        JLabel export = new JLabel("Export to file:");

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(export, constraints);

        JButton exportButton = new JButton("Export Frame");

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(exportButton, constraints);

        JLabel tagTitle = new JLabel("Load Tags from File");
        tagTitle.setFont(new Font(tagTitle.getFont().getName(), Font.BOLD, 14));

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(tagTitle, constraints);

        JButton loadTags = new JButton();
        loadTags.setIcon(Images.load("OpenFile.png"));

        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(loadTags, constraints);

        //
        // TODO for testing purposes
        //
        JButton showGraph = new JButton("Show Graph");
        showGraph.addActionListener(e -> showGraph());

        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(showGraph, constraints);

        add(inputPanel, BorderLayout.CENTER);
    }

    private void showGraph()
    {
        GraphEditorFactory.show(frameNode);
        Collection<Node> mainGraphNodes = frameNode.getGraph().getNodes();
        for (Node node : mainGraphNodes)
        {
            if (node.getClassName().equals(FrameNode.class.getName()))
            {
                FrameNode fNode = (FrameNode) node;
                FrameGraph frameGraphBuilder = new FrameGraph(fNode);
                frameGraphBuilder.buildGraph();
            }
        }
    }
}
