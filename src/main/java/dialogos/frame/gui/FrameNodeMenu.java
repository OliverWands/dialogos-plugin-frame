package dialogos.frame.gui;

import dialogos.frame.FrameNode;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FrameNodeMenu extends JPanel
{
    private final FrameNode frameNode;
    private final JButton exportButton;
    private final JLabel createLabel;
    private final JButton createButton;

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

        createLabel = new JLabel("Create:");

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(createLabel, constraints);

        createButton = new JButton("Create Frame");
        createButton.addActionListener(e ->
        {
            new NewFrameEditor(new JFrame(),frameNode)
            {
                @Override
                public void onCloseAction()
                {
                    updateGUI();
                }
            };
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
        importButton.addActionListener(e ->
        {
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON and XML files.", "json", "xml"));

            frame.add(fileChooser, BorderLayout.SOUTH);

            fileChooser.setEnabled(true);
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    String content = new String(Files.readAllBytes(fileChooser.getSelectedFile().toPath()),
                            Charset.defaultCharset().name());
                    frameNode.frameStruct.unmarshalStruct(new JSONObject(content));
                    updateGUI();
                } catch (IOException exp)
                {
                    exp.printStackTrace();
                }
            }
        });

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

        exportButton = new JButton("Export Frame");
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
                try (Writer writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)))
                {
                    writer.write(frameNode.frameStruct.marshalStruct().toString(4));
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
    }
}
