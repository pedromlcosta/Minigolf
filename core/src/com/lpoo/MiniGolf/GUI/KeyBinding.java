package com.lpoo.MiniGolf.GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class KeyBinding extends JDialog implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int keyToBeRead;
	String letter;
	JLabel newKeyLabel;

	public KeyBinding(int Key) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		this.setBounds(0, 0, 400, 150);
		this.setFocusable(true);
		setName("Key Binding");
		addKeyListener(this);

		newKeyLabel = new JLabel("Enter the new key");
		newKeyLabel.setBounds(92, 40, 189, 26);
		getContentPane().add(newKeyLabel);
		setModal(true);
		keyToBeRead = Key;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		keyToBeRead = arg0.getKeyCode();
		letter = KeyEvent.getKeyText(keyToBeRead);
		this.setVisible(false);
		this.setFocusable(false);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
