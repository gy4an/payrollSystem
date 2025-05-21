package org.example;

import javax.swing.*;
import java.awt.*;

public class PayslipFrame extends JFrame {
    public PayslipFrame() {
        setTitle("Payslip");
        setSize(400, 600);
        setLocationRelativeTo(null); // Center on screen

        JLabel placeholderLabel = new JLabel("Payslip will be displayed here.");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(placeholderLabel, BorderLayout.CENTER);
        contentPanel.add(closeButton, BorderLayout.SOUTH);

        this.add(contentPanel);
        this.setVisible(true);
    }
}
