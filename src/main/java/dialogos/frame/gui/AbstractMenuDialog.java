package dialogos.frame.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class AbstractMenuDialog extends JDialog
{
    public JPanel topPanel;
    private final Window window;

    AbstractMenuDialog(Window window, String title)
    {
        super(window, title, Dialog.ModalityType.APPLICATION_MODAL);

        this.window = window;

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
            }
        });

        add(topPanel);
    }

    /**
     * Creates a panel that contains an apply and cancel button.
     *
     * @return The panel containing the buttons.
     */
    public JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e ->
        {
            applyAction();
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e ->
        {
            cancelAction();
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        });

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;
        buttonPanel.add(cancelButton, constraints);

        constraints.gridx++;
        buttonPanel.add(applyButton, constraints);

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
