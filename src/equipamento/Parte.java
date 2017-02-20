/**
 * Autor Rubens Oliveira da Cunha Júnior
 */

package equipamento;

import conexaoJavaSql.Consulta;

public class Parte {
    private String descricao;
    
    // Parâmetros do banco de dados
    private String cod; // código
    private int idBD = 0;   // id
    
    Consulta consulta = new Consulta();
    
    // Construtores
    public Parte() {}
    public Parte(String pDescricao) {
        setDescricao(pDescricao);     
    }

    // Métodos set
    public void setDescricao(String pDescricao) {
        this.descricao = pDescricao;
    }
    public void setIdBD(int pId) {
        this.idBD = pId;
    }

    // Métodos get
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
    
    // Método de composição do campo código do banco de dados
    public String codigo() {
        cod = this.descricao.substring(0, 3).concat(
                this.descricao.substring(
                        this.descricao.length()-1, this.descricao.length()));
        return cod.toUpperCase();
    }
    
    public void alteraParte() {
        int n;
        
        n = consulta.updateParte(this.descricao, this.idBD);
    }
}