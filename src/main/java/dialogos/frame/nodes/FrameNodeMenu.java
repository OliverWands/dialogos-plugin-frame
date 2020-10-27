package dialogos.frame.nodes;

import com.clt.diamant.graph.Node;
import com.clt.diamant.gui.GraphEditorFactory;
import com.clt.gui.Images;
import dialogos.frame.FrameNode;
import dialogos.frame.utils.FrameGraph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.Map;

public class FrameNodeMenu extends JPanel
{
    private final Frame frame = new Frame();
    private File chosenFile;
    private final FrameNode frameNode;

    public FrameNodeMenu(Map<String, Object> properties, FrameNode frameNode, File global)
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
            frame.setLayout(new BorderLayout());

            frame.setAlwaysOnTop(true);
            frame.setLocationByPlatform(true);
            frame.add(createNewFrame(), BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
            frame.setFocusable(true);
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

        add(inputPanel, BorderLayout.CENTER);
    }

    private JPanel createNewFrame()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 5, 20, 5));

        JPanel topRowPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        topRowPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Create new Frame");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 18));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        topRowPanel.add(title, constraints);

        JLabel enter = new JLabel("Enter a FrameID:");

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        topRowPanel.add(enter, constraints);

        JTextField frameIDText = new JTextField();
        frameIDText.setColumns(30);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        topRowPanel.add(frameIDText, constraints);

        panel.add(topRowPanel, BorderLayout.NORTH);

        JPanel frameTablePanel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();

        frameTablePanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        String[] columnNames = {"SlotName", "Tags", "Nullable?", "Query String", ""};
        String[][] data = {{"-", "-", "-", "-", "EDIT"}};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        //JScrollPane scrollPane = new JScrollPane(JButtonTable.getTable());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        frameTablePanel.add(scrollPane, constraints);

        JButton newSlot = new JButton();
        newSlot.setText("Add Slot");
        newSlot.addActionListener(e ->
        {
            JFrame frame = new JFrame();

            frame.add(createNewSlot());
            frame.pack();
            frame.setVisible(true);
            frame.toFront();
        });

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        frameTablePanel.add(newSlot, constraints);

        JLabel helpLabel = new JLabel("Information when the user asks for help:");

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frameTablePanel.add(helpLabel, constraints);

        JTextArea textArea = new JTextArea();
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        textArea.setColumns(20);
        textArea.setRows(3);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frameTablePanel.add(textArea, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel selectHint = new JLabel("Please choose a custom tag file:");
        selectHint.setFont(new Font(selectHint.getFont().getName(), Font.BOLD, 14));
        selectHint.setBorder(new EmptyBorder(20, 0, 10, 0));
        frameTablePanel.add(selectHint, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel path = new JLabel("filePath");
        frameTablePanel.add(path, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JButton button = new JButton("OpenFile");
        frameTablePanel.add(button, constraints);

        panel.add(frameTablePanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createNewSlot()
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

