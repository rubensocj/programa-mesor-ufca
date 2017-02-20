/**
 * Autor Rubens Oliveira da Cunha Júnior
 */



public class Recurso {

    private String homemHoraDisc;
    private String homemHoraTotal;
    private String recursos; // Equipamentos necessarios para intervenção
    
    // Construtores
    public Recurso() {}
    public Recurso(String pHhDisc, String pHhTotal, String pRecurso) {
        setHomemHora(pHhDisc);
        setHomemHoraTotal(pHhTotal);
        setRecurso(pRecurso);
    }

    // Métodos set
    public void setHomemHora(String pHhDisc) {
        this.homemHoraDisc = pHhDisc;
    }
    public void setHomemHoraTotal(String pHhTotal) {
        this.homemHoraTotal = pHhTotal;
    }
    public void setRecurso(String pRecurso) {
        this.recursos = pRecurso;
    }
    
    // Métodos get
    public String getHomemHora() {
        return homemHoraDisc;
    }
    public String getHomemHoraTotal() {
        return homemHoraTotal;
    }
    public String getRecurso() {
        return recursos;
    }
    
    // Métodos de exibição
    // Exibe todos os parâmetros separados por "/" 
    @Override
    public String toString() {
        return getHomemHora()+ "/" + getHomemHoraTotal() + "/" + getRecurso();
    }
}