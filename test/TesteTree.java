/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class TesteTree {
    
    static JTree tree;
    static JLabel jlab;
    

//    @Override
//    public void init() {
//        try {
//            SwingUtilities.invokeAndWait(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        makeGUI();
//                    }
//                }
//            );
//        } catch (Exception ex) {
//                ex.printStackTrace();
//        }
//    }
        
    private void makeGUI() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // Cria nó de cima
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Options");
        
        // Cria subárvore de "A"
        DefaultMutableTreeNode a = new DefaultMutableTreeNode("A");
        top.add(a); // Adiciona ao nó "top"
        DefaultMutableTreeNode a1 = new DefaultMutableTreeNode("A1");
        a.add(a1);  // Adiciona ao subnó "a"
        DefaultMutableTreeNode a2 = new DefaultMutableTreeNode("A2");
        a.add(a2);  // Adiciona ao subnó "a"
        
        // Cria subárvore de "B"
        DefaultMutableTreeNode b = new DefaultMutableTreeNode("B");
        top.add(b); // Adiciona ao nó "top"
        DefaultMutableTreeNode b1 = new DefaultMutableTreeNode("B1");
        b.add(b1);  // Adiciona ao subnó "b"
        DefaultMutableTreeNode b2 = new DefaultMutableTreeNode("B2");
        b.add(b2);  // Adiciona ao subnó "b"
        DefaultMutableTreeNode b3 = new DefaultMutableTreeNode("B3");
        b.add(b3);  // Adiciona ao subnó "b"
        
        // Cria a tree
        tree = new JTree(top);
        
        // Adiciona a tree a um JScrollPane
        JScrollPane jsp = new JScrollPane(tree);
        
        // Cria um JPanel
        JPanel pnl = new JPanel(new BorderLayout());
        
        // Adiciona o JScrollPane ao conteudo do Applet
        pnl.add(jsp, BorderLayout.NORTH);
        
        // Adiciona o JLabel ao conteudo do Applet
        jlab = new JLabel("Selection is: ");
        pnl.add(jlab, BorderLayout.CENTER);
        
        // Lida com eventos de seleção da tree
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                jlab.setText("Selection is: " + e.getPath());
            }
        });
        
        JFrame frm = new JFrame("Teste JTree");
        frm.add(pnl);
        frm.pack();
//        frm.setPreferredSize(new Dimension(300, 300));
//        frm.setSize(new Dimension(300, 300));
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
    }
}


