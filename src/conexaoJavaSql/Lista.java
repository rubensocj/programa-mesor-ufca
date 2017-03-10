package conexaoJavaSql;

import javax.swing.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.Vector;

/**
 * Autor Rubens Oliveira da Cunha Júnior
 */
public class Lista extends AbstractListModel {
    
    private final Connection connection;
    private final Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numLinhas;
    
    // lida com a conexão com o BD
    private boolean conectado = false;
    
    // Nome e URL do banco de dados
    private static final String BD_NAME = "bdprograma";
    private static final String BD_URL = "jdbc:mysql:" + 
                                        "//192.168.1.12:3306/" + BD_NAME;
    
    // Acesso ao servidor: usuário e senha
    private static final String USERNAME = "mesor";
    private static final String PASSWORD = "mesorufca1506";
    
    private JList lista; //, listaAtributos;
    private Vector<String> vetorResultado;
    
    /**
     * Construtor.
     * @param consulta
     * @throws SQLException 
     */
    public Lista(String consulta) throws SQLException {
        
        connection = DriverManager.getConnection(
                    BD_URL, USERNAME, PASSWORD);
        statement = connection.createStatement(ResultSet.CONCUR_READ_ONLY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE);
        conectado = true;
        
        setQuery(consulta);
    }
    
    /**
     * Construtor.
     * @throws SQLException 
     */
    public Lista() throws SQLException {
        
        connection = DriverManager.getConnection(
                    BD_URL, USERNAME, PASSWORD);
        statement = connection.createStatement(ResultSet.CONCUR_READ_ONLY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE);
        conectado = true;        
    }
    
    // -------------------------------------------------------------------------
    // Métodos private.
    // -------------------------------------------------------------------------
    
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
     * @throws SQLException 
     */
    public Vector<String> toVector() throws SQLException {
        vetorResultado = new Vector();
        vetorResultado.addElement("");
        int numColuna = metaData.getColumnCount();
        
        resultSet.beforeFirst();
        while(resultSet.next()) {
            for (int i = 1; i <= numColuna; i++) {
                vetorResultado.addElement(resultSet.getString(i));
            }
        }
        return vetorResultado;
    }
    
    @Override
    public int getSize() {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        return numLinhas;
    } // Fim do método getSize

    @Override
    public Object getElementAt(int index) {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        try {
            resultSet.absolute(index + 1);
            return resultSet.getObject(1);
        } // Fim do try
        catch(SQLException ex) {
            ex.printStackTrace();
        } // Fim do catch
        
        return "";
    } // Fim do método getElementAt
    
    /**
     * @deprecated 
     * @return 
     */
    public Object[] asObject() {
        Object[] obj = null;
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
         try {
             for(int i = 0; resultSet.next(); i++) {
                 obj[i] = resultSet.getString(i + 1);
             }
        } // Fim do try
        catch(SQLException ex) {
            ex.printStackTrace();
        } // Fim do catch
        return obj;
    }
    
    /**
     * @deprecated 
     * @return 
     */
    public String[] asString() {
        String[] str = null;
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
         try {
             for(int i = 0; resultSet.next(); i++) {
                 str[i] = resultSet.getString(i + 1);
             }
        } // Fim do try
        catch(SQLException ex) {
            ex.printStackTrace();
        } // Fim do catch
        return str;
    }
     /**
      * @deprecated 
      * @return 
      */
    public Vector<String> asVector() {
        Vector<String> str;
        str = null;
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
         try {
             for(int i = 0; resultSet.next(); i++) {
                 str.add(resultSet.getString(i + 1));
             }
        } // Fim do try
        catch(SQLException ex) {
            ex.printStackTrace();
        } // Fim do catch
        return str;
    }
    
    /**
     * @deprecated 
     * @param index
     * @return 
     */
    public Object listaSubunidadesBanco(int index) {
        Object model = null;
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        try {
            resultSet = statement.executeQuery(
                "SELECT objetivos_especificos.* FROM " +
                    "equipe, objetivos_especificos WHERE " +
                    "objetivos_especificos.id_time = " + String.valueOf(index) +
                    " GROUP BY objetivos_especificos.id;");
            metaData = resultSet.getMetaData();
            
            model = resultSet.getObject("descricao");
            
            return model;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return model;
    }    
}
