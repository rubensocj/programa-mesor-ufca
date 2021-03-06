package mesor.equipamento;

import mesor.sql.Consulta;

/**
 * Parte.java
 * 
 * @version 1.0 1/11/2016
 * @author Rubens Jr
 */
public class Parte {
    private String descricao;
    
    // Parâmetros do banco de dados
    private String cod; // código
    private int idBD = 0;   // id
    
    /**
     * Construtores.
     */
    public Parte() {}
    
    public Parte(String pDescricao) {
        setDescricao(pDescricao);     
    }
    
    public Parte(String pDescricao, int id) {
        setDescricao(pDescricao);
        setIdBD(id);
    }

    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /* Métodos setters */
    public void setDescricao(String pDescricao) {
        this.descricao = pDescricao;
    }
    public void setIdBD(int pId) {
        this.idBD = pId;
    }
    
    /* Métodos getters */
    public String getDescricao() {
        return descricao;
    }
    public int getIdBD() {
        return idBD;
    }
    
    /**
     * Sobrescreve o método toString(). Assim, o vetor de partes de objetos
     * da classe Componente é composto pelas descrições das partes.
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
        int n = Consulta.updateParte(this.descricao, this.idBD);
    }
    
    /**
     * Deleta informações do banco de dados.
     */
    public void sqlExcluir() {
        int n = Consulta.deleteParte(this.idBD);
    }
}