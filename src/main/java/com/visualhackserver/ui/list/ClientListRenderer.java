package com.visualhackserver.ui.list;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.visualhackserver.client.Client;

/**
 *
 * @author DougM
 */
public class ClientListRenderer extends DefaultListCellRenderer {
	
	private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);

        if (value instanceof Client) {
            Client client = (Client) value;

            label.setText(client.getName());
        }

        return label;
    }
}
