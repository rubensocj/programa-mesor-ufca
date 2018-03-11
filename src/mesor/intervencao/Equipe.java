/**
 * Autor Rubens Oliveira da Cunha Júnior
 */

package mesor.intervencao;

import mesor.sql.Consulta;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Equipe {
    
    private String objGeral;
    
    private final Vector<String> objEspecificos = new Vector(),
                                 habRequeridas = new Vector(),
                                 expRequeridas = new Vector();
    
    private final Vector<Interventor> interventores = new Vector();
    
    private Intervencao intervencao;
    
    private String codigo; // Parâmetros do banco de dados: codigo, id.
    private int idBD;
    
    Consulta consulta = new Consulta();
    SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
    
    // Construtores
    public Equipe() {}
    public Equipe(String pObjGeral, String pObjEspecifico, String pHabRequerida,
                String pExpRequerida) {
        
        setObjetivoGeral(pObjGeral);
        setObjetivoEspecifico(pObjEspecifico);
        setHabilidade(pHabRequerida);
        setExperiencia(pExpRequerida);
    }

    // Métodos set
    public void setObjetivoGeral(String pObjGeral) {
        this.objGeral = pObjGeral;
    }
    public void setObjetivoEspecifico(String pObjEspecifico) {
        this.objEspecificos.addElement(pObjEspecifico);
    }
    public void setHabilidade(String pHabRequerida) {
        this.habRequeridas.addElement(pHabRequerida);
    }
    public void setExperiencia(String pExpRequerida) {
        this.expRequeridas.addElement(pExpRequerida);
    }
    public void setInterventor(Interventor pInterventor) {
        this.interventores.addElement(pInterventor);
    }
    public void setIntervencao(Intervencao pIntervencao) {
        this.intervencao = pIntervencao;
    }
    public void setIdBD(int idBD) {
        this.idBD = idBD;
    }

    // Métodos get
    public String getObjetivoGeral() {
        return objGeral;
    }
    public String getElementoObjEspecifico(int i) {
        return objEspecificos.get(i);
    }
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public Vector<String> getVetorObjEspecifico() {
        return objEspecificos;
    }
    public String getElementoHabilidade(int i) {
        return habRequeridas.get(i);
    }
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public Vector<String> getVetorHabilidade() {
        return habRequeridas;
    }
    public String getElementoExperiencia(int i) {
        return expRequeridas.get(i);
    }
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public Vector<String> getVetorExperiencia() {
        return expRequeridas;
    }
    public Interventor getElementoInterventor(int i) {
        return interventores.get(i);
    }
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public Vector<Interventor> getVetorInterventor() {
        return interventores;
    }
    public Intervencao getIntervencao() {
        return intervencao;
    }
    public int getIdBD() {
        return idBD;
    }
    
    /**
     * Insere informações no banco de dados.
     */
    public void sqlInserir() {
        int n;
        // Insere na entidade "equipe"
        n = Consulta.insertEquipe(this.objGeral);
        // Insere um a um na entidade "objetivosespecificos"
        for(String ob : this.getVetorObjEspecifico()) {
            n = Consulta.insertObjetivoEspecifico(Consulta.idEquipe, ob);
        }
        // Insere uma a uma na entidade "experienciasrequeridas"
        for(String er : this.getVetorExperiencia()) {
            n = Consulta.insertExperiencia(Consulta.idEquipe, er);
        }
        // Insere uma a uma na entidade "habilidadesrequeridas"
        for(String hr : this.getVetorHabilidade()) {
            n = Consulta.insertHabilidade(Consulta.idEquipe, hr);
        }
        // Insere um a um na entidade "aux_equipe"
        for(Interventor it : this.getVetorInterventor()) {
            n = Consulta.vincularEquipe(Consulta.idEquipe, it.getIdBD());
        }
    }
    
    /**
     * Altera a intervencao vinculada a esta equipe
     */
    public void sqlVincularIntervencao() {
        int n;
        n = Consulta.vincularIntervencao(this.getIdBD(), this.intervencao.getIdBD());
    }
}
