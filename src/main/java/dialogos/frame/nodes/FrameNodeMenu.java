package dialogos.frame.nodes;

import com.clt.diamant.graph.Node;
import com.clt.diamant.gui.GraphEditorFactory;
import com.clt.gui.Images;
import dialogos.frame.utils.FrameGraphBuilder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collection;
import java.util.Map;

public class FrameNodeMenu extends JPanel
{
    private File chosenFile;
    private FrameNode frameNode;

    FrameNodeMenu(Map<String, Object> properties, FrameNode frameNode)
    {
        this.frameNode = frameNode;
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel horiz = new JPanel();
        horiz.setBackground(Color.YELLOW);
        JLabel field = new JLabel("Enter Slot");
        field.setEnabled(false);
        horiz.add(field);

        //
        // set x- and y-position of JPanel in GridBagLayout
        //
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(horiz, constraints);

        horiz = new JPanel();
        horiz.setBackground(Color.RED);
        String[] columnNames =
                {"SlotName", "Tags", "Nullable?", "Custom tags"};
        String[][] data = {
                {"Start", "City", "false", "Hamburg, Berlin, Rome"},
                {"Start", "City", "false", "Hamburg, Berlin, Rome"}};

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        horiz.add(scrollPane);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(horiz, constraints);

        horiz = new JPanel();
        horiz.setBackground(Color.CYAN);
        JTextField slotField = new JTextField("");
        slotField.setColumns(30);
        horiz.add(slotField);

        JButton button = new JButton();
        button.setIcon(Images.load("OpenFile.png"));
        button.addActionListener(e -> getFileChooser());

        horiz.add(button);

        constraints.gridx = 0;
        constraints.gridy = 2;
        add(horiz, constraints);

        horiz = new JPanel();
        horiz.setBackground(Color.GREEN);

        JButton graphButton = new JButton("Show Graph");
        graphButton.addActionListener(actionEvent ->
        {
//            JFrame frame = new JFrame();
//            frame.setLayout(new BorderLayout());
//            frame.add(createNewRow(columnNames), BorderLayout.SOUTH);
//            frame.pack();
//            frame.setVisible(true);
//            System.out.println("Button press!");
            GraphEditorFactory.show(frameNode);
            Collection<Node> mainGraphNodes = frameNode.getGraph().getNodes();
            for (Node node : mainGraphNodes)
            {
                if (node.getClassName().equals(FrameNode.class.getName()))
                {
                    FrameNode fNode = (FrameNode) node;

                    FrameGraphBuilder frameGraphBuilder = new FrameGraphBuilder(fNode);
                    frameGraphBuilder.buildGraph();
                }
            }
        });
        horiz.add(graphButton);
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(horiz, constraints);
    }

    public void getFileChooser()
    {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        JFileChooser fileChooser = new JFileChooser();

        frame.add(fileChooser, BorderLayout.SOUTH);

        fileChooser.addChoosableFileFilter(
                new FileNameExtensionFilter("JSON and XML files.", "json", "xml"));

        fileChooser.addActionListener(e ->
        {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
            {
                chosenFile = fileChooser.getSelectedFile();
            }
            if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
            {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                frame.setVisible(false);
            }

        });

        frame.pack();
        frame.setVisible(true);
        frame.toFront();
    }

    public JPanel createNewRow(String[] columnNames)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        for (int inx = 0; inx < columnNames.length; inx++)
        {
            JLabel slotLabel = new JLabel(columnNames[inx]);
            constraints.gridx = 0;
            constraints.gridy = inx;
            panel.add(slotLabel, constraints);

            JTextField slotField = new JTextField("");
            slotField.setColumns(15);
            constraints.gridx = 2;
            constraints.gridy = inx;
            panel.add(slotField, constraints);
        }

        JButton graphButton = new JButton("Apply");
        graphButton.addActionListener(actionEvent ->
        {
            System.out.println("Button press!");
        });

        constraints.gridx = 1;
        constraints.gridy = columnNames.length;
        panel.add(graphButton, constraints);

        return panel;
    }
}

