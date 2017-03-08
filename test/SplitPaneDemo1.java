/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

/**
 * Autor Rubens Oliveira da Cunha Júnior
 */
public class SplitPaneDemo1 extends JPanel implements ListSelectionListener {
    private final JList listaUnidade; //, listaAtributos;
    private final JSplitPane splitPane;
    private final BasicSplitPaneDivider divider;
    
    private final Connection connection;
    private final Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numLinhas;
    
    // lida com a conexão com o BD
    private boolean conectado = false;
    
    // Nome e URL do banco de dados
    private static final String BD_NAME = "bdprograma";
    private static final String BD_URL = "jdbc:mysql://localhost:3306/" + BD_NAME;
    
    // Acesso ao servidor: usuário e senha
    private static final String USERNAME = "mesor";
    private static final String PASSWORD = "mesorufca1506";
    
    public SplitPaneDemo1() throws SQLException {
        
        connection = DriverManager.getConnection(
                    BD_URL, USERNAME, PASSWORD);
        statement = connection.createStatement(ResultSet.CONCUR_READ_ONLY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE);
        conectado = true;
        
        resultSet = statement.executeQuery("SELECT * FROM sistema;");
        metaData = resultSet.getMetaData();
        int numColuna = metaData.getColumnCount();
        
        JMenu menu = new JMenu();
        java.util.Vector<String> vec = new java.util.Vector();
        while(resultSet.next()) {
            for (int i = 2; i <= numColuna; i++) {
                JMenuItem item = new JMenuItem(
                            resultSet.getString(i - 1) + " - " + 
                            resultSet.getString(i));
                menu.add(item);
                JPanel pnl = new JPanel(new FlowLayout());
                pnl.setOpaque(true);
                pnl.add(new JLabel(
                            resultSet.getString(i - 1) + " - " + 
                            resultSet.getString(i)));
                pnl.add(new JButton(String.valueOf(i)));
//                vec.addElement(item);
                vec.addElement(resultSet.getString(i - 1) + " - " + resultSet.getString(i));
            }

        } // end while
        // ---------------------------------------------------------------------
        
        listaUnidade = new JList(vec);
        listaUnidade.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        listaUnidade.setSelectedIndex(0);
        listaUnidade.addListSelectionListener(this);
        
//        listaAtributos
        
//        "%-8s\t"
        
        JScrollPane listScrollPane = new JScrollPane(listaUnidade);
        
//        try {
//            JTable tabUnidade = new JTable(
//                        new sql.TabelaModelo("SELECT * FROM unidade"));
//        }
JPanel pnl = new JPanel(new FlowLayout());
pnl.setOpaque(true);
pnl.add(new JButton("Botao"));
        System.out.println(pnl.getBackground());
        JScrollPane pictureScrollPane = new JScrollPane(pnl);
 
        //Create a split pane with the two scroll panes in it.
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   listScrollPane, new JScrollPane());
        splitPane.setOpaque(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);
 
        divider = (BasicSplitPaneDivider) splitPane.getComponent(2);
        divider.setDividerSize(5);
        divider.setBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(238, 238, 238)));
        
        //Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        pictureScrollPane.setMinimumSize(minimumSize);
 
        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(700, 400));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList)e.getSource();
        int indexUnidade = list.getSelectedIndex();
    }
    
    public JSplitPane getSplitPane() {
        return splitPane;
    }
    
    private static void createAndShowGUI() throws SQLException {
 
        //Create and set up the window.
        JFrame frame = new JFrame("SplitPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SplitPaneDemo1 splitPaneDemo = new SplitPaneDemo1();
        frame.getContentPane().add(splitPaneDemo.getSplitPane());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createAndShowGUI();
                } catch (SQLException ex) {
                    Logger.getLogger(SplitPaneDemo1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
//        
        SplitPaneDemo1 sp;
        try {
            sp = new SplitPaneDemo1();
            System.out.println(sp.divider.getColorModel());
            System.out.println(sp.divider.getBackground());
            System.out.println(sp.divider.getBounds());
            System.out.println(sp.divider.getForeground());
            System.out.println(sp.divider.getPreferredSize());
            System.out.println(sp.divider.getClass());
            
//            System.out.println(sp.splitPane.getComponentCount());
//            System.out.println(sp.splitPane.getComponent(0));
//            System.out.println(sp.splitPane.getComponent(1));
//            System.out.println(sp.splitPane.getComponent(2).getClass());
//            System.out.println(Arrays.toString(sp.splitPane.getComponents()));
        } catch (SQLException ex) {
            Logger.getLogger(SplitPaneDemo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
