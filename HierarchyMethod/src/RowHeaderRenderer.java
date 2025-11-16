import javax.swing.*;
import java.awt.*;

public class RowHeaderRenderer extends JLabel implements ListCellRenderer<String> {
    RowHeaderRenderer(JTable table) {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setForeground(table.getTableHeader().getForeground());
        setBackground(table.getTableHeader().getBackground());
        setFont(table.getTableHeader().getFont());
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setText(value);
        return this;
    }
}
