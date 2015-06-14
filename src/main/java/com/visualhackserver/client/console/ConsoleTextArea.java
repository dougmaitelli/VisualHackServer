package com.visualhackserver.client.console;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;

public class ConsoleTextArea extends JTextArea {
	
	private static final long serialVersionUID = 1L;
	
	private StringBuilder inputBuffer;
	private String currentCommand = "";
	private String lastEnteredCommand = "";
	
	private OnConsoleEnterPressed onConsoleEnterPressed;
	
	public ConsoleTextArea() {
		final ConsoleTextArea console = this;
		
		inputBuffer = new StringBuilder();
		
		setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setFont(new Font("Lucida Console", Font.PLAIN, 12));
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        
//        DefaultCaret caret = (DefaultCaret) getCaret();
//        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				char keyChar = arg0.getKeyChar();
				
				switch (arg0.getKeyCode()) {
					case KeyEvent.VK_ENTER:
						console.pressEnter();
						break;
					case KeyEvent.VK_BACK_SPACE:
						console.backspace();
						break;
					default:
						if (!Character.isIdentifierIgnorable(keyChar)) {
							console.typeCommand(Character.toString(keyChar));
						}
				}
			}
		});
	}
	
	public static abstract class OnConsoleEnterPressed {
		
		public abstract void onPressed(String command);
	}
	
	public void setOnConsoleEnterPressed(OnConsoleEnterPressed onConsoleEnterPressed) {
		this.onConsoleEnterPressed = onConsoleEnterPressed;
	}
	
	public void pressEnter() {
		lastEnteredCommand = currentCommand;
		currentCommand = "";
		refresh();
		fireOnConsoleEnterPressed();
	}
	
	private void fireOnConsoleEnterPressed() {
		if (onConsoleEnterPressed != null) {
			onConsoleEnterPressed.onPressed(lastEnteredCommand);
		}
	}
	
	public void appendInput(String text) {
		inputBuffer.append(text);
		
		append(text);
	}
	
	public String getCurrentCommand() {
		return currentCommand;
	}
	
	public void setCurrentCommand(String currentCommand) {
		this.currentCommand = currentCommand;
		
		refresh();
	}
	
	public void typeCommand(String command) {
		currentCommand += command;
		
		append(command);
	}
	
	public void backspace() {
		if (currentCommand.length() == 0) {
			return;
		}
		
		currentCommand = currentCommand.substring(0, currentCommand.length() - 1);
		
		refresh();
	}
	
	private void refresh() {
		setText(inputBuffer.toString());
		append(currentCommand);
	}

}
