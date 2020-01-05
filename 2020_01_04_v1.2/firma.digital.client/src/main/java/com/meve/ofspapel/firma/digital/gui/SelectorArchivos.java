package com.meve.ofspapel.firma.digital.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class SelectorArchivos implements ActionListener {
	
	final Map<String, Object> modelo;
	
	public SelectorArchivos( Map<String, Object> modelo) {
		super();
		this.modelo = modelo;
	}


	public void actionPerformed(ActionEvent e) {
		JFileChooser browser = new JFileChooser();
		
		int returnValue = browser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = browser.getSelectedFile();
          ((JTextField) modelo.get( e.getActionCommand())).setText( selectedFile.getAbsolutePath());
        }
	}
}
