
package conexaoSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ConnectionFactory.java
 *
 * @version 1.0 20/03/2017
 * @author Rubens Júnior
 * @deprecated 
 */
public class ConnectionFactory {
    
    // Nome e URL do banco de dados.
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String BD_NAME = "bdprograma";
    private static final String BD_HOST = "192.168.1.12:3306/";
    private static final String BD_URL = "jdbc:mysql://" + BD_HOST + BD_NAME;
    
    // Acesso ao servidor: usuário e senha.
    private static final String USER = "mesor";
    private static final String PASSWORD = "mesorufca1506";
    
    /**
     * Construtores.
     */
    public ConnectionFactory() {}
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(BD_URL, USER, PASSWORD);
    }
    
    public static void closeConnection(Connection con) {
        if(con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void closeConnection(Connection con, PreparedStatement stm) {
        closeConnection(con);
        
        if(stm != null) {
            try {
                stm.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void closeConnection(Connection con, PreparedStatement stm,
                ResultSet rs) {
        
        closeConnection(con, stm);
        
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}