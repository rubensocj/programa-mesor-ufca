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
}
