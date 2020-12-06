package dialogos.frame.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class AbstractMenuDialog extends JDialog
{
    public JPanel topPanel;

    AbstractMenuDialog(Window window, String title)
    {
        super(window, title, Dialog.ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 5, 10, 5));

        addWindowListener(new WindowListener()
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
                System.out.println("#####");
            }
        });

        add(topPanel);

        window.pack();
        window.setVisible(true);
    }

    /**
     * Creates a panel that contains an apply and cancel button.
     *
     * @return The panel containing the buttons.
     */
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

    /**
     * This action will be executed, when the "Apply" button is pressed.
     */
    public abstract void applyAction();

    /**
     * This action will be executed, when the "Cancel" button is pressed.
     */
    public abstract void cancelAction();

    /**
     * This action will be executed, when the dialog is closed.
     */
    public void onCloseAction()
    {
    }
}
