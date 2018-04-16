/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.menu;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class DialogoConfirma {

    private JDialog dialog;
    private JOptionPane mPane;
    private String m;

    public DialogoConfirma(String m) {
        this.m = m;
        
        mPane = new JOptionPane();
        mPane.setMessage(this.m);
        mPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        mPane.setMessageType(JOptionPane.WARNING_MESSAGE);
    }
    
    public DialogoConfirma(String m, Object[] options) {
        /* Se houver erro, exibe mensagem de erro */
        this.m = m;
        
        mPane = new JOptionPane();
        mPane.setMessage(this.m);
        mPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
        mPane.setOptions(options);
        mPane.setMessageType(JOptionPane.WARNING_MESSAGE);
    }
    
    public void show(/*String m, Object[] options*/) {
//        /* Se houver erro, exibe mensagem de erro */
//        JOptionPane mPane = new JOptionPane();
//        mPane.setMessage(m);
//        mPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
//        mPane.setOptions(options);
//        mPane.setMessageType(JOptionPane.WARNING_MESSAGE);

        dialog = mPane.createDialog(mPane, "Aviso");
        dialog.pack();
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
//        JDialog mDialog = mPane.createDialog(mPane, "Aviso");
//        mDialog.pack();
//        mDialog.setVisible(true);
//        mDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    public void dispose() {
        dialog.dispose();
    }
    
    public void setOptions(Object[] options) {
        mPane.setOptions(options);
    }
}
