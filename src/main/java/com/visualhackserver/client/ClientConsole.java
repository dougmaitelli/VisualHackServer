package com.visualhackserver.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.visualhackserver.thread.ConsoleThread;

/**
 *
 * @author DougM
 */
public class ClientConsole extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	
	private Client clientStruct;
    private final ConsoleThread consoleThread;
    private JTextArea console;
    private JTextField input;

    public ClientConsole(ConsoleThread thread, Client client) {
        super(client.getName(), true, true, true, true);
        setSize(700, 400);
        setOpaque(true);

        clientStruct = client;
        consoleThread = thread;

        Container c = getContentPane();

        JPanel panel = new JPanel(new BorderLayout());
        c.add(panel, BorderLayout.CENTER);

        console = new JTextArea() {

            @Override
            public void append(String text) {
                super.append(text);
                this.setCaretPosition(this.getCaretPosition() + text.length());
            }
        };
        console.setBackground(Color.BLACK);
        console.setForeground(Color.WHITE);
        console.setFont(new Font("Lucida Console", Font.PLAIN, 12));
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        console.setEditable(false);

        input = new JTextField();
        input.setBackground(Color.BLACK);
        input.setForeground(Color.WHITE);
        input.setFont(new Font("Lucida Console", Font.PLAIN, 12));
        panel.add(input, BorderLayout.SOUTH);

        input.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_ENTER) {
                    JTextField input = getInput();

                    consoleThread.send(input.getText());
                    input.setText("");
                }
            }
        });

        JScrollPane scroller = new JScrollPane(console);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroller, BorderLayout.CENTER);

        addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameClosing(InternalFrameEvent evt) {
                //clientStruct.setActive(false);
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

    public JTextArea getConsole() {
        return console;
    }

    public void write(String str) {
        getConsole().append(str + "\n");
    }

    public JTextField getInput() {
        return input;
    }
}
