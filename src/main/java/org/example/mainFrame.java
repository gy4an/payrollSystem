package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class mainFrame extends JFrame {
    JTextField idTextField, nameTextField, positionTextField, salaryTextField;
    JLabel idLabel, nameLabel, positionLabel, salaryLabel;
    JButton addButton;
    JTable table;
    DefaultTableModel tableModel;

    public mainFrame() {
        this.setTitle("Payroll System");
    }
}
