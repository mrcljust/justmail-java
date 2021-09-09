package src.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import src.JustMail;
import src.ui.table.EMail;
import src.utils.CloseableUtil;

public class Main extends javax.swing.JFrame
{

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

    public final MainMailPanel mainMailPnl;
    private boolean notByUser = false;
    private boolean startSearch = true;
    TrayIcon trayIcon;
    
    final SystemTray tray = SystemTray.getSystemTray();

    public Main()
    {
        this.setTitle("Posteingang - justMail");
        Image image = Toolkit.getDefaultToolkit().getImage("src/img/icon.png");
        this.setIconImage(image);
        initComponents();
        this.setLocationRelativeTo(null);
        startUp();

        mainMailPnl = new MainMailPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add(mainMailPnl, BorderLayout.CENTER);
        listMailaccountsToCombobox();
        createTray();
    }
    
    public void showTrayIconMsg(String title, String text, TrayIcon.MessageType msgType)
    {
        trayIcon.displayMessage(title, text, msgType);
    }
    
    private void createTray() {
        if(!SystemTray.isSupported()){
        System.out.println("SystemTray not supported");
        return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("src/img/icon.png");
        PopupMenu trayPopupMenu = new PopupMenu();
        
        MenuItem aboutMenuItem = new MenuItem("Über");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                About neuerFrame = new About();
                neuerFrame.setVisible(true);
            }
        });     
        trayPopupMenu.add(aboutMenuItem);
        trayPopupMenu.addSeparator();
        
        MenuItem setVisibilityMenuItem = new MenuItem("justMail ausblenden");
        setVisibilityMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (setVisibilityMenuItem.getLabel().equals("justMail ausblenden"))
                {
                    JustMail.mainForm.setVisible(false);
                    setVisibilityMenuItem.setLabel("justMail anzeigen");
                }
                else
                {
                    JustMail.mainForm.setVisible(true);
                    setVisibilityMenuItem.setLabel("justMail ausblenden");
                }
            }
        });     
        trayPopupMenu.add(setVisibilityMenuItem);
        
        MenuItem close = new MenuItem("Beenden");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);             
            }
        });
        trayPopupMenu.addSeparator();
        trayPopupMenu.add(close);
        
        trayIcon = new TrayIcon(image, "justMail", trayPopupMenu);
        //adjust to default size as per system recommendation 
        trayIcon.setImageAutoSize(true);
        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }
    }

    public void listMailaccountsToCombobox()
    {
        cbReceiveMails.removeAllItems();
        File dir = new File(System.getProperty("user.dir") + "/mailaccounts/");
        File[] mailAccountFolders = dir.listFiles();
        cbReceiveMails.removeAll();
        cbReceiveMails.addItem("Alle Konten abrufen");
        for (File aFile : mailAccountFolders)
        {
            FileWrapper fw = new FileWrapper(aFile);
            cbReceiveMails.addItem(fw);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    public String readFile(String filepath)
    {
        File file = new File(filepath);

        StringBuffer buffSmtpSrv = new StringBuffer();
        FileReader fr = null;
        int c;

        if (!file.canRead() || !file.isFile())
        {
            System.exit(0);
        }

        try
        {
            fr = new FileReader(file);
            while ((c = fr.read()) != -1)
            {
                buffSmtpSrv.append((char) c);
            }
            fr.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffSmtpSrv.toString();

    }

    public void setStatusText(String text)
    {
        txtStatus.setText("Status: " + text);
    }

    private boolean createFile(File filepath, String content)
    {
        try
        {
            FileWriter writer1 = new FileWriter(filepath, true);
            writer1.write(content);
            writer1.flush();
            writer1.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        btnWriteMail = new javax.swing.JButton();
        btnAddressbook = new javax.swing.JButton();
        cbReceiveMails = new javax.swing.JComboBox();
        txtSearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txtStatus = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuWriteMail = new javax.swing.JMenuItem();
        menuOpenEmail = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menuAddEmailAcc = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(868, 400));

        jPanel1.setPreferredSize(new java.awt.Dimension(855, 35));

        btnWriteMail.setText("E-Mail verfassen");
        btnWriteMail.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnWriteMailActionPerformed(evt);
            }
        });

        btnAddressbook.setText("Adressbuch");
        btnAddressbook.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddressbookActionPerformed(evt);
            }
        });

        cbReceiveMails.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cbReceiveMailsActionPerformed(evt);
            }
        });

        txtSearch.setText("Suchen nach...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(cbReceiveMails, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnWriteMail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddressbook)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddressbook, btnWriteMail});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnWriteMail)
                    .addComponent(btnAddressbook)
                    .addComponent(cbReceiveMails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        txtStatus.setText("Status:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 566, Short.MAX_VALUE)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStatus))
                .addGap(2, 2, 2))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        jPanel3.setMinimumSize(new java.awt.Dimension(868, 365));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 868, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 372, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Datei");

        menuWriteMail.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuWriteMail.setText("E-Mail verfassen");
        menuWriteMail.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuWriteMailActionPerformed(evt);
            }
        });
        jMenu1.add(menuWriteMail);

        menuOpenEmail.setText("E-Mail öffnen...");
        menuOpenEmail.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuOpenEmailActionPerformed(evt);
            }
        });
        jMenu1.add(menuOpenEmail);
        jMenu1.add(jSeparator1);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Speichern unter...");
        jMenu1.add(jMenuItem9);
        jMenu1.add(jSeparator7);

        jMenuItem11.setText("Seite einrichten...");
        jMenu1.add(jMenuItem11);

        jMenuItem12.setText("Druckvorschau");
        jMenu1.add(jMenuItem12);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem13.setText("Drucken...");
        jMenu1.add(jMenuItem13);

        menuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuExit.setText("Beenden");
        menuExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuExitActionPerformed(evt);
            }
        });
        jMenu1.add(menuExit);
        jMenu1.add(jSeparator8);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Bearbeiten");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Rückgängig");
        jMenu2.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Wiederherstellen");
        jMenu2.add(jMenuItem4);
        jMenu2.add(jSeparator4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Ausschneiden");
        jMenu2.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Kopieren");
        jMenu2.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Einfügen");
        jMenu2.add(jMenuItem7);
        jMenu2.add(jSeparator5);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("Alles auswählen");
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Ansicht");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Statusleiste");
        jMenu3.add(jCheckBoxMenuItem1);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Navigation");

        jMenuItem10.setText("Nächste Nachricht");
        jMenu4.add(jMenuItem10);

        jMenuItem14.setText("Vorherige Nachricht");
        jMenu4.add(jMenuItem14);
        jMenu4.add(jSeparator6);

        jMenuItem15.setText("Startseite");
        jMenu4.add(jMenuItem15);

        jMenuBar1.add(jMenu4);

        jMenu7.setText("Nachricht");

        jMenuItem16.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem16.setText("E-Mail verfassen");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem16);
        jMenu7.add(jSeparator9);

        jMenuItem17.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem17.setText("Antworten");
        jMenu7.add(jMenuItem17);

        jMenuItem18.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem18.setText("Weiterleiten");
        jMenu7.add(jMenuItem18);

        jMenuBar1.add(jMenu7);

        jMenu5.setText("Extras");

        jMenuItem19.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem19.setText("Adressbuch");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem19);

        jMenuItem22.setText("Spam-Filter verwalten");
        jMenu5.add(jMenuItem22);
        jMenu5.add(jSeparator3);

        menuAddEmailAcc.setText("E-Mail Konto hinzufügen");
        menuAddEmailAcc.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuAddEmailAccActionPerformed(evt);
            }
        });
        jMenu5.add(menuAddEmailAcc);

        jMenuItem1.setText("Kontenverwaltung");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem1);
        jMenu5.add(jSeparator2);

        jMenuItem20.setText("Einstellungen");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem20);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Hilfe");

        jMenuItem21.setText("Auf Aktualisierung prüfen...");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem21);
        jMenu6.add(jSeparator10);

        jMenuItem2.setText("Über");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem2);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuAddEmailAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddEmailAccActionPerformed
        newAddMailWindow();
    }//GEN-LAST:event_menuAddEmailAccActionPerformed

    private void btnWriteMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteMailActionPerformed
        WriteEmail neuerFrame = new WriteEmail();
        neuerFrame.init();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_btnWriteMailActionPerformed

    public void newAddMailWindow()
    {
        AddEmail neuerFrame = new AddEmail();
        neuerFrame.setVisible(true);
    }

    private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuExitActionPerformed

    private void cbReceiveMailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbReceiveMailsActionPerformed
        if (!cbReceiveMails.getSelectedItem().toString().equals("Alle Konten abrufen"))
        {
            final FileWrapper fw = (FileWrapper) cbReceiveMails.getSelectedItem();
            final String mailSelected = fw.toString();
            getMails(mailSelected);
        }
        else if (cbReceiveMails.getSelectedItem().toString().equals("Alle Konten abrufen"))
        {
            if (startSearch == true)
            {
                File dir = new File(System.getProperty("user.dir") + "/mailaccounts/");
                File[] mailAccountFolders = dir.listFiles();

                for (File aFile : mailAccountFolders)
                {
                    try
                    {
                        CConfig cfg = new CConfig();
                        Properties props = cfg.loadConfig(aFile.getAbsolutePath());
                        boolean getMails = Boolean.valueOf(props.getProperty("MAILS_AT_START"));
                        if (getMails == true)
                        {
                            FileWrapper fw = new FileWrapper(aFile);
                            System.out.println("Prüfe auf neue E-Mail(s) an " + fw.toString() + "...");
                            getMails(fw.toString());
                        }
                    }
                    catch (Exception ex)
                    {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                startSearch = false;
                return;
            }
            else if (startSearch == false)
            {
                if (notByUser == false)
                {
                    File dir = new File(System.getProperty("user.dir") + "/mailaccounts/");
                    File[] mailAccountFolders = dir.listFiles();
                    List<String> allMails;

                    for (File aFile : mailAccountFolders)
                    {
                        FileWrapper fw = new FileWrapper(aFile);
                        System.out.println("Prüfe auf neue E-Mail(s) an alle Konten...");
                        getMails(fw.toString());
                    }
                }
                else if (notByUser == true)
                {
                    notByUser = false;
                    return;
                }
            }
        }
    }//GEN-LAST:event_cbReceiveMailsActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ManageEmails neuerFrame = new ManageEmails();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menuWriteMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuWriteMailActionPerformed
        WriteEmail neuerFrame = new WriteEmail();
        neuerFrame.init();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_menuWriteMailActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        About neuerFrame = new About();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        UserSettings neuerFrame = new UserSettings();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        Addressbook neuerFrame = new Addressbook();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void btnAddressbookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddressbookActionPerformed
        Addressbook neuerFrame = new Addressbook();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_btnAddressbookActionPerformed

    private void menuOpenEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenEmailActionPerformed
        JFileChooser fc = new JFileChooser("C:\\Users\\P c\\Documents\\NetBeansProjects\\justMail\\mailaccounts\\marcelxjust@aol.de\\inbox");
        fc.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("E-Mail", "eml");   
        fc.addChoosableFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(menuOpenEmail);
        File selFile = fc.getSelectedFile();

        EmailDisplayer newFrame = new EmailDisplayer();
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(selFile);
            MimeMessage mime = new MimeMessage(null, fis);
            EMail mailToOpen = new EMail(mime, selFile.getAbsolutePath());
            
            String type = mailToOpen.getContentTypeFull();
            if (type.startsWith("multipart"))
            {
                try
                {
                    String multiPartContent = mailToOpen.getMultiPartText(mailToOpen.getMessage());
                    newFrame.openMailAttachments(multiPartContent, mailToOpen);
                }
                catch (MessagingException | IOException ex)
                {
                    Logger.getLogger(MainMailPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                newFrame.openMailNoAttachments(mailToOpen);
            }
            newFrame.setVisible(true);
        }
        catch (FileNotFoundException | MessagingException ex)
        {
            Logger.getLogger(MainMailPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        CloseableUtil.close(fis);
    }//GEN-LAST:event_menuOpenEmailActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        WriteEmail neuerFrame = new WriteEmail();
        neuerFrame.init();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem21ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem21ActionPerformed
        setStatusText("Prüfe auf Aktualisierungen...");
        boolean updatesFound = checkForUpdates();
        if (updatesFound)
        {
            setStatusText("");
            showTrayIconMsg("justMail", "Es wurden neue Aktualisierungen gefunden", TrayIcon.MessageType.INFO);  
        }
        else 
        {
            setStatusText("");
            showTrayIconMsg("justMail", "Es wurden keine Aktualisierungen gefunden", TrayIcon.MessageType.INFO);
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private boolean checkForUpdates() 
    {
        try
        {
            return false;
        }
        catch (Exception ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            showTrayIconMsg("justMail", "Es konnte nicht auf Aktualisierungen geprüft werden", TrayIcon.MessageType.ERROR);
            return false;
        }
    }
    
    private void getMails(String mail)
    {
        CConfig cfg = new CConfig();
        String mailFolder = System.getProperty("user.dir") + "/mailaccounts/" + mail;
        try
        {
            Properties props = cfg.loadConfig(mailFolder);

            final String protocol = props.getProperty("PROTOCOL");

            final ReceiveMails mailreceiver = new ReceiveMails();

            if (protocol.contains("IMAP"))
            {
                final String email = mail;
                final String pw = props.getProperty("MAIL_PW");
                final String imapServer = props.getProperty("INBOX_SERVER");
                final String imapPort = props.getProperty("INBOX_PORT");

                SwingWorker sw = new SwingWorker()
                {

                    @Override
                    protected Object doInBackground() throws Exception
                    {
                        cbReceiveMails.setEnabled(false);
                        setStatusText("Prüfe auf neue E-Mail(s) an " + email + "...");
                        List<Message> receivedMails = mailreceiver.getMailsImap(email, pw, imapServer, imapPort, true);
                        if (receivedMails != null)
                        {
                            try
                            {
                                if (receivedMails.size() != 0)
                                {
                                    setStatusText(String.valueOf(receivedMails.size()) + " neue E-Mail(s) gefunden");
                                    showTrayIconMsg("justMail", receivedMails.size() + " neue E-Mail(s) an " + email, TrayIcon.MessageType.INFO);
                                    System.out.println(String.valueOf(receivedMails.size()) + " neue E-Mail(s) über das Protokoll " + protocol + " empfangen. Lege Dateien an.");
                                    int i = 1;
                                    jProgressBar1.setMaximum(receivedMails.size());
                                    jProgressBar1.setMinimum(0);
                                    for (Message mi : receivedMails)
                                    {
                                        //.eml datei für neue mails anlegen
                                        setStatusText("Empfange Email " + String.valueOf(i) + "/" + receivedMails.size() + "...");
                                        final String folderPath = new String(mailFolder + "/inbox/");

                                        if (!mi.getFolder().isOpen())
                                        {
                                            mi.getFolder().open(Folder.READ_ONLY);
                                        }

                                        File mailFile = new File(folderPath, UUID.randomUUID().toString() + ".eml");
                                        FileOutputStream fos = new FileOutputStream(mailFile);
                                        mi.writeTo(fos);
                                        fos.close();
                                        boolean added = false;
                                        if (mainMailPnl.selectedNode == "Posteingang")
                                        {
                                            EMail obj = new EMail(mi, mailFile.getAbsolutePath());
                                            mainMailPnl.addEmailToTable(obj);
                                            mainMailPnl.sortTableByDate();
                                            added = true;
                                        }
                                        if (added)
                                        {
                                            setStatusText("E-Mail " + String.valueOf(i) + "/" + receivedMails.size() + " empfangen.");
                                            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
                                            System.out.println("Datei " + String.valueOf(i) + "/" + receivedMails.size() + " angelegt. Zum Table hinzugefügt.");
                                        }
                                        if (!added)
                                        {
                                            setStatusText("E-Mail " + String.valueOf(i) + "/" + receivedMails.size() + " empfangen.");
                                            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
                                            System.out.println("Datei " + String.valueOf(i) + "/" + receivedMails.size() + " angelegt.");
                                        }
                                        i += 1;
                                    }
                                    showTrayIconMsg("justMail", "Es wurden " + receivedMails.size() + " neue E-Mail(s) an " + email + " empfangen", TrayIcon.MessageType.INFO);
                                    jProgressBar1.setValue(0);
                                    setStatusText(receivedMails.size() + " neue E-Mail(s) empfangen");
                                    Thread.sleep(2500);
                                    setStatusText("");
                                    return null;
                                }
                                else
                                {
                                    setStatusText("Keine neue E-Mail(s) an " + mail + " gefunden");
                                    showTrayIconMsg("justMail", "Keine neue E-Mail(s) an " + email, TrayIcon.MessageType.INFO);
                                    Thread.sleep(2500);
                                    setStatusText("");
                                    System.out.println("Keine neue E-Mail(s) über das Protokoll " + protocol + " empfangen.");
                                }
                            }
                            catch (Exception interruptedException)
                            {
                                interruptedException.printStackTrace();
                                return null;
                            }
                        }
                        else
                        {
                            setStatusText("");
                        }
                        return null;
                    }

                    @Override
                    protected void done()
                    {
                        cbReceiveMails.setEnabled(true);
                        notByUser = true;
                        cbReceiveMails.setSelectedIndex(0);
                    }
                ;
                };
                sw.execute();
            }
            else if (protocol.contains("POP"))
            {
                final String email = mail;
                final String pw = props.getProperty("MAIL_PW");
                final String popServer = props.getProperty("INBOX_SERVER");
                final String popPort = props.getProperty("INBOX_PORT");

                SwingWorker sw = new SwingWorker()
                {

                    @Override
                    protected Object doInBackground() throws Exception
                    {
                        cbReceiveMails.setEnabled(false);
                        setStatusText("Prüfe auf neue E-Mail(s) an " + email + "...");
                        List<Message> receivedMails = mailreceiver.getMailsPop(email, pw, popServer, popPort, true);
                        if (receivedMails != null)
                        {
                            try
                            {
                                if (receivedMails.size() != 0)
                                {
                                    setStatusText(String.valueOf(receivedMails.size()) + " neue E-Mail(s) gefunden");
                                    showTrayIconMsg("justMail", receivedMails.size() + " neue E-Mail(s) an " + email, TrayIcon.MessageType.INFO);
                                    System.out.println(String.valueOf(receivedMails.size()) + " neue E-Mail(s"
                                            + " über das Protokoll " + protocol + " empfangen. Lege Dateien an.");
                                    int i = 1;
                                    jProgressBar1.setMaximum(receivedMails.size());
                                    jProgressBar1.setMinimum(0);
                                    for (Message mi : receivedMails)
                                    {
                                        //.eml datei für neue mails anlegen
                                        setStatusText("Empfange Email " + String.valueOf(i) + "/" + receivedMails.size() + "...");
                                        final String folderPath = new String(mailFolder + "/inbox/");

                                        if (!mi.getFolder().isOpen())
                                        {
                                            mi.getFolder().open(Folder.READ_ONLY);
                                        }

                                        File mailFile = new File(folderPath, UUID.randomUUID().toString() + ".eml");
                                        FileOutputStream fos = new FileOutputStream(mailFile);
                                        mi.writeTo(fos);
                                        fos.close();
                                        boolean added = false;
                                        if (mainMailPnl.selectedNode == "Posteingang")
                                        {
                                            EMail obj = new EMail(mi, mailFile.getAbsolutePath());
                                            mainMailPnl.addEmailToTable(obj);
                                            mainMailPnl.sortTableByDate();
                                            added = true;
                                        }
                                        if (added)
                                        {
                                            setStatusText("E-Mail " + String.valueOf(i) + "/" + receivedMails.size() + " empfangen.");
                                            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
                                            System.out.println("Datei " + String.valueOf(i) + "/" + receivedMails.size() + " angelegt. Zum Table hinzugefügt.");
                                        }
                                        if (!added)
                                        {
                                            setStatusText("E-Mail " + String.valueOf(i) + "/" + receivedMails.size() + " empfangen.");
                                            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
                                            System.out.println("Datei " + String.valueOf(i) + "/" + receivedMails.size() + " angelegt.");
                                        }
                                        
                                        
                                        i += 1;
                                    }
                                    showTrayIconMsg("justMail", "Es wurden " + receivedMails.size() + " neue E-Mail(s) an " + email + " empfangen", TrayIcon.MessageType.INFO);
                                    jProgressBar1.setValue(0);
                                    setStatusText(receivedMails.size() + " neue E-Mail(s) empfangen");
                                    Thread.sleep(2500);
                                    setStatusText("");
                                    return null;
                                }
                                else
                                {
                                    setStatusText("Keine neue E-Mail(s) an " + mail + " gefunden");
                                    showTrayIconMsg("justMail", "Keine neue E-Mail(s) an " + email, TrayIcon.MessageType.INFO);
                                    Thread.sleep(2500);
                                    setStatusText("");
                                    System.out.println("Keine neue E-Mail(s) über das Protokoll " + protocol + " empfangen.");
                                    return null;
                                }
                            }
                            catch (Exception interruptedException)
                            {
                                interruptedException.printStackTrace();
                                return null;
                            }
                        }
                        else
                        {
                            setStatusText("");
                        }
                        return null;
                    }

                    @Override
                    protected void done()
                    {
                        cbReceiveMails.setEnabled(true);
                        notByUser = true;
                        cbReceiveMails.setSelectedIndex(0);
                    }
                ;
                };
                sw.execute();
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static boolean checkDir(File dirName)
    {
        if (dirName.exists()) // Überprüfen, ob es den Ordner gibt
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void startUp()
    {
        File mailAccountsFolder = new File(System.getProperty("user.dir") + "/mailaccounts/");
        if (checkDir(mailAccountsFolder))
        {
            if (mailAccountsFolder.list().length > 0)
            {
                //mails vorhanden -> fahre fort
            }
            else
            {
                newAddMailWindow();
            }
        }
        else
        {
            newAddMailWindow();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddressbook;
    private javax.swing.JButton btnWriteMail;
    private javax.swing.JComboBox cbReceiveMails;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JMenuItem menuAddEmailAcc;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenuItem menuOpenEmail;
    private javax.swing.JMenuItem menuWriteMail;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JLabel txtStatus;
    // End of variables declaration//GEN-END:variables
}
