/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mesor.sql;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.Vector;
import mesor.menu.DialogoAviso;
/**
 * Query.java
 *
 * @version 1.0 24/11/2017
 * @author Rubens Júnior
 */
public class Query extends Consulta{

    //private final Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static ResultSetMetaData metaData;
    private static int numLinhas;
    
    /**
     * Construtores.
     */
    public Query() {

    }
    
    public static int consultaSQLInt(String consulta) {
        Integer i = 0;
        try {
            statement = Consulta.connection.createStatement(
                        ResultSet.CONCUR_READ_ONLY,
                        ResultSet.TYPE_SCROLL_INSENSITIVE);

            setQuery(consulta);

            i = (int) (long) resultSet.getObject(1);
        } catch (SQLException e) {
            DialogoAviso.show(e.getMessage());
            e.printStackTrace();            
            Consulta.conectar();

        } catch(Exception ex2) {
            DialogoAviso.show(ex2.getMessage());
            ex2.printStackTrace();
            Consulta.conectar();
            
        } finally {
            try {
                statement = Consulta.connection.createStatement(
                            ResultSet.CONCUR_READ_ONLY,
                            ResultSet.TYPE_SCROLL_INSENSITIVE);
                
                setQuery(consulta);
            } catch (SQLException ex) {
                DialogoAviso.show("SQLException em construtor Query(consulta): " + 
                        ex.getMessage());
                ex.printStackTrace();
            }
        }
        return i;
    }
    
    /**
     * Define a consulta SQL a ser executada, passada como String pelo
     * parâmetro.
     * 
     * @param q
     * @throws SQLException
     * @throws IllegalStateException 
     */
    public static void setQuery(String q) throws SQLException, IllegalStateException {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        resultSet = statement.executeQuery(q);
        metaData = resultSet.getMetaData();
        
        // Determina o número de linhas no ResultSet.
        resultSet.last(); // Move para a última linha.
        numLinhas = resultSet.getRow(); // Pega o número de linhas.
    }
}