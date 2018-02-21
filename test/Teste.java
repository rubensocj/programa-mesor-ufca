/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */

import java.sql.PreparedStatement;
import mesor.equipamento.Componente;
import mesor.equipamento.Parte;
import mesor.equipamento.Subunidade;
import mesor.equipamento.Unidade;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.*;
//import equipamento.*;

public class Teste {
    
    // Nome e URL do banco de dados
    static final String BD_NAME = "bdprograma";
    static final String BD_URL = "jdbc:mysql://localhost:3306/" + BD_NAME;
    
    // Acesso ao servidor: usuário e senha
    static final String USERNAME = "mesor";
    static final String PASSWORD = "mesorufca1506";
    
    public static void main(String[] args) throws SQLException {
        
        Connection connection = null; // manages connection
        Statement statement = null; // query statement
        ResultSet resultSet = null; // manages results
        
        // connect to database books and query database
        try {
            // establish connection to database
            connection = DriverManager.getConnection(
                    BD_URL, USERNAME, PASSWORD);
            
            // create Statement for querying database
            statement = connection.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
//            
//            PreparedStatement consultaLista;
//            consultaLista = connection.prepareStatement(
//                "SELECT objetivos_especificos.descricao " +
//                "FROM equipe, objetivos_especificos " +
//                "WHERE objetivos_especificos.id_time = 1 " +
//                "GROUP BY objetivos_especificos.id;");
//            
//            int resultado = consultaLista.executeUpdate();
//            
//            //System.out.println(resultado.getString(1) + "\t######");
//            
            
            // query database
            resultSet = statement.executeQuery(
            "select * from sistema");
//            
//            // process query results
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numColuna = metaData.getColumnCount();
//            
//            System.out.println("Tabela Descrição do banco de dados dbteste:\n");
//            
//            for(int i = 1; i <= numColuna; i++) {
//                System.out.printf("%-8s\t", metaData.getColumnName(i));
//            }
//            System.out.println();
//            //javax.swing.JList lista = new JList();
//            
//            resultSet.last();
//            int numLinhas = resultSet.getRow();
//            String[] obj = new String[numLinhas];
            java.util.Vector<String> vec = new java.util.Vector();
            
            resultSet.beforeFirst();
            while(resultSet.next()) {
                for (int i = 1; i <= numColuna; i++) {
                    vec.addElement(resultSet.getString(i));
                }
                
            } // end while
            System.out.println(vec.toString());
//        } // end try // end try // end try // end try
//        
//        catch(SQLException sqlException) {
//            sqlException.printStackTrace();
//        } // end catch
//        
//        // ensure resultSet, statement and Connection are closed
//        finally {
//            try {
//                resultSet.close();
//                statement.close();
//                connection.close();
//            } // end try
//            catch(Exception exception){
//                exception.printStackTrace();
//            } // end catch
//        } // end finally
//        
//        String tabela = "objetivos_especificos";
//        String idEquipe = "2";
//        conexaoJavaSql.Lista lista = new conexaoJavaSql.Lista();
//        lista.setQuery("SELECT " + tabela + ".descricao " +
//            "FROM equipe, " + tabela + " " +
//            "WHERE " + tabela + ".id_equipe = " + idEquipe + " " +
//            "GROUP BY " + tabela + ".id;");
//            System.out.println(lista.toVector());
        } catch(SQLException ex) {
            ex.printStackTrace();
        }finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } // end try
            catch(Exception exception){
                exception.printStackTrace();
            } // end catch
        } // end finally
        
        
        // Cria unidade
        Unidade unidade = new Unidade();
        unidade.setClasse("unidade");
        // Cria duas subunidades
        Subunidade subunidade1, subunidade2;
        subunidade1 = new Subunidade();
        subunidade1.setDescricao("subunidade1");        
        subunidade2 = new Subunidade();
        subunidade2.setDescricao("subunidade2");
        // Cria 4 componentes (2 para cada subunidade)
        Componente componente1, componente2, componente3, componente4;
        componente1 = new Componente("componente1");
        componente2 = new Componente("componente2");
        componente3 = new Componente("componente3");
        componente4 = new Componente("componente4");
        // Cria 8 partes (2 para cada componente)
        Parte parte1, parte2, parte3, parte4, parte5, parte6, parte7, parte8;
        parte1 = new Parte("parte1");
        parte2 = new Parte("parte2");
        parte3 = new Parte("parte3");
        parte4 = new Parte("parte4");
        parte5 = new Parte("parte5");
        parte6 = new Parte("parte6");
        parte7 = new Parte("parte7");
        parte8 = new Parte("parte8");
        // Adiciona 2 partes em cada componente
        componente1.setParte(parte1);
        componente1.setParte(parte2);
        componente2.setParte(parte3);
        componente2.setParte(parte4);
        componente3.setParte(parte5);
        componente3.setParte(parte6);
        componente4.setParte(parte7);
        componente4.setParte(parte8);
        // Adiciona componentes em cada subunidade
        subunidade1.setComponente(componente1);
        subunidade1.setComponente(componente2);
        subunidade2.setComponente(componente3);
        subunidade2.setComponente(componente4);
        // Adiciona subunidades à unidade
        unidade.setSubunidade(subunidade1);
        unidade.setSubunidade(subunidade2);
        // Printa a unidade com o método toString()
        //System.out.println(unidade.toString());
        //System.out.println(unidade.getVetorSubunidade());
//        System.out.println(unidade.getElementoSubunidade(0).toString());
//        System.out.println(unidade.toString());
//        System.out.println(unidade.getElementoSubunidade(0).getElementoComponente(0).getElementoParte(0).toString());
//        System.out.println(componente1.getVetorParte().size());
////        
//        //----------------------------------------------------------------------
//        
//        // Mostra nova janela de configuração de equipamento
//        JanelaTeste menuTeste;
//        menuTeste = new JanelaTeste();
//        menuTeste.mostraJanelaTeste();

        //TesteDialogo test = new TesteDialogo();
//        String s;
//        try(java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("trecho frankenstein.txt"))) {
//            while((s = br.readLine()) != null) {
//                System.out.println(s);
//            }
//        } catch (IOException ex) {
//            ex.getMessage();
//        }
//        
//        javax.swing.JEditorPane p = new javax.swing.JEditorPane();
//        p.setContentType("text/html");
//        p.setText("<html>\n" +
//"    <head>\n" +
//"        <title>Hora de fazer Tabelas</title>\n" +
//"    </head>\n" +
//"    <body>\n" +
//"        <table border=\"1px\">\n" +
//"            <thead>\n" +
//"                <tr>\n" +
//"                    <th>Monstro Famoso</th>\n" +
//"                    <th>Ano de Nascimento</th>\n" +
//"                </tr>\n" +
//"            </thead>\n" +
//"            <tbody>\n" +
//"                <tr>\n" +
//"                    <td>King Kong</td>\n" +
//"                    <td>1933</td>     \n" +
//"                </tr>\n" +
//"                <tr>\n" +
//"                    <td>Dracula</td>\n" +
//"                    <td>1897</td>\n" +
//"                </tr>\n" +
//"                <tr>\n" +
//"                    <td>A Esposa do Frankenstein</td>\n" +
//"                    <td>1935</td>\n" +
//"                </tr>\n" +
//"            </tbody>\n" +
//"        </table>\n" +
//"    </body>\n" +
//"</html>");
//        JFrame fr = new JFrame("teste");
//        fr.add(p);
//        fr.pack();
//        fr.setVisible(true);
//        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        
//        JEditorPane editorPane = new JEditorPane();
//        editorPane.setEditable(false);
        
   }    
}
