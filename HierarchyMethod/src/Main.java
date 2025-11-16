import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Метод иерархии");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        HierarchyTable tablePanel = new HierarchyTable(3, 3);
        Calculator calculator = new Calculator();

        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(600, 350));

        JButton printBtn = new JButton("начать расчёт");
        printBtn.addActionListener(e -> {
            tablePanel.stopEditing();
            Object data = tablePanel.getTableData();
            CalculationResult result = calculator.CalculateAll((Iterable<Iterable<Object>>) data);

            StringBuilder sb = new StringBuilder();

            sb.append("=== Вектор взвешивания ===\n");
            for (int i = 0; i < result.weightingVector.size(); i++) {
                sb.append(String.format("w%d = %.4f%n", i + 1, result.weightingVector.get(i)));
            }

            sb.append("\nНормирующий множитель =");
            sb.append(String.format("%.4f%n", result.normalizingMultiplier));

            sb.append("\n=== Нормализованные векторы ===\n");
            for (int i = 0; i < result.priorityVectors.size(); i++) {
                sb.append(String.format("q%d = %.4f%n", i + 1, result.priorityVectors.get(i)));
            }

            sb.append("\n=== Сумма столбцов ===\n");
            for (int i = 0; i < result.columnSum.size(); i++) {
                sb.append(String.format("S%d sum = %.4f%n", i + 1, result.columnSum.get(i)));
            }

            sb.append("\n=== Пропорции предпочтений ===\n");
            for (int i = 0; i < result.preference.size(); i++) {
                sb.append(String.format("p%d = %.4f%n", i + 1, result.preference.get(i)));
            }

            sb.append("\nСогласованность = ").append(String.format("%.4f%n", result.coherence));
            sb.append("\nИндекс согласованности ИС = ").append(String.format("%.4f%n", result.consistencyIndex));
            sb.append("\nСлучайный индекс СИ = ").append(String.format("%.4f%n", result.RandomIndex));
            sb.append("\nИндекс консистентности ОС = ").append(String.format("%.4f%n", result.consistency));
            if(result.consistency <= 0.1)
                sb.append(" Согласованность имеет место быть");
            else
                sb.append(" Согласованности нет");

            resultsArea.setText(sb.toString());
        });

        frame.setLayout(new BorderLayout());
        frame.add(tablePanel, BorderLayout.CENTER);
        frame.add(printBtn, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}