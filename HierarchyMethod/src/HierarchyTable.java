import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class HierarchyTable extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;

    public HierarchyTable(int rows, int cols){
        setLayout(new BorderLayout());

        String[] columnNames = new String[cols];
        for (int i = 0; i < cols; i++) {
            columnNames[i] = "Col " + (i + 1);
        }

        model = new DefaultTableModel(rows, cols);
        model.setColumnIdentifiers(columnNames);

        table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 20));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(i).setPreferredWidth(40);
        }

        scrollPane = new JScrollPane(table);

        JList<String> rowHeader = new JList<>();
        scrollPane.setRowHeaderView(rowHeader);
        rowHeader.setFixedCellWidth(50);
        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        updateRowHeader(rowHeader, table.getRowCount());

        scrollPane.setRowHeaderView(rowHeader);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton addRowBtn = new JButton("Добавить строку");
        JButton delRowBtn = new JButton("Удалить строку");

        JButton addColBtn = new JButton("Добавить столбец");
        JButton delColBtn = new JButton("Удалить столбец");

        buttons.add(addRowBtn);
        buttons.add(delRowBtn);
        buttons.add(addColBtn);
        buttons.add(delColBtn);

        addRowBtn.addActionListener(e -> {
            model.addRow(new Object[model.getColumnCount()]);
            updateRowHeader(rowHeader, table.getRowCount());
        });

        delRowBtn.addActionListener(e -> {
            if (model.getRowCount() > 0) {
                model.removeRow(model.getRowCount() - 1);
            }
            updateRowHeader(rowHeader, table.getRowCount());
        });

        addColBtn.addActionListener(e -> {
            int newColIndex = model.getColumnCount() + 1;
            model.addColumn("Col " + newColIndex);

            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                table.getColumnModel().getColumn(i).setPreferredWidth(40);
            }
        });

        delColBtn.addActionListener(e -> {
            int colCount = model.getColumnCount();
            if (colCount > 0) {
                model.setColumnCount(colCount - 1);
            }
        });


        add(buttons, BorderLayout.SOUTH);
    }

    public List<List<Object>> getTableData() {
        List<List<Object>> data = new ArrayList<>();

        for (int r = 0; r < model.getRowCount(); r++) {
            List<Object> row = new ArrayList<>();

            for (int c = 0; c < model.getColumnCount(); c++) {
                Object value = model.getValueAt(r, c);
                row.add(ParseValue(value));
            }
            data.add(row);
        }
        return data;
    }

    public Object getValue(int row, int col) {
        return model.getValueAt(row, col);
    }

    public void setValue(Object value, int row, int col) {
        model.setValueAt(value, row, col);
    }

    public JTable getTable() {
        return table;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    private double ParseValue(Object obj){
        if(obj == null) return 0;

        String s = obj.toString().trim();

        if(s.isEmpty()) return 0;

        if(s.contains("/")){
            String[] parts = s.split("/");
            if(parts.length == 2){
                double a = Double.parseDouble(parts[0].trim());
                double b = Double.parseDouble(parts[1].trim());
                return a / b;
            }
        }

        return Double.parseDouble(s);
    }

    public void stopEditing() {
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
    }

    private void updateRowHeader(JList<String> rowHeader, int rowCount) {
        String[] rowNames = new String[rowCount];
        for (int i = 0; i < rowCount; i++) {
            rowNames[i] = "Row " + (i + 1);
        }
        rowHeader.setListData(rowNames);
    }
}
