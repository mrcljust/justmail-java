package src.ui.table;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class ImageTableHeaderRenderer extends JLabel implements TableCellRenderer
{

    private ImageIcon m_image;
    private String m_text;

    public ImageTableHeaderRenderer(ImageIcon p_image)
    {
        m_image = p_image;
    }

    public ImageTableHeaderRenderer(String p_text)
    {
        m_text = p_text;
    }

    public ImageTableHeaderRenderer(String text, ImageIcon p_image)
    {
        m_image = p_image;
        m_text = text;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        this.setIcon(m_image);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setText(m_text);
        return this;
    }

}
