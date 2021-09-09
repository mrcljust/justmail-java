package src.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class WriteEmail extends javax.swing.JDialog {

    DefaultTableModel model = new DefaultTableModel();
    private List<String> Attachments = new ArrayList<String>();
    public String content = "";
    public String to = "";
    public String subject = "";

    public static class FileWrapper {

        private final File m_file;

        public FileWrapper(File p_fileToWrap) {
            m_file = p_fileToWrap;
        }

        @Override
        public String toString() {
            return m_file.getName();
        }

        public String getFilePath() {
            return m_file.getAbsolutePath();
        }
    }

    /**
     * Creates new form WriteEmail
     */
    public WriteEmail() {
        super((JFrame) null, "E-Mail verfassen - justMail", true);
        Image image = Toolkit.getDefaultToolkit().getImage("src/img/icon.png");
        this.setIconImage(image);
        initComponents();
        model.addColumn("Anhänge");
        tblAttachments.setModel(model);
        
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() { 
               @Override
               public void windowClosing(WindowEvent event) { 
                       closeApp(); 
               } 
           } 
        );     
    }

    public void init() {
        File dir = new File(System.getProperty("user.dir") + "/mailaccounts/");

        File[] children = dir.listFiles();
        if (children == null || children.length == 0) {
            System.exit(0);
        } else {
            for (File aFile : children) {
                FileWrapper fw = new FileWrapper(aFile);
                cbSender.addItem(fw);
            }
        }

        if (!subject.equals("")) {
            txtSubject.setText(subject);
            txtContent.setText(content);
            txtRecipient.setText(to);
        }
    }

    public static String toIdnAddress(String mail) {
        if (mail == null) {
            return null;
        }
        int idx = mail.indexOf('@');
        if (idx < 0) {
            return mail;
        }
        return localPart(mail, idx) + "@" + IDN.toASCII(domain(mail, idx));
    }

    private static String localPart(String mail, int idx) {
        return mail.substring(0, idx);
    }

    private static String domain(String mail, int idx) {
        return mail.substring(idx + 1);
    }

    public Message SendEmailSmtpTLS(String smtpserver, String smtpport, final String name, final String user, final String pw, String subject, String to, String context, String cc, String bcc) throws MessagingException {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpserver);
            props.put("mail.smtp.port", smtpport);
            props.put("mail.smtp.connectiontimeout", 10000);
            props.put("mail.smtp.timeout", 10000);

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(user, pw);
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user, name));
            if (!to.equals("")) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toIdnAddress(to.trim())));
            }
            if (!cc.equals("")) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(toIdnAddress(cc.trim())));
            }
            if (!bcc.equals("")) {
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(toIdnAddress(bcc.trim())));
            }

            message.setSubject(subject);
            Date today = Calendar.getInstance().getTime();
            DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            message.setHeader("Date", df.format(today));
            message.setText(context);

            //attachment_PathList.add("/home/lars/NetBeansProjects/Prak1/mailaccounts/cuukiee@live.de/mail.txt");
            //attachment_PathList.add("/home/lars/NetBeansProjects/Prak1/mailaccounts/cuukiee@live.de/pw.txt");
            if (Attachments.size() > 0) {
                Multipart multipart = new MimeMultipart("mixed");
                MimeBodyPart contentPart = new MimeBodyPart();
                contentPart.setContent(context, "text/plain");
                multipart.addBodyPart(contentPart);
                for (String str : Attachments) {
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(str);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(source.getName());

                    multipart.addBodyPart(messageBodyPart);
                }
                message.setContent(multipart);
            }
            Transport.send(message);
            System.out.println("E-Mail gesendet");
            return message;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WriteEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        cbSender = new javax.swing.JComboBox();
        txtRecipient = new javax.swing.JTextField();
        txtSubject = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCC = new javax.swing.JTextField();
        txtBCC = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAttachments = new javax.swing.JTable();
        btnRemoveAttachment = new javax.swing.JButton();
        btnAddAttachment = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtContent = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        btnSend = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(677, 379));
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        jSplitPane1.setDividerLocation(160);
        jSplitPane1.setDividerSize(0);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(675, 150));

        jLabel1.setText("Empfänger:");

        jLabel2.setText("Betreff:");

        jLabel3.setText("Absender:");

        jLabel4.setText("CC:");

        jLabel5.setText("BCC:");

        tblAttachments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Anhänge"
            }
        ));
        jScrollPane2.setViewportView(tblAttachments);

        btnRemoveAttachment.setText("-");
        btnRemoveAttachment.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveAttachmentActionPerformed(evt);
            }
        });

        btnAddAttachment.setText("+");
        btnAddAttachment.setToolTipText("");
        btnAddAttachment.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddAttachmentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtBCC)
                    .addComponent(txtCC)
                    .addComponent(txtRecipient)
                    .addComponent(cbSender, 0, 352, Short.MAX_VALUE)
                    .addComponent(txtSubject))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 161, Short.MAX_VALUE)
                        .addComponent(btnAddAttachment)
                        .addGap(3, 3, 3)
                        .addComponent(btnRemoveAttachment))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddAttachment, btnRemoveAttachment});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRemoveAttachment)
                            .addComponent(btnAddAttachment)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbSender, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRecipient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(6, 6, 6))
        );

        jSplitPane1.setTopComponent(jPanel1);

        jScrollPane1.setViewportView(txtContent);

        jSplitPane1.setRightComponent(jScrollPane1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setPreferredSize(new java.awt.Dimension(677, 35));

        btnSend.setText("Senden");
        btnSend.setMaximumSize(new java.awt.Dimension(144, 25));
        btnSend.setMinimumSize(new java.awt.Dimension(144, 25));
        btnSend.setPreferredSize(new java.awt.Dimension(144, 25));
        btnSend.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(583, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(677, 16));

        lblStatus.setText("Status:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(lblStatus)
                .addContainerGap(686, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(lblStatus)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jMenu1.setText("Datei");

        jMenuItem1.setText("Schließen");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Bearbeiten");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Ansicht");
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Format");
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Aktionen");
        jMenuBar1.add(jMenu5);

        jMenu6.setText("Extras");
        jMenuBar1.add(jMenu6);

        jMenu7.setText("Hilfe");
        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        FileWrapper fw = (FileWrapper) cbSender.getSelectedItem();
        CConfig cfg = new CConfig();
        try {
            Properties props = cfg.loadConfig(fw.getFilePath());
            
            String sender = fw.toString();
            String recipient = txtRecipient.getText();
            String cc = txtCC.getText();
            String bcc = txtBCC.getText();
            String _subject = txtSubject.getText();
            String _content = txtContent.getText();
            String smtpserver = props.getProperty("SMTP_SERVER");
            String smtpport = props.getProperty("SMTP_PORT");
            String pw = props.getProperty("MAIL_PW");
            String name = props.getProperty("NAME");

            boolean receiverGiven = false;
            if (txtBCC.getText().trim().length() > 1) {
                if (txtBCC.getText().contains("@"))
                    receiverGiven = true;
            }
            if (txtCC.getText().trim().length() > 1) {
                if (txtCC.getText().contains("@"))
                    receiverGiven = true;
            }
            if (txtRecipient.getText().trim().length() > 1) {
                if (txtRecipient.getText().contains("@"))
                    receiverGiven = true;
            }

            if (receiverGiven && !sender.trim().isEmpty() && !_subject.trim().isEmpty() && !_content.trim().isEmpty()) {
                try {
                    Message message = SendEmailSmtpTLS(smtpserver, smtpport, name, sender, pw, _subject, recipient, _content, cc, bcc);
                    String sentFolder = System.getProperty("user.dir") + "/mailaccounts/" + sender + "/" + "sent/";
                    File mailFile = new File(sentFolder, UUID.randomUUID().toString() + ".eml");
                    FileOutputStream fos = new FileOutputStream(mailFile);
                    message.writeTo(fos);
                    fos.close();
                    JOptionPane.showMessageDialog(null, "Ihre E-Mail (" + _subject + ") wurde erfolgreich versendet", "Meldung", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    return;
                } catch (Exception ex) {
                    if (ex.getMessage().contains("Could not connect to SMTP"))
                    {
                        JOptionPane.showMessageDialog(null, "Es konnte keine Verbindung zum SMTP-Server hergestellt werden", "Meldung", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (ex.getMessage().contains("Can't send command to SMTP"))
                    {
                        JOptionPane.showMessageDialog(null, "Der SMTP-Server weist die Anfrage ab. Möglicherweise besteht eine temporäre Sperre.", "Meldung", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (ex.getMessage().contains("Could not connect to SMTP"))
                    {
                        JOptionPane.showMessageDialog(null, "Es konnte keine Verbindung zum SMTP-Server hergestellt werden", "Meldung", JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Ihre E-Mail konnte nicht gesendet werden.\n" + ex.getMessage(), "Meldung", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                if (sender.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Absender aus", "Meldung", JOptionPane.ERROR_MESSAGE);
                }

                if (!receiverGiven) {
                    JOptionPane.showMessageDialog(null, "Bitte tragen Sie mindestens einen gültigen Empfänger ein", "Meldung", JOptionPane.ERROR_MESSAGE);
                }

                if (_subject.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Bitte tragen Sie einen Betreff ein", "Meldung", JOptionPane.ERROR_MESSAGE);
                }

                if (_content.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Bitte tragen Sie Inhalt ein", "Meldung", JOptionPane.ERROR_MESSAGE);
                }
            } 
        } catch (Exception ex) {
            Logger.getLogger(WriteEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnAddAttachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAttachmentActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        int returnVal = fc.showOpenDialog(btnAddAttachment);
        File[] selFiles = fc.getSelectedFiles();
        for (File f : selFiles) {
            addAttachment(f);
        }
    }//GEN-LAST:event_btnAddAttachmentActionPerformed

    public void addAttachment(File f) {
        model.addRow(new Object[]{f.getAbsolutePath()});
        Attachments.add(f.getAbsolutePath());
    }
    
    private void btnRemoveAttachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveAttachmentActionPerformed
        if (tblAttachments.getSelectedRow() > -1) {
            Attachments.remove(tblAttachments.getSelectedRow());
            model.removeRow(tblAttachments.getSelectedRow());
            model.fireTableDataChanged();
        }
    }//GEN-LAST:event_btnRemoveAttachmentActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       
    }//GEN-LAST:event_formWindowClosing

    private void closeApp()
    {
        if (txtContent.getText().trim().equals("") && txtCC.getText().trim().equals("") && txtBCC.getText().trim().equals("") && txtRecipient.getText().trim().equals("") && txtSubject.getText().trim().equals(""))
        {
            this.dispose();
        }
        else
        {
            int dialogResult = JOptionPane.showConfirmDialog (null, "Möchten Sie das Fenster wirklich schließen?", "Fenster schließen?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(dialogResult == JOptionPane.YES_OPTION){
                this.dispose();
            }
            else
            {
                //nein selektiert
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAttachment;
    private javax.swing.JButton btnRemoveAttachment;
    private javax.swing.JButton btnSend;
    private javax.swing.JComboBox cbSender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblAttachments;
    private javax.swing.JTextField txtBCC;
    private javax.swing.JTextField txtCC;
    private javax.swing.JTextPane txtContent;
    private javax.swing.JTextField txtRecipient;
    private javax.swing.JTextField txtSubject;
    // End of variables declaration//GEN-END:variables
}
