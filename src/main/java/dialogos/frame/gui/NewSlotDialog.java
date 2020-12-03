package dialogos.frame.gui;

import com.clt.diamant.Grammar;
import com.clt.diamant.gui.NodePropertiesDialog;
import dialogos.frame.SlotStruct;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewSlotDialog extends JDialog
{
    private SlotStruct slot;

    public NewSlotDialog(Map<String, Object> properties, List<Grammar> superGrammars, Window window, String title)
    {
        super(window, title, Dialog.ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        //
        // The panel that contains the input components
        //
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel dialogTitle = new JLabel("Create new Slot");
        dialogTitle.setFont(new Font(dialogTitle.getFont().getName(), Font.BOLD, 14));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(dialogTitle, constraints);

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

        List<String> grammarList = new ArrayList<>();
        grammarList.add("Tag1");
        grammarList.add("Tag2");
        grammarList.add("Tag3");
        grammarList.add("Tag4");
        grammarList.add("Tag5");

        final JComboBox grammars = NodePropertiesDialog.createComboBox(properties, "LANGUAGE", grammarList);

        JTextField tagsText = new JTextField();
        tagsText.setColumns(20);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(grammars, constraints);

        JLabel queryString = new JLabel("Enter query phrase:");

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(queryString, constraints);

        JTextField queryText = new JTextField();
        queryText.setColumns(20);

        constraints.gridx = 1;
        constraints.gridy = 3;
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
        cancel.addActionListener(e ->
        {
            dispose();
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        buttonPanel.add(cancel, constraints);

        JButton apply = new JButton("Apply");
        apply.addActionListener(e ->
        {
            String id = nameText.getText();
            String tag = grammarList.get(grammars.getSelectedIndex());
//            String tag = tagsText.getText();

            if (!id.isEmpty() && !id.matches(" +"))
            {
                slot = new SlotStruct();
                slot.setName(id);
                slot.setGrammarName(tag);
                slot.setQuery(queryText.getText());
            }

            dispose();
        });

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

        add(topPanel);

        pack();
        setLocation(250, 250);
        setVisible(true);
    }

    SlotStruct getSlot()
    {
        return slot;
    }
}
