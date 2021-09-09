package src.ui.table;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import src.ui.CContacts;

public class EMail
{

    private static DateFormat df1 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
    private static DateFormat df2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss Z", Locale.US);
    private static DateFormat df3 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    private static DateFormat dfNew = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public String m_subject;
    public String m_sender;
    public String m_recipient;
    public Date m_date;
    public boolean hasAttachments;
    public boolean isContact;

    private String m_content;
    private String m_contentType;
    private String m_contentTypeFull;
    private Message m_message;
    private String m_pathToMail;
    private String category;

    public EMail(Message message, String p_pathToMail)
    {
        try
        {
            CContacts con = new CContacts();
            m_subject = message.getSubject();
            m_sender = message.getFrom()[0].toString().replace("<", "(").replace(">", ")").replace("\"", "");
            m_recipient = message.getReplyTo()[0].toString().replace("<", "(").replace(">", ")").replace("\"", "");;

            String dateString[] = message.getHeader("Date");
            if (dateString != null && dateString.length > 0)
            {
                m_date = getDateFromString(dateString[0]);
            }

            m_content = message.getContent().toString();
            m_contentType = message.getContentType();
            ContentType contentTypeX = new ContentType(message.getContentType());
            m_contentTypeFull = contentTypeX.getPrimaryType() + "/" + contentTypeX.getSubType();
            hasAttachments = getContentTypeFull().startsWith("multipart");
            isContact = con.isContacted(m_sender);
            m_message = message;
            m_pathToMail = p_pathToMail;
            category = con.getCategoryEMail(m_sender);
        }
        catch (Exception messagingException)
        {
            messagingException.printStackTrace();
        }
    }

    public String getInhalt()
    {
        return m_content;
    }

    public String getContentType()
    {
        return m_contentType;
    }

    public String getContentTypeFull()
    {
        return m_contentTypeFull;
    }

    public boolean deleteMailLocal()
    {
        File mailFile = new File(m_pathToMail);
        boolean ret = mailFile.delete();
        return ret;
    }

    public boolean moveMailToTrash()
    {
        File mailFile = new File(m_pathToMail);
        if (mailFile.getAbsolutePath().contains("inbox"))
        {
            File newPath = new File(mailFile.getAbsolutePath().replace("inbox", "trash"));
            return mailFile.renameTo(newPath);
        }
        else
        {
            return false;
        }
    }

    public boolean hasAttachments()
    {
        return hasAttachments;
    }

    public boolean moveMailToInbox()
    {
        File mailFile = new File(m_pathToMail);
        if (mailFile.getAbsolutePath().contains("trash"))
        {
            File newPath = new File(mailFile.getAbsolutePath().replace("trash", "inbox"));
            mailFile.renameTo(newPath);
            return true;
        }
        else
        {
            return false;
        }
    }

    public String getPath()
    {
        return m_pathToMail;
    }

    public Message getMessage()
    {
        return m_message;
    }

    public String getMultiPartText(Part p) throws MessagingException, IOException
    {
        if (p.isMimeType("text/*"))
        {
            String s = (String) p.getContent();
            return s;
        }

        if (p.isMimeType("multipart/alternative"))
        {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++)
            {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain"))
                {
                    if (text == null)
                    {
                        text = getMultiPartText(bp);
                    }
                    continue;
                }
                else if (bp.isMimeType("text/html"))
                {
                    String s = getMultiPartText(bp);
                    if (s != null)
                    {
                        return s;
                    }
                }
                else
                {
                    return getMultiPartText(bp);
                }
            }
            return text;
        }
        else if (p.isMimeType("multipart/*"))
        {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++)
            {
                String s = getMultiPartText(mp.getBodyPart(i));
                if (s != null)
                {
                    return s;
                }
                else
                {
                    return "";
                }
            }
        }

        return null;
    }

    private static Date getDateFromString(String p_date)
    {
        Date dateS = null;
        try
        {
            dateS = df1.parse(p_date);
        }
        catch (ParseException ex)
        {
            try
            {
                dateS = df2.parse(p_date);
            }
            catch (ParseException ex1)
            {
                try
                {
                    dateS = df3.parse(p_date);
                }
                catch (ParseException ex2)
                {
                    Logger.getLogger(EntityTableModel.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return dateS;
    }
}
