package menu.painel.aba;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalIconFactory;
import menu.principal.PainelPrincipal;

/**
 * BotaoAba.java
 *
 * @version 1.0 25/02/2017
 * @author Rubens Jr
 */
public class BotaoAba extends JPanel {

    private Component aba;
    
    /**
     * Construtores.
     * @param titulo Título da aba
     * @param a Componente que recebe a aba
     */
    public BotaoAba(String titulo, final Component a) {
        super(new BorderLayout());
        
        // Define o componente que recebe a aba
        setAba(a);
        
        // Cria e configura o botão
        JButton btnFechar = new JButton();        
        btnFechar.setIcon(new ImageIcon(menu.JanelaAdicionarAlterar.LOCAL + 
                                "\\icone\\fechar_2.png"));
        btnFechar.setOpaque(false);
        btnFechar.setBackground(new Color(178,178,178));
        btnFechar.setBorder(BorderFactory.createEmptyBorder());
        btnFechar.setPreferredSize(new Dimension(14,14));
        btnFechar.addMouseListener(new MouseFecharAba());
//        btnFechar.setMargin(new Insets(0, 0, 0, 0));
        
        // Cria o painel que recebe o botão
        JPanel pnlBotao = new JPanel(new FlowLayout());
        pnlBotao.setOpaque(false);
        pnlBotao.add(btnFechar);
        
        add(new JLabel(titulo), BorderLayout.WEST); // Título da aba
        add(pnlBotao, BorderLayout.EAST);  // Botão de fechar
        setOpaque(false);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    /**
     * Define o componente que recebe a aba
     * 
     * @param a 
     */
    private void setAba(Component a) {
        aba = a;
    }

    private class MouseFecharAba implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() instanceof JButton) {
                // Força a criação de um JButton a partir de getSource
                // Força a criação de um JTabbedPane
                // Remove a aba
                JButton b = (JButton) e.getSource();
                JTabbedPane tb = (JTabbedPane) b.getParent().getParent().
                                getParent().getParent().getParent().getParent();
                tb.remove(aba);
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        
    }
    
//    /**
//     * ActionListener para ações no botão. Fechar a aba.
//     */
//    private static class ActionFechar implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            System.out.println("ACTIONEVENT: getSource: " + e.getSource());
//            // Força a criação de um Component a partir do Object getSource
//            JButton t = (JButton) e.getSource();
//            
//            // Este array de Component guarda os 2 componentes de uma aba
//            // O JLabel com o título da aba e painel com o botão
//            Component[] abaOrigemEvento = t.getParent().getParent().getComponents();
//            Component painelBotao = abaOrigemEvento[1];
//            
//            // Força a criação de um JTabbedPane a partir de um Component
//            JTabbedPane tb = (JTabbedPane) t.getParent().getParent().getParent().getParent().getParent().getParent();
//
//            int id = 0; // índice da aba a ser excluida
//            int nAbas = tb.getTabCount();
//            // Laço auxiliar 1: Printa os nomes das abas
//            for(int i = 0; i < nAbas; i++) {
//                Component cp = tb.getTabComponentAt(i);
//                System.out.println(cp.getClass() + "nome: " + cp.getName());
//            }
//            // Laço auxiliar 2: Verifica de qual aba veio o evento
//            for(int i = 0; i < nAbas; i++) {
//                Component cp = tb.getTabComponentAt(i);
//                if(cp.getName().equals(nome)) {
//                    System.out.println("true");
//                } else {System.out.println("false");}
//            }
//            // Laço principal: Pega o índice da aba cujo evento foi disparado
//            for(int i = 0; i < nAbas; i++) {
//                Component cp = tb.getTabComponentAt(i);
//                if(cp.getName().equals(nome)) {
//                    id = i; break;
//                }
//            }
            
            // Remove a aba
//            tb.remove(id);
           
            
//            System.out.println("paramString: " + e.paramString());
//            System.out.println("getModifiers: " + e.getModifiers());
//            System.out.println("aba.getName: " + aba.getName());
//            System.out.println("aba.getParent.getClass: " + aba.getParent().getClass());
//            System.out.println("aba.getParent.getName: " + aba.getParent().getName());
//            System.out.println("aba.getParent.getComponentCount: " + aba.getParent().getComponentCount());
//            System.out.println("aba.getParent.getParent: " + aba.getParent().getParent());
//            System.out.println("btnFechar.getParent: " + btnFechar.getParent());
//            System.out.println("btnFechar.getParent.getParent: " + btnFechar.getParent().getParent());
//            System.out.println("btnFechar.getParent.getParent.getParent.getComponents: " + Arrays.toString(btnFechar.getParent().getParent().getParent().getComponents()));
            
//            System.out.println("t.getParent: " + t.getParent());
//            System.out.println("t.getParent.getParent: " + t.getParent().getParent());
//            System.out.println("t.getParent.getParent.getComponentCount: " + t.getParent().getParent().getComponentCount());
//            System.out.println("t.getParent.getParent.getComponents: " + Arrays.toString(t.getParent().getParent().getComponents()));
//            
//            
//            System.out.println("t.getParent.getParent.getParent: " + t.getParent().getParent().getParent());
//            System.out.println("t.getParent.getParent.getParent.getParent: " + t.getParent().getParent().getParent().getParent());
//            System.out.println("t.getParent.getParent.getParent.getParent.getParent: " + t.getParent().getParent().getParent().getParent().getParent());
//            System.out.println("t.getParent.getParent.getParent.getParent.getParent.getParent: " + t.getParent().getParent().getParent().getParent().getParent().getParent());
//            System.out.println("t.getParent.getParent.getParent: " + t.getParent().getParent().getParent());
//            System.out.println("t.getParent.getParent.getParent.getParent.getParent: " + t.getParent().getParent().getParent().getParent().getParent());
//            System.out.println("t.getParent.getParent.getComponents: " + Arrays.toString(t.getParent().getParent().getComponents()));
            
//            Component[] a1 = t.getParent().getParent().getParent().getComponents();
//            Component a2 = t.getParent().getParent(), a3 = null;
//            int id = 0, cont = 0;
//            while(cont <= a1.length) {
//                if(a1[cont] == a2) {
//                    System.out.println(cont + ": " + a1[cont]);
//                    a3 = a1[cont];
//                    id = cont;
//                    break;
//                }
//                cont++;
//            }
//            PainelEmAbas tb1 = (PainelEmAbas) btnFechar.getParent().getParent().getParent().getParent().getParent().getParent();
//            tb1.removerAba(index);

//            System.out.println("btnFechar.getParent.getParent.getParent.getComponentCount: " + btnFechar.getParent().getParent().getParent().remove(index));
//            System.out.println("btnFechar.getParent.getParent.getParent.getParent: " + btnFechar.getParent().getParent().getParent().getParent());
//            JTabbedPane tbddPane = (JTabbedPane) aba.getParent().getParent();
//            tbddPane.remove(index);
            
//            aba.getParent().getParent().remove(index);
//            aba.getParent().remove(index);
//        }
//    }
}