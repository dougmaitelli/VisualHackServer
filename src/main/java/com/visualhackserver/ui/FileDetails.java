package com.visualhackserver.ui;

import com.visualhackserver.VisualHackServerApp;
import com.visualhackserver.thread.TransferThread;

/**
 *
 * @author DougM
 */
public class FileDetails extends javax.swing.JDialog {
	
	private static final long serialVersionUID = 1L;

    /** Creates new form FileDetails */
    public FileDetails(java.awt.Frame parent, boolean modal, TransferThread transferthread) {
        super(parent, modal);
        initComponents();

        FileNameLabel.setText(FileNameLabel.getText() + " " + transferthread.getFileName());
        FileHashLabel.setText(FileHashLabel.getText() + " " + transferthread.getHashCode());
    }

    private void initComponents() {
        FileNameLabel = new javax.swing.JLabel();
        FileHashLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.visualhackserver.VisualHackServerApp.class).getContext().getResourceMap(FileDetails.class);
        FileNameLabel.setText(resourceMap.getString("FileNameLabel.text")); // NOI18N
        FileNameLabel.setName("FileNameLabel"); // NOI18N

        FileHashLabel.setText(resourceMap.getString("FileHashLabel.text")); // NOI18N
        FileHashLabel.setName("FileHashLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FileNameLabel)
                    .addComponent(FileHashLabel))
                .addContainerGap(281, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(FileNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FileHashLabel)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }

    private javax.swing.JLabel FileHashLabel;
    private javax.swing.JLabel FileNameLabel;

}
