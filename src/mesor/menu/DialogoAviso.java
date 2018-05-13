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
public class DialogoAviso {

    private static JOptionPane pane;
    private static JDialog dialog;
    
    public DialogoAviso() {}
    
    public static void show(String m) {
        /* Se houver erro, exibe mensagem de erro */
        JOptionPane mPane = new JOptionPane();
        mPane.setMessage(m);
        mPane.setOptionType(JOptionPane.PLAIN_MESSAGE);
        mPane.setMessageType(JOptionPane.WARNING_MESSAGE);

        JDialog mDialog = mPane.createDialog(mPane, "Aviso");
        mDialog.pack();
        mDialog.setVisible(true);
        mDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    public void showProgress(String m) {
        /* Se houver erro, exibe mensagem de erro */
        pane = new JOptionPane(m, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
//        pane.setMessage(m);
//        pane.setOptionType(JOptionPane.WARNING_MESSAGE);
//        pane.setMessageType(JOptionPane.INFORMATION_MESSAGE);

        dialog = pane.createDialog(pane, "Aviso");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setVisible(true);
    }
    
    public void dispose() {
        dialog.dispose();
    }
}
