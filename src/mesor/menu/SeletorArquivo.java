/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * SeletorArquivo.java
 *
 * @version 1.0 16/10/2018
 * @author Rubens Júnior
 */
public class SeletorArquivo extends JPanel implements ActionListener {
    
    JButton btnSelecionar, btnSalvar;
    JTextArea txt;
    JFileChooser fch;

    /**
     * Construtores.
     */
    public SeletorArquivo() {
        super(new BorderLayout());
        
        txt = new JTextArea(5,20);
        txt.setMargin(new Insets(5,5,5,5));
        txt.setEditable(false);
        JScrollPane scrPanetxt = new JScrollPane(txt);
        
        // file chooser
        fch = new JFileChooser();
        
        // permite selecionar apenas diretorios
        // pode-se alterar para selecionar arquivos tambem
        fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        // botoes
        btnSelecionar = new JButton("Selecionar");
        btnSelecionar.addActionListener(this);
        
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this);
        
        JPanel pnl = new JPanel();
        pnl.add(btnSelecionar);
        pnl.add(btnSalvar);
        
        add(pnl, BorderLayout.PAGE_START);
        add(scrPanetxt, BorderLayout.CENTER);
        
        //Create and set up the window.
        JFrame frame = new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add content to the window.
        frame.add(this);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == btnSelecionar) {
            int ret = fch.showOpenDialog(SeletorArquivo.this);
            
            if(ret == JFileChooser.APPROVE_OPTION) {
                File file = fch.getSelectedFile();
                //This is where a real application would open the file.
                txt.append("Opening: " + file.getName() + ".\n");
            } else {
                txt.append("Open command cancelled by user.\n");
            }
            txt.setCaretPosition(txt.getDocument().getLength());
        
        
        } else if(e.getSource() ==  btnSalvar) {
            int ret = fch.showSaveDialog(SeletorArquivo.this);
            
            if(ret == JFileChooser.APPROVE_OPTION) {
                File file = fch.getSelectedFile();
                //This is where a real application would open the file.
                txt.append("Opening: " + file.getName() + ".\n");
            } else {
                txt.append("Open command cancelled by user.\n");
            }
            txt.setCaretPosition(txt.getDocument().getLength());
        }
    }
}