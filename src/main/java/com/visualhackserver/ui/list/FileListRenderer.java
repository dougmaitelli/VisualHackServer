package com.visualhackserver.ui.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.visualhackserver.thread.TransferThread;

/**
 *
 * @author DougM
 */
public class FileListRenderer extends DefaultListCellRenderer {
	
	private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(label.getBackground());
        panel.add(label, BorderLayout.WEST);

        if (value instanceof TransferThread) {
            TransferThread transferThread = (TransferThread) value;

            label.setText(transferThread.getFileName());

            JProgressBar progressBar = new JProgressBar();
            progressBar.setMaximum(Integer.parseInt(String.valueOf(transferThread.getSize())));
            progressBar.setValue(Integer.parseInt(String.valueOf(transferThread.getTotalReceived())));
            progressBar.setStringPainted(true);
            panel.add(progressBar, BorderLayout.EAST);

            switch (transferThread.getStatus()) {
                case TransferThread.COMPLETE:
                    label.setForeground(Color.GREEN);
                    break;
                case TransferThread.ERROR:
                    label.setForeground(Color.red);
                    break;
            }
        }

        return panel;
    }
}
