package dialogos.frame.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class AbstractMenuDialog extends JDialog
{
    AbstractMenuDialog(Window window, String title)
    {
        super(window, title, Dialog.ModalityType.APPLICATION_MODAL);

        this.addWindowListener(new WindowListener()
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
                onCloseAction();
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
    }

    public JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JButton applyButton = new JButton();
        applyButton.setText("Apply");
        applyButton.addActionListener(e -> applyAction());

        JButton cancelButton = new JButton();
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(e -> cancelAction());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;
        buttonPanel.add(cancelButton, constraints);

        constraints.gridx++;
        buttonPanel.add(applyButton, constraints);

        constraints.gridx++;
        constraints.weightx = 0.0;
        constraints.gridwidth = 1;
        return buttonPanel;
    }

    public abstract void applyAction();

    public abstract void cancelAction();

    public void onCloseAction()
    {
    }
}
