package ToDoListWithUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Main extends JFrame {

    private final DefaultListModel<String> listModel;
    private final JList<String> taskList;
    private final JTextField taskField;
    private final JComboBox<String> priorityComboBox;

    public Main() {
        setTitle("To-Do List");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(taskList);

        taskField = new JTextField(40);

        priorityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});

        // Adding padding to the text field by wrapping it in a JPanel
        JPanel taskFieldPanel = new JPanel();
        taskFieldPanel.setLayout(new BorderLayout());
        taskFieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        taskFieldPanel.add(taskField, BorderLayout.CENTER);
        taskFieldPanel.add(priorityComboBox, BorderLayout.EAST);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new AddButtonListener());

        JButton removeButton = new JButton("Remove Task");
        removeButton.addActionListener(new RemoveButtonListener());

        JButton markDoneButton = new JButton("Mark as Done");
        markDoneButton.addActionListener(new MarkDoneButtonListener());

        JButton editButton = new JButton("Edit Task");
        editButton.addActionListener(new EditButtonListener());

        JButton sortButton = new JButton("Sort by Priority");
        sortButton.addActionListener(new SortButtonListener());

        JButton loadButton = new JButton("Load from File");
        loadButton.addActionListener(new LoadButtonListener());

        JButton saveButton = new JButton("Save to File");
        saveButton.addActionListener(new SaveButtonListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 4, 5, 5));  // 2 rows, 4 columns, 5px horizontal and vertical gaps
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(markDoneButton);
        buttonPanel.add(editButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(loadButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(taskFieldPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(saveButton, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String task = taskField.getText().trim();
            String priority = (String) priorityComboBox.getSelectedItem();
            if (!task.isEmpty() && priority != null) {
                listModel.addElement(priority + " - " + task);
                taskField.setText("");
            }
        }
    }

    private class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
            }
        }
    }

    private class MarkDoneButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String task = listModel.getElementAt(selectedIndex);
                if (!task.endsWith(" (Done)")) {
                    listModel.set(selectedIndex, task + " (Done)");
                }
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String currentTask = listModel.getElementAt(selectedIndex);
                String currentPriority = currentTask.split(" - ")[0];
                String taskOnly = currentTask.substring(currentPriority.length() + 3);
                String newTask = JOptionPane.showInputDialog(Main.this, "Edit Task", taskOnly);
                if (newTask != null && !newTask.trim().isEmpty()) {
                    String newPriority = (String) JOptionPane.showInputDialog(
                            Main.this, "Select Priority", "Edit Priority",
                            JOptionPane.QUESTION_MESSAGE, null,
                            new String[]{"Low", "Medium", "High"}, currentPriority
                    );
                    if (newPriority != null && !newPriority.trim().isEmpty()) {
                        listModel.set(selectedIndex, newPriority + " - " + newTask.trim());
                    }
                }
            }
        }
    }

    private class SortButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> tasks = Collections.list(listModel.elements());
            Collections.sort(tasks, new Comparator<String>() {
                @Override
                public int compare(String t1, String t2) {
                    String p1 = t1.split(" - ")[0];
                    String p2 = t2.split(" - ")[0];
                    return getPriorityValue(p1) - getPriorityValue(p2);
                }
            });
            listModel.clear();
            for (String task : tasks) {
                listModel.addElement(task);
            }
        }

        private int getPriorityValue(String priority) {
            switch (priority) {
                case "High": return 1;
                case "Medium": return 2;
                case "Low": return 3;
                default: return 4;
            }
        }
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(Main.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    listModel.clear();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        listModel.addElement(line);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(Main.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (int i = 0; i < listModel.size(); i++) {
                        writer.write(listModel.getElementAt(i));
                        writer.newLine();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
