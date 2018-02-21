package mesor.intervencao;

import mesor.equipamento.Unidade;
import mesor.equipamento.Subunidade;
import mesor.equipamento.Parte;
import mesor.equipamento.Componente;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import mesor.sql.Consulta;

/**
 * Demanda.java
 * 
 * @version 1.0 3/2/2017
 * @author Rubens Jr
 */
public class Demanda {
    
    private Unidade unidade;
    private Subunidade subunidade;
    private Componente componente;
    private Parte parte;
    
    private String data, modo, impacto, causa, modoOperacional;
    
    private int idBD = 0;  // Parâmetros do banco de dados: id.
    
    private final SimpleDateFormat formatoSQLTimestamp;
    
    private Date dataDate;
    
    private final Consulta consulta = new Consulta();

    /**
     * Construtores.
     */
    public Demanda() {
        this.formatoSQLTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    public Demanda(Unidade unidade, String data, String modo, String impacto,
                String causa, String condicao, Subunidade subunidade,
                Componente componente, Parte parte) {
        
        this.formatoSQLTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        setUnidade(unidade);
        setData(data);
        setModo(modo);
        setImpacto(impacto);
        setCausa(causa);
        setModoOperacional(condicao);
        setSubunidade(subunidade);
        setComponente(componente);
        setParte(parte);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    /* Métodos setters */
    public void setUnidade(Unidade uni) {
        this.unidade = uni;
    }
    public void setData(String data) {
        this.data = data;
    }
    public void setModo(String modo) {
        this.modo = modo;
    }
    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }
    public void setCausa(String causa) {
        this.causa = causa;
    }
    public void setModoOperacional(String fCondicao) {
        this.modoOperacional = fCondicao;
    }
    public void setSubunidade(Subunidade sub) {
        this.subunidade = sub;
    }
    public void setComponente(Componente comp) {
        this.componente = comp;
    }
    public void setParte(Parte pte) {
        this.parte = pte;
    }
    public void setIdBD(int pId) {
        this.idBD = pId;
    }

    /* Métodos getters */
    public Unidade getUnidade() {
        return unidade;
    }
    public String getData() {
        return data;
    }
    public String getModo() {
        return modo;
    }
    public String getImpacto() {
        return impacto;
    }
    public String getCausa() {
        return causa;
    }
    public String getModoOperacional() {
        return modoOperacional;
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
    public int getIdBD() {
        return idBD;
    }
    
    /**
     * Insere informações no banco de dados.
     */
    public void sqlInserir() {
        int n;
        
        dataDate = null;    // Receberá a data no formato Timestamp.
        
        try {
            // Converte "String" como "Timestamp" para "Date".
            dataDate = formatoSQLTimestamp.parse(this.getData());
            
        } catch (ParseException ex) {ex.printStackTrace();} // Fim do try-catch.
        
        n = Consulta.insertDemanda(dataDate, this.modo, this.impacto,
                this.causa, this.modoOperacional, this.unidade.getIdBD(),
                this.subunidade.getIdBD(), this.componente.getIdBD(),
                this.parte.getIdBD());
    }
    
    /**
     * Altera informações do banco de dados.
     */
    public void sqlAlterar() {
        int n;
        
        dataDate = null;    // Receberá a data no formato Timestamp.
        
        try {
            // Converte "String" como "Timestamp" para "Date".
            dataDate = formatoSQLTimestamp.parse(this.getData());
            
        } catch (ParseException ex) {ex.printStackTrace();} // Fim do try-catch.
        
        n = Consulta.updateDemanda(dataDate, this.modo, this.impacto,
                this.causa, this.modoOperacional, this.idBD);
    }

    /**
     * Deleta informações do banco de dados.
     */
    public void sqlExcluir() {
        int n;
        n = Consulta.deleteDemanda(this.idBD);
    }
}