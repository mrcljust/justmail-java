package src.ui;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import src.JustMail;

public class ReceiveMails
{

    List<Message> mails = null;

    public static class MailHeader
    {

        private String to;
        private String from;
        private String contentType;
        private String returnPath;
        private String subject;
        private String date;

        public MailHeader(Enumeration<Header> allHeaders)
        {

            while (allHeaders.hasMoreElements())
            {
                Header h = allHeaders.nextElement();
                if (h.getName().equals("To"))
                {
                    to = h.getValue();
                }
                if (h.getName().equals("Subject"))
                {
                    subject = h.getValue();
                }
                if (h.getName().equals("From"))
                {
                    from = h.getValue();
                }
                if (h.getName().equals("Content-Type"))
                {
                    contentType = h.getValue();
                }
                if (h.getName().equals("Return-Path"))
                {
                    returnPath = h.getValue();
                }
                if (h.getName().equals("Date"))
                {
                    date = h.getValue();
                }
            }
        }

        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 31 * hash + Objects.hashCode(this.to);
            hash = 31 * hash + Objects.hashCode(this.from);
            hash = 31 * hash + Objects.hashCode(this.contentType);
            hash = 31 * hash + Objects.hashCode(this.returnPath);
            hash = 31 * hash + Objects.hashCode(this.subject);
            hash = 31 * hash + Objects.hashCode(this.date);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final MailHeader other = (MailHeader) obj;
            if (!Objects.equals(this.to, other.to))
            {
                return false;
            }
            if (!Objects.equals(this.from, other.from))
            {
                return false;
            }
            if (!Objects.equals(this.contentType, other.contentType))
            {
                return false;
            }
            if (!Objects.equals(this.returnPath, other.returnPath))
            {
                return false;
            }
            if (!Objects.equals(this.subject, other.subject))
            {
                return false;
            }
            if (!Objects.equals(this.date, other.date))
            {
                return false;
            }
            return true;
        }
    }

    private boolean mailWasAlreadySeen(String inboxFolderPath, String trashFolderPath, MailHeader p_mh)
    {
        File inbox = new File(inboxFolderPath);
        for (File aFile : inbox.listFiles())
        {
            try
            {
                MailHeader mh;
                try (FileInputStream fis = new FileInputStream(aFile))
                {
                    MimeMessage mime = new MimeMessage(null, fis);
                    mh = new MailHeader(mime.getAllHeaders());
                }
                if (p_mh.equals(mh))
                {
                    return true;
                }
            }
            catch (Exception ex)
            {
                Logger.getLogger(ReceiveMails.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        File trash = new File(trashFolderPath);
        for (File aFile : trash.listFiles())
        {
            try
            {
                MailHeader mh;
                try (FileInputStream fis = new FileInputStream(aFile))
                {
                    MimeMessage mime = new MimeMessage(null, fis);
                    mh = new MailHeader(mime.getAllHeaders());
                }
                if (p_mh.equals(mh))
                {
                    return true;
                }
            }
            catch (Exception ex)
            {
                Logger.getLogger(ReceiveMails.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;

    }

    public List<Message> getMailsPop(final String mail, final String pw, final String server, final String port, final Boolean enableStartTLS)
    {
        List<Message> ret = new ArrayList<>();
        try
        {
            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", server);
            properties.put("mail.pop3.port", port);
            properties.put("mail.pop3.starttls.enable", enableStartTLS);
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");
            store.connect(server, mail, pw);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("E-Mail Zahl auf " + mail + ": " + messages.length);

            //neue emails
            int i = 1;
            for (Message aMesaage : messages)
            {
                Enumeration e = aMesaage.getAllHeaders();
                MailHeader mh = new MailHeader(e);
                boolean mailAlreadySeen = mailWasAlreadySeen(System.getProperty("user.dir") + "/mailaccounts/" + mail + "/inbox/", System.getProperty("user.dir") + "/mailaccounts/" + mail + "/trash/", mh);

                if (!mailAlreadySeen)
                {
                    JustMail.mainForm.setStatusText(i + " neue E-Mails");
                    System.out.println("Empfange E-Mail " + String.valueOf(i) + "...");
                    i += 1;
                    ret.add(aMesaage);
                }
                else
                {
                    //mail bereits heruntergeladen
                }
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        }
        catch (NoSuchProviderException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (AuthenticationFailedException e)
        {
            if (e.getMessage().contains("This account has POP disabled"))
            {
                JOptionPane.showMessageDialog(null, "Auf Ihrem E-Mail Konto ist POP deaktiviert", "Meldung", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            else if (e.getMessage().contains("authentication failed"))
            {
                JOptionPane.showMessageDialog(null, "Authentifizierung fehlgeschlagen. E-Mail und/oder Passwort falsch", "Meldung", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            else
            {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
                return null;
            }
        }
        catch (MessagingException e)
        {
            if (e.getMessage().contains("Open failed"))
            {
                JOptionPane.showMessageDialog(null, "Sie haben in den letzten 15 Minuten zu oft neue E-Mails abgerufen", "Meldung", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            else
            {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
                System.out.println(e.getMessage());
                return null;
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());
            return null;
        }
        return ret;
    }

    public List<Message> getMailsImap(final String mail, final String pw, final String server, final String port, final Boolean enableStartTLS)
    {
        List<Message> ret = new ArrayList<>();
        try
        {
            //create properties field
            Properties properties = new Properties();

            properties.put("mail.imap.host", server);
            properties.put("mail.imap.port", port);
            properties.put("mail.pop3.starttls.enable", enableStartTLS);
            properties.put("mail.store.protocol", "imaps");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("imaps");
            store.connect(server, mail, pw);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("E-Mail Zahl auf " + mail + ": " + messages.length);

            //neue emails
            int i = 1;
            for (Message aMessage : messages)
            {
                Enumeration e = aMessage.getAllHeaders();
                MailHeader mh = new MailHeader(e);
                aMessage.setFlag(Flags.Flag.SEEN, true);
                boolean mailAlreadySeen = mailWasAlreadySeen(System.getProperty("user.dir") + "/mailaccounts/" + mail + "/inbox/", System.getProperty("user.dir") + "/mailaccounts/" + mail + "/trash/", mh);

                if (!mailAlreadySeen)
                {
                    JustMail.mainForm.setStatusText(i + " neue E-Mails");
                    System.out.println("Empfange E-Mail " + String.valueOf(i) + "...");
                    i += 1;
                    ret.add(aMessage);
                }
                else
                {
                    //mail bereits heruntergeladen
                }
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        }
        catch (NoSuchProviderException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (AuthenticationFailedException e)
        {
            if (e.getMessage().contains("This account has IMAP disabled"))
            {
                JOptionPane.showMessageDialog(null, "Auf Ihrem E-Mail Konto ist IMAP deaktiviert", "Meldung", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            else if (e.getMessage().contains("LOGIN failed"))
            {
                JOptionPane.showMessageDialog(null, "Login fehlgeschlagen", "Meldung", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            else if (e.getMessage().contains("AUTHENTICATIONFAILED"))
            {
                JOptionPane.showMessageDialog(null, "Authentifizierung fehlgeschlagen. E-Mail und/oder Passwort falsch", "Meldung", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            else
            {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
                return null;
            }
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return ret;
    }

}
