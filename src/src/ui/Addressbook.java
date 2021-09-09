package src.ui;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import src.JustMail;

public class Addressbook extends javax.swing.JDialog
{

    private final JTree tree;
    private Properties loadedProps;
    private String curSelMail;
    CContacts con = new CContacts();

    public Addressbook()
    {
        super((JFrame) null, "Adressbuch - justMail", true);
        //super((JFrame) null, "Adressbuch - justMail", true);
        Image image = Toolkit.getDefaultToolkit().getImage("src/img/icon.png");
        this.setIconImage(image);
        initComponents();
        tree = new JTree();
        tree.removeAll();
        this.setLocationRelativeTo(null);
        loadFoldersAndContacts();
        tree.setRootVisible(false);
        TreeSelectionListener l = new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                handleTreeSelectionChange();
            }
        };
        tree.addTreeSelectionListener(l);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        pnlTree.add(tree);
    }

    public void loadFoldersAndContacts()
    {
        try
        {
            tree.removeAll();
            cbCat.removeAllItems();
            List<String> folders = con.getCategories();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
            for (String folder : folders)
            {
                DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(folder.replace(".cfg", ""));
                cbCat.addItem(folder.replace(".cfg", ""));
                System.out.println(folder.replace(".cfg", ""));
                File contactsDir = new File(System.getProperty("user.dir") + "/contacts/");
                for (File aFile : contactsDir.listFiles())
                {
                    if (!aFile.getAbsolutePath().contains("folder"))
                    {
                        String contactName = aFile.getName().replace(".cfg", "");
                        Properties props = con.loadContact(contactName);
                        String folderOfContact = props.getProperty("FOLDER");
                        if (folder.replace(".cfg", "").equals(folderOfContact))
                        {
                            folderNode.add(new DefaultMutableTreeNode(contactName));
                        }
                    }
                }
                //if (folderNode.getChildCount() > 0) {
                root.add(folderNode);
                //}
            }
            tree.setCellRenderer(new DefaultTreeCellRenderer()
            {
                private ImageIcon contactIcon = new ImageIcon(getClass().getResource("/img/contact.png"));
                private ImageIcon folderIcon = new ImageIcon(getClass().getResource("/img/folder.png"));

                @Override
                public Component getTreeCellRendererComponent(JTree tree,
                        Object value, boolean selected, boolean expanded,
                        boolean isLeaf, int row, boolean focused)
                {
                    Component c = super.getTreeCellRendererComponent(tree, value,
                            selected, expanded, isLeaf, row, focused);
                    if (folders.contains(value + ".cfg"))
                    {
                        setIcon(folderIcon);
                    }
                    else
                    {
                        setIcon(contactIcon);
                    }
                    return c;
                }
            });
            tree.setModel(new DefaultTreeModel(root));
            for (int i = 0; i < tree.getRowCount(); i++)
            {
                tree.expandRow(i);
            }

            tree.setSelectionRow(1);
            handleTreeSelectionChange();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleTreeSelectionChange()
    {
        //System.out.println(tree.getSelectionPath().getPathCount());  2 = kategorie 3 = kontakt
        if (tree.getSelectionPath() == null)
        {
            btnRemoveCategory.setEnabled(false);
            btnEditCategory.setEnabled(false);
            btnRemoveContact.setEnabled(false);
            setPanelInvisible();
            return;
        }

        if (tree.getSelectionPath().getPathCount() == 3)
        {
            //kontakt
            setPanelVisible();
            resetPanel();
            curSelMail = tree.getSelectionPath().getLastPathComponent().toString();
            btnRemoveCategory.setEnabled(false);
            btnEditCategory.setEnabled(false);
            btnRemoveContact.setEnabled(true);
            try
            {
                Properties props = con.loadContact(curSelMail);
                String folderOfContact = props.getProperty("FOLDER");
                String nameOfContact = props.getProperty("NAME");
                txtEmail.setText(curSelMail);
                cbCat.setSelectedItem(folderOfContact);
                txtName.setText(nameOfContact);
            }
            catch (Exception ex)
            {
                Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if (tree.getSelectionPath().getPathCount() == 2)
        {
            //kategorie
            setPanelInvisible();
            if (!tree.getSelectionPath().getLastPathComponent().toString().equals("Allgemein"))
            {
                btnRemoveContact.setEnabled(false);
                btnRemoveCategory.setEnabled(true);
                btnEditCategory.setEnabled(true);
            }
            else //ordner allgemein
            {
                btnRemoveContact.setEnabled(false);
                btnRemoveCategory.setEnabled(false);
                btnEditCategory.setEnabled(false);
            }
        }
        else if (tree.getSelectionPath().getPathCount() == 1)
        {
            //keine kontakte
            setPanelInvisible();
            btnRemoveContact.setEnabled(false);
            btnRemoveCategory.setEnabled(false);
            btnEditCategory.setEnabled(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnlTree = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        cbCat = new javax.swing.JComboBox();
        txtName = new javax.swing.JTextField();
        btnEditMail = new javax.swing.JButton();
        btnEditCat = new javax.swing.JButton();
        btnEditName = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnAddCategory = new javax.swing.JButton();
        btnRemoveCategory = new javax.swing.JButton();
        btnAddContact = new javax.swing.JButton();
        btnRemoveContact = new javax.swing.JButton();
        btnEditCategory = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(null);
        setMinimumSize(null);
        setPreferredSize(new java.awt.Dimension(640, 405));
        setResizable(false);

        jSplitPane2.setDividerLocation(330);
        jSplitPane2.setDividerSize(1);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setEnabled(false);

        jSplitPane1.setDividerLocation(230);

        pnlTree.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlTree.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(pnlTree);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("E-Mail-Adresse:");

        jLabel3.setText("Kategorie:");

        jLabel4.setText("Anzeigename:");

        txtEmail.setText("...");
        txtEmail.setEnabled(false);

        cbCat.setEnabled(false);

        txtName.setText("...");
        txtName.setEnabled(false);

        btnEditMail.setText("E");
        btnEditMail.setEnabled(false);

        btnEditCat.setText("E");
        btnEditCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCatActionPerformed(evt);
            }
        });

        btnEditName.setText("E");
        btnEditName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(jLabel2))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbCat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                    .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEditMail, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEditName, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnEditCat)
                        .addGap(10, 10, 10))))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtEmail, txtName});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditMail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditName)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditCat)
                    .addComponent(cbCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(233, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        jSplitPane2.setTopComponent(jSplitPane1);

        btnAddCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/addfolder.png"))); // NOI18N
        btnAddCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCategoryActionPerformed(evt);
            }
        });

        btnRemoveCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/removefolder.png"))); // NOI18N
        btnRemoveCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveCategoryActionPerformed(evt);
            }
        });

        btnAddContact.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/addcontact.png"))); // NOI18N
        btnAddContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddContactActionPerformed(evt);
            }
        });

        btnRemoveContact.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/removecontact.png"))); // NOI18N
        btnRemoveContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveContactActionPerformed(evt);
            }
        });

        btnEditCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editfolder.png"))); // NOI18N
        btnEditCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCategoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddContact)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveContact)
                .addGap(294, 294, 294))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditCategory)
                    .addComponent(btnAddCategory)
                    .addComponent(btnAddContact)
                    .addComponent(btnRemoveContact)
                    .addComponent(btnRemoveCategory))
                .addGap(16, 16, 16))
        );

        jSplitPane2.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setPanelInvisible()
    {
        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
        jLabel4.setVisible(false);
        txtEmail.setVisible(false);
        txtName.setVisible(false);
        cbCat.setVisible(false);
        btnEditMail.setVisible(false);
        btnEditCat.setVisible(false);
        btnEditName.setVisible(false);
    }

    private void setPanelVisible()
    {
        jLabel2.setVisible(true);
        jLabel3.setVisible(true);
        jLabel4.setVisible(true);
        txtEmail.setVisible(true);
        txtName.setVisible(true);
        cbCat.setVisible(true);
        btnEditMail.setVisible(true);
        btnEditCat.setVisible(true);
        btnEditName.setVisible(true);
    }

    private void btnAddCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCategoryActionPerformed
        try
        {
            String folderName = JOptionPane.showInputDialog(null, "Bitte tragen Sie einen Namen für die neue Kategorie ein:", "Neue Kategorie hinzufügen", JOptionPane.OK_CANCEL_OPTION);
            if (folderName == null) //abbrechen geklickt
                return;
            if (folderName.trim().isEmpty()) //keinen namen eingegeben, ok geklickt
            {
                JOptionPane.showMessageDialog(null, "Bitte tragen Sie einen Namen für die neue Kategorie ein", "Meldung", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!con.doesCategoryExist(folderName))
            {
                //kategorie existiert nicht, lege an
                con.createCategory(folderName);
                loadFoldersAndContacts();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Es existiert bereits eine Kategorie unter dem Namen " + folderName, "Meldung", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddCategoryActionPerformed

    private void btnRemoveContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveContactActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Möchten Sie den selektierten Kontakt wirklich entfernen?", "Kontakt entfernen?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION)
        {
            try
            {
                con.removeContact(curSelMail);
                loadFoldersAndContacts();
                JustMail.mainForm.mainMailPnl.handleTreeSelectionChange();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Der selektierte Kontakt konnte nicht entfernt werden\n" + ex.getMessage(), "Meldung", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            //nein selektiert
        }
    }//GEN-LAST:event_btnRemoveContactActionPerformed

    private void btnRemoveCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveCategoryActionPerformed
        String folder = tree.getSelectionPath().getLastPathComponent().toString();
        
        int dialogResult = JOptionPane.showConfirmDialog(null, "Möchten Sie die selektierte Kategorie wirklich entfernen?", "Kategorie entfernen?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION)
        {
            List<String> contactsList = getContactsInDir();
            if (contactsList != null)
            {
                int dialogResult2 = JOptionPane.showConfirmDialog(null, "Möchten Sie die Kontakte, die der Kategorie angehören, in die Kategorie Allgemein verschieben? Ansonsten werden diese ebenfalls gelöscht", "Kontakte verschieben?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (dialogResult2 == JOptionPane.YES_OPTION)
                {
                    //kontakte in allgemein verschieben
                    for (String aContact : contactsList)
                    {
                        try
                        {
                            Properties props = con.loadContact(aContact);
                            String mail = aContact;
                            String name = props.getProperty("NAME");
                            con.removeContact(aContact);
                            con.addContact(mail, name, "Allgemein");
                        }
                        catch (Exception ex)
                        {
                            Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    removeCategory(folder);
                }
                else
                {
                    //kontakte mitlöschen
                    for (String aContact : contactsList)
                    {
                        try
                        {
                            con.removeContact(aContact);
                        }
                        catch (Exception ex)
                        {
                            Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    removeCategory(folder);
                }
            }
            else
            {
                removeCategory(folder);
            }
        }
        else
        {
            //nein selektiert
        }
    }//GEN-LAST:event_btnRemoveCategoryActionPerformed

    private void removeCategory(String folder)
    {
        try
        {
            con.removeCategory(folder);
            loadFoldersAndContacts();
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Die Kategorie konnte nicht entfernt werden\n" + ex.getMessage(), "Meldung", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void btnAddContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddContactActionPerformed
        AddContact neuerFrame = new AddContact();
        neuerFrame.setModelOfCb(cbCat.getModel(), this);
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_btnAddContactActionPerformed

    private void btnEditNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditNameActionPerformed
        if (btnEditName.getText().equals("E"))
        {
            txtName.setEnabled(true);
            btnEditName.setText("S");
        }
        else if (btnEditName.getText().equals("S"))
        {
            if (!String.valueOf(txtName.getText().trim()).equals(""))
            {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Möchten Sie den Anzeigenamen wirklich ändern?", "Passwort ändern?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (dialogResult == JOptionPane.YES_OPTION)
                {
                    try
                    {
                        con.removeContact(curSelMail);
                        con.addContact(curSelMail, txtName.getText(), cbCat.getSelectedItem().toString());
                        loadFoldersAndContacts();
                    }
                    catch (Exception ex)
                    {
                        Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    //nein selektiert
                }
                txtName.setEnabled(false);
                btnEditName.setText("E");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Bitte tragen Sie einen Anzeigenamen ein", "Meldung", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEditNameActionPerformed

    private void btnEditCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditCatActionPerformed
        if (btnEditCat.getText().equals("E"))
        {
            cbCat.setEnabled(true);
            btnEditCat.setText("S");
        }
        else if (btnEditCat.getText().equals("S"))
        {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Möchten Sie die Kategorie wirklich ändern?", "Kategorie ändern?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION)
            {
                try
                {
                    con.removeContact(curSelMail);
                    con.addContact(curSelMail, txtName.getText(), cbCat.getSelectedItem().toString());
                    loadFoldersAndContacts();
                }
                catch (Exception ex)
                {
                    Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                //nein selektiert
            }
            cbCat.setEnabled(false);
            btnEditCat.setText("E");
        }
    }//GEN-LAST:event_btnEditCatActionPerformed

    private void btnEditCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditCategoryActionPerformed
        String curFolder = tree.getSelectionPath().getLastPathComponent().toString();
        try
        {
            String folderName = JOptionPane.showInputDialog(null, "Bitte geben Sie den neuen Namen für die selektierte Kategorie ein:", "Kategorie umbenennen", JOptionPane.OK_CANCEL_OPTION);
            if (folderName == null) //abbrechen geklickt
                return;
            if (folderName.trim().isEmpty()) //nichts eingegeben & ok geklickt
                return;
            
            if (!con.doesCategoryExist(folderName))
            {
                //kategorie existiert nicht, lege an
                List<String> contactsList = getContactsInDir();
                
                if (contactsList != null)
                {
                    int dialogResult2 = JOptionPane.showConfirmDialog(null, "Möchten Sie die Kontakte, die der Kategorie angehören, ebenfalls verschieben? Ansonsten werden diese in die Kategorie Allgemein verschoben", "Kontakte verschieben?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (dialogResult2 == JOptionPane.YES_OPTION)
                        {
                            //kontakte mitverschieben
                            moveContactsToAnotherCat(contactsList, folderName);
                        }
                        else
                        {
                            //kontakte in allgemein verschieben
                            moveContactsToAnotherCat(contactsList, "Allgemein");
                        }
                }
                else
                {
                }
                con.createCategory(folderName);
                removeCategory(curFolder);
                    loadFoldersAndContacts();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Es existiert bereits eine Kategorie unter dem Namen " + folderName, "Meldung", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnEditCategoryActionPerformed

    private void moveContactsToAnotherCat(List<String> contactsList, String cat)
    {
        for (String aContact : contactsList)
        {
            try
            {
                Properties props = con.loadContact(aContact);
                String mail = aContact;
                String name = props.getProperty("NAME");
                con.removeContact(aContact);
                con.addContact(mail, name, cat);
            }
            catch (Exception ex)
            {
                Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private List<String> getContactsInDir()
    {
        List<String> contactsList = null;
        File contactsDir = new File(System.getProperty("user.dir") + "/contacts/");
        String folder = tree.getSelectionPath().getLastPathComponent().toString();
        for (File aFile : contactsDir.listFiles())
        {
            if (!aFile.getAbsolutePath().contains("folder"))
            {
                try
                {
                    String contactName = aFile.getName().replace(".cfg", "");
                    Properties props = con.loadContact(contactName);
                    String folderOfContact = props.getProperty("FOLDER");
                    if (folder.equals(folderOfContact))
                    {
                        if (contactsList == null)
                            contactsList = new ArrayList<String>();
                        contactsList.add(aFile.getName().replace(".cfg", ""));
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger(Addressbook.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return contactsList;
    }
    
    private void resetPanel()
    {
        jLabel2.setEnabled(false);
        jLabel3.setEnabled(false);
        jLabel4.setEnabled(false);
        txtEmail.setEnabled(false);
        txtName.setEnabled(false);
        cbCat.setEnabled(false);
        btnEditMail.setText("E");
        btnEditCat.setText("E");
        btnEditName.setText("E");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCategory;
    private javax.swing.JButton btnAddContact;
    private javax.swing.JButton btnEditCat;
    private javax.swing.JButton btnEditCategory;
    private javax.swing.JButton btnEditMail;
    private javax.swing.JButton btnEditName;
    private javax.swing.JButton btnRemoveCategory;
    private javax.swing.JButton btnRemoveContact;
    private javax.swing.JComboBox cbCat;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JPanel pnlTree;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
