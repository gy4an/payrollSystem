package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Attendance extends JFrame {
    JTable attendanceTable;
    DefaultTableModel attendanceModel;
    JTextField idField, dateField, timeField;
    JLabel idLabel, dateLabel, timeLabel;
    JButton checkInOutButton;
    Map<String, String> employeeMap = new HashMap<>();
    int selectedRow = -1;
    public Attendance() {
        this.setTitle("Employee Attendance Log");
        this.setLayout(new BorderLayout());

        attendanceModel = new DefaultTableModel(new String[]{"Name", "Date", "Time Checked In", "Time Checked Out"},0);
        attendanceTable = new JTable(attendanceModel);
        add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(2,4,5,5));
        idField = new JTextField();
        dateField = new JTextField();
        timeField = new JTextField();
        idLabel = new JLabel("ID:");
        dateLabel = new JLabel("Date (mm/dd/yyyy):");
        timeLabel = new JLabel("Time:");
        checkInOutButton = new JButton("Check In");

        inputPanel.setBorder(BorderFactory.createTitledBorder("Employee Attendance"));

        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        inputPanel.add(timeLabel);
        inputPanel.add(timeField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(checkInOutButton);

        add(inputPanel, BorderLayout.SOUTH);

        checkInOutButton.addActionListener(e -> handleCheck());

        attendanceTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && attendanceTable.getSelectedRow() != -1) {
                int row = attendanceTable.getSelectedRow();
                Object checkoutTime = attendanceModel.getValueAt(row, 3);

                if (checkoutTime == null || checkoutTime.toString().isEmpty()) {
                    String selectedName = attendanceModel.getValueAt(row, 0).toString();
                    String selectedDate = attendanceModel.getValueAt(row, 1).toString();

                    String selectedId = getEmployeeIdByName(selectedName);
                    idField.setText(selectedId);
                    dateField.setText(selectedDate);
                    checkInOutButton.setText("Check Out");
                    selectedRow = row;
                }
            }
        });

        attendanceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = attendanceTable.getSelectedRow();
                if (row != -1) {
                    Object checkOutValue = attendanceModel.getValueAt(row, 3);
                    if (checkOutValue == null || checkOutValue.toString().isEmpty()) {
                        selectedRow = row;
                        checkInOutButton.setText("Check Out");
                    } else {
                        checkInOutButton.setText("Check In");
                        selectedRow = -1;
                    }
                }
            }
        });

        this.setSize(700,400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void addEmployee(String id, String name) {
        employeeMap.put(id,name);
    }
    public void handleCheck() {
        String id = idField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();

        if (!employeeMap.containsKey(id)) {
            JOptionPane.showMessageDialog(this, "ID not found in employee list");
            return;
        }

        String name = employeeMap.get(id);

        if (checkInOutButton.getText().equals("Check In")) {
            if (timeField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a time.");
            } else {
                attendanceModel.addRow(new Object[]{name,date,time,null});
                JOptionPane.showMessageDialog(this, name + " checked in at " + time);
            }
            idField.setText("");
            dateField.setText("");
            timeField.setText("");
        } else {

            if (selectedRow != -1 && attendanceModel.getValueAt(selectedRow, 0).equals(name) && attendanceModel
                    .getValueAt(selectedRow, 1).equals(date)) {
                if (timeField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a time.");
                } else {
                    attendanceModel.setValueAt(time, selectedRow, 3);
                    JOptionPane.showMessageDialog(this, name + " checked out at " + time);
                }
                checkInOutButton.setText("Check In");
                selectedRow = -1;
                attendanceTable.clearSelection();
                idField.setText("");
                dateField.setText("");
                timeField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please select the correct row for check out.");
            }
        }

        timeField.setText("");
    }
    public String getEmployeeIdByName(String name) {
        for (Map.Entry<String, String> entry : employeeMap.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        return "";
    }

    public java.util.List<AttendanceRecord> getAttendanceDataById(String id) {
        String name = employeeMap.get(id);
        java.util.List<AttendanceRecord> records = new java.util.ArrayList<>();

        for (int i = 0; i < attendanceModel.getRowCount(); i++) {
            String recordName = (String) attendanceModel.getValueAt(i, 0);
            if (recordName.equals(name)) {
                String date = (String) attendanceModel.getValueAt(i, 1);
                String timeIn = (String) attendanceModel.getValueAt(i, 2);
                String timeOut = (String) attendanceModel.getValueAt(i, 3);
                records.add(new AttendanceRecord(date, timeIn, timeOut));
            }
        }
        return records;
    }


}

