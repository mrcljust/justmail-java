package src.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import src.JustMail;

public class AddContact extends javax.swing.JDialog
{

    Addressbook adrb;
    CContacts con = new CContacts();

    public AddContact()
    {
        super((JFrame) null, "Kontakt hinzufügen - Adressbuch - justMail", true);
        Image image = Toolkit.getDefaultToolkit().getImage("src/img/icon.png");
        this.setIconImage(image);
        initComponents();
        this.setLocationRelativeTo(null);
    }

    public void setModelOfCb(ComboBoxModel cbm, Addressbook adr)
    {
        cbCat.setModel(cbm);
        adrb = adr;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cbCat = new javax.swing.JComboBox();
        txtMail = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("E-Mail-Adresse:");

        jLabel2.setText("Kategorie:");

        jLabel3.setText("Anzeigename:");

        btnAdd.setText("Kontakt hinzufügen");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtName)
                    .addComponent(txtMail)
                    .addComponent(cbCat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(btnAdd)
                .addGap(73, 73, 73))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(btnAdd)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String mail = txtMail.getText();
        String name = txtName.getText();
        String cat = cbCat.getSelectedItem().toString();

        if (!mail.trim().isEmpty() && !name.trim().isEmpty() && mail.contains("@") && mail.contains("."))
        {
            try
            {
                if (!con.doesContactExist(mail))
                {
                    con.addContact(mail, name, cat);
                    adrb.loadFoldersAndContacts();
                    JustMail.mainForm.mainMailPnl.handleTreeSelectionChange();
                    this.dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Die angegebene E-Mail ist bereits als Kontakt gespeichert", "Meldung", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Der Kontakt konnte nicht hinzugefügt werden\n" + ex.getMessage(), "Meldung", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(AddContact.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            if (mail.trim().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Bitte tragen Sie eine gültige E-Mail-Adresse ein", "Meldung", JOptionPane.ERROR_MESSAGE);
            }
            else if (!mail.contains("@"))
            {   
                JOptionPane.showMessageDialog(null, "Bitte tragen Sie eine gültige E-Mail-Adresse ein", "Meldung", JOptionPane.ERROR_MESSAGE);
            }
            else if (!mail.contains("."))
            {
                JOptionPane.showMessageDialog(null, "Bitte tragen Sie eine gültige E-Mail-Adresse ein", "Meldung", JOptionPane.ERROR_MESSAGE);
            }

            if (name.trim().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Bitte tragen Sie einen Anzeigenamen ein", "Meldung", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnAddActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JComboBox cbCat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
