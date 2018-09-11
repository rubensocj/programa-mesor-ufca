/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * JanelaAjuda.java
 *
 * @version 1.0 05/09/2018
 * @author Rubens Júnior
 */
public class JanelaAjuda extends Janela {

    private JEditorPane ePane;
    private String html;
    private final JScrollPane scrPane;
    private JFrame frm;
    
    /**
     * Construtores.
     * @param tipoAjuda
     */
    public JanelaAjuda(String tipoAjuda) {

        html = tipoAjuda;
        ePane = new JEditorPane();
        ePane.setContentType("text/html;charset=UTF-8");
        ePane.setEditable(false);
        try {
            ePane.setPage(Toolkit.getDefaultToolkit().getClass().getResource("/res/ajuda/ajuda" + html + ".html"));
        } catch (IOException ex) {
            DialogoAviso.show(ex.getMessage());
            ex.printStackTrace();
        }
        
        scrPane = new JScrollPane(ePane,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ePane.setPreferredSize(new Dimension(700,600));

    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
 
    public void mostrarFrame() {

        frm = new JFrame("Conteúdo da ajuda");
        frm.setContentPane(scrPane);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setMinimumSize(new Dimension(700,600));
        frm.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frm.setVisible(true);
    }
 
    public void mostrarDialog(Component c) {

        dialog = new JDialog();
        dialog.setContentPane(scrPane);
        dialog.setTitle("Conteúdo da ajuda");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(c);
        dialog.setIconImage(Janela.criarIcon("/res/icone/mesor.jpg").getImage());
        dialog.pack();
        dialog.setVisible(true);
    }
}