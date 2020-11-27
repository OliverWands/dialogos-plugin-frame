package dialogos.frame.gui;

import dialogos.frame.SlotStruct;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NewSlotDialog extends JDialog
{
    private SlotStruct slot;

    public NewSlotDialog(Window window, String title)
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
            String tag = tagsText.getText();

            if (!id.isEmpty() && !id.matches(" +"))
            {
                slot = new SlotStruct();
                slot.setName(id);
                slot.setGrammarName(tag);
                slot.setIsAdditional(additionalCheck.isSelected());
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
