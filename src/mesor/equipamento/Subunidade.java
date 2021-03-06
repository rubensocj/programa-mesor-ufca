package mesor.equipamento;

import java.util.Vector;

import mesor.sql.Consulta;

/**
 * Subunidade.java
 * 
 * @version 1.0 1/11/2016
 * @author Rubens Jr
 */
public class Subunidade {
    private String descricao;
    private final Vector<Componente> componente = new Vector();
    
    // Parâmetros do banco de dados
    private String cod; // código
    private int idBD = 0;   // id
    
    /**
     * Construtores.
     */
    public Subunidade() {}
    
    public Subunidade(String pDescricao) {
        setDescricao(pDescricao);
    }
    
    public Subunidade(String pDescricao, int id) {
        setDescricao(pDescricao);
        setIdBD(id);
    }
    
    public Subunidade(String pDescricao, Componente pComponente) {
        setDescricao(pDescricao);
        setComponente(pComponente);
    }

    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /* Métodos setters */
    public void setDescricao(String pDescricao) {
        this.descricao = pDescricao;
    }
    public void setComponente(Componente pComponente) {
        this.componente.addElement(pComponente);
    }
    public void setIdBD(int pId) {
        this.idBD = pId;
    }
    
    /* Métodos getters */
    public String getDescricao() {
        return descricao;
    }
    public String getComponente() {
        return componente.toString();
    }
    public Componente getElementoComponente(int i) {
        return componente.get(i);
    }
    public Vector<Componente> getVetorComponente() {
        return componente;
    }
    public int getIdBD() {
        return idBD;
    }
    /** @deprecated */
    public void removeTudo() {
        this.componente.clear();
    }
        
    /**
     * Sobrescreve o método toString(). Assim, o vetor de subunidades de objetos
     * da classe Unidade é composto pelas descrições das subunidades.
     * 
     * @return Um String como a descrição da subunidade.
     */
    @Override
    public String toString() {
        return descricao;
    }
    
    /**
     * Método de composição do campo código do banco de dados
     * 
     * @deprecated 
     * @return 
     */
    public String codigo() {
        cod = this.descricao.substring(0, 3).concat(
                this.descricao.substring(
                        this.descricao.length()-1, this.descricao.length()));
        return cod.toUpperCase();
    }
    
    /**
     * Altera informações no banco de dados.
     */
    public void sqlAlterar() {
        int n = Consulta.updateSubunidade(this.descricao, this.idBD);
    }
    
    /**
     * Deleta informações do banco de dados.
     */
    public void sqlExcluir() {
        int n = Consulta.deleteSubunidade(this.idBD);
    }
}