package mesor.equipamento;

import mesor.sql.Consulta;
import java.util.Vector;

/**
 * Sistema.java
 * 
 * @version 1.0 12/2/2017
 * @author Rubens Jr
 */
public class Sistema {

    private String nome;
    private String descricao;
    
    private final Vector<Unidade> unidade = new Vector();
    
    private int idBD;   // Parâmetros do banco de dados: id.
    
//    Consulta consulta = new Consulta();
    
    /**
     * Construtores.
     */
    public Sistema() {}
    public Sistema(int id) { setIdBD(id);}
    public Sistema(int id, String nome) {
        setIdBD(id);
        setNome(nome);
    }
    public Sistema(String nome, String descricao) {
        setDescricao(descricao);
        setNome(nome);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /* Métodos setters */
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setUnidade(Unidade unidade) {
        this.unidade.addElement(unidade);
    }
    public void setIdBD(int id) {
        this.idBD = id;
    }
    
    /* Métodos getters */    
    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getUnidade() {
        return unidade.toString();
    }
    public Unidade getElementoUnidade(int i) {
        return unidade.get(i);
    }
    public Vector<Unidade> getVetorUnidade() {
        return unidade;
    }
    public int getIdBD() {
        return idBD;
    }
    
    /**
     * Isere informações no banco de dados.
     */
    public void sqlInserir() {
        int n;
        n = Consulta.insertSistema(this.nome, this.descricao);
    }
    
    /**
     * Altera informações no banco de dados.
     */
    public void sqlAlterar() {
        int n;
        n = Consulta.updateSistema(this.nome, this.descricao, this.idBD);
    }
    
    /**
     * Altera informações no banco de dados.
     */
    public void sqlExcluir() {
        int n;
        n = Consulta.deleteSistema(this.idBD);
    }
}