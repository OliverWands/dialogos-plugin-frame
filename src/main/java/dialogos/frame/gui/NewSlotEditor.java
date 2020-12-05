package dialogos.frame.gui;

import com.clt.diamant.Grammar;
import dialogos.frame.FrameNode;
import dialogos.frame.SlotStruct;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewSlotEditor extends AbstractMenuDialog
{
    private final int index;
    private final FrameNode node;

    private JTextField nameText;
    private JComboBox<String> grammarCombo;
    private JTextField queryText;

    public NewSlotEditor(Window window, FrameNode node, String title, int index)
    {
        super(window, title);

        this.node = node;
        this.index = index;

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        //
        // Assemble the final panel
        //
        topPanel.add(createInputPanel(), BorderLayout.NORTH);
        topPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        populateInputPanel();

        add(topPanel);

        pack();
        setLocation(250, 250);
        setVisible(true);
    }

    private JPanel createInputPanel()
    {
        //
        // The panel that contains the input components
        //
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel name = new JLabel("Name:");

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(name, constraints);

        nameText = new JTextField();
        nameText.setColumns(20);

        constraints.gridx = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(nameText, constraints);

        JLabel tags = new JLabel("Tags:");

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(tags, constraints);

        grammarCombo = new JComboBox<>(getAllGrammars().toArray(new String[]{}));
//        grammarCombo = NodePropertiesDialog.createComboBox(properties, "LANGUAGE", getAllGrammars());

        constraints.gridx = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(grammarCombo, constraints);

        JLabel queryString = new JLabel("Enter query phrase:");

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(queryString, constraints);

        queryText = new JTextField();
        queryText.setColumns(20);

        constraints.gridx = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(queryText, constraints);

        return inputPanel;
    }

    private void populateInputPanel()
    {
        if (index != node.frameStruct.size())
        {
            SlotStruct slotStruct = node.frameStruct.getSlot(index);
            nameText.setText(slotStruct.getName());
            String grammar = slotStruct.getGrammarName();
            for (int inx = 0; inx < grammarCombo.getItemCount(); inx++)
            {
                if (grammarCombo.getItemAt(inx) != null && grammar.equals(grammarCombo.getItemAt(inx)))
                {
                    grammarCombo.setSelectedIndex(inx);
                    break;
                }
            }

            queryText.setText(slotStruct.getQuery());
        }
    }

    @Override
    public void applyAction()
    {
        if (index == node.frameStruct.size())
        {
            node.frameStruct.addSlot(new SlotStruct());
        }

        node.frameStruct.getSlot(index)
                .setName(nameText.getText())
                .setGrammarName(grammarCombo.getItemAt(grammarCombo.getSelectedIndex()))
                .setQuery(queryText.getText());

        dispose();
    }

    @Override
    public void cancelAction()
    {
        dispose();
    }

    private List<String> getAllGrammars()
    {
        List<String> ownedGrammars = new ArrayList<>(node.frameStruct.getGrammars().keySet());
        List<Grammar> superGrammars = node.getSuperGraph().getGrammars();

        for (Grammar grammar : superGrammars)
        {
            ownedGrammars.add(grammar.getName());
        }

        ownedGrammars.sort(String::compareToIgnoreCase);

        return ownedGrammars;
    }
}
