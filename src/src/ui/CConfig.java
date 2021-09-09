package src.ui;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class CConfig
{

    public void createConfig(String mailAccFolder, String protocol, String inboxSrv, String inboxPort, String smtpSrv, String smtpPort, String name, String mailAdr, String pw, boolean getMailsAtStart) throws Exception
    {
        Properties props = new Properties();
        props.setProperty("PROTOCOL", protocol);
        props.setProperty("INBOX_SERVER", inboxSrv);
        props.setProperty("INBOX_PORT", inboxPort);
        props.setProperty("SMTP_SERVER", smtpSrv);
        props.setProperty("SMTP_PORT", smtpPort);
        props.setProperty("NAME", name);
        props.setProperty("MAIL_ADR", mailAdr);
        props.setProperty("MAIL_PW", pw);
        props.setProperty("MAILS_AT_START", String.valueOf(getMailsAtStart));

        File f = new File(mailAccFolder + "config.cfg");
        FileWriter fw = new FileWriter(f);
        props.store(fw, null);
        fw.close();
    }

    public Properties loadConfig(String mailAccFolder) throws Exception
    {
        Properties props = new Properties();
        File f = new File(mailAccFolder + "/config.cfg");
        FileReader fr = new FileReader(f);
        props.load(fr);
        fr.close();
        return props;
    }
}
