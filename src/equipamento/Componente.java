/**
 * Autor Rubens Oliveira da Cunha Júnior
 */

package equipamento;

import java.util.Vector;
import conexaoJavaSql.Consulta;

public class Componente {
    private String descricao;
    private final Vector<Parte> parte = new Vector();
    
    // Parâmetros do banco de dados
    private String cod; // código
    private int idBD = 0;   // id
    
    Consulta consulta = new Consulta();
    
    // Construtores
    public Componente() {}
    public Componente(String pDescricao) {
        setDescricao(pDescricao);
    }
    public Componente(String pDescricao, Parte pParte) {
        setDescricao(pDescricao);
        setParte(pParte);
    }
    
    // Métodos set
    public void setDescricao(String pDescricao) {
        this.descricao = pDescricao;
    }
    public void setParte(Parte pParte) {
        this.parte.addElement(pParte);
    }
    public void setIdBD(int pId) {
        this.idBD = pId;
    }
    
    //Métodos get
    public String getDescricao() {
        return descricao;
    }
    public String getParte() {
        return parte.toString();
    }
    public Parte getElementoParte(int i) {
        return parte.get(i);
    }
    public Vector<Parte> getVetorParte() {
        return parte;
    }
    public int getIdBD() {
        return idBD;
    }
    
    /**
     * Sobrescreve o método toString(). Assim, o vetor de componentes de objetos
     * da classe Subunidade é composto pelas descrições dos componentes.
     * 
     * @return Um String como a descrição do componente.
     */
    @Override
    public String toString() {
        return descricao;
    }
    
    // Método de composição do campo código do banco de dados
    public String codigo() {
        cod = this.descricao.substring(0, 3).concat(
                this.descricao.substring(
                        this.descricao.length()-1, this.descricao.length()));
        return cod.toUpperCase();
    }
    
    public void alteraComponente() {
        int n;
        
        n = consulta.updateComponente(this.descricao, this.idBD);
    }
}