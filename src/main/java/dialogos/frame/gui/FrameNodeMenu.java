package dialogos.frame.gui;

import com.clt.gui.FileChooser;
import com.clt.io.FileExtensionFilter;
import com.clt.xml.XMLWriter;
import dialogos.frame.FrameNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class FrameNodeMenu extends JPanel
{
    private final FrameNode frameNode;
    private final JButton exportButton = new JButton("Export Frame");
    private final JLabel createLabel = new JLabel();
    private final JButton createButton = new JButton();

    public FrameNodeMenu(FrameNode frameNode)
    {
        super(new BorderLayout());

        this.frameNode = frameNode;

        setBorder(new EmptyBorder(0, 5, 20, 5));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel title = new JLabel("Enter or create a Frame");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 14));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(title, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(createLabel, constraints);

        createButton.addActionListener(e -> new NewFrameEditor(frameNode)
        {
            @Override
            public void onCloseAction()
            {
                updateGUI();
            }
        });

        constraints.gridx = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(createButton, constraints);

        JLabel tags = new JLabel("Import from file:");

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(tags, constraints);

        AbstractAction importFrameAction = new AbstractAction("Import Frame")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                FileChooser fc = new FileChooser(new FileExtensionFilter("xml", "XML exported frame"));
                File file = fc.standardGetFile(inputPanel);
                if (file != null)
                {
                    frameNode.frameStruct.readFromXML(file);
                    updateGUI();
                }
            }
        };

        JButton importButton = new JButton(importFrameAction);

        constraints.gridx = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(importButton, constraints);

        JLabel export = new JLabel("Export to file:");

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(export, constraints);

        exportButton.setEnabled(false);
        exportButton.addActionListener(e ->
                                       {
                                           JFrame frame = new JFrame();
                                           frame.setLayout(new BorderLayout());
                                           JFileChooser fileChooser = new JFileChooser();

                                           frame.add(fileChooser, BorderLayout.SOUTH);
                                           if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                                           {
                                               File file = fileChooser.getSelectedFile();

                                               try
                                               {
                                                   XMLWriter writer = new XMLWriter(file);
                                                   frameNode.frameStruct.writeToXML(writer);
                                                   writer.close();
                                               } catch (IOException exp)
                                               {
                                                   exp.printStackTrace();
                                               }
                                           }
                                       });

        constraints.gridx = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(exportButton, constraints);

        updateGUI();

        add(inputPanel, BorderLayout.CENTER);
    }

    private void updateGUI()
    {
        if (frameNode.frameStruct.isEdited())
        {
            createLabel.setText("Edit:");
            createButton.setText("Edit Frame");
            exportButton.setEnabled(true);
        }
        else
        {
            createLabel.setText("Create:");
            createButton.setText("Create Frame");
            exportButton.setEnabled(false);
        }
    }
}
