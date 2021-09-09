package src.ui;

import src.ui.controls.SwingWebView;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import src.ui.table.EMail;

public class EmailDisplayer extends javax.swing.JDialog {

    final SwingWebView swingWebView = new SwingWebView();
    EMail mailOpened = null;
    
    public EmailDisplayer() 
    {
        super((JFrame)null, "Viewer", true);
        
        Image image = Toolkit.getDefaultToolkit().getImage("src/img/icon.png");
        this.setIconImage(image);
        initComponents();
        //swingWebView.setPreferredSize(new Dimension(200, 200));
        txtSender.setVisible(false);
        txtRecipient.setVisible(false);
        txtDate.setVisible(false);
        this.add(swingWebView, BorderLayout.CENTER);
        this.setSize(new Dimension(640, 480));
        this.setLocationRelativeTo(null);
    }
    
    public void loadMail(EMail mail)
    {
        openMailNoAttachments(mail);
    }
    
    public void openMailNoAttachments(final EMail selMail)
    {
        swingWebView.setVisible(true);
        swingWebView.openContent(selMail.getInhalt());
        buildForm(selMail);
    }
    
    public void openMailAttachments(final String content, final EMail selMail)
    {
        swingWebView.setVisible(true);
        swingWebView.openContent(content);
        buildForm(selMail);
    }
    
    private void buildForm(EMail selMail) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        mailOpened = selMail;
        this.setTitle(selMail.m_subject + " - justMail");
        setSubject(selMail.m_subject);
        setFrom(selMail.m_sender);
        setDate(df.format(selMail.m_date));
        setTo(selMail.m_recipient);
    }
    
    private void setSubject(String subject)
    {
        if (!subject.equals(""))
        {
            txtSubject.setText("Betreff: " + subject);
            txtSubject.setFont(txtSubject.getFont().deriveFont(Font.BOLD));
        }
        else
        {
            txtSubject.setText("");
        }
    }

    private void setFrom(String from)
    {
        if (!from.equals(""))
        {
            txtSender.setText("Absender: " + from);
        }
        else
        {
            txtSender.setText("");
        }
    }
    
    private void setTo(String to)
    {
        if (!to.equals(""))
        {
            txtRecipient.setText("Empfänger: " + to);
        }
        else
        {
            txtRecipient.setText("");
        }
    }

    private void setDate(String date)
    {
        if (date != null)
        {
            txtDate.setText("Datum: " + date);
        }
        else
        {
            txtDate.setText("");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        txtSubject = new javax.swing.JLabel();
        txtSender = new javax.swing.JLabel();
        txtDate = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtRecipient = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(50, 50));
        addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                formPropertyChange(evt);
            }
        });

        jPanel1.setMinimumSize(new java.awt.Dimension(0, 50));
        jPanel1.setPreferredSize(new java.awt.Dimension(707, 70));

        txtSubject.setText("Betreff:");

        txtSender.setText("Absender:");

        txtDate.setText("Datum:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Antworten", "Allen Antworten" }));
        jComboBox1.setMinimumSize(new java.awt.Dimension(109, 23));
        jComboBox1.setPreferredSize(new java.awt.Dimension(109, 23));

        jButton1.setText("Weiterleiten");

        jButton2.setText("Anhänge zeigen");

        txtRecipient.setText("Empfänger:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtSubject)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSender)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRecipient)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDate)))
                .addContainerGap(267, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jComboBox1});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSubject)
                    .addComponent(txtSender)
                    .addComponent(txtDate)
                    .addComponent(txtRecipient))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(14, 14, 14))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jComboBox1});

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_formPropertyChange
    {//GEN-HEADEREND:event_formPropertyChange
        swingWebView.revalidate();
    }//GEN-LAST:event_formPropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txtDate;
    private javax.swing.JLabel txtRecipient;
    private javax.swing.JLabel txtSender;
    private javax.swing.JLabel txtSubject;
    // End of variables declaration//GEN-END:variables
}
