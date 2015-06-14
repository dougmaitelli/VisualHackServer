package com.visualhackserver.client.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.visualhackserver.client.Client;
import com.visualhackserver.thread.ConsoleThread;

/**
 *
 * @author DougM
 */
public class ClientConsole extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	
	private Client clientStruct;
    private final ConsoleThread consoleThread;
    
    private JScrollPane scroller;
    private ConsoleTextArea console;

    public ClientConsole(ConsoleThread thread, Client client) {
        super(client.getName(), true, true, true, true);
        setSize(700, 400);
        setOpaque(true);

        clientStruct = client;
        consoleThread = thread;

        Container c = getContentPane();

        JPanel panel = new JPanel(new BorderLayout());
        c.add(panel, BorderLayout.CENTER);

        console = new ConsoleTextArea();
        console.setOnConsoleEnterPressed(new ConsoleTextArea.OnConsoleEnterPressed() {
			
			@Override
			public void onPressed(String command) {
				consoleThread.send(command);
			}
		});

        scroller = new JScrollPane(console);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroller, BorderLayout.CENTER);

        addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameClosing(InternalFrameEvent evt) {
            	if (!consoleThread.isInterrupted()) {
            		consoleThread.interrupt();
            	}
            }

            public void internalFrameOpened(InternalFrameEvent e) {
            }

            public void internalFrameClosed(InternalFrameEvent e) {
            }

            public void internalFrameIconified(InternalFrameEvent e) {
            }

            public void internalFrameDeiconified(InternalFrameEvent e) {
            }

            public void internalFrameActivated(InternalFrameEvent e) {
            }

            public void internalFrameDeactivated(InternalFrameEvent e) {
            }
        });

        show();
    }

    public void write(String str) {
        console.appendInput(str);
    }

}
