package src.ui;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import src.ui.table.AttachFile;
import src.ui.table.AttachmentTableModel;
import src.ui.table.EMail;

public class ShowAttachments extends javax.swing.JDialog
{

    private String randomID = UUID.randomUUID().toString();
    private String curMail = null;
    private Map<String, String> m_defaultColumnNameMap = new HashMap<String, String>();

    public ShowAttachments()
    {
        super((JFrame) null, "Anhänge - - justMail", true);
        Image image = Toolkit.getDefaultToolkit().getImage("src/img/icon.png");
        this.setIconImage(image);
        initComponents();
        m_defaultColumnNameMap.put("m_fileName", "Anhänge");
        //m_defaultColumnNameMap.put("m_filePath", "Pfad");
        this.setLocationRelativeTo(null);
    }

    public void loadAttachments(EMail mail, String curMailFolder)
    {
        curMail = curMailFolder;
        try
        {
            Message mimeMail = mail.getMessage();
            this.setTitle("Anhänge - " + mimeMail.getSubject() + " - justMail");
            List<File> attachments = new ArrayList<File>();

            Multipart multipart = (Multipart) mimeMail.getContent();
            // System.out.println(multipart.getCount());

            jTable1.removeAll();
            AttachmentTableModel mtm = new AttachmentTableModel();
            mtm.setColumnNames(m_defaultColumnNameMap);
            mtm.setHiddenFields(new String[]
            {
                "m_filePath"
            });
            jTable1.setModel(mtm);

            for (int i = 0; i < multipart.getCount(); i++)
            {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))
                {
                    continue; //nur mit anhängen fortsetzen
                }
                InputStream is = bodyPart.getInputStream();
                File attachmentDirectory = new File(System.getProperty("user.dir") + "/mailaccounts/" + curMail + "/tmp/" + randomID + "/");
                if (!attachmentDirectory.exists())
                {
                    attachmentDirectory.mkdir();
                }
                File attachmentFile = new File(System.getProperty("user.dir") + "/mailaccounts/" + curMail + "/tmp/" + randomID + "/" + bodyPart.getFileName());
                FileOutputStream fos = new FileOutputStream(attachmentFile);
                byte[] buf = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buf)) != -1)
                {
                    fos.write(buf, 0, bytesRead);
                }
                fos.close();
                AttachFile attach = new AttachFile(attachmentFile);
                addAttachmentToTable(attach);
            }
        }
        catch (MessagingException ex)
        {
            Logger.getLogger(ShowAttachments.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ShowAttachments.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ShowAttachments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addAttachmentToTable(final AttachFile attachment)
    {
        if (SwingUtilities.isEventDispatchThread())
        {
            AttachmentTableModel model = (AttachmentTableModel) jTable1.getModel();
            model.addAttach(attachment);
            model.fireTableDataChanged();
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {

                @Override
                public void run()
                {
                    addAttachmentToTable(attachment);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null}
            },
            new String [] {
                "Anhänge"
            }
        ));
        jTable1.setToolTipText("");
        jScrollPane1.setViewportView(jTable1);

        jToggleButton1.setText("Anhang öffnen");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Bitte öffnen Sie nur Anhänge aus vertrauensvollen E-Mails!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jLabel1))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        if (jTable1.getSelectedRowCount() == 1)
        {
            int selRow = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
            AttachmentTableModel mtm = (AttachmentTableModel) jTable1.getModel();
            AttachFile selectedAttachment = mtm.getAttachment(selRow);
            try
            {
                Desktop.getDesktop().open(new File(selectedAttachment.m_filePath));
            }
            catch (IOException ex)
            {
                if (ex.getMessage().contains("error=13"))
                {
                    //JOptionPane.showMessageDialog(null, "E-Mail Client hat nicht die benötigte Berechtigung, um auf die Datei zuzugreifen");
                    Logger.getLogger(ShowAttachments.class.getName()).log(Level.SEVERE, null, ex);
                }
                else
                {
                    Logger.getLogger(ShowAttachments.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private boolean delDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] entries = dir.list();
            for (int x = 0; x < entries.length; x++)
            {
                File aktFile = new File(dir.getPath(), entries[x]);
                delDir(aktFile);
            }
            if (dir.delete())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (dir.delete())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        File attachmentDirectory = new File(System.getProperty("user.dir") + "/mailaccounts/" + curMail + "/tmp/" + randomID + "/");
        delDir(attachmentDirectory);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
