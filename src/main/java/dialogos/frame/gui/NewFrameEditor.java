package dialogos.frame.gui;

import com.clt.gui.Images;
import dialogos.frame.FrameNode;
import dialogos.frame.utils.tags.TagIO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;

public class NewFrameEditor extends AbstractMenuDialog
{
    private final FrameNode node;

    private JTextField nameTextField;
    private JTextField helpPromptField;

    private final DefaultTableModel model = new DefaultTableModel()
    {
        @Override
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private JTextField grammarPathTextField;
    private final JLabel grammarInfo = new JLabel(" ");

    public NewFrameEditor(Window window, FrameNode node)
    {
        super(window, "New Frame");

        this.node = node;
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 5, 10, 5));

        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        container.add(createNameGrammarEdit(), constraints);

        constraints.gridy++;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        container.add(createFrameTable(), constraints);

        constraints.gridy++;
        container.add(createControlButtons(), constraints);

        constraints.gridy++;
        container.add(createButtonPanel(), constraints);

        populateInputPanel();

        panel.add(container, BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);
        pack();
        setLocation(200, 200);
        setVisible(true);
    }

    private JPanel createNameGrammarEdit()
    {
        JPanel nameEditor = new JPanel(new GridBagLayout());
        nameEditor.setBorder(new EmptyBorder(0, 0, 10, 0));

        GridBagConstraints constraints = new GridBagConstraints();

        JLabel nameLabel = new JLabel("Name:");

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        nameEditor.add(nameLabel, constraints);

        nameTextField = new JTextField();
        nameTextField.setColumns(30);

        constraints.gridx++;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        nameEditor.add(nameTextField, constraints);

        //
        // Second Row
        //

        JLabel helpLabel = new JLabel("Help prompt:");

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        nameEditor.add(helpLabel, constraints);

        helpPromptField = new JTextField();
        helpPromptField.setColumns(30);

        constraints.gridx++;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        nameEditor.add(helpPromptField, constraints);

        //
        // Third row
        //
        JLabel grammarLabel = new JLabel("Grammar file:");

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        nameEditor.add(grammarLabel, constraints);

        grammarPathTextField = new JTextField();
        grammarPathTextField.setColumns(30);
        grammarPathTextField.addActionListener(e -> processGrammarFile());

        constraints.gridx++;
        constraints.anchor = GridBagConstraints.LINE_END;
        nameEditor.add(grammarPathTextField, constraints);

        JButton openFile = new JButton();
        openFile.setIcon(Images.load("OpenFile.png"));
        openFile.addActionListener(e ->
        {
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            JFileChooser fileChooser = new JFileChooser();

            frame.add(fileChooser, BorderLayout.SOUTH);

            fileChooser.addChoosableFileFilter(
                    new FileNameExtensionFilter("JSON and XML files.", "json", "xml"));

            fileChooser.addActionListener(f ->
            {
                if (f.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
                {
                    File selected = fileChooser.getSelectedFile();
                    node.frameStruct.setTagsFromFile(selected);
                    grammarPathTextField.setText(selected.getAbsolutePath());
                    grammarInfo.setText(TagIO.fileToGrammarInfo(selected));
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
        });

        constraints.gridx++;
        nameEditor.add(openFile, constraints);

        constraints.gridy++;
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        nameEditor.add(grammarInfo, constraints);

        return nameEditor;
    }

    //
    // TODO Only editable when there are in the super graph or a grammarFile present
    //
    private JPanel createFrameTable()
    {
        model.addColumn("SlotName");
        model.addColumn("Tags");
        model.addColumn("Query String");

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.NORTH);
        return tablePanel;
    }

    //
    // TODO
    //  Delete slot
    //  Move up or down
    //
    private JPanel createControlButtons()
    {
        JPanel topPanel = new JPanel(new GridBagLayout());

        JButton newSlot = new JButton("New");
        newSlot.addActionListener(e -> createSlotEditor());

        JButton editSlot = new JButton("Edit");
        editSlot.addActionListener(e -> createSlotEditor(table.getSelectedRow()));

        JButton deleteSlot = new JButton("Delete");
        deleteSlot.addActionListener(e -> node.frameStruct.removeSlot(table.getSelectedRow()));

        JButton up = new JButton("Up");
        JButton down = new JButton("Down");

        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        editPanel.add(newSlot, constraints);
        constraints.gridx++;
        editPanel.add(editSlot, constraints);
        constraints.gridx++;
        editPanel.add(deleteSlot, constraints);

        JPanel movePanel = new JPanel(new GridBagLayout());
        constraints.gridx++;
        movePanel.add(up, constraints);
        constraints.gridx++;
        movePanel.add(down, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 0, 5, 5);
        constraints.weightx = 0.1;
        constraints.weighty = 1.0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        topPanel.add(editPanel, constraints);

        constraints.gridx = 2;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(5, 5, 5, 0);
        constraints.weightx = 1.0;
        topPanel.add(movePanel, constraints);

        return topPanel;
    }

    private void createSlotEditor()
    {
        createSlotEditor(node.frameStruct.size());
    }

    private void createSlotEditor(int selected)
    {
        JFrame frame = new JFrame();

        processGrammarFile();

        new NewSlotEditor(frame, node, "Edit Slot", selected)
        {
            @Override
            public void onCloseAction()
            {
                updateFrameTable();
            }
        };

        frame.pack();
        frame.setVisible(true);
    }

    private void processGrammarFile()
    {
        String path = grammarPathTextField.getText();
        File file = Paths.get(path).toFile();
        if (file.exists())
        {
            node.frameStruct.setTagsFromFile(file);
            grammarInfo.setText(TagIO.fileToGrammarInfo(file));
        }
    }

    private void updateFrameTable()
    {
        while (model.getRowCount() > 0)
        {
            model.removeRow(0);
        }

        for (int inx = 0; inx < node.frameStruct.size(); inx++)
        {
            model.insertRow(inx, node.frameStruct.getSlot(inx).getContent());
        }
    }

    private void populateInputPanel()
    {
        if (node.frameStruct.isEdited())
        {
            nameTextField.setText(node.frameStruct.getName());
            helpPromptField.setText(node.frameStruct.getHelpPrompt());
            grammarPathTextField.setText(node.frameStruct.getGrammarFile().getAbsolutePath());
            updateFrameTable();
        }
    }

    @Override
    public void applyAction()
    {
        node.frameStruct.setName(nameTextField.getText());
        node.frameStruct.setHelpPrompt(helpPromptField.getText());
        processGrammarFile();

        dispose();
    }

    @Override
    public void cancelAction()
    {
        dispose();
    }

}