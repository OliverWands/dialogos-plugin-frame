package dialogos.frame.nodes;

import com.clt.diamant.graph.Node;
import com.clt.diamant.gui.GraphEditorFactory;
import com.clt.gui.Images;
import dialogos.frame.FrameNode;
import dialogos.frame.utils.FrameGraph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collection;
import java.util.Map;

public class FrameNodeMenu extends JPanel
{
    private File chosenFile;
    private final FrameNode frameNode;

    public FrameNodeMenu(Map<String, Object> properties, FrameNode frameNode, File global)
    {
        this.frameNode = frameNode;
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel horiz = new JPanel();
        horiz.setBackground(Color.YELLOW);

        // TODO do something useful
        JLabel field;
        if (global != null)
        {
            field = new JLabel(global.getName());
        }
        else
        {
            field = new JLabel("Slot:");
        }

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

                    FrameGraph frameGraphBuilder = new FrameGraph(fNode);
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
                frame.setAlwaysOnTop(false);
            }
        });

        fileChooser.showOpenDialog(frame);
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

    public JPanel createNewSlot()
    {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        //
        // The panel that contain the input components
        //
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel title = new JLabel("Create new Slot");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 14));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(title, constraints);

        JLabel name = new JLabel("Name:");

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(name, constraints);

        JTextField nameText = new JTextField();
        nameText.setColumns(20);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(nameText, constraints);

        JLabel tags = new JLabel("Tags:");

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(tags, constraints);

        JTextField tagsText = new JTextField();
        tagsText.setColumns(20);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(tagsText, constraints);

        JLabel isAdditional = new JLabel("Is additional?");

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(isAdditional, constraints);

        JTextField isAdditionalText = new JTextField();
        isAdditionalText.setColumns(20);
        JCheckBox additionalCheck = new JCheckBox();

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(additionalCheck, constraints);

        JLabel queryString = new JLabel("Enter query phrase:");

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(queryString, constraints);

        JTextField queryText = new JTextField();
        queryText.setColumns(20);

        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(queryText, constraints);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        //
        // Panel with Apply and Cancel button
        //
        JButton cancel = new JButton("Cancel");

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        buttonPanel.add(cancel, constraints);

        JButton apply = new JButton("Apply");

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        buttonPanel.add(apply, constraints);

        //
        // Assemble the final panel
        //
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        return topPanel;
    }
}

