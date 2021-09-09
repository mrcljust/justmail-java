package src.ui.table;

import java.io.File;

public class AttachFile
{

    public String m_fileName;
    public String m_filePath;

    public AttachFile(File attachment)
    {
        m_fileName = attachment.getName();
        m_filePath = attachment.getAbsolutePath();
    }

    public String getFilePath()
    {
        return m_filePath;
    }

    public String getFileName()
    {
        return m_fileName;
    }
}
