package mesor.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;
import mesor.menu.DialogoAviso;
import static mesor.sql.Consulta.conectado;
import static mesor.sql.Consulta.conectar;

/**
 * ModeloTabela.java
 * 
 * @version 1.0 17/2/2017
 * @author Rubens Jr
 */
public class ModeloTabela extends AbstractTableModel {
    
//    private final Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numLinhas;
    
    // lida com a conexão com o BD
//    private boolean conectado = false;
    
    // Nome e URL do banco de dados.
//    private static final String BD_NAME = "bdprograma";
////    private static final String BD_HOST = "192.168.1.12:3306/";
//    private static final String BD_HOST = "localhost:3306/";
//    private static final String BD_URL = "jdbc:mysql://" + BD_HOST + BD_NAME;
//    
//    // Acesso ao servidor: usuário e senha
//    private static final String USERNAME = "mesor";
//    private static final String PASSWORD = "mesorufca1506";
    
    // Construtor
    public ModeloTabela(String consulta) {
        try {
            statement = Consulta.connection.createStatement(
                        ResultSet.CONCUR_READ_ONLY,
                        ResultSet.TYPE_SCROLL_INSENSITIVE);
            setQuery(consulta);
        } catch (SQLException e) {
            
            e.printStackTrace();
            Consulta.conectar();

        } catch(Exception ex2) {
            
            ex2.printStackTrace();
            Consulta.conectar();
            
        } finally {            
            try {
                statement = Consulta.connection.createStatement(
                            ResultSet.CONCUR_READ_ONLY,
                            ResultSet.TYPE_SCROLL_INSENSITIVE);
                
                setQuery(consulta);
            } catch (SQLException ex) {
                DialogoAviso.show("SQLException em construtor ModeloTabela(consulta): " + 
                        ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        }
        
    } // Fim do construtor -----------------------------------------------------
    
    // Pega a classe que representa o tipo da coluna
    @Override
    public Class getColumnClass(int coluna) throws IllegalStateException {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        try {
            String nomeClasse = metaData.getColumnClassName(coluna + 1);
            
            return Class.forName(nomeClasse);
        } // Fim do try
        catch(SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } // Fim do catch
        
        return Object.class;
    } // Fim do métddo getColumnClass
    
    // Pega o número de colunas no ResultSet
    @Override
    public int getColumnCount() throws IllegalStateException {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        try {
            return metaData.getColumnCount();
        } // Fim do try
        catch(SQLException ex) {
            ex.printStackTrace();
        } // Fim do catch
        
        return 0;
    } // Fim do método getColumnCount
    
    // pega o nome de uma coluna em particular de ResultSet
    @Override
    public String getColumnName(int coluna) {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        try {
            return metaData.getColumnName(coluna + 1);
        } // Fim do try
        catch(SQLException ex) {
            ex.printStackTrace();
        } // Fim do catch
        
        return "";
    } // Fim do método getColumnName
    
    // Pega o número de linhas no ResulSet
    @Override
    public int getRowCount() throws IllegalStateException {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        return numLinhas;
    } // Fim do método getRowCount
    
    // Retorna o valor em uma linha e coluna
    @Override
    public Object getValueAt(int linha, int coluna) throws IllegalStateException {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        try {
            resultSet.absolute(linha + 1);
            return resultSet.getObject(coluna + 1);
        } // Fim do try.
        catch(SQLException ex) {
            ex.printStackTrace();
        } // Fim do catch.
        
        return "";
    } // Fim do método getValueAt
    
    /**
     * Determina consulta SQL que terá os resultados exibidos na tabela.
     * 
     * @param consulta
     * @throws SQLException
     * @throws IllegalStateException 
     */
    // 
    public void setQuery(String consulta) throws SQLException, IllegalStateException {
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        
        resultSet = statement.executeQuery(consulta);
        metaData = resultSet.getMetaData();
        
        // Determina o número de linhas no ResultSet
        resultSet.last(); // move para a última linha
        numLinhas = resultSet.getRow(); // pega o número de linhas
        
        // Notifica JTable que o modelo mudou
        fireTableStructureChanged();
    } // Fim do método setQuery
    
    public Object listaSubunidadesBanco(int index) {
        Object model = null;
        if(!conectado) {
            throw new IllegalStateException("Sem conexão com o banco de dados");
        }
        try {
            resultSet = statement.executeQuery(
                        "SELECT descricao FROM subunidade WHERE id_unidade = "
            + getValueAt(index, 1).toString());
            metaData = resultSet.getMetaData();
            
            model = resultSet.getObject("descricao");
            
            return model;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return model;
    }
    
    // Fecha statement e conexão -----------------------------------------------
    public void desconectaDoBanco() {
        if(conectado) {
            try {
                resultSet.close();
                statement.close();
//                connection.close();
            } // Fim do try
            catch(SQLException ex) {
                ex.printStackTrace();
            } // Fim do catch
            finally {
                conectado = false;
            } // Fim do finally
        } // Fim do if
    } // Fim do método desconectaDoBanco()
} // Fim da classe ModeloTabela
