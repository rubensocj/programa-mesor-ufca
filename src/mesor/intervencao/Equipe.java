/**
 * Autor Rubens Oliveira da Cunha Júnior
 */

package mesor.intervencao;

import mesor.sql.Consulta;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

public class Equipe {
    
    private String objGeral;
    
    private final ArrayList objEsp = new ArrayList<>(),
                        habReq = new ArrayList<>(),
                        expReq = new ArrayList<>();
    private final ArrayList<Integer> idObjsEsp = new ArrayList<>(),
                                idHabReq = new ArrayList<>(),
                                idExpReq = new ArrayList<>();
    
    private final ArrayList<Interventor> intv = new ArrayList<>();
    
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
        objEsp.add(pObjEspecifico);
    }
    public void setObjetivoEspecifico(String pObjEspecifico, int id) {
        objEsp.add(pObjEspecifico);
        idObjsEsp.add(id);
    }
    
    public void setHabilidade(String pHabRequerida) {
        habReq.add(pHabRequerida);
    }
    public void setHabilidade(String pHabRequerida, int id) {
        habReq.add(pHabRequerida);
        idHabReq.add(id);
    }
    
    public void setExperiencia(String pExpRequerida) {
        expReq.add(pExpRequerida);
    }
    public void setExperiencia(String pExpRequerida, int id) {
        expReq.add(pExpRequerida);
        idExpReq.add(id);
    }
    
    public void setInterventor(Interventor pInterventor) {
        intv.add(pInterventor);
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
//        return objEspecificos.get(i);
        return (String) objEsp.get(i);
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
    
    public ArrayList getListaObjetivos() {
        return objEsp;
    }
    public ArrayList getListaHabilidades() {
        return habReq;
    }
    public ArrayList getListaExperiencias() {
        return expReq;
    }
    public ArrayList getListaInterventor() {
        return intv;
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
        setIdBD(Consulta.idEquipe);
        
        sqlInserirExperiencia();
        sqlInserirHabilidade();
        sqlInserirObjetivo();
        sqlInserirInterventor();
    }
    
    /**
     * Altera a intervencao vinculada a esta equipe
     */
    public void sqlVincularIntervencao() {
        int n;
        n = Consulta.vincularIntervencao(this.getIdBD(), this.intervencao.getIdBD());
    }
    
    /**
     * Altera o objetivo geral da equipe e atualiza o idEquipe da classe Consulta
     */
    public void sqlAlterarEquipe() {
        sqlAlterarObjetivoGeral();
        sqlAlterarExperiencia();
        sqlAlterarHabilidade();
        sqlAlterarObjetivo();
        sqlAlterarInterventor();
    }
    
    /**
     * Exclui a equipe selecionada e os vínculos entre equipe e interventor
     */
    public void sqlExcluir() {
        int n;
        n = Consulta.deleteEquipe(this.idBD);
        n = Consulta.desvincularEquipe(this.idBD);
    }
    
    // Métodos de Experiencias
    public void sqlInserirExperiencia() {
        int n;
        for(Object er : expReq) {
            n = Consulta.insertExperiencia(this.idBD, (String) er);
        }
    }
    public void sqlAlterarExperiencia() {
        int n;
        for(int i = 0; i < expReq.size(); i++) {
            n = Consulta.updateExperiencia((String) expReq.get(i), idExpReq.get(i), this.getIdBD());
        }
    }
    public void sqlExcluirExperiencia() {
        int n;
        for(int i = 0; i < expReq.size(); i++) {
            n = Consulta.deleteExperiencia(idExpReq.get(i));
        }
        expReq.clear();
        idExpReq.clear();
    }
    
    // Métodos de Objetivos
    public void sqlInserirObjetivo() {
        int n;
        for(Object ob : objEsp) {
            n = Consulta.insertObjetivoEspecifico(this.getIdBD(), (String) ob);
        }
    }
    public void sqlAlterarObjetivo() {
        int n;
        for(int i = 0; i < objEsp.size(); i++) {
            n = Consulta.updateObjetivoEspecifico((String) objEsp.get(i), idObjsEsp.get(i), this.getIdBD());
        }
    }
    public void sqlExcluirObjetivo() {
        int n;
        for(int i = 0; i < objEsp.size(); i++) {
            n = Consulta.deleteObjetivoEspecifico(idObjsEsp.get(i));
        }
        objEsp.clear();
        idObjsEsp.clear();
    }
    
    // Métodos de Habilidades
    public void sqlInserirHabilidade() {
        int n;
        for(Object hr : objEsp) {
            n = Consulta.insertHabilidade(this.getIdBD(), (String) hr);
        }
    }
    public void sqlAlterarHabilidade() {
        int n;
        for(int i = 0; i < habReq.size(); i++) {
            n = Consulta.updateHabilidade((String) habReq.get(i), idHabReq.get(i), this.getIdBD());
        }
    }
    public void sqlExcluirHabilidade() {
        int n;
        for(int i = 0; i < habReq.size(); i++) {
            n = Consulta.deleteHabilidade(idHabReq.get(i));
        }
        habReq.clear();
        idHabReq.clear();
    }
    
    // Métodos de interventores
    public void sqlInserirInterventor() {
        int n;
        for(Interventor it : intv) {
            n = Consulta.vincularEquipe(this.getIdBD(), it.getIdBD());
        }
    }
    public void sqlAlterarInterventor() {
        int n;
        for(Interventor it : intv) {
            n = Consulta.vincularEquipe(this.getIdBD(), it.getIdBD());
        }
    }
    
    public void sqlAlterarObjetivoGeral() {
        int n;
        n = Consulta.updateEquipe(this.objGeral, this.getIdBD());
    }
}
