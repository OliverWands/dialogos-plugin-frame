package dialogos.frame.nodes;

import com.clt.gui.Images;
import dialogos.frame.FrameStruct;
import dialogos.frame.SlotStruct;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class NewFrameDialog extends JDialog
{
    private final JTable table;
    private final DefaultTableModel model;
    private FrameStruct frameStruct = null;

    public NewFrameDialog(Window window, String title)
    {
        super(window, title, Dialog.ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 5, 20, 5));

        JPanel topRowPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        topRowPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Create new Frame");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        topRowPanel.add(titleLabel, constraints);

        JLabel enter = new JLabel("Enter a FrameID:");

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        topRowPanel.add(enter, constraints);

        JTextField frameIDText = new JTextField();
        frameIDText.setColumns(30);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        topRowPanel.add(frameIDText, constraints);

        panel.add(topRowPanel, BorderLayout.NORTH);

        JPanel frameTablePanel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();

        frameTablePanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        model = new DefaultTableModel();
        table = new JTable(model);

        for (String columnName : new String[]{"SlotName", "Tags", "Nullable?", "Query String"})
        {
            model.addColumn(columnName);
        }

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        frameTablePanel.add(new JScrollPane(table), constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;
        frameTablePanel.add(getSlotButtons(), constraints);

        JLabel helpLabel = new JLabel("Information when the user asks for help:");

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frameTablePanel.add(helpLabel, constraints);

        JTextArea textArea = new JTextArea();
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        textArea.setColumns(20);
        textArea.setRows(3);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frameTablePanel.add(textArea, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel selectLabel = new JLabel("Please choose a custom tag file:");
        selectLabel.setFont(new Font(selectLabel.getFont().getName(), Font.BOLD, 14));
        selectLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        frameTablePanel.add(selectLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel path = new JLabel("filePath");
        frameTablePanel.add(path, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JButton button = new JButton();
        button.setIcon(Images.load("OpenFile.png"));
        button.addActionListener(e ->
        {
            getFileChooser();
        });

        frameTablePanel.add(button, constraints);

        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frameTablePanel.add(getButtons(), constraints);

        panel.add(frameTablePanel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        pack();
        setLocation(200, 200);
        setVisible(true);
    }

    public JPanel getSlotButtons()
    {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JButton newSlot = new JButton();
        newSlot.setText("Add Slot");
        newSlot.addActionListener(e ->
        {
            JFrame frame = new JFrame();
            NewSlotDialog slotDialog = new NewSlotDialog(frame, "Create Slot");
            slotDialog.addWindowListener(new WindowListener()
            {
                @Override
                public void windowOpened(WindowEvent e)
                {

                }

                @Override
                public void windowClosing(WindowEvent e)
                {

                }

                @Override
                public void windowClosed(WindowEvent e)
                {
                    SlotStruct slotStruct = slotDialog.getSlot();
                    if (slotStruct != null)
                    {
                        frameStruct.addSlot(slotStruct);
                        updateFrameTable();
                    }
                }

                @Override
                public void windowIconified(WindowEvent e)
                {

                }

                @Override
                public void windowDeiconified(WindowEvent e)
                {

                }

                @Override
                public void windowActivated(WindowEvent e)
                {

                }

                @Override
                public void windowDeactivated(WindowEvent e)
                {

                }
            });

            frame.pack();
            frame.setVisible(true);
        });

        JButton deleteSlot = new JButton();
        deleteSlot.setText("Delete Slot");
        deleteSlot.addActionListener(e ->
        {
            frameStruct.removeSlot(table.getSelectedRow());
            updateFrameTable();
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        buttonPanel.add(deleteSlot, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        buttonPanel.add(newSlot, constraints);
        return buttonPanel;
    }

    public JPanel getButtons()
    {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JButton newSlot = new JButton();
        newSlot.setText("Apply");
        newSlot.addActionListener(e ->
        {
            dispose();
        });

        JButton deleteSlot = new JButton();
        deleteSlot.setText("Cancel");
        deleteSlot.addActionListener(e ->
        {
            frameStruct = null;
            dispose();
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        buttonPanel.add(deleteSlot, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        buttonPanel.add(newSlot, constraints);
        return buttonPanel;
    }

    FrameStruct getFrameStruct()
    {
        return frameStruct;
    }

    //TODO
    // Tag files (global and chosen) into the FrameStruct
    private void getFileChooser()
    {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        JFileChooser fileChooser = new JFileChooser();

        frame.add(fileChooser, BorderLayout.SOUTH);

        fileChooser.addChoosableFileFilter(
                new FileNameExtensionFilter("JSON and XML files.", "json", "xml"));

        fileChooser.addActionListener(e ->
        {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
            {
                //filePath = fileChooser.getSelectedFile().getPath();
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
    }

    private void updateFrameTable()
    {
        for (int inx = 0; inx < frameStruct.size(); inx++)
        {
            model.insertRow(inx, frameStruct.getSlot(inx).getContent());
        }
    }
}

