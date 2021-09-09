package src.ui;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

public class CContacts
{

    public String getCategoryEMail(String m_sender)
    {
        File categoryDir = new File(System.getProperty("user.dir") + "/contacts/folders/");
        List<String> allCategories = new ArrayList<String>();
        for (File aFile : categoryDir.listFiles())
        {
            String folderName = aFile.getName().replace(".cfg", "");
            allCategories.add(folderName);
        }
        boolean ret = false;
        for (String aCat : allCategories)
        {
            if (m_sender.contains(aCat))
            {
                ret = true;
            }
        }
        return "";
    }

    public static class FileWrapper
    {

        private final File m_file;

        public FileWrapper(File p_fileToWrap)
        {
            m_file = p_fileToWrap;
        }

        @Override
        public String toString()
        {
            return m_file.getName();
        }

        public String getFilePath()
        {
            return m_file.getAbsolutePath();
        }
    }

    public void addContact(String email, String name, String category) throws Exception
    {
        GregorianCalendar now = new GregorianCalendar();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        Properties props = new Properties();
        props.setProperty("MAIL_ADR", email);
        props.setProperty("NAME", name);
        props.setProperty("FOLDER", category);
        props.setProperty("DATE_ADDED", df.format(now.getTime()));

        File propFile = new File(System.getProperty("user.dir") + "/contacts/" + email + ".cfg");
        FileWriter fw = new FileWriter(propFile);
        props.store(fw, null);
        fw.close();
    }

    public Properties loadContact(String email) throws Exception
    {
        Properties props = new Properties();
        File propFile = new File(System.getProperty("user.dir") + "/contacts/" + email + ".cfg");
        FileReader fr = new FileReader(propFile);
        props.load(fr);
        fr.close();
        return props;
    }

    public boolean removeContact(String email) throws Exception
    {
        File contactFile = new File(System.getProperty("user.dir") + "/contacts/" + email + ".cfg");
        contactFile.delete();
        return true;
    }

    public boolean doesContactExist(String email) throws Exception
    {
        File contactFile = new File(System.getProperty("user.dir") + "/contacts/" + email + ".cfg");
        if (contactFile.exists())
        {
            return true;
        }

        return false;
    }

    public boolean isContacted(String email) throws Exception
    {
        File contactsDir = new File(System.getProperty("user.dir") + "/contacts/");
        List<String> allContacts = new ArrayList<String>();
        for (File aFile : contactsDir.listFiles())
        {
            if (!aFile.getAbsolutePath().contains("/folder"))
            {
                String contactName = aFile.getName().replace(".cfg", "");
                allContacts.add(contactName);
            }
        }
        boolean ret = false;
        for (String aContact : allContacts)
        {
            if (email.contains(aContact))
            {
                ret = true;
            }
        }
        return ret;
    }

    public void createCategory(String categoryName) throws Exception
    {
        File dir = new File(System.getProperty("user.dir") + "/contacts/folders/" + categoryName + ".cfg");
        dir.createNewFile();
    }

    public boolean removeCategory(String categoryName) throws Exception
    {
        File dir = new File(System.getProperty("user.dir") + "/contacts/folders/" + categoryName + ".cfg");
        return dir.delete();
    }

    public List<String> getCategories() throws Exception
    {
        File categoryFolder = new File(System.getProperty("user.dir") + "/contacts/folders/");
        List<String> categories = new ArrayList<String>();
        categories.add("Allgemein.cfg");
        for (File aFile : categoryFolder.listFiles())
        {
            categories.add(aFile.getName());
        }
        return categories;
    }

    public boolean doesCategoryExist(String categoryName) throws Exception
    {
        if (categoryName.trim().equals("Allgemein"))
        {
            return true;
        }

        File categoryFile = new File(System.getProperty("user.dir") + "/contacts/folders/" + categoryName + ".cfg");
        if (categoryFile.exists())
        {
            return true;
        }

        return false;
    }
}
