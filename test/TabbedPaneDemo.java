//import menu.painel.aba.BotaoAba;
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.plaf.basic.BasicSplitPaneDivider;
//import javax.swing.plaf.basic.BasicTabbedPaneUI;
//
//public class TabbedPaneDemo extends JFrame {
//
//    private final JTabbedPane tbdPane = new JTabbedPane();
//    
//    // Construtor
//    public TabbedPaneDemo() {
//        super("JTabbedPane Demo");
//        
//        // Configura o JTabbedPane
//        tbdPane.setTabPlacement(JTabbedPane.TOP);
//        tbdPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
//        tbdPane.setOpaque(true);
//        tbdPane.setPreferredSize(new Dimension(400, 200));
//        ImageIcon img = new ImageIcon(
//            "C:\\Users\\Rubens Jr\\Documents\\NetBeansProjects\\Programa" + 
//                        "\\icone\\seta_dupla_direita.png");
//        
//        tbdPane.addTab("AAAAAAAAAAAAAAAA", img, new JPanel());
//        tbdPane.addTab("bbb", new JPanel());
//        
//        tbdPane.setTabComponentAt(0, new BotaoAba(tbdPane.getTitleAt(0)));
//        tbdPane.setTabComponentAt(1, new BotaoAba(tbdPane.getTitleAt(1)));
//        
//        
////        Component cp = tbdPane.getTabComponentAt(0);
////        Component[] cop = tbdPane.getComponents();
////
////        System.out.println(cop[0].getClass());
////        System.out.println(cop[1].getClass());
////        System.out.println(cop[2].getClass());
//        
////        System.out.println(tabbedPane.getComponent);
////        System.out.println(tabbedPane.getComponentAt(1));
////        System.out.println(tabbedPane.getComponentAt(2));
////        
////        /** 
////         * Pega o divisor do JSplitPane como objeto
////         * da classe BasicSplitPaneDivider, usando getComponent(index)
////         */
////        BasicSplitPaneDivider div;
////        div = (BasicSplitPaneDivider) tabbedPane.getComponent(2);
////        
////        // Define a largura, em pixel, do divisor
////        int largura = 20;
////        div.setDividerSize(largura);
////        
////        // Define a nova cor da borda externa do divisor
////        Color cor = new Color(172, 172, 172);
////        
////        /**
////         * Define a nova borda do divisor do JSplitPane, 
////         * com a cor e a largura especificadas
////         */
////        div.setBorder(BorderFactory.createCompoundBorder(
////                BorderFactory.createMatteBorder(0, 1, 0, 1, cor),
////                BorderFactory.createEmptyBorder(0, largura, 0, 0)));
////        
//        Container pnl = getContentPane();
//        pnl.setLayout(new FlowLayout());
//        pnl.add(tbdPane);
//        
//        setVisible(true);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        pack();
//    }
//    
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        TabbedPaneDemo split = new TabbedPaneDemo();
//    }
//}
//
//
////        div.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(238, 238, 238)));