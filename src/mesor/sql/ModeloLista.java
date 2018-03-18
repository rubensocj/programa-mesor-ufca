package mesor.sql;

import javax.swing.*;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.Vector;
import mesor.equipamento.Sistema;
import mesor.menu.DialogoAviso;

/**
 * Autor Rubens Oliveira da Cunha Júnior
 */
public class ModeloLista extends DefaultListModel {
    
//    private final Connection con;
    private final Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int linhas;
    
    public JList l; //, listaAtributos;
    private Vector<String> vetorResultado;
    public String[] arrayRes;
    public Sistema[] arraySistema;
    
    /**
     * Construtor.
     * @param consulta
     * @throws SQLException 
     */
    public ModeloLista(String consulta) throws SQLException {
        if(!Consulta.conectado) { Consulta.conectar(); }
        statement = Consulta.connection.createStatement(
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE);
        
        setQuery(consulta);
        
        arrayRes = new String[linhas+1];
        arraySistema = new Sistema[linhas+1];
        
        arrayRes[0] = "Selecionar...";
        arraySistema[0] = null;
        
        try {
            resultSet.first();
            
            int k = 1;
            while(k <= linhas) {

                arraySistema[k] = new Sistema(resultSet.getInt(1), 
                                            resultSet.getString(2));
                
                arrayRes[k] = arraySistema[k].getNome();
                resultSet.next();
                k++;
            }
        } catch (SQLException e) {
            DialogoAviso.show("Erro de operação. " + e.getLocalizedMessage());
        }
    }
    
    /**
     * Construtor.
     * @throws SQLException 
     */
    public ModeloLista() throws SQLException {
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
        if(!Consulta.conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        resultSet = statement.executeQuery(q);
        metaData = resultSet.getMetaData();
        
        // Determina o número de linhas no ResultSet.
        resultSet.last(); // Move para a última linha.
        linhas = resultSet.getRow(); // Pega o número de linhas.
    }
    
//    /**
//     * Monta um vetor com o resultado da consulta.
//     * 
//     * @return Um vetor.
//     * @throws SQLException 
//     */
//    public Vector<String> toVector() throws SQLException {
//        vetorResultado = new Vector();
////        vetorResultado.addElement("");
//        int numColuna = metaData.getColumnCount();
//        
//        resultSet.beforeFirst();
//        while(resultSet.next()) {
//            for (int i = 1; i <= numColuna; i++) {
//                vetorResultado.addElement(resultSet.getString(i));
//            }
//        }
//        return vetorResultado;
//    }
//    
//    public String getString(int i) throws SQLException {
//        return (String) toVector().get(i);
//    }
    
    /**
     * Monta um array com o resultado da consulta.
     * 
     * @return Um array.
     */
    public String[] toArrayAluno() {
        return arrayRes;
    }
    
    @Override
    public String[] toArray() {
        try {
            resultSet.first();
            
            int k = 1;
            while(k <= linhas) {

                arrayRes[k] = resultSet.getString(2);
                resultSet.next();
                k++;
            }
        } catch (SQLException e) {
            DialogoAviso.show("Erro de operação. " + e.getLocalizedMessage());
        }
        return arrayRes;
    }
    
    /**
     * 
     * @param coluna Coluna
     * @param tabela Tabela
     * @throws java.sql.SQLException
     */
    public void reiniciarLista(String coluna, String tabela) throws SQLException {
        setQuery("SELECT " + coluna + " FROM " + tabela + ";");
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
