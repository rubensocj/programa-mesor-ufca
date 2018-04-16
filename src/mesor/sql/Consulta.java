package mesor.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mesor.menu.DialogoAviso;

/**
 * Consulta.java
 * 
 * @version 1.0 4/2/2017
 * @author Rubens Jr
 */
public class Consulta {
    
    // Virtual host
//    private static final String V_HOST = "sql10.freesqldatabase.com"; // online
    private static final String V_HOST = "localhost"; // localhost
//    private static final String V_HOST = "sql38.main-hosting.eu"; // remoto
    
    // Obsoletos
//    private static final String V_HOST = "db4free.net";
//    private static final String V_HOST = "mesor.ufca.rubens.ocj";
//    private static final String V_HOST = "rubensoliveira";
//    private static final String V_HOST = "rubensteste.ddsnet.com";
    
    // Host do banco de dados
    private static final String BD_HOST = V_HOST + ":3306";
    // Nome do banco de dados.
//    private static final String BD_NAME = "sql10190138";    // online
        private static final String BD_NAME = "bdprograma";   // localhost
//        private static final String BD_NAME = "u789035304_mesor";   // remoto

    // Obsoleto
//    private static final String BD_NAME = "bdmesorprograma";
    
    // URL de conexão
    private static final String BD_URL = "jdbc:mysql://" + BD_HOST + "/" + BD_NAME;
    
    // Acesso ao servidor: usuário e senha.
//    private static final String USERNAME = "sql10190138";   // online
    private static final String USERNAME = "mesor";   // localhost
//    private static final String USERNAME = "u789035304_mesor";   // remoto
    
    // Obsoleto
//    private static final String USERNAME = "mxdvirtualage084";

//    private static final String PASSWORD = "EPFGjwYGyq";    // online
    private static final String PASSWORD = "mesorufca1506";   // localhost
//    private static final String PASSWORD = "mesor54321";   // remoto
    
    // Obsoleto
//    private static final String PASSWORD = "r1sk305qr7dwn";

    // gerencia a conexão.
    public static Connection connection = null;
    private static ResultSet resultSet = null;
    
    // lida com a conexão com o BD
    public static boolean conectado = false;
    
    /**
     * id gerado automaticamente a cada inserção. É usados nas chaves
     * estrangeiras quando necessário.
     */
    public static int idUnidade, idSubunidade, idComponente, idParte, idDemanda,
                idIntervencao, idInterventor, idEquipe;
    
    /**
     * Consulta em SQL previamente preparada que insere no banco de dados
     * elementos criados pelo usuário.
     */
    private static PreparedStatement inserirSistema = null,
                inserirUnidade = null,
                inserirSubunidade = null,
                inserirComponente = null,
                inserirParte = null,
                inserirDemanda = null,
                inserirIntervencao = null,
                inserirInterventor = null,
                inserirEquipe = null,
                inserirObjetivoEspecifico = null,
                inserirHabilidadeRequerida = null,
                inserirExperienciaRequerida = null;
    
    /**
     * Consulta em SQL previamente preparada que atualiza no banco de dados
     * elementos alterados pelo usuário.
     */
    private static PreparedStatement alterarSistema = null,
                alterarUnidade = null,
                alterarSubunidade = null,
                alterarComponente = null,
                alterarParte = null,
                alterarDemanda = null,
                alterarIntervencao = null,
                alterarInterventor = null,
                alterarEquipe = null,
                alterarObjetivoEspecifico = null,
                alterarHabilidadeRequerida = null,
                alterarExperienciaRequerida = null;
    
    /**
     * Consulta em SQL previamente preparada que exclui do banco de dados
     * elementos selecionados pelo usuário.
     */
    private static PreparedStatement deletarUnidade = null,
                deletarSubunidade = null,
                deletarComponente = null,
                deletarParte = null,
                deletarDemanda = null,
                deletarIntervencao = null,
                deletarInterventor = null,
                deletarEquipe = null,
                deletarObjetivoEspecifico = null,
                deletarHabilidadeRequerida = null,
                deletarExperienciaRequerida = null;
    
    private static PreparedStatement vincularEquipe = null,
                vincularIntervencao = null,
                desvincularEquipe = null;
    
    // -------------------------------------------------------------------------
    // Métodos da classe Sistema
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade SISTEMA do banco de dados com as
     * informações dadas pelo usuário.
     * 
     * @param nome
     * @param descricao
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertSistema(String nome, String descricao) {
        int resultado = 0;
        try {
            inserirSistema.setString(1, nome);
            if(descricao.isEmpty()) {
                inserirSistema.setNull(2, java.sql.Types.VARCHAR);
            } else {inserirSistema.setString(2, descricao);}
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirSistema.executeUpdate();
            inserirSistema.getQueryTimeout();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     * Altera uma linha selecionada da entidade SISTEMA do banco de dados com as
     * novas informações dadas pelo usuário.
     * 
     * @param nome
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int updateSistema(String nome, String descricao, int id) {
        int resultado = 0;
        try {
            alterarSistema.setString(1, nome);
            if(descricao.isEmpty()) {
                alterarSistema.setNull(2, java.sql.Types.VARCHAR);
            } else {alterarSistema.setString(2, descricao);}
            alterarSistema.setInt(3, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarSistema.executeUpdate();
            alterarSistema.getQueryTimeout();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return resultado;
    }
    
    // -------------------------------------------------------------------------
    // Métodos da classe Unidade
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade UNIDADE do banco de dados com as
     * informações dadas pelo usuário.
     * 
     * @param classe
     * @param tipo
     * @param fabricante
     * @param identificacao
     * @param categoria
     * @param localizacao
     * @param aquisicao
     * @param operacao
     * @param inicio
     * @param idSistema
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertUnidade(String classe, String tipo,
            String fabricante, String identificacao, String categoria,
            String localizacao, java.util.Date aquisicao, String operacao,
            java.util.Date inicio, int idSistema) {
        
        int resultado = 0;        
        try {
            inserirUnidade.setString(1, classe);
            inserirUnidade.setString(2, tipo);
            inserirUnidade.setString(3, fabricante);
            inserirUnidade.setString(4, identificacao);
            if(categoria.isEmpty()) {
                inserirUnidade.setNull(5, java.sql.Types.VARCHAR);
            } else {inserirUnidade.setString(5, categoria);}
            if(localizacao.isEmpty()) {
                inserirUnidade.setNull(6, java.sql.Types.VARCHAR);
            } else {inserirUnidade.setString(6, localizacao);}
            if(aquisicao == null) {
                inserirUnidade.setNull(7, java.sql.Types.DATE);                
            } else {
                java.sql.Date sqlDataAq;
                sqlDataAq = new java.sql.Date(aquisicao.getTime());
                inserirUnidade.setDate(7, sqlDataAq);
            }
            inserirUnidade.setString(8, operacao);
            java.sql.Date sqlDataOp;
            sqlDataOp = new java.sql.Date(inicio.getTime());
            inserirUnidade.setDate(9, sqlDataOp);
            inserirUnidade.setInt(10, idSistema);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirUnidade.executeUpdate();
            inserirUnidade.getQueryTimeout();
            
            // Pega os ID gerados automaticamente na inserção.
            resultSet = inserirUnidade.getGeneratedKeys();
            if(resultSet.next()) {
                idUnidade = resultSet.getInt(1);
            }
        } // Fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Altera uma linha selecionada da entidade UNIDADE do banco de dados com as
     * novas informações dadas pelo usuário.
     * 
     * @param classe
     * @param tipo
     * @param fabricante
     * @param identificacao
     * @param categoria
     * @param localizacao
     * @param aquisicao
     * @param operacao
     * @param inicio
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int updateUnidade(String classe, String tipo, String fabricante,
            String identificacao, String categoria, String localizacao,
            java.util.Date aquisicao, String operacao, java.util.Date inicio,
            int id) {
        
        int resultado = 0;        
        try {
            alterarUnidade.setString(1, classe);
            alterarUnidade.setString(2, tipo);
            alterarUnidade.setString(3, fabricante);
            alterarUnidade.setString(4, identificacao);
            // Se categoria, localizacao, e/ou data de aquisicao não forem
            // informadas, insere NULL.
            if(categoria.isEmpty()) {
                alterarUnidade.setNull(5, java.sql.Types.VARCHAR);
            } else {alterarUnidade.setString(5, categoria);}
            if(localizacao.isEmpty()) {
                alterarUnidade.setNull(6, java.sql.Types.VARCHAR);
            } else {alterarUnidade.setString(6, localizacao);}
            if(aquisicao == null) {
                alterarUnidade.setNull(7, java.sql.Types.DATE);                
            } else {
                java.sql.Date sqlDataAq;
                sqlDataAq = new java.sql.Date(aquisicao.getTime());
                alterarUnidade.setDate(7, sqlDataAq);
            }
            alterarUnidade.setString(8, operacao);
            java.sql.Date sqlDataOp;
            sqlDataOp = new java.sql.Date(inicio.getTime());
            alterarUnidade.setDate(9, sqlDataOp);
            alterarUnidade.setInt(10, id);
                        
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarUnidade.executeUpdate();
            alterarUnidade.getQueryTimeout();
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Deleta uma linha selecionada da entidade UNIDADE do banco de dados.
     * 
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int deleteUnidade(int id) {
        
        int resultado = 0;        
        try {
            deletarUnidade.setInt(1, id);
                        
            // executa a operação; retorna número de linhas atualizadas.
            resultado = deletarUnidade.executeUpdate();
            deletarUnidade.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    // -------------------------------------------------------------------------
    // Métodos da classe Subunidade
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade SUBUNIDADE do banco de dados com as
     * informações dadas pelo usuário.
     * 
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertSubunidade(String descricao, int id) {
        int resultado = 0;        
        try {
            inserirSubunidade.setString(1, descricao);
            inserirSubunidade.setObject(2, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirSubunidade.executeUpdate();
            inserirSubunidade.getQueryTimeout();
            
            // Pega os ID gerados automaticamente na inserção.
            resultSet = inserirSubunidade.getGeneratedKeys();
            if(resultSet.next()) {
                idSubunidade = resultSet.getInt(1);
            }
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Altera uma linha selecionada da entidade SUBUNIDADE do banco de dados
     * com as novas informações dadas pelo usuário.
     * 
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int updateSubunidade(String descricao, int id) {
        int resultado = 0;        
        try {
            alterarSubunidade.setString(1, descricao);
            alterarSubunidade.setObject(2, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarSubunidade.executeUpdate();
            alterarSubunidade.getQueryTimeout();
            
        } // fim do try
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch
        
        return resultado;
    }
    
    /**
     * Deleta uma linha selecionada da entidade SUBUNIDADE do banco de dados.
     * 
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int deleteSubunidade(int id) {
        int resultado = 0;        
        try {
            deletarSubunidade.setInt(1, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = deletarSubunidade.executeUpdate();
            deletarSubunidade.getQueryTimeout();
            
        } // fim do try
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch
        
        return resultado;
    }
    
    // -------------------------------------------------------------------------
    // Métodos da classe Componente
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade COMPONENTE do banco de dados com as
     * informações dadas pelo usuário.
     * 
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertComponente(String descricao, int id) {
        int resultado = 0;        
        try {
            inserirComponente.setString(1, descricao);
            inserirComponente.setObject(2, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirComponente.executeUpdate();
            inserirComponente.getQueryTimeout();
            
            // Pega os ID gerados automaticamente na inserção.
            resultSet = inserirComponente.getGeneratedKeys();
            if(resultSet.next()) {
                idComponente = resultSet.getInt(1);
            }
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Altera uma linha selecionada da entidade COMPONENTE do banco de dados
     * com as novas informações dadas pelo usuário.
     * 
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int updateComponente(String descricao, int id) {
        int resultado = 0;        
        try {
            alterarComponente.setString(1, descricao);
            alterarComponente.setObject(2, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarComponente.executeUpdate();
            alterarComponente.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Deleta uma linha selecionada da entidade COMPONENTE do banco de dados.
     * 
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int deleteComponente(int id) {
        int resultado = 0;        
        try {
            deletarComponente.setInt(1, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = deletarComponente.executeUpdate();
            deletarComponente.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    // -------------------------------------------------------------------------
    // Métodos da classe Parte
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade PARTE do banco de dados com as
     * informações dadas pelo usuário.
     * 
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertParte(String descricao, int id) {
        int resultado = 0;        
        try {
            inserirParte.setString(1, descricao);
            inserirParte.setObject(2, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirParte.executeUpdate();
            inserirParte.getQueryTimeout();

        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Altera uma linha selecionada da entidade PARTE do banco de dados
     * com as novas informações dadas pelo usuário.
     * 
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int updateParte(String descricao, int id) {
        int resultado = 0;        
        try {
            alterarParte.setString(1, descricao);
            alterarParte.setObject(2, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarParte.executeUpdate();
            alterarParte.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Deleta uma linha selecionada da entidade PARTE do banco de dados.
     * 
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int deleteParte(int id) {
        int resultado = 0;        
        try {
            deletarParte.setInt(1, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = deletarParte.executeUpdate();
            deletarParte.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }

    // -------------------------------------------------------------------------
    // Métodos da classe Intervenção
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade INTERVENCAO do banco de dados com as
     * informações dadas pelo usuário.
     * 
     * @param categoria
     * @param atividade
     * @param inicio
     * @param termino
     * @param idUni
     * @param idSub
     * @param idCp
     * @param idPte
     * @param idDem
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertIntervencao(String categoria, String atividade,
                java.util.Date inicio, java.util.Date termino, int idUni,
                int idSub, int idCp, int idPte, int idDem) {
        /* Timestamp: formato SQL "data (yyyy-MM-dd) + hora (HH:mm:ss) */
        java.sql.Timestamp sqlInicio = new java.sql.Timestamp(inicio.getTime());
        
        int resultado = 0;
        try {
            inserirIntervencao.setString(1, categoria);
            inserirIntervencao.setString(2, atividade);
            inserirIntervencao.setTimestamp(3, sqlInicio);
            // Se data e hora de término da intervenção não forem informadas,
            // insere NULL.
            if(termino == null) {
                inserirIntervencao.setNull(4, java.sql.Types.TIMESTAMP);                
            } else {
                java.sql.Timestamp sqlTermino;
                sqlTermino = new java.sql.Timestamp(termino.getTime());
                inserirIntervencao.setTimestamp(4, sqlTermino);
            }
            inserirIntervencao.setInt(5, idUni);
            // Se a subunidade, o componente, a parte e/ou a demanda
            // não forem informados, insere NULL.
            if(idSub == 0) {
                inserirIntervencao.setNull(6, java.sql.Types.INTEGER);}
            else {inserirIntervencao.setInt(6, idSub);}
            if(idCp == 0) {
                inserirIntervencao.setNull(7, java.sql.Types.INTEGER);}
            else {inserirIntervencao.setInt(7, idCp);}
            if(idPte == 0) {
                inserirIntervencao.setNull(8, java.sql.Types.INTEGER);}
            else {inserirIntervencao.setInt(8, idPte);}
            if(idDem == 0) {
                inserirIntervencao.setNull(9, java.sql.Types.INTEGER);}
            else {inserirIntervencao.setInt(9, idDem);}
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirIntervencao.executeUpdate();
            inserirIntervencao.getQueryTimeout();

            // Pega os ID gerados automaticamente na inserção.
            resultSet = inserirIntervencao.getGeneratedKeys();
            if(resultSet.next()) {
                idIntervencao = resultSet.getInt(1);
            }
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Altera uma linha selecionada da entidade INTERVENCAO do banco de dados
     * com as novas informações dadas pelo usuário.
     * 
     * @param categoria
     * @param atividade
     * @param inicio
     * @param termino
     * @param idDem
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int uptadeIntervencao(String categoria, String atividade, 
            java.util.Date inicio, java.util.Date termino, int idDem, int id) {
        /* Timestamp: formato SQL "data (yyyy-MM-dd) + hora (HH:mm:ss) */
        java.sql.Timestamp sqlInicio = new java.sql.Timestamp(inicio.getTime());
        
        int resultado = 0;
        try {
            alterarIntervencao.setString(1, categoria);
            alterarIntervencao.setString(2, atividade);
            alterarIntervencao.setTimestamp(3, sqlInicio);
            // Se data e hora de término da intervenção não forem informadas,
            // insere NULL.
            if(termino == null) {
                alterarIntervencao.setNull(4, java.sql.Types.TIMESTAMP);                
            } else {
                java.sql.Timestamp sqlTermino;
                sqlTermino = new java.sql.Timestamp(termino.getTime());
                alterarIntervencao.setTimestamp(4, sqlTermino);
            }
            if(idDem == 0) {
                alterarIntervencao.setNull(5, java.sql.Types.INTEGER);}
            else {alterarIntervencao.setInt(5, idDem);}
            alterarIntervencao.setInt(6, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarIntervencao.executeUpdate();
            alterarIntervencao.getQueryTimeout();
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Deleta uma linha selecionada da entidade PARTE do banco de dados.
     * 
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int deleteIntervencao(int id) {
        int resultado = 0;
        try {
            deletarIntervencao.setInt(1, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = deletarIntervencao.executeUpdate();
            deletarIntervencao.getQueryTimeout();
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    // -------------------------------------------------------------------------
    // Métodos da classe Demanda
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade DEMANDA do banco de dados com as
     * informações dadas pelo usuário.
     *
     * @param data
     * @param modo
     * @param impacto
     * @param causa
     * @param modoOperacional 
     * @param idUni
     * @param idSub
     * @param idCp
     * @param idPte
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertDemanda(java.util.Date data, String modo,
                String impacto, String causa, String modoOperacional, int idUni,
                int idSub, int idCp, int idPte) {
        /* Timestamp: formato SQL "data (yyyy-MM-dd) + hora (HH:mm:ss)" */
        java.sql.Timestamp sqlData = new java.sql.Timestamp(data.getTime());
        
        int resultado = 0;        
        try {
            // setString(numero do parametro da tabela, valor a ser inserido)
            inserirDemanda.setTimestamp(1, sqlData);
            if(modo == null) {
                inserirDemanda.setNull(2, java.sql.Types.VARCHAR);
            } else {inserirDemanda.setString(2, modo);}
            if(impacto == null) {
                inserirDemanda.setNull(3, java.sql.Types.VARCHAR);
            } else {inserirDemanda.setString(3, impacto);}
            if(causa == null) {
                inserirDemanda.setNull(4, java.sql.Types.VARCHAR);
            } else {inserirDemanda.setString(4, causa);}
            if(modoOperacional == null) {
                inserirDemanda.setNull(5, java.sql.Types.VARCHAR);
            } else {inserirDemanda.setString(5, modoOperacional);}
            inserirDemanda.setInt(6, idUni);
            if(idSub == 0) {inserirDemanda.setNull(7, java.sql.Types.INTEGER);}
                else {inserirDemanda.setInt(7, idSub);}
            if(idCp == 0) {inserirDemanda.setNull(8, java.sql.Types.INTEGER);}
                else {inserirDemanda.setInt(8, idCp);}
            if(idPte == 0) {inserirDemanda.setNull(9, java.sql.Types.INTEGER);}
                else {inserirDemanda.setInt(9, idPte);}
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirDemanda.executeUpdate();
            inserirDemanda.getQueryTimeout();
            
            // Pega os ID gerados automaticamente na inserção.
            resultSet = inserirDemanda.getGeneratedKeys();
            if(resultSet.next()) {
                idDemanda = resultSet.getInt(1);
            }
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Altera uma linha selecionada da entidade DEMANDA do banco de dados com as
     * novas informações dadas pelo usuário.
     * 
     * @param data
     * @param modo
     * @param impacto
     * @param causa
     * @param modoOperacional
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int updateDemanda(java.util.Date data, String modo,
                String impacto, String causa, String modoOperacional, int id) {
        /* Timestamp: formato SQL "data (yyyy-MM-dd) + hora (HH:mm:ss) */
        java.sql.Timestamp sqlData = new java.sql.Timestamp(data.getTime());
        
        int resultado = 0;
        try {
            alterarDemanda.setTimestamp(1, sqlData);
            if(modo == null) {
                alterarDemanda.setNull(2, java.sql.Types.VARCHAR);
            } else {alterarDemanda.setString(2, modo);}
            if(impacto == null) {
                alterarDemanda.setNull(3, java.sql.Types.VARCHAR);
            } else {alterarDemanda.setString(3, impacto);}
            if(causa == null) {
                alterarDemanda.setNull(4, java.sql.Types.VARCHAR);
            } else {alterarDemanda.setString(4, causa);}
            if(modoOperacional == null) {
                alterarDemanda.setNull(5, java.sql.Types.VARCHAR);
            } else {alterarDemanda.setString(5, modoOperacional);}
            alterarDemanda.setInt(6, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarDemanda.executeUpdate();
            alterarDemanda.getQueryTimeout();
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Deleta uma linha selecionada da entidade PARTE do banco de dados.
     * 
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int deleteDemanda(int id) {
        
        int resultado = 0;
        try {
            deletarDemanda.setInt(1, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = deletarDemanda.executeUpdate();
            deletarDemanda.getQueryTimeout();
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    // -------------------------------------------------------------------------
    // Métodos da classe Interventor
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade INTERVENTOR do banco de dados com as
     * informações dadas pelo usuário.
     * 
     * @param nome
     * @param sexo
     * @param dataNascimento
     * @param dataAdmissao
     * @param cargo
     * @param formacao
     * @param esp
     * @param remuneracao
     * @param estadoCivil
     * @param endereco
     * @param cidade
     * @param estado
     * @param contato
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertInterventor(String nome, String sexo,
                java.util.Date dataNascimento, java.util.Date dataAdmissao,
                String cargo, String formacao, String esp, float remuneracao,
                String estadoCivil, String endereco, String cidade,
                String estado, String contato) {

        int resultado = 0;        
        try {
            inserirInterventor.setString(1, nome);
            // Se sexo, data de nascimento, data de admissão, cargo, formação,
            // estado civil e/ou estado não forem informados, insere NULL.
            if(sexo.isEmpty()) {
                inserirInterventor.setNull(2, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(2, sexo);}
            if(dataNascimento == null) {
                inserirInterventor.setNull(3, java.sql.Types.DATE);                
            } else {
                java.sql.Date sqlDataNasc;
                sqlDataNasc = new java.sql.Date(dataNascimento.getTime());
                inserirInterventor.setDate(3, sqlDataNasc);
            }
            if(dataAdmissao == null) {
                inserirInterventor.setNull(4, java.sql.Types.DATE);                
            } else {
                java.sql.Date sqlDataAdms;
                sqlDataAdms = new java.sql.Date(dataAdmissao.getTime());
                inserirInterventor.setDate(4, sqlDataAdms);
            }
            if(cargo.isEmpty()) {
                inserirInterventor.setNull(5, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(5, cargo);}
            if(formacao.isEmpty()) {
                inserirInterventor.setNull(6, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(6, formacao);}
            if(esp.isEmpty()) {
                inserirInterventor.setNull(7, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(7, esp);}
            inserirInterventor.setFloat(8, remuneracao);
            if(estadoCivil.isEmpty()) {
                inserirInterventor.setNull(9, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(9, estadoCivil);}
            if(endereco.isEmpty()) {
                inserirInterventor.setNull(10, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(10, endereco);}
            if(cidade.isEmpty()) {
                inserirInterventor.setNull(11, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(11, cidade);}
            if(estado.isEmpty()) {
                inserirInterventor.setNull(12, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(12, estado);}
            if(contato.isEmpty()) {
                inserirInterventor.setNull(13, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(13, contato);}
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirInterventor.executeUpdate();
            inserirInterventor.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Altera uma linha selecionada da entidade DEMANDA do banco de dados com as
     * novas informações dadas pelo usuário.
     * 
     * @param nome
     * @param sexo
     * @param dataNascimento
     * @param dataAdmissao
     * @param cargo
     * @param formacao
     * @param esp
     * @param remuneracao
     * @param estadoCivil
     * @param endereco
     * @param cidade
     * @param estado
     * @param contato
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int updateInterventor(String nome, String sexo,
                java.util.Date dataNascimento, java.util.Date dataAdmissao,
                String cargo, String formacao, String esp, float remuneracao,
                String estadoCivil, String endereco, String cidade,
                String estado, String contato, int id) {
        
        int resultado = 0;
        try {
            alterarInterventor.setString(1, nome);
            // Se sexo, data de nascimento, data de admissão, cargo, formação,
            // estado civil e/ou estado não forem informados, insere NULL.
            if(sexo.isEmpty()) {
                alterarInterventor.setNull(2, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(2, sexo);}
            if(dataNascimento == null) {
                alterarInterventor.setNull(3, java.sql.Types.DATE);                
            } else {
                java.sql.Date sqlDataNasc;
                sqlDataNasc = new java.sql.Date(dataNascimento.getTime());
                alterarInterventor.setDate(3, sqlDataNasc);
            }
            if(dataAdmissao == null) {
                alterarInterventor.setNull(4, java.sql.Types.DATE);                
            } else {
                java.sql.Date sqlDataAdms;
                sqlDataAdms = new java.sql.Date(dataAdmissao.getTime());
                alterarInterventor.setDate(4, sqlDataAdms);
            }
            if(cargo.isEmpty()) {
                alterarInterventor.setNull(5, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(5, cargo);}
            if(formacao.isEmpty()) {
                alterarInterventor.setNull(6, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(6, formacao);}
            if(esp.isEmpty()) {
                alterarInterventor.setNull(7, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(7, esp);}
            alterarInterventor.setFloat(8, remuneracao);
            if(estadoCivil.isEmpty()) {
                alterarInterventor.setNull(9, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(9, estadoCivil);}
            if(endereco.isEmpty()) {
                alterarInterventor.setNull(10, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(10, endereco);}
            if(cidade.isEmpty()) {
                alterarInterventor.setNull(11, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(11, cidade);}
            if(estado.isEmpty()) {
                alterarInterventor.setNull(12, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(12, estado);}
            if(contato.isEmpty()) {
                alterarInterventor.setNull(13, java.sql.Types.VARCHAR);
            } else {alterarInterventor.setString(13, contato);}
            alterarInterventor.setInt(14, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarInterventor.executeUpdate();
            alterarInterventor.getQueryTimeout();
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Deleta uma linha selecionada da entidade INTERVENTOR do banco de dados.
     * 
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int deleteInterventor(int id) {
        
        int resultado = 0;        
        try {
            deletarInterventor.setInt(1, id);
                        
            // executa a operação; retorna número de linhas atualizadas.
            resultado = deletarInterventor.executeUpdate();
            deletarInterventor.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    // -------------------------------------------------------------------------
    // Métodos da classe Equipe
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade EQUIPE do banco de dados com as informações
     * dadas pelo usuário.
     * 
     * @param objetivoGeral
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertEquipe(String objetivoGeral) {
        
        int resultado = 0;        
        try {
            /**
             * Insere um NULL no campo "id_intervencao" porque na criação do
             * equipe não é vinculada nenhuma intervenção. Quando uma
             * intervenção for vinculada a esta equipe, o campo será alterado.
             */
            inserirEquipe.setString(1, objetivoGeral);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirEquipe.executeUpdate();
            inserirEquipe.getQueryTimeout();
            
            // Pega os ID gerados automaticamente na inserção.
            resultSet = inserirEquipe.getGeneratedKeys();
            if(resultSet.next()) {
                idEquipe = resultSet.getInt(1);
            }
        } // fim do try. // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Deleta uma linha selecionada da entidade EQUIPE do banco de dados.
     * 
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int deleteEquipe(int id) {
        int resultado = 0;        
        try {
            deletarEquipe.setInt(1, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = deletarEquipe.executeUpdate();
            deletarEquipe.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Atualiza uma linha selecionada da entidade EQUIPE do banco de dados.
     * 
     * @param obj
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int updateEquipe(String obj, int id) {
        int resultado = 0;        
        try {
            alterarEquipe.setString(1, obj);
            alterarEquipe.setInt(2, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = alterarEquipe.executeUpdate();
            alterarEquipe.getQueryTimeout();
            
            // Pega os ID gerados automaticamente na inserção.
            resultSet = alterarEquipe.getGeneratedKeys();
            if(resultSet.next()) {
                idEquipe = resultSet.getInt(1);
            }
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Adiciona uma linha à entidade OBJETIVOSESPECIFICOS do banco de dados
     * com as informações dadas pelo usuário.
     * 
     * @param idTime
     * @param descricao
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertObjetivoEspecifico(int idTime, String descricao) {
        
        int resultado = 0;        
        try {
            inserirObjetivoEspecifico.setInt(1, idTime);
            if(descricao.isEmpty()) {
                inserirObjetivoEspecifico.setNull(2,java.sql.Types.VARCHAR);
            } else {inserirObjetivoEspecifico.setString(2, descricao);}            
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirObjetivoEspecifico.executeUpdate();
            inserirObjetivoEspecifico.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     *
     * @param des
     * @param id
     * @param idEquipe
     * @return
     */
    public static int updateObjetivoEspecifico(String des, int id, int idEquipe) {
        
        int res = 0;
        try {
            alterarObjetivoEspecifico.setString(1, des);
            alterarObjetivoEspecifico.setInt(2, id);
            alterarObjetivoEspecifico.setInt(3, idEquipe);
            
            res = alterarObjetivoEspecifico.executeUpdate();
            alterarObjetivoEspecifico.getQueryTimeout();
            
        } catch (SQLException e) {
            DialogoAviso.show(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return res;
    }
    
    public static int deleteObjetivoEspecifico(int id) {
        int r = 0;
        try {
            deletarObjetivoEspecifico.setInt(1, id);
            
            r = deletarObjetivoEspecifico.executeUpdate();
            deletarObjetivoEspecifico.getQueryTimeout();
        } catch(SQLException e) {
            DialogoAviso.show(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return r;
    }
    
    /**
     * Adiciona uma linha à entidade HABILIDADESREQUERIDAS do banco de dados
     * com as informações dadas pelo usuário.
     * 
     * @param idTime
     * @param descricao
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertHabilidade(int idTime, String descricao) {
        
        int resultado = 0;        
        try {
            inserirHabilidadeRequerida.setInt(1, idTime);
            if(descricao.isEmpty()) {
                inserirHabilidadeRequerida.setNull(2,java.sql.Types.VARCHAR);
            } else {inserirHabilidadeRequerida.setString(2, descricao);}
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirHabilidadeRequerida.executeUpdate();
            inserirHabilidadeRequerida.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     *
     * @param des
     * @param id
     * @param idEquipe
     * @return
     */
    public static int updateHabilidade(String des, int id, int idEquipe) {
        
        int res = 0;
        try {
            alterarHabilidadeRequerida.setString(1, des);
            alterarHabilidadeRequerida.setInt(2, id);
            alterarHabilidadeRequerida.setInt(3, idEquipe);
            
            res = alterarHabilidadeRequerida.executeUpdate();
            alterarHabilidadeRequerida.getQueryTimeout();
            
        } catch (SQLException e) {
            DialogoAviso.show(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return res;
    }
    
    public static int deleteHabilidade(int id) {
        int r = 0;
        try {
            deletarHabilidadeRequerida.setInt(1, id);
            
            r = deletarHabilidadeRequerida.executeUpdate();
            deletarHabilidadeRequerida.getQueryTimeout();
        } catch(SQLException e) {
            DialogoAviso.show(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return r;
    }
    
    /**
     * Adiciona uma linha à entidade EXPERIENCIASSREQUERIDAS do banco de dados
     * com as informações dadas pelo usuário.
     * 
     * @param idEquipe
     * @param descricao
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertExperiencia(int idEquipe, String descricao) {
        
        int resultado = 0;        
        try {
            inserirExperienciaRequerida.setInt(1, idEquipe);
            if(descricao.isEmpty()) {
                inserirExperienciaRequerida.setNull(2,java.sql.Types.VARCHAR);
            } else {inserirExperienciaRequerida.setString(2, descricao);}
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirExperienciaRequerida.executeUpdate();
            inserirExperienciaRequerida.getQueryTimeout();
            
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return resultado;
    }
    
    /**
     *
     * @param des
     * @param id
     * @param idEquipe
     * @return
     */
    public static int updateExperiencia(String des, int id, int idEquipe) {
        
        int res = 0;
        try {
            alterarExperienciaRequerida.setString(1, des);
            alterarExperienciaRequerida.setInt(2, id);
            alterarExperienciaRequerida.setInt(3, idEquipe);
            
            res = alterarExperienciaRequerida.executeUpdate();
            alterarExperienciaRequerida.getQueryTimeout();
            
        } catch (SQLException e) {
            DialogoAviso.show(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return res;
    }
    
    public static int deleteExperiencia(int id) {
        int r = 0;
        try {
            deletarExperienciaRequerida.setInt(1, id);
            
            r = deletarExperienciaRequerida.executeUpdate();
            deletarExperienciaRequerida.getQueryTimeout();
        } catch(SQLException e) {
            DialogoAviso.show(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        return r;
    }
    
    /**
     * Adiciona uma linha à entidade AUX_EQUIPE do banco de dados
     * com as informações dadas pelo usuário.
     * 
     * @param idEquipe
     * @param idIntv
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int vincularEquipe(int idEquipe, int idIntv) {
        
        int res = 0;
        try {
            vincularEquipe.setInt(1, idEquipe);
            vincularEquipe.setInt(2, idIntv);
            
            res = vincularEquipe.executeUpdate();
            vincularEquipe.getQueryTimeout();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return res;
    }
    
    /**
     * Remove todas as linhas da entidade AUX_EQUIPE do banco de dados
     * nas quais têm o id da equipe informado pelo usuário.
     * 
     * @param idEquipe
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int desvincularEquipe(int idEquipe) {
        
        int res = 0;
        try {
            desvincularEquipe.setInt(1, idEquipe);
            
            res = desvincularEquipe.executeUpdate();
            desvincularEquipe.getQueryTimeout();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return res;
    }
    
    /**
     * Adiciona uma linha à entidade AUX_INTERVENCAO do banco de dados
     * com as informações dadas pelo usuário.
     * 
     * @param idEquipe
     * @param idIntv
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int vincularIntervencao(int idEquipe, int idIntv) {
        
        int res = 0;
        try {
            vincularIntervencao.setInt(1, idEquipe);
            vincularIntervencao.setInt(2, idIntv);
            
            res = vincularIntervencao.executeUpdate();
            vincularIntervencao.getQueryTimeout();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return res;
    }
    
    /**
     * Tenta conctar ao banco de dados e captura possíveis exceções, exibindo
     * diálogo de "erro".
     */
    public static void testarConexão() {
        try {
            System.out.println("Testando conexão com o servidor...\n");
            connection = DriverManager.getConnection(
                        BD_URL, USERNAME, PASSWORD);
        } // fim do try.
        catch(SQLException e) {
            /* Se houver erro, exibe mensagem de erro */
            DialogoAviso.show(e.getLocalizedMessage());
            e.printStackTrace();
        }
        
        System.out.println("Conexão estabelecida.");
        conectado = true;
    }
    
    /**
     * Conecta ao banco de dados e cria os preparedStatements
     */
    public static void conectar() {
        try {
            testarConexão();
            
            /* Definição de métodos para a classe SISTEMA */
            inserirSistema = connection.prepareStatement(
                "INSERT INTO sistema (nome, descricao) VALUES (?, ?);");
            alterarSistema = connection.prepareStatement(
                "UPDATE sistema SET nome = ?, descricao = ? WHERE id = ?;");

            /* Definição de métodos para a classe UNIDADE */
            inserirUnidade = connection.prepareStatement(
                "INSERT INTO unidade " + 
                    "(classe, tipo, fabricante, identificacao, categoria, " +
                    "localizacao, data_aquisicao, modo_operacional, " +
                    "data_inicio_operacao, id_sistema) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            alterarUnidade = connection.prepareStatement(
                "UPDATE unidade SET " +
                    "classe = ?, tipo = ?, fabricante = ?, identificacao = ?, "+
                    "categoria = ?, localizacao = ?, data_aquisicao = ?, " +
                    "modo_operacional = ?, data_inicio_operacao = ? " +
                "WHERE id = ?;");
            deletarUnidade = connection.prepareStatement(
                "DELETE FROM unidade WHERE id = ?;");
            
            /* Definição de métodos para a classe SUBUNIDADE */
            inserirSubunidade = connection.prepareStatement(
                "INSERT INTO subunidade" +
                    "(descricao, id_unidade) VALUES (?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            alterarSubunidade = connection.prepareStatement(
                "UPDATE subunidade SET descricao = ? WHERE id = ?");
            deletarSubunidade = connection.prepareStatement(
                "DELETE FROM subunidade WHERE id = ?;");            
            
            /* Definição de métodos para a classe COMPONENTE */
            inserirComponente = connection.prepareStatement(
                "INSERT INTO componente" +
                    "(descricao, id_subunidade)" +
                "VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            alterarComponente = connection.prepareStatement(
                "UPDATE componente SET descricao = ? WHERE id = ?");
            deletarComponente = connection.prepareStatement(
                "DELETE FROM componente WHERE id = ?;");
            
            /* Definição de métodos para a classe PARTE */
            inserirParte = connection.prepareStatement(
                "INSERT INTO parte" +
                    "(descricao, id_componente)" +
                "VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            alterarParte = connection.prepareStatement(
                "UPDATE parte SET descricao = ? WHERE id = ?");
            deletarParte = connection.prepareStatement(
                "DELETE FROM parte WHERE id = ?;");
            
            /* Definição de métodos para a classe DEMANDA */
            inserirDemanda = connection.prepareStatement(
                "INSERT INTO demanda" +
                    "(data, modo, impacto, causa, modo_operacional, "
                    + "id_unidade, id_subunidade, id_componente, id_parte)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            alterarDemanda = connection.prepareStatement(
                "UPDATE demanda SET " +
                    "data = ?, modo = ?, impacto = ?, causa = ?, " +
                    "modo_operacional = ? WHERE id = ?");
            deletarDemanda = connection.prepareStatement(
                "DELETE FROM demanda WHERE id = ?");

            /* Definição de métodos para a classe INTERVENÇÃO */
            inserirIntervencao = connection.prepareStatement(
                "INSERT INTO intervencao" +
                    "(categoria, atividade, inicio, termino, id_unidade, "
                    + "id_subunidade, id_componente, id_parte, id_demanda)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            alterarIntervencao = connection.prepareStatement(
                "UPDATE intervencao SET " +
                    "categoria = ?, atividade = ?, inicio = ?, termino = ?, "
                    + "id_demanda = ? WHERE id = ?");
            deletarIntervencao = connection.prepareStatement(
                "DELETE FROM intervencao WHERE id = ?");
            
            /* Definição de métodos para a classe INTERVENTOR */
            inserirInterventor = connection.prepareStatement(
                "INSERT INTO interventor" +
                    "(nome, sexo, nascimento, admissao, cargo, formacao, "
                    + "especializacao, remuneracao, estado_civil, endereco, "
                    + "cidade, estado, contato)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            alterarInterventor = connection.prepareStatement(
                "UPDATE interventor SET " +
                    "nome = ?, sexo = ?, nascimento = ?, admissao = ?, " + 
                    "cargo = ?, formacao = ?, especializacao = ?, " +
                    "remuneracao = ?, estado_civil = ?, endereco = ?, " +
                    "cidade = ?, estado = ?, contato = ? WHERE id = ?",
                        Statement.RETURN_GENERATED_KEYS);
            deletarInterventor = connection.prepareStatement(
                "DELETE FROM interventor WHERE id = ?;");
            
            /* Definição de métodos para a classe EQUIPE */
            inserirEquipe = connection.prepareStatement(
                "INSERT INTO equipe (objetivo_geral) VALUES (?);",
                        Statement.RETURN_GENERATED_KEYS);
            alterarEquipe = connection.prepareStatement(
                "UPDATE equipe SET objetivo_geral = ? WHERE id = ?;",
                        Statement.RETURN_GENERATED_KEYS);
            deletarEquipe = connection.prepareStatement(
                "DELETE FROM equipe WHERE id = ?");
            
            inserirObjetivoEspecifico = connection.prepareStatement(
                "INSERT INTO objetivos_especificos" +
                    "(id_equipe, descricao)" +
                    "VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            alterarObjetivoEspecifico = connection.prepareStatement(
                "UPDATE objetivos_especificos SET descricao = ? WHERE id = ? AND id_equipe = ?;");
            deletarObjetivoEspecifico = connection.prepareStatement(
                        "DELETE FROM objetivos_especificos WHERE id = ?;");
            
            inserirHabilidadeRequerida = connection.prepareStatement(
                "INSERT INTO habilidades_requeridas" +
                    "(id_equipe, descricao)" +
                    "VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            alterarHabilidadeRequerida = connection.prepareStatement(
                "UPDATE habilidades_requeridas SET descricao = ? WHERE id = ? AND id_equipe = ?;");
            deletarHabilidadeRequerida = connection.prepareStatement(
                        "DELETE FROM habilidades_requeridas WHERE id = ?;");
            
            inserirExperienciaRequerida = connection.prepareStatement(
                "INSERT INTO experiencias_requeridas" +
                    "(id_equipe, descricao)" +
                    "VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            alterarExperienciaRequerida = connection.prepareStatement(
                "UPDATE experiencias_requeridas SET descricao = ? WHERE id = ? AND id_equipe = ?;");
            deletarExperienciaRequerida = connection.prepareStatement(
                        "DELETE FROM experiencias_requeridas WHERE id = ?;");
            
            /* Definição de métodos para a relação EQUIPE-INTERVENTOR */
            vincularEquipe = connection.prepareStatement(
                "INSERT INTO aux_equipe (id_equipe, id_interventor) " + 
                    "VALUES (?, ?)");
            desvincularEquipe = connection.prepareStatement(
                "DELETE FROM aux_equipe WHERE id_equipe = ?");
            
            /* Definição de métodos para a relação EQUIPE-INTERVENCAO */
            vincularIntervencao = connection.prepareStatement(
                "INSERT INTO aux_intervencao (id_equipe, id_intervencao) " +
                    "VALUES (?, ?)");
            
        }
        catch(SQLException sqlException) {
            /* Se houver erro, exibe mensagem de erro */
            DialogoAviso.show("Erro ao conectar ao banco de dados");
            
            /* Printa no prompt a exceção */
            sqlException.printStackTrace();
        } // fim do catch.
    }
    
    /**
     * Encerra a conexão com o banco de dados.
     */
    public static void desconectar() {
        try {
            connection.close();
            conectado = false;
        }
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
}