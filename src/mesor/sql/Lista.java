package mesor.sql;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javax.swing.*;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import mesor.menu.DialogoAviso;

/**
 * Autor Rubens Oliveira da Cunha Júnior
 */
public class Lista extends Consulta {
    
//    private final Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numLinhas;
    
    
    // Nome e URL do banco de dados
 //   private static final String BD_NAME = "bdprograma";
 //   private static final String BD_URL = "jdbc:mysql:" + 
//                                        "//192.168.1.12:3306/" + BD_NAME;
//    
//    // Acesso ao servidor: usuário e senha
//    private static final String USERNAME = "mesor";
//    private static final String PASSWORD = "mesorufca1506";
    
    public JList l; //, listaAtributos;
    public List<String> lista = new ArrayList<>();
    private Vector<String> vetorResultado;
    private String[] arrayResultado;
    
    /**
     * Construtor.
     * @param consulta
     */
    public Lista(String consulta) {

        try {
            statement = Consulta.connection.createStatement(
                        ResultSet.CONCUR_READ_ONLY,
                        ResultSet.TYPE_SCROLL_INSENSITIVE);
        
            setQuery(consulta);
        } catch (SQLException e) {
//            DialogoAviso.show(e.getMessage());
            e.printStackTrace();
            Consulta.conectar();

        } catch(Exception ex2) {
//            DialogoAviso.show(ex2.getMessage());
            ex2.printStackTrace();
            Consulta.conectar();
            
        } finally {
            try {
                statement = Consulta.connection.createStatement(
                            ResultSet.CONCUR_READ_ONLY,
                            ResultSet.TYPE_SCROLL_INSENSITIVE);
                
                setQuery(consulta);
            } catch (SQLException ex) {
                DialogoAviso.show("SQLException em construtor Lista(consulta): " + 
                        ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Construtor.
     * @throws SQLException 
     */
    public Lista() throws SQLException {
        statement = Consulta.connection.createStatement(
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE);
    }

    // -------------------------------------------------------------------------
    // Métodos public.
    // -------------------------------------------------------------------------
    
    /**
     * Define a consulta SQL a ser executada, passada como String pelo
     * parâmetro.
     * 
     * @param q
     * @throws SQLException
     * @throws IllegalStateException 
     */
    public void setQuery(String q) throws SQLException, IllegalStateException {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        resultSet = statement.executeQuery(q);
        metaData = resultSet.getMetaData();
        
        // Determina o número de linhas no ResultSet.
        resultSet.last(); // Move para a última linha.
        numLinhas = resultSet.getRow(); // Pega o número de linhas.
    }
    
    /**
     * Monta um vetor com o resultado da consulta.
     * 
     * @return Um vetor.
     */
    public Vector<String> toVector() {
        vetorResultado = new Vector();
//        vetorResultado.addElement("");
        try {
            int numColuna = metaData.getColumnCount();

            resultSet.beforeFirst();
            while(resultSet.next()) {
                for (int i = 1; i <= numColuna; i++) {
                    vetorResultado.addElement(resultSet.getString(i));
                }
            }
        } catch (SQLException e) {
            DialogoAviso.show("SQLExcpetion em Lista.toVector(): " + e.getMessage());
            e.printStackTrace();
        }
        return vetorResultado;
    }
    
    public List toList() {
        for (String t : toVector()) {
            lista.add(t);
        }
        return lista;
    }
    
    public int size() {
        int s = 0;
        s = toVector().size();
        return s;
    }
    
    public Object get(int i) {
        return toVector().get(i);
    }
    
    public String getString(int i) {
        return (String) toVector().get(i);
    }
    
    /**
     * Monta um array com o resultado da consulta.
     * 
     * @return Um array.
     */
    public String[] toArray() {
        arrayResultado = new String[numLinhas];
        
        try {
            int numColuna = metaData.getColumnCount();

            resultSet.first();
            while(resultSet.next()) {
                int j = resultSet.getRow();
                arrayResultado[j - 1] = resultSet.getString(j - 1);
    //            for (int i = 1; i <= numColuna; i++) {
    //                arrayResultado[i - 1] = resultSet.getString(i);
    //            }
            }
        } catch(SQLException e) {
            DialogoAviso.show(e.getMessage());
            e.printStackTrace();
        }
        return arrayResultado;
    }
    
//    @Override
//    public int getSize() {
//        if(!conectado) {
//            throw new IllegalStateException("Sem conexão com o banco de dados");
//        }
//        
//        return numLinhas;
//    }

//    @Override
//    public Object getElementAt(int index) {
//        if(!conectado) {
//            throw new IllegalStateException("Sem conexão com o banco de dados");
//        }
//        
//        try {
//            resultSet.absolute(index + 1);
//            return resultSet.getObject(1);
//        }
//        catch(SQLException ex) {
//            ex.printStackTrace();
//        }
//        
//        return "";
//    }
}
