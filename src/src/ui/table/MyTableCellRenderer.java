package src.ui.table;

import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import src.JustMail;

public class MyTableCellRenderer extends DefaultTableCellRenderer
{

    private final DateFormat dfNew = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private Font boldFont;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (boldFont == null)
        {
            boldFont = getFont().deriveFont(Font.BOLD);
        }

        Component ret = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        JLabel label = (JLabel) ret;
        label.setIcon(null);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        if (value instanceof Date)
        {
            String formattedDate = dfNew.format((Date) value);
            label.setText(formattedDate);
            //label.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if (value instanceof Boolean)
        {
            Boolean bool = (Boolean) value;
            int idxAttach = JustMail.mainForm.mainMailPnl.tblEmails.getColumnModel().getColumnIndex(JustMail.mainForm.mainMailPnl.m_defaultColumnNameMap.get("hasAttachments"));
            int idxContact = JustMail.mainForm.mainMailPnl.tblEmails.getColumnModel().getColumnIndex(JustMail.mainForm.mainMailPnl.m_defaultColumnNameMap.get("isContact"));
            
            if (column == idxAttach)
            {
                if (bool)
                {
                    label.setText("");
                    label.setIcon(new ImageIcon(getClass().getResource("/img/attachment.png")));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
                else
                {
                    label.setText("");
                    label.setIcon(null);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
            }
            else if (column == idxContact)
            {
                if (bool)
                {
                    label.setText("");
                    label.setIcon(new ImageIcon(getClass().getResource("/img/contact.png")));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
                else
                {
                    label.setText("");
                    label.setIcon(null);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
            }

        }
        else
        {
            label.setHorizontalAlignment(SwingConstants.LEFT);
        }
        return ret;
    }
}
