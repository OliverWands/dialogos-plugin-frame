package dialogos.frame.gui;

import com.clt.diamant.Grammar;
import dialogos.frame.FrameNode;
import dialogos.frame.SlotStruct;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class NewSlotEditor extends AbstractMenuDialog
{
    private final int index;
    private final FrameNode frameNode;

    private JTextField nameText;
    private JComboBox<String> grammarCombo;
    private JTextField queryText;

    public NewSlotEditor(Window window, FrameNode frameNode, String title, int index)
    {
        super(window, title);

        this.frameNode = frameNode;
        this.index = index;

        topPanel.add(createInputPanel(), BorderLayout.NORTH);
        topPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        repopulateInputPanel();

        setLocation(250, 250);
        pack();
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

        List<Grammar> all = frameNode.getAllGrammars();
        all.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        String[] grammarNames = new String[all.size()];
        for (int inx = 0; inx < all.size(); inx++)
        {
            grammarNames[inx] = all.get(inx).getName();
        }

        grammarCombo = new JComboBox<>(grammarNames);

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

    public void repopulateInputPanel()
    {
        if (index != frameNode.frameStruct.size())
        {
            SlotStruct slotStruct = frameNode.frameStruct.getSlot(index);
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
        if (index == frameNode.frameStruct.size())
        {
            frameNode.frameStruct.addSlot(new SlotStruct());
        }

        frameNode.frameStruct.getSlot(index)
                .setName(nameText.getText())
                .setGrammarName(grammarCombo.getItemAt(grammarCombo.getSelectedIndex()))
                .setQuery(queryText.getText());
    }

    @Override
    public void cancelAction()
    {
    }
}