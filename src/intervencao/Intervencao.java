package intervencao;

import equipamento.Unidade;
import equipamento.Subunidade;
import equipamento.Parte;
import equipamento.Componente;

import java.text.*;

import java.util.Date;

import sql.Consulta;

/**
 * Intervencao.Java
 * 
 * @version 1.0 6/2/2017
 * @author Rubens Jr
 */
public class Intervencao {
    
    /**
     * Categoria principal da intervenção: corretiva (C) ou preventiva (P).
     */
    private String categoria;
    /**
     * Atividade desenvolvida na intervenção, de acordo com a tabela B.5 da ISO
     * 14224.
     * 
     * Substituição (C, P);
     * Reparo       (C);
     * Modificação  (C, P);
     * Ajuste       (C, P);
     * Conserto     (C, P);
     * Checagem     (C);
     * Serviço      (P);
     * Teste        (P);
     * Inspeção     (P);
     * Revisão      (C, P);
     * Combinação   (C, P);
     * Outro        (C, P).
     */
    private String atividade; // descrição da atividade (ver tabela B.5)
    
    /* Data de início e término da intervenção */
    private String inicio, termino;
            
    /**
     * Falha ocorrida no equipamento, aplicável apenas em intervenção corretiva.
     */
    private Demanda demanda;
    
    // Item de equipamento onde ocorreu a intervenção
    private Unidade unidade;
    private Subunidade subunidade;
    private Componente componente;
    private Parte parte;
    
    private String codigo; // Parâmetros do banco de dados: codigo, id.
    private int idBD;
    
    // SimpleDateFormat formata como SQL Timestamp
    private final SimpleDateFormat formatoSQLTimeDate;
    private Date dataInicioDate, dataTerminoDate;
    
    Consulta consulta = new Consulta();

    /**
     * Construtor.
     */
    public Intervencao() {
        formatoSQLTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    public Intervencao(String pCategoria, String pAtiv, String pEntrada, 
            String pSaida, Unidade pUnidade, Subunidade pSubunidade,
            Componente pComponente, Parte pParte, Demanda pDemanda) {
        
        formatoSQLTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        setCategoria(pCategoria);
        setAtividade(pAtiv);
        setInicio(pEntrada);
        setTermino(pSaida);
        setUnidade(pUnidade);
        setSubunidade(pSubunidade);
        setComponente(pComponente);
        setParte(pParte);
        setDemanda(pDemanda);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /* Métodos setters */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }
    public void setInicio(String entrada) {
        this.inicio = entrada;
    }
    public void setTermino(String saida) {
        this.termino = saida;
    }
    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
    public void setSubunidade(Subunidade subunidade) {
        this.subunidade = subunidade;
    }
    public void setComponente(Componente componente) {
        this.componente = componente;
    }
    public void setParte(Parte parte) {
        this.parte = parte;
    }
    public void setDemanda(Demanda demanda) {
        this.demanda = demanda;
    }
    public void setIdBD(int idBD) {
        this.idBD = idBD;
    }

    /* Métodos getters */
    public String getCategoria() {
        return categoria;
    }
    public String getAtividade() {
        return atividade;
    }
    public String getInicio() {
        return inicio;
    }
    public String getTermino() {
        return termino;
    }
    public Unidade getUnidade() {
        return unidade;
    }
    public Subunidade getSubunidade() {
        return subunidade;
    }
    public Componente getComponente() {
        return componente;
    }
    public Parte getParte() {
        return parte;
    }
    public Demanda getDemanda() {
        return demanda;
    }
    public int getIdBD() {
        return idBD;
    }
    public String codigo() {
        codigo = this.categoria.substring(0,4);
        return codigo.toUpperCase();
    }
    
    /**
     * Insere informações no banco de dados.
     */
    public void adicionaIntervencao() {
        int n;
        
        /**
         * Variáveis do tipo Date. Receberão a data de início e término
         * no formato Timestamp.
         */
        dataInicioDate = null;
        dataTerminoDate = null;
        
        try {
            // Converte "String" para "Date".
            dataInicioDate = formatoSQLTimeDate.parse(this.getInicio());
            
            if(this.getTermino().equals("")) {
                // Se não houver data e hora de término, dataTerminoDate = null.
                dataTerminoDate = null;
            } else {
                // Caso contrário, converte "String" para "Date".
                dataTerminoDate = formatoSQLTimeDate.parse(this.getTermino());
            }                
        } catch (ParseException ex) { ex.printStackTrace();}
        
        n = Consulta.insertIntervencao(categoria, atividade, dataInicioDate,
                    dataTerminoDate, this.unidade.getIdBD(),
                    this.subunidade.getIdBD(), this.componente.getIdBD(),
                    this.parte.getIdBD(), this.demanda.getIdBD());
    }
    
    /**
     * Altera informações no banco de dados.
     */
    public void alteraIntervencao() {
        int n;
        
        /**
         * Variáveis do tipo Date. Receberão a data de início e término
         * no formato Timestamp.
         */
        dataInicioDate = null;
        dataTerminoDate = null;
        
        try {
            // Converte "String" para "Date".
            dataInicioDate = formatoSQLTimeDate.parse(this.getInicio());
            
            if(this.getTermino().equals("")) {
                // Se não houver data e hora de término, dataTerminoDate = null.
                dataTerminoDate = null;
            } else {
                // Caso contrário, converte "String" para "Date".
                dataTerminoDate = formatoSQLTimeDate.parse(this.getTermino());
            }                
        } catch (ParseException ex) { ex.printStackTrace();}
        
        n = Consulta.uptadeIntervencao(categoria, atividade, dataInicioDate,
                    dataTerminoDate, this.demanda.getIdBD(), this.idBD);
    }
}