package src.ui;

import src.ui.controls.SwingWebView;
import src.ui.table.EntityTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import src.JustMail;
import src.ui.table.AttachFile;
import src.ui.table.EMail;
import src.ui.table.ImageTableHeaderRenderer;
import src.ui.table.MyTableCellRenderer;
import src.utils.CloseableUtil;

public class MainMailPanel extends javax.swing.JPanel
{
    public String selectedNode = "";
    public String curRootNode = "";
    final SwingWebView swingWebView = new SwingWebView();
    private final JTree tree;
    public Map<String, String> m_defaultColumnNameMap = new HashMap<String, String>();
    public JTable tblEmails = new JTable();

    private static final String POSTEINGANG = "Posteingang";
    private static final String SPAM = "Junk E-Mails";
    private static final String GESENDET = "Gesendet";
    private static final String PAPIERKORB = "Papierkorb";

    public MainMailPanel()
    {
        initComponents();

        tree = new JTree();
        tree.removeAll();
        tree.setRootVisible(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tree);
        jPanel1.setLayout(new BorderLayout());
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

        jPanel1.removeAll();
        jPanel1.add(scrollPane, BorderLayout.CENTER);

        DefaultTableCellRenderer dtcr = new MyTableCellRenderer();
        tblEmails.setDefaultRenderer(Object.class, dtcr);
        tblEmails.setDefaultRenderer(Date.class, dtcr);
        tblEmails.setDefaultRenderer(Boolean.class, dtcr);
        tblEmails.setAutoCreateRowSorter(true);
        tblEmails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblEmails.setComponentPopupMenu(jPopupMenu1);
        tblEmails.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                handleMailSelectionChange();
            }
        });
        //tblEmails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tblEmails);
        //jScrollPane1.getViewport().getView();

        m_defaultColumnNameMap.put("m_subject", "Betreff");
        m_defaultColumnNameMap.put("m_date", "Datum");
        m_defaultColumnNameMap.put("m_sender", "Absender");
        m_defaultColumnNameMap.put("m_recipient", "Empfänger");
        m_defaultColumnNameMap.put("hasAttachments", "attach");
        m_defaultColumnNameMap.put("isContact", "contact");

        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(swingWebView, BorderLayout.CENTER);

        buildTree();
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
            txtSenderRecipient.setText("Absender: " + from);
        }
        else
        {
            txtSenderRecipient.setText("");
        }
    }

    private void setTo(String to)
    {
        if (!to.equals(""))
        {
            txtSenderRecipient.setText("Empfänger: " + to);
        }
        else
        {
            txtSenderRecipient.setText("");
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

    public void refreshTree()
    {
        buildTree();
    }

    private void buildTree()
    {
        File dir = new File(System.getProperty("user.dir") + "/mailaccounts/");
        String[] mailAccountNames = dir.list();
        if (mailAccountNames == null)
        {
            System.exit(0);
        }
        else
        {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
            for (String mailAccountName : mailAccountNames)
            {

                DefaultMutableTreeNode mailAdr = new DefaultMutableTreeNode(mailAccountName);
                DefaultMutableTreeNode mailsInbox = new DefaultMutableTreeNode(POSTEINGANG);
                DefaultMutableTreeNode mailsSpam = new DefaultMutableTreeNode(SPAM);
                DefaultMutableTreeNode mailsTrash = new DefaultMutableTreeNode(PAPIERKORB);
                DefaultMutableTreeNode mailsSent = new DefaultMutableTreeNode(GESENDET);
                root.add(mailAdr);
                mailAdr.add(mailsInbox);
                mailsInbox.add(mailsSpam);
                mailAdr.add(mailsTrash);
                mailAdr.add(mailsSent);
            }

            tree.setModel(new DefaultTreeModel(root));
            for (int i = 0; i < tree.getRowCount(); i++)
            {
                if (!tree.getPathForRow(i).getLastPathComponent().toString().equals("Posteingang"))
                    tree.expandRow(i);
            }
            
            tree.setCellRenderer(new DefaultTreeCellRenderer()
            {
                private ImageIcon inboxIcon = new ImageIcon(getClass().getResource("/img/inbox.png"));
                private ImageIcon spamIcon = new ImageIcon(getClass().getResource("/img/junk.png"));
                private ImageIcon trashIcon = new ImageIcon(getClass().getResource("/img/trash.png"));
                private ImageIcon sentIcon = new ImageIcon(getClass().getResource("/img/sent.png"));
                private ImageIcon folderIcon = new ImageIcon(getClass().getResource("/img/folder.png"));

                @Override
                public Component getTreeCellRendererComponent(JTree jtree,
                        Object value, boolean selected, boolean expanded,
                        boolean isLeaf, int row, boolean focused)
                {
                    Component c = super.getTreeCellRendererComponent(jtree, value,
                            selected, expanded, isLeaf, row, focused);
                    if (value.toString().equals("Posteingang"))
                    {
                        setIcon(inboxIcon);
                    }
                    else if (value.toString().equals("Junk E-Mails"))
                    {
                        setIcon(spamIcon);
                    }
                    else if (value.toString().equals("Papierkorb"))
                    {
                        setIcon(trashIcon);
                    }
                    else if (value.toString().equals("Gesendet"))
                    {
                        setIcon(sentIcon);
                    }
                    else
                    {
                        setIcon(folderIcon);
                    }
                    return c;
                }
            });

            tree.setSelectionRow(1);
        }
    }

    private void handleMailSelectionChange()
    {
        if (tblEmails.getSelectedRow() < 0)
        {
            return;
        }

        
        
        int selRow = tblEmails.convertRowIndexToModel(tblEmails.getSelectedRow());
        EntityTableModel mtm = (EntityTableModel) tblEmails.getModel();
        EMail selMail = mtm.getMessage(selRow);

        String type = selMail.getContentTypeFull();

        
        if (selectedNode == "Papierkorb")
            btnMoveToInbox.setEnabled(true);
        else
            btnMoveToInbox.setEnabled(false);
        
        if (type.startsWith("multipart"))
        {
            try
            {
                btnShowAttachments.setEnabled(true);
                btnReply.setEnabled(true);
                btnForward.setEnabled(true);
                btnRemove.setEnabled(true);
                String multiPartContent = selMail.getMultiPartText(selMail.getMessage());
                openMailAttachments(multiPartContent, selMail);
            }
            catch (MessagingException | IOException ex)
            {
                Logger.getLogger(MainMailPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            btnShowAttachments.setEnabled(false);
            btnReply.setEnabled(true);
            btnForward.setEnabled(true);
            btnRemove.setEnabled(true);
            openMailNoAttachments(selMail);
        }
    }

    public void handleTreeSelectionChange()
    {
        if (tree.getSelectionCount() != 1)
        {
            JustMail.mainForm.setTitle("justMail");
            return;
        }

        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        selectedNode = tree.getSelectionPath().getLastPathComponent().toString();
        curRootNode = selNode.getParent().toString();

        if (!selectedNode.equals(POSTEINGANG) && !selectedNode.equals(PAPIERKORB) && !selectedNode.equals(GESENDET))
        {
            // EMAIL SELEKTIERT, NICHTS ANZEIGEN
            JustMail.mainForm.setTitle(selectedNode + " - justMail");
            btnReply.setEnabled(false);
            btnForward.setEnabled(false);
            btnShowAttachments.setEnabled(false);
            btnRemove.setEnabled(false);
            btnMoveToInbox.setEnabled(false);
            setSubject("");
            setFrom("");
            setDate(null);
            tblEmails.clearSelection();
            swingWebView.openContent("");
        }
        else
        {
            DefaultMutableTreeNode selNode2 = (DefaultMutableTreeNode) selNode.getParent();
            String mailFolderName = selNode2.toString();

            File mailFolder = null;
            if (selectedNode.equals(POSTEINGANG))
            {
                try {
                JustMail.mainForm.setTitle(selectedNode  +  " - justMail");
                } catch (Exception ex) {}
                
                menuItemDelete.setText("E-Mail in Papierkorb verschieben");
                menuItemMoveToInbox.setEnabled(false);
                tblEmails.removeAll();
                setUpInboxTrash();
                mailFolder = new File(System.getProperty("user.dir") + "/mailaccounts/" + mailFolderName + "/inbox/");
            }
            else if (selectedNode.equals(PAPIERKORB))
            {
                JustMail.mainForm.setTitle(selectedNode  +  " - justMail");
                menuItemDelete.setText("E-Mail lokal löschen");
                menuItemMoveToInbox.setEnabled(true);
                tblEmails.removeAll();
                setUpInboxTrash();
                mailFolder = new File(System.getProperty("user.dir") + "/mailaccounts/" + mailFolderName + "/trash/");
            }
            else if (selectedNode.equals(GESENDET))
            {
                JustMail.mainForm.setTitle(selectedNode  +  " - justMail");
                menuItemDelete.setText("E-Mail lokal löschen");
                menuItemMoveToInbox.setEnabled(false);
                tblEmails.removeAll();
                setUpSent();
                mailFolder = new File(System.getProperty("user.dir") + "/mailaccounts/" + mailFolderName + "/sent/");
            }

            if (mailFolder != null && mailFolder.exists() && mailFolder.list().length > 0)
            {
                for (File mail : mailFolder.listFiles())
                {
                    FileInputStream fis = null;
                    try
                    {
                        fis = new FileInputStream(mail);
                        MimeMessage mime = new MimeMessage(null, fis);
                        EMail obj = new EMail(mime, mail.getAbsolutePath());
                        addEmailToTable(obj);
                    }
                    catch (FileNotFoundException | MessagingException ex)
                    {
                        Logger.getLogger(MainMailPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    CloseableUtil.close(fis);
                }
                sortTableByDate();
                System.out.println("E-Mails im Ordner " + mailFolderName + " - " + selectedNode + " gelistet (" + mailFolder.list().length + ")");
            }
            else
            {
                System.out.println("Keine E-Mails im Ordner " + mailFolderName + " - " + selectedNode + " gefunden");
            }
        }
    }

    public void sortTableByDate() {
        int idx = tblEmails.getColumnModel().getColumnIndex(m_defaultColumnNameMap.get("m_date"));
        List<RowSorter.SortKey> skeys = new ArrayList<RowSorter.SortKey>();
        RowSorter.SortKey sk = new RowSorter.SortKey(idx, SortOrder.DESCENDING);
        skeys.add(sk);
        tblEmails.getRowSorter().setSortKeys(skeys);
    }    
    
    private void openMailNoAttachments(final EMail selMail)
    {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        swingWebView.setVisible(true);
        swingWebView.openContent(selMail.getInhalt());
        setSubject(selMail.m_subject);
        setFrom(selMail.m_recipient);
        String dateRetStr = null;
        if (selMail.m_date != null)
        {
            dateRetStr = df.format(selMail.m_date);
        }
        setDate(dateRetStr);
    }

    private void openMailAttachments(final String content, final EMail selMail)
    {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        swingWebView.setVisible(true);
        swingWebView.openContent(content);
        setSubject(selMail.m_subject);
        setFrom(selMail.m_recipient);
        String dateRetStr = df.format(selMail.m_date);
        setDate(dateRetStr);
    }

    private void setUpMain()
    {
        swingWebView.setVisible(false);
        btnReply.setEnabled(false);
        btnForward.setEnabled(false);
        btnShowAttachments.setEnabled(false);
        btnRemove.setEnabled(false);
        btnMoveToInbox.setEnabled(false);
        setSubject("");
        setFrom("");
        setDate(null);
    }

    private void setHeaderRendererAttachment(TableColumn column)
    {
        column.setMaxWidth(20);
        column.setResizable(false);
        column.setWidth(20);
        column.setPreferredWidth(20);
        column.setMinWidth(20);
        column.setHeaderRenderer(new ImageTableHeaderRenderer(new ImageIcon(getClass().getResource("/img/attachment.png"))));
    }

    private void setHeaderRendererContact(TableColumn column)
    {
        column.setMaxWidth(20);
        column.setResizable(false);
        column.setWidth(20);
        column.setPreferredWidth(20);
        column.setMinWidth(20);
        column.setHeaderRenderer(new ImageTableHeaderRenderer(new ImageIcon(getClass().getResource("/img/contact.png"))));
    }

    private void setUpSent()
    {
        EntityTableModel mtm = new EntityTableModel();
        mtm.setColumnNames(m_defaultColumnNameMap);
        mtm.setHiddenFields(new String[]
        {
            "m_sender"
        });
        tblEmails.setModel(mtm);

        Object identifierAttachment = m_defaultColumnNameMap.get("hasAttachments");
        TableColumn tcAttachment = tblEmails.getColumn(identifierAttachment);
        setHeaderRendererAttachment(tcAttachment);

        Object identifierContact = m_defaultColumnNameMap.get("isContact");
        TableColumn tcContact = tblEmails.getColumn(identifierContact);
        setHeaderRendererContact(tcContact);

        setUpMain();
    }

    private void setUpInboxTrash()
    {
        EntityTableModel mtm = new EntityTableModel();
        mtm.setColumnNames(m_defaultColumnNameMap);
        mtm.setHiddenFields(new String[]
        {
            "m_recipient"
        });
        tblEmails.setModel(mtm);

        Object identifierAttachment = m_defaultColumnNameMap.get("hasAttachments");
        TableColumn tcAttachment = tblEmails.getColumn(identifierAttachment);
        setHeaderRendererAttachment(tcAttachment);

        Object identifierContact = m_defaultColumnNameMap.get("isContact");
        TableColumn tcContact = tblEmails.getColumn(identifierContact);
        setHeaderRendererContact(tcContact);

        setUpMain();
    }

    public void addEmailToTable(final EMail mail)
    {

        if (SwingUtilities.isEventDispatchThread())
        {
            EntityTableModel model = (EntityTableModel) tblEmails.getModel();
            model.addMessage(mail);
            model.fireTableDataChanged();
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {

                @Override
                public void run()
                {
                    addEmailToTable(mail);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        menuAnswer = new javax.swing.JMenu();
        menuItemAnswer = new javax.swing.JMenuItem();
        menuItemAnswerAll = new javax.swing.JMenuItem();
        menuItemForward = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuItemMoveToInbox = new javax.swing.JMenuItem();
        menuItemDelete = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        btnShowAttachments = new javax.swing.JButton();
        btnForward = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txtSubject = new javax.swing.JLabel();
        txtSenderRecipient = new javax.swing.JLabel();
        txtDate = new javax.swing.JLabel();
        btnReply = new javax.swing.JComboBox();
        btnRemove = new javax.swing.JButton();
        btnMoveToInbox = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        pnlTblEmails = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();

        menuAnswer.setText("Antworten");

        menuItemAnswer.setText("Antworten");
        menuAnswer.add(menuItemAnswer);

        menuItemAnswerAll.setText("Allen antworten");
        menuAnswer.add(menuItemAnswerAll);

        jPopupMenu1.add(menuAnswer);

        menuItemForward.setText("Weiterleiten");
        jPopupMenu1.add(menuItemForward);
        jPopupMenu1.add(jSeparator1);

        menuItemMoveToInbox.setText("Email in Posteingang verschieben");
        menuItemMoveToInbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemMoveToInboxActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuItemMoveToInbox);

        menuItemDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        menuItemDelete.setText("E-Mail lokal löschen");
        menuItemDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemDeleteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuItemDelete);

        jSplitPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        jSplitPane1.setDividerLocation(200);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jSplitPane2.setDividerLocation(120);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setOneTouchExpandable(true);
        jSplitPane2.setPreferredSize(new java.awt.Dimension(173, 65));

        jSplitPane3.setDividerLocation(105);
        jSplitPane3.setDividerSize(0);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane3.setEnabled(false);

        btnShowAttachments.setText("Anhänge zeigen");
        btnShowAttachments.setEnabled(false);
        btnShowAttachments.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnShowAttachmentsActionPerformed(evt);
            }
        });

        btnForward.setText("Weiterleiten");
        btnForward.setEnabled(false);
        btnForward.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnForwardActionPerformed(evt);
            }
        });

        jPanel4.setPreferredSize(new java.awt.Dimension(70, 68));

        txtSubject.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        txtSubject.setText("Betreff:");

        txtSenderRecipient.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        txtSenderRecipient.setText("Absender:");

        txtDate.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        txtDate.setText("Datum:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDate)
                    .addComponent(txtSubject)
                    .addComponent(txtSenderRecipient))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(txtSubject)
                .addGap(3, 3, 3)
                .addComponent(txtSenderRecipient)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDate)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnReply.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Antworten", "Allen antworten" }));
        btnReply.setEnabled(false);

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRemoveActionPerformed(evt);
            }
        });

        btnMoveToInbox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/inbox.png"))); // NOI18N
        btnMoveToInbox.setEnabled(false);
        btnMoveToInbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnMoveToInboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(184, Short.MAX_VALUE)
                .addComponent(btnReply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnForward)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowAttachments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMoveToInbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnMoveToInbox, btnRemove});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnForward, btnReply, btnShowAttachments});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnForward)
                            .addComponent(btnReply, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnShowAttachments))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnMoveToInbox, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnMoveToInbox, btnRemove, btnShowAttachments});

        jSplitPane3.setLeftComponent(jPanel3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());
        jSplitPane3.setRightComponent(jPanel2);

        jSplitPane2.setRightComponent(jSplitPane3);

        pnlTblEmails.setLayout(new java.awt.BorderLayout());
        pnlTblEmails.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane2.setLeftComponent(pnlTblEmails);

        jSplitPane1.setRightComponent(jSplitPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnShowAttachmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAttachmentsActionPerformed
        ShowAttachments neuerFrame = new ShowAttachments();
        EntityTableModel mtm = (EntityTableModel) tblEmails.getModel();
        int selRow = tblEmails.convertRowIndexToModel(tblEmails.getSelectedRow());
        EMail selMail = mtm.getMessage(selRow);
        neuerFrame.loadAttachments(selMail, curRootNode);
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_btnShowAttachmentsActionPerformed

    private void menuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDeleteActionPerformed
        moveDeleteMail();
    }//GEN-LAST:event_menuItemDeleteActionPerformed

    private void moveMailToInbox() {
        if (tblEmails.getSelectedRow() == -1)
        {
            return;
        }
        
        int selRow = tblEmails.convertRowIndexToModel(tblEmails.getSelectedRow());
        EntityTableModel mtm = (EntityTableModel) tblEmails.getModel();
        EMail selMail = mtm.getMessage(selRow);
 
        int dialogResult = JOptionPane.showConfirmDialog(null, "Möchten Sie die selektierte E-Mail (" + selMail.m_subject + ") wirklich in den Papierkorb verschieben?", "E-Mail in den Posteingang verschieben?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION)
        {
            if (selMail.moveMailToInbox())
            {
                //mail verschoben
                System.out.println("E-Mail " + selMail.m_subject + " (" + selectedNode + ") in den Papierkorb verschoben");
                handleTreeSelectionChange();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Die E-Mail " + selMail.m_subject + " kann nicht verschoben werden.", "Meldung", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            //nein gewählt
        }
    }
    
    private void moveDeleteMail() {
        if (tblEmails.getSelectedRow() == -1)
        {
            return;
        }
        int selRow = tblEmails.convertRowIndexToModel(tblEmails.getSelectedRow());
        EntityTableModel mtm = (EntityTableModel) tblEmails.getModel();
        EMail selMail = mtm.getMessage(selRow);
        if (selectedNode.equals(GESENDET))
        {

            int dialogResult = JOptionPane.showConfirmDialog(null, "Möchten Sie die selektierte E-Mail (" + selMail.m_subject + ") wirklich lokal löschen?", "E-Mail wirklich löschen?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION)
            {
                if (selMail.deleteMailLocal())
                {
                    //mail gelöscht
                    System.out.println("E-Mail " + selMail.m_subject + " (" + selectedNode + ") gelöscht");
                    handleTreeSelectionChange();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Die E-Mail (" + selMail.m_subject + ") kann nicht lokal gelöscht werden.", "Meldung", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (selectedNode.equals(POSTEINGANG))
        {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Möchten Sie die selektierte E-Mail (" + selMail.m_subject + ") wirklich in den Papierkorb verschieben?", "E-Mail in den Papierkorb verschieben?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION)
            {
                if (selMail.moveMailToTrash())
                {
                    //mail verschoben
                    System.out.println("E-Mail " + selMail.m_subject + " (" + selectedNode + ") in den Papierkorb verschoben");
                    handleTreeSelectionChange();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Die E-Mail (" + selMail.m_subject + ") kann nicht verschoben werden.", "Meldung", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (selectedNode.equals(PAPIERKORB))
        {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Möchten Sie die selektierte E-Mail (" + selMail.m_subject + ") wirklich lokal löschen?", "E-Mail wirklich löschen?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION)
            {
                if (selMail.deleteMailLocal())
                {
                    //mail gelöscht
                    System.out.println("E-Mail " + selMail.m_subject + " (" + selectedNode + ") gelöscht");
                    handleTreeSelectionChange();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Die E-Mail (" + selMail.m_subject + ") kann nicht gelöscht werden.", "Meldung", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private List<AttachFile> getAttachments(EMail mail, String currentMail) {
        try
        {
            String randomID = UUID.randomUUID().toString();
            Message mimeMail = mail.getMessage();
            List<AttachFile> attachments = new ArrayList<AttachFile>();

            Multipart multipart = (Multipart) mimeMail.getContent();
            // System.out.println(multipart.getCount());

            for (int i = 0; i < multipart.getCount(); i++)
            {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))
                {
                    continue; //nur mit anhängen fortsetzen
                }
                InputStream is = bodyPart.getInputStream();
                File attachmentDirectory = new File(System.getProperty("user.dir") + "/mailaccounts/" + currentMail + "/tmp/" + randomID + "/");
                if (!attachmentDirectory.exists())
                {
                    attachmentDirectory.mkdir();
                }
                File attachmentFile = new File(System.getProperty("user.dir") + "/mailaccounts/" + currentMail + "/tmp/" + randomID + "/" + bodyPart.getFileName());
                FileOutputStream fos = new FileOutputStream(attachmentFile);
                byte[] buf = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buf)) != -1)
                {
                    fos.write(buf, 0, bytesRead);
                }
                CloseableUtil.close(fos);
                CloseableUtil.close(is);
                
                AttachFile attach = new AttachFile(attachmentFile);
                attachments.add(attach);
            }
            return attachments;
        }
        catch (MessagingException ex)
        {
            Logger.getLogger(ShowAttachments.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ShowAttachments.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        catch (IOException ex)
        {
            Logger.getLogger(ShowAttachments.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    private void btnForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForwardActionPerformed
        WriteEmail neuerFrame = new WriteEmail();
        EntityTableModel mtm = (EntityTableModel) tblEmails.getModel();
        int selRow = tblEmails.convertRowIndexToModel(tblEmails.getSelectedRow());
        EMail selMail = mtm.getMessage(selRow);

        if (selMail.hasAttachments)
        {
            try
            {
                List<AttachFile> newList = getAttachments(selMail, curRootNode);
                for (AttachFile atf : newList)
                {
                    File f = new File(atf.m_filePath);
                    neuerFrame.addAttachment(f);
                    
                }
                neuerFrame.content = selMail.getMultiPartText(selMail.getMessage());
            }
            catch (MessagingException ex)
            {
                Logger.getLogger(MainMailPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(MainMailPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            neuerFrame.content = selMail.getInhalt();
        }
        neuerFrame.subject = "FWD: " + selMail.m_subject;
        neuerFrame.to = selMail.m_sender;
        neuerFrame.init();
        neuerFrame.setVisible(true);
    }//GEN-LAST:event_btnForwardActionPerformed
    
    private void menuItemMoveToInboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemMoveToInboxActionPerformed
        moveMailToInbox();
    }//GEN-LAST:event_menuItemMoveToInboxActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRemoveActionPerformed
    {//GEN-HEADEREND:event_btnRemoveActionPerformed
        moveDeleteMail();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnMoveToInboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnMoveToInboxActionPerformed
    {//GEN-HEADEREND:event_btnMoveToInboxActionPerformed
        moveMailToInbox();
    }//GEN-LAST:event_btnMoveToInboxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnForward;
    private javax.swing.JButton btnMoveToInbox;
    private javax.swing.JButton btnRemove;
    private javax.swing.JComboBox btnReply;
    private javax.swing.JButton btnShowAttachments;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JMenu menuAnswer;
    private javax.swing.JMenuItem menuItemAnswer;
    private javax.swing.JMenuItem menuItemAnswerAll;
    private javax.swing.JMenuItem menuItemDelete;
    private javax.swing.JMenuItem menuItemForward;
    private javax.swing.JMenuItem menuItemMoveToInbox;
    private javax.swing.JPanel pnlTblEmails;
    private javax.swing.JLabel txtDate;
    private javax.swing.JLabel txtSenderRecipient;
    private javax.swing.JLabel txtSubject;
    // End of variables declaration//GEN-END:variables
}
