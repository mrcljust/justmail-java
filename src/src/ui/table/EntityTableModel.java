package src.ui.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class EntityTableModel extends AbstractTableModel
{

    private final List<Field> m_fieldsToUse = new ArrayList<>();
    private final List<EMail> m_mailList = new ArrayList<>();
    private Map<String, String> m_columnNames;

    public EntityTableModel()
    {
        setHiddenFields(null);
    }

    @Override
    public int getRowCount()
    {
        return m_mailList.size();
    }

    public void removeRow(int i)
    {
        m_mailList.remove(i);
    }

    public final void setColumnNames(Map<String, String> p_names)
    {
        m_columnNames = p_names;
    }

    public final void setHiddenFields(String[] p_hiddenFields)
    {
        Field[] allFields = EMail.class.getFields();
        m_fieldsToUse.clear();
        for (Field aField : allFields)
        {
            boolean shouldHide = false;

            if (p_hiddenFields != null)
            {
                for (String fieldName : p_hiddenFields)
                {
                    if (aField.getName().equals(fieldName))
                    {
                        shouldHide = true;
                    }
                }
            }

            if (!shouldHide)
            {
                m_fieldsToUse.add(aField);
            }
        }
    }

    public void addMessage(EMail mail)
    {
        m_mailList.add(mail);
    }

    public EMail getMessage(int selRow)
    {
        if (selRow > -1)
        {
            return m_mailList.get(selRow);
        }
        else
        {
            return null;
        }
    }

    @Override
    public int getColumnCount()
    {
        return m_fieldsToUse.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        try
        {
            EMail currentRow = m_mailList.get(rowIndex);
            if (currentRow == null)
                return null;
            Field aField = m_fieldsToUse.get(columnIndex);
            if (aField == null)
                return null;
            return aField.get(currentRow);
        }
        catch (Exception ex)
        {
            Logger.getLogger(EntityTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        String fieldName = m_fieldsToUse.get(columnIndex).getName();
        String colName = m_columnNames.get(fieldName);
        if (colName == null)
        {
            colName = convertFirstLetterUpper(fieldName);
        }
        return colName;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        Field aField = m_fieldsToUse.get(columnIndex);
        return aField.getType();

    }

    private String convertFirstLetterUpper(String name)
    {
        String firstLetter = name.substring(0, 1);
        String rest = name.substring(1, name.length());
        String ret = firstLetter.toUpperCase() + rest;
        return ret;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

}
