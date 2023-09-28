/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.folderlocker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Folderlocker extends JFrame {
    private JTextField folderPathField;
    private JPasswordField passwordField;
    private JButton lockButton;
    private JButton unlockButton;

    public Folderlocker() {
        setTitle("Folder Locker");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel folderLabel = new JLabel("Folder Path:");
        folderPathField = new JTextField();
        panel.add(folderLabel);
        panel.add(folderPathField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        lockButton = new JButton("Lock");
        unlockButton = new JButton("Unlock");

        lockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lockFolder();
            }
        });

        unlockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                unlockFolder();
            }
        });

        panel.add(lockButton);
        panel.add(unlockButton);

        add(panel);
    }

    private void lockFolder() {
        String folderPath = folderPathField.getText();
        String password = new String(passwordField.getPassword());

        if (folderPath.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter folder path and password.");
            return;
        }

        try {
            // Create a file (e.g., a hidden file) within the folder to indicate it's locked
            File lockFile = new File(folderPath, ".lock");
            lockFile.createNewFile();

            // Encrypt the password and store it in a file
            try (FileWriter writer = new FileWriter(new File(folderPath, ".password"))) {
                writer.write(password);
            }

            JOptionPane.showMessageDialog(this, "Folder locked successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while locking the folder: " + ex.getMessage());
        }
    }

    private void unlockFolder() {
        String folderPath = folderPathField.getText();
        String password = new String(passwordField.getPassword());

        if (folderPath.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter folder path and password.");
            return;
        }

        try {
            // Check if the folder is locked
            File lockFile = new File(folderPath, ".lock");
            if (lockFile.exists()) {
                // Read the stored password
                try (BufferedReader reader = new BufferedReader(new FileReader(new File(folderPath, ".password")))) {
                    String storedPassword = reader.readLine();

                    if (password.equals(storedPassword)) {
                        // Delete the lock file to unlock the folder
                        lockFile.delete();
                        JOptionPane.showMessageDialog(this, "Folder unlocked successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Folder is not locked.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Folderlocker().setVisible(true);
            }
        });
    }
}
