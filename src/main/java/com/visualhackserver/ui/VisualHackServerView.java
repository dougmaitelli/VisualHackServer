package com.visualhackserver.ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;

import com.visualhackserver.VisualHackServerApp;
import com.visualhackserver.client.Client;
import com.visualhackserver.client.ClientConsole;
import com.visualhackserver.client.FileDetails;
import com.visualhackserver.thread.FileThread;
import com.visualhackserver.thread.ServerThread;
import com.visualhackserver.thread.TransferThread;
import com.visualhackserver.ui.list.ClientListRenderer;
import com.visualhackserver.ui.list.FileListRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * The application's main frame.
 */
public class VisualHackServerView extends FrameView {

    public VisualHackServerView(SingleFrameApplication app) throws IOException {
        super(app);
        
        initComponents();
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        ServerThread.getInstance().start();

        ClientList.setCellRenderer(new ClientListRenderer());
        ClientList.setModel(new DefaultListModel<Client>());

        FileList.setCellRenderer(new FileListRenderer());
        FileList.setModel(new DefaultListModel<TransferThread>());
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = VisualHackServerApp.getApplication().getMainFrame();
            aboutBox = new VisualHackServerAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        VisualHackServerApp.getApplication().show(aboutBox);
    }


    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        ConsolePane = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        ClientList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        FileList = new javax.swing.JList<>();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        FileListMenu = new javax.swing.JPopupMenu();
        FileListMenuCancelFile = new javax.swing.JMenuItem();

        mainPanel.setMaximumSize(new java.awt.Dimension(0, 0));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(640, 480));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.visualhackserver.VisualHackServerApp.class).getContext().getResourceMap(VisualHackServerView.class);
        ConsolePane.setBackground(resourceMap.getColor("ConsolePane.background")); // NOI18N
        ConsolePane.setMaximumSize(new java.awt.Dimension(0, 0));
        ConsolePane.setName("ConsolePane"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        ClientList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ClientList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        ClientList.setMaximumSize(new java.awt.Dimension(150, 0));
        ClientList.setName("ClientList"); // NOI18N
        ClientList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClientListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(ClientList);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        FileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        FileList.setComponentPopupMenu(FileListMenu);
        FileList.setMaximumSize(new java.awt.Dimension(150, 0));
        FileList.setName("FileList"); // NOI18N
        FileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FileListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(FileList);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ConsolePane, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(ConsolePane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.visualhackserver.VisualHackServerApp.class).getContext().getActionMap(VisualHackServerView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 461, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        FileListMenu.setName("FileListMenu"); // NOI18N

        FileListMenuCancelFile.setText(resourceMap.getString("FileListMenuCancelFile.text")); // NOI18N
        FileListMenuCancelFile.setActionCommand(resourceMap.getString("FileListMenuCancelFile.actionCommand")); // NOI18N
        FileListMenuCancelFile.setName("FileListMenuCancelFile"); // NOI18N
        FileListMenuCancelFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FileListMenuCancelFileMouseClicked(evt);
            }
        });
        FileListMenu.add(FileListMenuCancelFile);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }

    private void ClientListMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            int index = ClientList.locationToIndex(evt.getPoint());

            Client client = ServerThread.getInstance().getClients().get(index);

            try {
                client.requestConsole();
            } catch (IOException ex) {
                Logger.getLogger(VisualHackServerView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void FileListMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            int index = FileList.locationToIndex(evt.getPoint());

            TransferThread transferThread = FileThread.getInstance().getFiles().get(index);

            JFrame mainFrame = VisualHackServerApp.getApplication().getMainFrame();
            FileDetails fileDetails = new FileDetails(mainFrame, false, transferThread);
            fileDetails.setLocationRelativeTo(mainFrame);
            VisualHackServerApp.getApplication().show(fileDetails);
        }
    }

    private void FileListMenuCancelFileMouseClicked(java.awt.event.MouseEvent evt) {
        int index = FileList.locationToIndex(evt.getPoint());

        TransferThread transferThread = FileThread.getInstance().getFiles().get(index);

        transferThread.cancel();
    }

    public void addClient(Client client) {
        DefaultListModel<Client> model = (DefaultListModel<Client>) ClientList.getModel();

        model.addElement(client);
    }

    public void removeClient(Client client) {
        DefaultListModel<Client> model = (DefaultListModel<Client>) ClientList.getModel();

        model.removeElement(client);
    }

    public void addConsole(ClientConsole console) {
        ConsolePane.add(console);
    }

    public void addFile(TransferThread transferThread) {
        DefaultListModel<TransferThread> model = (DefaultListModel<TransferThread>) FileList.getModel();

        model.addElement(transferThread);
    }

    public void removeFile(TransferThread transferThread) {
        DefaultListModel<TransferThread> model = (DefaultListModel<TransferThread>) FileList.getModel();

        model.removeElement(transferThread);
    }

    public void updateFile(TransferThread transferThread) {
        DefaultListModel<TransferThread> model = (DefaultListModel<TransferThread>) FileList.getModel();

        model.setElementAt(transferThread, model.indexOf(transferThread));
    }

    private javax.swing.JList<Client> ClientList;
    private javax.swing.JDesktopPane ConsolePane;
    private javax.swing.JList<TransferThread> FileList;
    private javax.swing.JPopupMenu FileListMenu;
    private javax.swing.JMenuItem FileListMenuCancelFile;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
