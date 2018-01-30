package equipamento;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Vector;

import sql.Consulta;

/**
 * Unidade.java
 * 
 * @version 1.0 1/2/2017
 * @author Rubens Jr
 */
public class Unidade {
    private String categoria;
    private String classe;
    private String tipo;
    private String fabricante;
    private String identificacao;
    private String local;
    private String dataAquisicao;
    
    private String dataInicioOperacao;
    private String modoOperacional;
    
    private int sistema;
    
    private int idBD = 0;   // Parâmetros do banco de dados: id.
    
    private final Vector<Subunidade> subunidade = new Vector();
    
    private final SimpleDateFormat formatoSQLDate;
    private Date sqlDataAq, sqlDataOp, sqlData;
    int sqlIntTempFisc, sqlIntDem;
    
    /**
     * Construtores.
     */
    public Unidade() {
        this.formatoSQLDate = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    public Unidade(String classe, int id) {
        this.formatoSQLDate = new SimpleDateFormat("yyyy-MM-dd");
        
        setClasse(classe);
        setIdBD(id);
    }
    
    public Unidade(String categoria, String classe, String tipo,
            String fabricante, String identificacao, String localizacao,
            String dataAquisicao, String operacional, String dataInicio,
            Subunidade subunidade) {
        
        this.formatoSQLDate = new SimpleDateFormat("yyyy-MM-dd");
        
        setCategoria(categoria);
        setClasse(classe);
        setTipo(tipo);
        setFabricante(fabricante);
        setIdentificacao(identificacao);
        setLocal(localizacao);
        setDataAquisicao(dataAquisicao);
        setModoOperacional(operacional);
        setDataInicioOperacao(dataInicio);
        setSubunidade(subunidade);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /* Métodos setters */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }
    public void setLocal(String localizacao) {
        this.local = localizacao;
    }
    public void setDataAquisicao(String dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }
    public void setModoOperacional(String operacional) {
        this.modoOperacional = operacional;
    }
    public void setDataInicioOperacao(String dataInicio) {
        this.dataInicioOperacao = dataInicio;
    }
    public void setSubunidade(Subunidade subunidade) {
        this.subunidade.addElement(subunidade);
    }
    public void setIdBD(int id) {
        this.idBD = id;
    }
    public void setSistema(int id) {
        this.sistema = id;
    }
    
    /* Métodos getters */
    public String getCategoria() {
        return categoria;
    }
    public String getClasse() {
        return classe;
    }
    public String getTipo() {
        return tipo;
    }
    public String getFabricante() {
        return fabricante;
    }
    public String getIdentificacao() {
        return identificacao;
    }
    public String getLocal() {
        return local;
    }
    public String getDataAquisicao() {
        return dataAquisicao;
    }
    public String getModoOperacional() {
        return modoOperacional;
    }
    public String getDataInicioOperacao() {
        return dataInicioOperacao;
    }
    public String getSubunidade() {
        return subunidade.toString();
    }
    public Subunidade getElementoSubunidade(int i) {
        return subunidade.get(i);
    }
    public Vector<Subunidade> getVetorSubunidade() {
        return subunidade;
    }
    public int getIdBD() {
        return idBD;
    }
    public int getSistema() {
        return sistema;
    }
    
    /**
     * 
     * @param dataString
     * @return Um Date formatado.
     * @throws ParseException 
     */
    private Date converterData(String dataString) throws ParseException {
        sqlData = null;
        
        if(dataString.equals("")) {
            sqlData = null; // Se não houver data de aquisição, sqlData = null.
        } else {
            sqlData = formatoSQLDate.parse(dataString);
        }
        
        return sqlData;
    }
    
    /**
     * Insere informações no banco de dados.
     */
    public void sqlInserir() {
        sqlDataAq = null;
        sqlDataOp = null;
        
        try {
            sqlDataAq = converterData(dataAquisicao);
            sqlDataOp = converterData(dataInicioOperacao);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        int n;
        // Insere na entidade "unidade".
        n = Consulta.insertUnidade(this.classe, this.tipo, this.fabricante,
                this.identificacao, this.categoria, this.local, sqlDataAq,
                this.modoOperacional, sqlDataOp, this.sistema);
        // Insere as subunidades uma a uma na entidade "subunidade".
        for(Subunidade sb : this.getVetorSubunidade()) {
            n = Consulta.insertSubunidade(sb.getDescricao(),
                            Consulta.idUnidade);
            // Insere os componentes um a um na entidade "componente".
            for(Componente cp : sb.getVetorComponente()) {
                n = Consulta.insertComponente(cp.getDescricao(),
                            Consulta.idSubunidade);
                // Insere as partes uma a uma na entidade "parte".
                for(Parte pt : cp.getVetorParte()) {
                    n = Consulta.insertParte(pt.getDescricao(),
                                Consulta.idComponente);
                }
            }
        }
    }
    
    /**
     * Altera informações no banco de dados.
     */
    public void sqlAlterar() {
        sqlDataAq = null;
        sqlDataOp = null;
        
        try {
            sqlDataAq = converterData(dataAquisicao);
            sqlDataOp = converterData(dataInicioOperacao);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        int n;        
        n = Consulta.updateUnidade(this.classe, this.tipo, this.fabricante,
                this.identificacao, this.categoria, this.local, sqlDataAq,
                this.modoOperacional, sqlDataOp, this.idBD);
    }
    
    /**
     * Deleta informações do banco de dados.
     */
    public void sqlExcluir() {
        int n;
        n = Consulta.deleteUnidade(this.idBD);
    }
}