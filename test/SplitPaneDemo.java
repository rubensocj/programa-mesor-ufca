/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Autor Rubens Oliveira da Cunha Júnior
 */
public class SplitPaneDemo extends JPanel implements ListSelectionListener {
    private final JList listaUnidade; //, listaAtributos;
    private final JSplitPane splitPane;
    
    private final Connection connection;
    private final Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numLinhas;
    
    // lida com a conexão com o BD
    private boolean conectado = false;
    
    // Nome e URL do banco de dados
    private static final String BD_NAME = "dbprograma";
    private static final String BD_URL = "jdbc:mysql://localhost:3306/" + BD_NAME;
    
    // Acesso ao servidor: usuário e senha
    private static final String USERNAME = "root";
    private static final String PASSWORD = "150613";
    
    public SplitPaneDemo() throws SQLException {
        
        connection = DriverManager.getConnection(
                    BD_URL, USERNAME, PASSWORD);
        statement = connection.createStatement(ResultSet.CONCUR_READ_ONLY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE);
        conectado = true;
        
        resultSet = statement.executeQuery("SELECT cod, classe FROM unidade;");
        metaData = resultSet.getMetaData();
        int numColuna = metaData.getColumnCount();
        
        java.util.Vector<String> vec = new java.util.Vector();
        while(resultSet.next()) {
            for (int i = 2; i <= numColuna; i++) {
                vec.addElement(resultSet.getString(i - 1) + " - " + resultSet.getString(i));
            }

        } // end while
        // ---------------------------------------------------------------------
        
        listaUnidade = new JList(vec);
        listaUnidade.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUnidade.setSelectedIndex(0);
        listaUnidade.addListSelectionListener(this);
        
//        listaAtributos
        
//        "%-8s\t"
        
        JScrollPane listScrollPane = new JScrollPane(listaUnidade);
        
//        try {
//            JTable tabUnidade = new JTable(
//                        new sql.TabelaModelo("SELECT * FROM unidade"));
//        }
        JScrollPane pictureScrollPane = new JScrollPane(new JLabel("teste"));
 
        //Create a split pane with the two scroll panes in it.
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                   listScrollPane, pictureScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);
 
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
        SplitPaneDemo splitPaneDemo = new SplitPaneDemo();
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
                    Logger.getLogger(SplitPaneDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
