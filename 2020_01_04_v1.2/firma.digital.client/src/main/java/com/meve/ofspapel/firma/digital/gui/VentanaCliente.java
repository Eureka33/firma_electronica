package com.meve.ofspapel.firma.digital.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.meve.ofspapel.firma.digital.client.ClienteFirmaElectronica;

public class VentanaCliente {
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
    	final Map<String, Object> modelo = new HashMap<String, Object>();

    	final ActionListener FILE_BROWSER_ACTION_LISTENER = new SelectorArchivos( modelo);
    	final ActionListener FIRMA_ELECTRONICA_ACTION_LISTENER = new ClienteFirmaElectronica( modelo);
    	
    	//Create and set up the window.
        JFrame frame = new JFrame("Firma Electrónica: MEVE Soluciones");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container pane = frame.getContentPane();
        
        // Crea los componentes gráficos
        JPanel labelPanel = new JPanel(new GridLayout(6, 1));
        labelPanel.setBorder( new EmptyBorder(10, 10, 10, 0));
        
        JPanel fieldPanel = new JPanel(new GridLayout(6, 1));
        fieldPanel.setBorder( new EmptyBorder(10, 0, 10, 10));
        
        pane.add(labelPanel, BorderLayout.WEST);
        pane.add(fieldPanel, BorderLayout.CENTER);
        

        //---
        JTextField pathArchivo = new JTextField();
        pathArchivo.setColumns( 20);
        pathArchivo.setToolTipText( "Seleccione el archivo que desea firmar");
        pathArchivo.setEditable( false);
        
        JButton browseArchivo = new JButton( "Buscar");
        browseArchivo.setSize( browseArchivo.getSize().width,(int)(0.8 * browseArchivo.getSize().height));
        browseArchivo.setActionCommand( "archivo");
        modelo.put( "archivo", pathArchivo);
        browseArchivo.addActionListener( FILE_BROWSER_ACTION_LISTENER);
        
        JLabel etiqueta = new JLabel( "Documento: ", JLabel.RIGHT);
        etiqueta.setLabelFor( pathArchivo);
           
        labelPanel.add(etiqueta);
        JPanel p = new JPanel( new FlowLayout(FlowLayout.LEFT));
        p.add( pathArchivo);
        p.add( browseArchivo);
        fieldPanel.add(p);
        
        //---
        JTextField cadenaTexto = new JTextField();
        cadenaTexto.setColumns( 20);
        cadenaTexto.setToolTipText( "Ingrese una cadena de texto para generar su firma electrónica");
        
        modelo.put( "cadena", cadenaTexto);
        
        etiqueta = new JLabel( "Cadena Texto: ", JLabel.RIGHT);
        etiqueta.setLabelFor( cadenaTexto);
           
        labelPanel.add(etiqueta);
        p = new JPanel( new FlowLayout(FlowLayout.LEFT));
        p.add( cadenaTexto);
        fieldPanel.add(p);
        
        // ---
        JTextField pathCertificado = new JTextField();
        pathCertificado.setColumns( 20);
        pathCertificado.setToolTipText( "Seleccione el archivo de certificado (*.cer) de su FIEL");
        pathCertificado.setEditable( false);
        
        JButton browseCertificado = new JButton( "Buscar");
        browseCertificado.setActionCommand( "certificado");
        modelo.put( "certificado", pathCertificado);
        browseCertificado.addActionListener( FILE_BROWSER_ACTION_LISTENER);
        
        etiqueta = new JLabel( "Certificado: ", JLabel.RIGHT);
        etiqueta.setLabelFor( pathCertificado);
           
        labelPanel.add(etiqueta);
        p = new JPanel( new FlowLayout(FlowLayout.LEFT));
        p.add( pathCertificado);
        p.add( browseCertificado);
        fieldPanel.add(p);

        // ---
        JTextField pathLlave = new JTextField();
        pathLlave.setColumns( 20);
        pathLlave.setToolTipText( "Seleccione el archivo de llave privada (*.key) de su FIEL");
        pathLlave.setEditable( false);
        
        JButton browseLlave = new JButton( "Buscar");
        browseLlave.setActionCommand( "llave");
        modelo.put( "llave", pathLlave);
        browseLlave.addActionListener( FILE_BROWSER_ACTION_LISTENER);
        
        etiqueta = new JLabel( "Llave Privada: ", JLabel.RIGHT);
        etiqueta.setLabelFor( pathLlave);
           
        labelPanel.add(etiqueta);
        p = new JPanel( new FlowLayout(FlowLayout.LEFT));
        p.add( pathLlave);
        p.add( browseLlave);
        fieldPanel.add(p);
 
        // ---
        JPasswordField password = new JPasswordField();
        password.setColumns( 10);
        password.setToolTipText( "Introduzca la contraseña de su llave privada");   
        
        etiqueta = new JLabel( "Contraseña: ", JLabel.RIGHT);
        etiqueta.setLabelFor( password);
        modelo.put( "password", password);
           
        labelPanel.add(etiqueta);
        p = new JPanel( new FlowLayout(FlowLayout.LEFT));
        p.add( password);
        fieldPanel.add(p);
        
        
        
        JButton executar = new JButton( "Firma");
        executar.setToolTipText( "Invoca al servicio web de firma digital");
        executar.setActionCommand( "ejecutar" );
        executar.addActionListener( FIRMA_ELECTRONICA_ACTION_LISTENER);
        
        labelPanel.add( new JLabel(""));
        p = new JPanel( new FlowLayout(FlowLayout.LEFT));
        p.add( executar);
        fieldPanel.add(p);
              
        
        JPanel firmaPanel = new JPanel();
        firmaPanel.setBorder( new EmptyBorder(10, 0, 10, 10));
        
        pane.add(firmaPanel, BorderLayout.SOUTH);
        
        JTextArea firma = new JTextArea( 20, 60);
        firma.setBorder(BorderFactory.createLineBorder(Color.black));
        modelo.put( "firma", firma);
        
        firmaPanel.add(new JScrollPane( firma));
        
        
        frame.pack();
        frame.setVisible(true);
        frame.setResizable( false);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
