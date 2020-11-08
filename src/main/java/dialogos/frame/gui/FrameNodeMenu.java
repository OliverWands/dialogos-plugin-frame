package dialogos.frame.gui;

import dialogos.frame.FrameNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FrameNodeMenu extends JPanel
{
    FrameNode frameNode;

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
            new NewFrameDialog(frameNode.frameStruct, frame, "Create Frame");
//            frameDialog.addWindowListener(new WindowListener()
//            {
//                @Override
//                public void windowOpened(WindowEvent e)
//                {
//
//                }
//
//                @Override
//                public void windowClosing(WindowEvent e)
//                {
//
//                }
//
//                @Override
//                public void windowClosed(WindowEvent e)
//                {
//                    frameNode.frameStruct = frameDialog.getFrameStruct();
//                }
//
//                @Override
//                public void windowIconified(WindowEvent e)
//                {
//
//                }
//
//                @Override
//                public void windowDeiconified(WindowEvent e)
//                {
//
//                }
//
//                @Override
//                public void windowActivated(WindowEvent e)
//                {
//
//                }
//
//                @Override
//                public void windowDeactivated(WindowEvent e)
//                {
//
//                }
//            });
            frame.pack();
            frame.setVisible(true);
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
        exportButton.addActionListener(e ->
        {
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            JFileChooser fileChooser = new JFileChooser();

            frame.add(fileChooser, BorderLayout.SOUTH);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                File file = fileChooser.getSelectedFile();
                try (Writer writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)))
                {
                    writer.write(frameNode.frameStruct.marshal().toString(4));
                } catch (IOException exp)
                {
                    exp.printStackTrace();
                }
            }
        });

        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(exportButton, constraints);

        add(inputPanel, BorderLayout.CENTER);
    }
}
