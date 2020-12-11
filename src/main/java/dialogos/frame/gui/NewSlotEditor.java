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
    private final FrameNode frameNode;

    private JTextField nameText;
    private JComboBox<String> grammarCombo;
    private JTextField queryText;

    /**
     * @param frameNode
     * @param title
     * @param index
     */
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

    /**
     * @return
     */
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

    /**
     *
     */
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

        System.out.printf("%s  %s %s \n", nameText.getText(), grammarCombo.getItemAt(grammarCombo.getSelectedIndex()), queryText.getText());

        frameNode.frameStruct.getSlot(index)
                .setName(nameText.getText())
                .setGrammarName(grammarCombo.getItemAt(grammarCombo.getSelectedIndex()))
                .setQuery(queryText.getText());
    }

    @Override
    public void cancelAction()
    {
    }

    /**
     * @return
     */
    private List<String> getAllGrammars()
    {
        List<String> ownedGrammars = new ArrayList<>(frameNode.frameStruct.getGrammars().keySet());
        List<Grammar> superGrammars = frameNode.getSuperGraph().getGrammars();

        for (Grammar grammar : superGrammars)
        {
            ownedGrammars.add(grammar.getName());
        }

        ownedGrammars.sort(String::compareToIgnoreCase);

        return ownedGrammars;
    }
}