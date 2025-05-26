package org.example;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Attendance extends JFrame {
    JTable attendanceTable;
    DefaultTableModel attendanceModel;
    JTextField idField, dateField, timeField;
    JLabel idLabel, dateLabel, timeLabel;
    JButton checkInOutButton;
    int selectedRow = -1;

    private Firestore db;

    public Attendance() {
        this.setTitle("Employee Attendance Log");
        this.setLayout(new BorderLayout());

        attendanceModel = new DefaultTableModel(new String[]{"Name", "Date", "Time Checked In", "Time Checked Out", "Date Checked Out"}, 0);
        attendanceTable = new JTable(attendanceModel);
        add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
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

        this.setSize(700, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        db = FirebaseUtil.getDb(); // Initialize Firestore
        loadAttendanceFromFirestore(); // Load on startup
    }

    private void addAttendanceToFirestore(String name, String date, String time, String employeeId) {
        CollectionReference attendance = db.collection("attendance");

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("date", date);
        data.put("timeIn", time);
        data.put("timeOut", "");
        data.put("dateOut", "");
        data.put("employeeId", employeeId);

        ApiFuture<DocumentReference> future = attendance.add(data);
        try {
            DocumentReference docRef = future.get();
            System.out.println("Check-in saved with ID: " + docRef.getId());
            loadAttendanceFromFirestore();
        } catch (Exception ex) {
            System.err.println("Error saving check-in: " + ex.getMessage());
        }
    }

    private void updateAttendanceInFirestore(String name, String date, String timeOut, String dateOut) {
        CollectionReference attendance = db.collection("attendance");

        ApiFuture<QuerySnapshot> future = attendance
                .whereEqualTo("name", name)
                .whereEqualTo("date", date)
                .get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                DocumentReference docRef = documents.get(0).getReference(); // Only update the first match
                Map<String, Object> updates = new HashMap<>();
                updates.put("timeOut", timeOut);
                updates.put("dateOut", dateOut);
                docRef.update(updates);
                System.out.println("Checkout updated in Firestore.");
                loadAttendanceFromFirestore();
            } else {
                System.out.println("No check-in record found for update.");
            }
        } catch (Exception e) {
            System.err.println("Error updating checkout: " + e.getMessage());
        }
    }

    private void loadAttendanceFromFirestore() {
        CollectionReference attendance = db.collection("attendance");

        ApiFuture<QuerySnapshot> future = attendance.get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            attendanceModel.setRowCount(0); // Clear table

            for (QueryDocumentSnapshot document : documents) {
                String name = document.getString("name");
                String date = document.getString("date");
                String timeIn = document.getString("timeIn");
                String timeOut = document.getString("timeOut");
                String dateOut = document.getString("dateOut");

                attendanceModel.addRow(new Object[]{name, date, timeIn, timeOut, dateOut});
            }
        } catch (Exception e) {
            System.err.println("Error loading attendance: " + e.getMessage());
        }
    }

    public void handleCheck() {
        String id = idField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String dateOut = date;

        if (!isValidMilitaryTime(time)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid military time (HH:mm).");
            return;
        }

        if (!isValidDateFormat(date)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date (mm/dd/yyyy).");
            return;
        }

        String name = getEmployeeNameFromFirestore(id);
        if (name == null) {
            JOptionPane.showMessageDialog(this, "Employee with ID " + id + " not found.");
            return;
        }

        if (checkInOutButton.getText().equals("Check In")) {
            attendanceModel.addRow(new Object[]{name, date, time, null, null});
            addAttendanceToFirestore(name, date, time, id);
            JOptionPane.showMessageDialog(this, name + " checked in at " + time);
        } else {
            if (selectedRow != -1 &&
                    attendanceModel.getValueAt(selectedRow, 0).equals(name) &&
                    attendanceModel.getValueAt(selectedRow, 1).equals(date)) {

                attendanceModel.setValueAt(time, selectedRow, 3);
                String[] inParts = attendanceModel.getValueAt(selectedRow, 2).toString().split(":");
                String[] outParts = time.split(":");
                int inHour = Integer.parseInt(inParts[0]);
                int outHour = Integer.parseInt(outParts[0]);

                if (outHour < inHour) {
                    dateOut = getNextDate(date);
                }

                attendanceModel.setValueAt(dateOut, selectedRow, 4);
                updateAttendanceInFirestore(name, date, time, dateOut);
                JOptionPane.showMessageDialog(this, name + " checked out at " + time);
                checkInOutButton.setText("Check In");
                selectedRow = -1;
                attendanceTable.clearSelection();
            } else {
                JOptionPane.showMessageDialog(this, "Please select the correct row for check out.");
            }
        }

        idField.setText("");
        dateField.setText("");
        timeField.setText("");
    }

    public String getEmployeeIdByName(String name) {
        for (int i = 0; i < attendanceModel.getRowCount(); i++) {
            String recordName = (String) attendanceModel.getValueAt(i, 0);
            if (recordName.equals(name)) {
                return getEmployeeIdFromFirestore(name);
            }
        }
        return "";
    }

    public List<AttendanceRecord> getAttendanceDataById(String id) {
        List<AttendanceRecord> records = new java.util.ArrayList<>();

        for (int i = 0; i < attendanceModel.getRowCount(); i++) {
            String recordId = getEmployeeIdByName((String) attendanceModel.getValueAt(i, 0));
            if (recordId.equals(id)) {
                String date = (String) attendanceModel.getValueAt(i, 1);
                String timeIn = (String) attendanceModel.getValueAt(i, 2);
                String timeOut = (String) attendanceModel.getValueAt(i, 3);
                String dateOut = (String) attendanceModel.getValueAt(i, 4);
                records.add(new AttendanceRecord(date, timeIn, timeOut, dateOut));
            }
        }
        return records;
    }

    public String getNextDate(String date) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            java.util.Date parsedDate = sdf.parse(date);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(parsedDate);
            calendar.add(java.util.Calendar.DATE, 1);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return date;
        }
    }

    public boolean isValidMilitaryTime(String time) {
        return time.matches("([01]\\d|2[0-3]):[0-5]\\d");
    }

    public boolean isValidDateFormat(String date) {
        return date.matches("(0[1-9]|1[0-2])/([0][1-9]|[12][0-9]|3[01])/\\d{4}");
    }

    private String getEmployeeNameFromFirestore(String employeeId) {
        try {
            DocumentSnapshot document = db.collection("employees").document(employeeId).get().get();
            if (document.exists()) {
                return document.getString("name");
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getEmployeeIdFromFirestore(String employeeName) {
        try {
            CollectionReference employees = db.collection("employees");
            QuerySnapshot querySnapshot = employees.whereEqualTo("name", employeeName).get().get();
            if (!querySnapshot.isEmpty()) {
                return querySnapshot.getDocuments().get(0).getId();
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
