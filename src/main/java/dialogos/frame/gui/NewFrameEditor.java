package dialogos.frame.gui;

import com.clt.gui.Images;
import dialogos.frame.FrameNode;
import dialogos.frame.struct.SlotStruct;
import dialogos.frame.utils.GrammarIO;

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
    private JTextField grammarPathTextField;
    private final JLabel grammarInfo = new JLabel(" ");

    private final DefaultTableModel model = new DefaultTableModel()
    {
        @Override
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public NewFrameEditor(FrameNode node)
    {
        super(new JFrame(), "New Frame");

        this.node = node;

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

        repopulateInputPanel();

        topPanel.add(container, BorderLayout.CENTER);

        setLocation(200, 200);
        pack();
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
                                                                             node.frameStruct.setGrammarsFromFile(selected);
                                                                             grammarPathTextField.setText(selected.getAbsolutePath());
                                                                             grammarInfo.setText(GrammarIO.fileToGrammarInfo(selected));
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

    private JPanel createFrameTable()
    {
        model.addColumn("SlotName");
        model.addColumn("Tags");
        model.addColumn("Query String");

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.NORTH);
        return tablePanel;
    }

    private JPanel createControlButtons()
    {
        JPanel controlPanel = new JPanel(new GridBagLayout());

        JButton newSlot = new JButton("New");
        newSlot.addActionListener(e -> createSlotEditor(node.frameStruct.size()));

        JButton editSlot = new JButton("Edit");
        editSlot.addActionListener(e -> createSlotEditor(table.getSelectedRow()));

        JButton deleteSlot = new JButton("Delete");
        deleteSlot.addActionListener(e ->
                                     {
                                         node.frameStruct.removeSlot(table.getSelectedRow());
                                         updateFrameTable();
                                     });

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

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 0, 5, 5);
        constraints.weightx = 0.1;
        constraints.weighty = 1.0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        controlPanel.add(editPanel, constraints);

        //
        // Move buttons
        //
        JButton up = new JButton("Up");
        up.addActionListener(e -> moveSlot(table.getSelectedRow(), true));

        JButton down = new JButton("Down");
        down.addActionListener(e -> moveSlot(table.getSelectedRow(), false));

        JPanel movePanel = new JPanel(new GridBagLayout());
        constraints.gridx++;
        movePanel.add(up, constraints);

        constraints.gridx++;
        movePanel.add(down, constraints);

        constraints.gridx = 2;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(5, 5, 5, 0);
        constraints.weightx = 1.0;
        controlPanel.add(movePanel, constraints);

        return controlPanel;
    }

    private void createSlotEditor(int selected)
    {
        processGrammarFile();

        if (selected != -1)
        {
            new NewSlotEditor(new JFrame(), node, "Edit Slot", selected)
            {
                @Override
                public void onCloseAction()
                {
                    updateFrameTable();
                }
            };
        }
    }

    private void processGrammarFile()
    {
        String path = grammarPathTextField.getText();
        File file = Paths.get(path).toFile();
        if (file.exists())
        {
            node.frameStruct.setGrammarsFromFile(file);
            grammarInfo.setText(GrammarIO.fileToGrammarInfo(file));
        }
    }

    /**
     *
     */
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

    public void repopulateInputPanel()
    {
        if (node.frameStruct.isEdited())
        {
            nameTextField.setText(node.frameStruct.getName());
            helpPromptField.setText(node.frameStruct.getHelpPrompt());
            if (node.frameStruct.getGrammarFile() != null)
            {
                grammarPathTextField.setText(node.frameStruct.getGrammarFile().getAbsolutePath());
            }
            updateFrameTable();
        }
    }

    private void moveSlot(int inx, boolean moveUp)
    {
        if (moveUp && inx > 0)
        {
            SlotStruct temp = node.frameStruct.getSlot(inx - 1);
            SlotStruct current = node.frameStruct.getSlot(inx);

            node.frameStruct.setSlot(inx - 1, current);
            node.frameStruct.setSlot(inx, temp);
            inx--;
        }
        else if (!moveUp && inx <= node.frameStruct.size() - 1)
        {
            SlotStruct temp = node.frameStruct.getSlot(inx + 1);
            SlotStruct current = node.frameStruct.getSlot(inx);

            node.frameStruct.setSlot(inx + 1, current);
            node.frameStruct.setSlot(inx, temp);
            inx++;
        }

        updateFrameTable();
        table.setRowSelectionInterval(inx, inx);
    }

    @Override
    public void applyAction()
    {
        node.frameStruct.setName(nameTextField.getText());
        node.frameStruct.setHelpPrompt(helpPromptField.getText());
        node.setUsedGrammars();
        processGrammarFile();
    }

    @Override
    public void cancelAction()
    {
    }

}