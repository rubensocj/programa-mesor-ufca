package conexaoSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Consulta.java
 * 
 * @version 1.0 4/2/2017
 * @author Rubens Jr
 */
public class Consulta {
    
//    private static final String BD_HOST = "192.168.1.12:3306/";
    // Virtual host
    private static final String V_HOST = "mesor.ufca.rubens.ocj";
    // Nome do banco de dados.
    private static final String BD_NAME = "bdprograma";
    // Host do banco de dados
    private static final String BD_HOST = V_HOST + ":3306/";
    private static final String BD_URL = "jdbc:mysql://" + BD_HOST + BD_NAME;
    
    // Acesso ao servidor: usuário e senha.
    private static final String USERNAME = "mesor";
    private static final String PASSWORD = "mesorufca1506";
    
    // gerencia a conexão.
    public static Connection connection = null;
    
    private static ResultSet resultSet = null;
    
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
                alterarIntervencao = null;
    
    /**
     * Consulta em SQL previamente preparada que exclui do banco de dados
     * elementos selecionados pelo usuário.
     */
    private static PreparedStatement deletarUnidade = null,
                deletarSubunidade = null,
                deletarComponente = null,
                deletarParte = null,
                deletarDemanda = null,
                deletarIntervencao = null;
    
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
     * @param cod
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
    public static int insertIntervencao(String cod, String categoria,
                String atividade, java.util.Date inicio, java.util.Date termino,
                int idUni, int idSub, int idCp, int idPte, int idDem) {
        /* Timestamp: formato SQL "data (yyyy-MM-dd) + hora (HH:mm:ss) */
        java.sql.Timestamp sqlInicio = new java.sql.Timestamp(inicio.getTime());
        
        int resultado = 0;
        try {
            inserirIntervencao.setString(1, cod);
            inserirIntervencao.setString(2, categoria);
            inserirIntervencao.setString(3, atividade);
            inserirIntervencao.setTimestamp(4, sqlInicio);
            // Se data e hora de término da intervenção não forem informadas,
            // insere NULL.
            if(termino == null) {
                inserirIntervencao.setNull(5, java.sql.Types.TIMESTAMP);                
            } else {
                java.sql.Timestamp sqlTermino;
                sqlTermino = new java.sql.Timestamp(termino.getTime());
                inserirIntervencao.setTimestamp(5, sqlTermino);
            }
            inserirIntervencao.setInt(6, idUni);
            // Se a subunidade, o componente, a parte e/ou a demanda
            // não forem informados, insere NULL.
            if(idSub == 0) {
                inserirIntervencao.setNull(7, java.sql.Types.INTEGER);}
            else {inserirIntervencao.setInt(7, idSub);}
            if(idCp == 0) {
                inserirIntervencao.setNull(8, java.sql.Types.INTEGER);}
            else {inserirIntervencao.setInt(8, idCp);}
            if(idPte == 0) {
                inserirIntervencao.setNull(9, java.sql.Types.INTEGER);}
            else {inserirIntervencao.setInt(9, idPte);}
            if(idDem == 0) {
                inserirIntervencao.setNull(10, java.sql.Types.INTEGER);}
            else {inserirIntervencao.setInt(10, idDem);}
            
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
     * @param idTime
     * @param nome
     * @param sexo
     * @param dataNascimento
     * @param dataAdmissao
     * @param cargo
     * @param formacao
     * @param remuneracao
     * @param estadoCivil
     * @param endereco
     * @param estado
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertInterventor(int idTime, String nome, String sexo,
                java.util.Date dataNascimento, java.util.Date dataAdmissao,
                String cargo, String formacao, float remuneracao,
                String estadoCivil, String endereco, String estado) {

        int resultado = 0;        
        try {
            inserirInterventor.setInt(1, idTime);
            inserirInterventor.setString(2, nome);
            // Se sexo, data de nascimento, data de admissão, cargo, formação,
            // estado civil e/ou estado não forem informados, insere NULL.
            if(sexo.isEmpty()) {
                inserirInterventor.setNull(3, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(3, sexo);}
            if(dataNascimento == null) {
                inserirInterventor.setNull(4, java.sql.Types.DATE);                
            } else {
                java.sql.Date sqlDataNasc;
                sqlDataNasc = new java.sql.Date(dataNascimento.getTime());
                inserirInterventor.setDate(4, sqlDataNasc);
            }
            if(dataAdmissao == null) {
                inserirInterventor.setNull(5, java.sql.Types.DATE);                
            } else {
                java.sql.Date sqlDataAdms;
                sqlDataAdms = new java.sql.Date(dataAdmissao.getTime());
                inserirInterventor.setDate(5, sqlDataAdms);
            }
            if(cargo.isEmpty()) {
                inserirInterventor.setNull(6, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(6, cargo);}
            if(formacao.isEmpty()) {
                inserirInterventor.setNull(7, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(7, formacao);}
            inserirInterventor.setFloat(8, remuneracao);
            if(estadoCivil.isEmpty()) {
                inserirInterventor.setNull(9, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(9, estadoCivil);}
            if(endereco.isEmpty()) {
                inserirInterventor.setNull(10, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(10, endereco);}
            if(estado.isEmpty()) {
                inserirInterventor.setNull(11, java.sql.Types.VARCHAR);
            } else {inserirInterventor.setString(11, estado);}
            
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
    
    // -------------------------------------------------------------------------
    // Métodos da classe Equipe
    // -------------------------------------------------------------------------
    
    /**
     * Adiciona uma linha à entidade EQUIPE do banco de dados com as informações
     * dadas pelo usuário.
     * 
     * @param objetivoGeral
     * @param idIntervencao
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertTime(String objetivoGeral, int idIntervencao) {
        
        int resultado = 0;        
        try {
            /**
             * Insere um NULL no campo "id_intervencao" porque na criação do
             * equipe não é vinculada nenhuma intervenção. Quando uma
             * intervenção for vinculada a esta equipe, o campo será alterado.
             */
            inserirEquipe.setString(1, objetivoGeral);
            inserirEquipe.setNull(2, java.sql.Types.INTEGER);
            
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
     * Adiciona uma linha à entidade EXPERIENCIASSREQUERIDAS do banco de dados
     * com as informações dadas pelo usuário.
     * 
     * @param idTime
     * @param descricao
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public static int insertExperiencia(int idTime, String descricao) {
        
        int resultado = 0;        
        try {
            inserirExperienciaRequerida.setInt(1, idTime);
            if(descricao.isEmpty()) {
                inserirExperienciaRequerida.setNull(2,java.sql.Types.VARCHAR);
            } else {inserirExperienciaRequerida.setString(2, descricao);}
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirExperienciaRequerida.executeUpdate();
            inserirExperienciaRequerida.getQueryTimeout();
            
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            desconectar();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Conecta ao banco de dados.
     */
    public static void conectar() {
        try {
            connection = DriverManager.getConnection(
                        BD_URL, USERNAME, PASSWORD);
            
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
                    "(cod, categoria, atividade, inicio, termino, id_unidade, "
                    + "id_subunidade, id_componente, id_parte, id_demanda)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
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
                    "(id_equipe, nome, sexo, nascimento, admissao, cargo,"
                    + "formacao, remuneracao, estado_civil, endereco, estado)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            
            /* Definição de métodos para a classe EQUIPE */
            inserirEquipe = connection.prepareStatement(
                "INSERT INTO equipe" +
                    "(objetivo_geral, id_intervencao)" +
                    "VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            
            inserirObjetivoEspecifico = connection.prepareStatement(
                "INSERT INTO objetivos_especificos" +
                    "(id_equipe, descricao)" +
                    "VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            
            inserirHabilidadeRequerida = connection.prepareStatement(
                "INSERT INTO habilidades_requeridas" +
                    "(id_equipe, descricao)" +
                    "VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
            
            inserirExperienciaRequerida = connection.prepareStatement(
                "INSERT INTO experiencias_requeridas" +
                    "(id_equipe, descricao)" +
                    "VALUES (?, ?);",
                        Statement.RETURN_GENERATED_KEYS);
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } // fim do catch.
    }
    
    /**
     * Encerra a conexão com o banco de dados.
     */
    public static void desconectar() {
        try {
            connection.close();
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        } // fim do catch.
    }
}