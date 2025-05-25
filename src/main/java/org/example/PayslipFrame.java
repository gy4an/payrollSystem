package org.example;

import javax.swing.*;
import java.awt.*;

public class PayslipFrame extends JFrame {
    public PayslipFrame(String payslipContent) {
        setTitle("Payslip");
        setSize(400, 600);
        setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea(payslipContent);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(closeButton, BorderLayout.SOUTH);

        this.add(contentPanel);
        this.setVisible(true);
    }

}
