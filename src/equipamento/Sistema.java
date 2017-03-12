package equipamento;

import java.util.Vector;
import conexaoSql.Consulta;

/**
 * Sistema.java
 * 
 * @version 1.0 12/2/2017
 * @author Rubens Jr
 */
public class Sistema {

    private String descricao;
    
    private final Vector<Unidade> unidade = new Vector();
    
    private int idBD;   // Parâmetros do banco de dados: id.
    
//    Consulta consulta = new Consulta();
    
    /**
     * Construtores.
     */
    public Sistema() {}
    public Sistema(String descricao) {
        setDescricao(descricao);
    }
    
    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------
    
    /* Métodos setters */    
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
}