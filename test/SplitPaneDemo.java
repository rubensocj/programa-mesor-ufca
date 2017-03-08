import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

public class SplitPaneDemo extends JFrame {

    private final JSplitPane splitPane;
    private final JLabel label1, label2;
    
    // Construtor
    public SplitPaneDemo() {
        super("SplitPane Demo");
        
        // Inicializa os JLabels
        label1 = new JLabel();
        label2 = new JLabel();
        
        // Inicializa e configura o JSplitPane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, label1, label2);
        splitPane.setOpaque(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);
        splitPane.setPreferredSize(new Dimension(400, 200));
        
        /** 
         * Pega o divisor do JSplitPane como objeto
         * da classe BasicSplitPaneDivider, usando getComponent(index)
         */
        BasicSplitPaneDivider div;
        div = (BasicSplitPaneDivider) splitPane.getComponent(2);
        
        // Define a largura do divisor, em pixels
        int largura = 8;
        div.setDividerSize(largura);
        
        // Cria a borda externa
        MatteBorder bordaE;
        bordaE = BorderFactory.createMatteBorder(0, 1, 0, 1, Color.LIGHT_GRAY);
        
        // Cria a borda interna
        Border bordaI;
        bordaI = BorderFactory.createEmptyBorder(0, largura, 0, 0);
        
        /**
         * Define a nova borda do divisor do JSplitPane:
         * uma borda composta pelas bordas externa e
         * interna definidas.
         */
        div.setBorder(BorderFactory.createCompoundBorder(bordaE, bordaI));
        
        Container pnl = getContentPane();
        pnl.setLayout(new FlowLayout());
        pnl.add(splitPane);
        
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SplitPaneDemo split = new SplitPaneDemo();
    }
}