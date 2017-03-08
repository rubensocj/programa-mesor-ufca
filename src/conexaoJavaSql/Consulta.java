package conexaoJavaSql;

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
    
    // Nome e URL do banco de dados.
    private static final String BD_NAME = "bdprograma";
    private static final String BD_URL = "jdbc:mysql://192.168.1.4:3306/"+BD_NAME;
    
    // Acesso ao servidor: usuário e senha.
    private static final String USERNAME = "mesor";
    private static final String PASSWORD = "mesorufca1506";
    
    // gerencia a conexão.
    private Connection connection = null;
    
    private ResultSet resultSet = null;
    
    /**
     * id gerado automaticamente a cada inserção. É usados nas chaves
     * estrangeiras quando necessário.
     */
    public int idUnidade, idSubunidade, idComponente, idParte, idDemanda,
                idIntervencao, idInterventor, idEquipe;
    
    /**
     * Consulta em SQL previamente preparada que insere no banco de dados
     * elementos criados pelo usuário.
     */
    private static PreparedStatement inserirUnidade = null,
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
    private static PreparedStatement alterarUnidade = null,
                alterarSubunidade = null,
                alterarComponente = null,
                alterarParte = null,
                alterarDemanda = null,
                alterarIntervencao = null;
    
    /**
     * Construtor.
     */
    public Consulta() {
        /* Conecta-se ao banco de dados e cria consultas preparadas */
        try {
            // estabelece conexão com banco de dados.
            connection = DriverManager.getConnection(
                    BD_URL, USERNAME, PASSWORD);
            
            /* Definição de métodos para a classe UNIDADE */
            inserirUnidade = connection.prepareStatement(
                "INSERT INTO unidade" + 
                    "(classe, tipo, fabricante, identificacao, categoria" +
                    "localizacao, data_aquisicao, modo_operacional, " +
                    "data_inicio_operacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            
            alterarUnidade = connection.prepareStatement(
                "UPDATE unidade SET " +
                    "classe = ?, tipo = ?, fabricante = ?, identificacao = ?, "+
                    "categoria = ?, localizacao = ?, data_aquisicao = ?, " +
                    "modo_operacional = ?, data_inicio_operacao = ? " +
                "WHERE id = ?;");
            
            /* Definição de métodos para a classe SUBUNIDADE */
            inserirSubunidade = connection.prepareStatement(
                "INSERT INTO subunidade" +
                    "(cod, descricao, id_unidade)" +
                "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            
            alterarSubunidade = connection.prepareStatement(
                "UPDATE subunidade SET descricao = ? WHERE id = ?");
            
            /* Definição de métodos para a classe COMPONENTE */
            inserirComponente = connection.prepareStatement(
                "INSERT INTO componente" +
                    "(cod, descricao, id_subunidade)" +
                "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            
            alterarComponente = connection.prepareStatement(
                "UPDATE componente SET descricao = ? WHERE id = ?");
            
            /* Definição de métodos para a classe PARTE */
            inserirParte = connection.prepareStatement(
                "INSERT INTO parte" +
                    "(cod, descricao, id_componente)" +
                "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            
            alterarParte = connection.prepareStatement(
                "UPDATE parte SET descricao = ? WHERE id = ?");

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
            System.exit(1);
        } // fim do catch.
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
    public int insertDemanda(java.util.Date data, String modo, String impacto,
                String causa, String modoOperacional, int idUni, int idSub,
                int idCp, int idPte) {
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
            close();
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
    public int updateDemanda(java.util.Date data, String modo, String impacto,
                String causa, String modoOperacional, int id) {
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
            close();
        } // fim do catch.
        
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
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public int insertUnidade(String classe, String tipo, String fabricante,
            String identificacao, String categoria, String localizacao,
            java.util.Date aquisicao, String operacao, java.util.Date inicio) {
        
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
            close();
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
    public int updateUnidade(String classe, String tipo, String fabricante,
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
            close();
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
     * @param cod
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public int insertSubunidade(String cod, String descricao, int id) {
        int resultado = 0;        
        try {
            inserirSubunidade.setString(1, cod);
            inserirSubunidade.setString(2, descricao);
            inserirSubunidade.setObject(3, id);
            
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
            close();
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
    public int updateSubunidade(String descricao, int id) {
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
            close();
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
     * @param cod
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public int insertComponente(String cod, String descricao, int id) {
        int resultado = 0;        
        try {
            inserirComponente.setString(1, cod);
            inserirComponente.setString(2, descricao);
            inserirComponente.setObject(3, id);
            
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
            close();
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
    public int updateComponente(String descricao, int id) {
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
            close();
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
     * @param cod
     * @param descricao
     * @param id
     * @return Um inteiro como resultado da execução do PreparedStatement.
     */
    public int insertParte(String cod, String descricao, int id) {
        int resultado = 0;        
        try {
            inserirParte.setString(1, cod);
            inserirParte.setString(2, descricao);
            inserirParte.setObject(3, id);
            
            // executa a operação; retorna número de linhas atualizadas.
            resultado = inserirParte.executeUpdate();
            inserirParte.getQueryTimeout();

        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
            close();
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
    public int updateParte(String descricao, int id) {
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
            close();
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
    public int insertIntervencao(String cod, String categoria, String atividade, 
                java.util.Date inicio, java.util.Date termino, int idUni,
                int idSub, int idCp, int idPte, int idDem) {
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
            close();
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
    public int uptadeIntervencao(String categoria, String atividade, 
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
            close();
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
    public int insertInterventor(int idTime, String nome, String sexo,
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
            close();
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
    public int insertTime(String objetivoGeral, int idIntervencao) {
        
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
            close();
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
    public int insertObjetivoEspecifico(int idTime, String descricao) {
        
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
            close();
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
    public int insertHabilidade(int idTime, String descricao) {
        
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
            close();
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
    public int insertExperiencia(int idTime, String descricao) {
        
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
            close();
        } // fim do catch.
        
        return resultado;
    }
    
    /**
     * Encerra a conexão com o banco de dados.
     */
    public void close() {
        try {
            connection.close();
        } // fim do try.
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } // fim do catch.
    }
}