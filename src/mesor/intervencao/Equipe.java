/**
 * Autor Rubens Oliveira da Cunha Júnior
 */

package mesor.intervencao;

import mesor.sql.Consulta;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Equipe {
    
    private String objGeral;
    
    @SuppressWarnings("UseOfObsoleteCollectionType")
    private final Vector<String> objEspecificos = new Vector(),
                                 habRequeridas = new Vector(),
                                 expRequeridas = new Vector();
    
    @SuppressWarnings("UseOfObsoleteCollectionType")
    private final Vector<Interventor> interventores = new Vector();
    
    Consulta consulta = new Consulta();
    SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
    
    // Construtores
    public Equipe() {}
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Equipe(String pObjGeral, String pObjEspecifico, String pHabRequerida,
                String pExpRequerida, Interventor pInterventor) {
        
        setObjetivoGeral(pObjGeral);
        setObjetivoEspecifico(pObjEspecifico);
        setHabilidade(pHabRequerida);
        setExperiencia(pExpRequerida);
        setInterventor(pInterventor);
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
    
    // Método que adiciona as informações ao BD
    public void adicionaEquipe() {
        java.util.Date sqlDataNascimento = null, sqlDataAdmissao = null;
        float sqlRemuneracao = 0;
        
        int n;
        // Insere na entidade "equipe"
        n = Consulta.insertTime(this.objGeral, 0);
        // Insere os objetivos expecíficos um a um na entidade "objetivosespecificos"
        for(String ob : this.getVetorObjEspecifico()) {
            n = Consulta.insertObjetivoEspecifico(Consulta.idEquipe, ob);
        }
        // Insere as experiências requeridas uma a uma na entidade "experienciasrequeridas"
        for(String er : this.getVetorExperiencia()) {
            n = Consulta.insertExperiencia(Consulta.idEquipe, er);
        }
        // Insere as habilidades requeridas uma a uma na entidade "habilidadesrequeridas"
        for(String hr : this.getVetorHabilidade()) {
            n = Consulta.insertHabilidade(Consulta.idEquipe, hr);
        }
        // Insere as habilidades requeridas uma a uma na entidade "habilidadesrequeridas"
        for(Interventor it : this.getVetorInterventor()) {
            try {
                // Converte os dados String para Date e Int
                
                // Se a não for informado a data, não faz nada.
                // sqlDataNascimento = null
                // sqlDataAdmissao = null
                if(it.getNascimento().contains(" ")) {}
                else {
                    // Se a data for informada, formata-as
                    sqlDataNascimento = formatoData.parse(it.getNascimento());
                    sqlDataAdmissao = formatoData.parse(it.getAdmissao());
                }                
                // Formata a remuneração
                sqlRemuneracao = Float.parseFloat(
                        it.getRemuneracao().replace(".", "").replace(",", "."));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            } // Fim do try-catch
            n = Consulta.insertInterventor(Consulta.idEquipe, it.getNome(),
                        it.getSexo(), sqlDataNascimento, sqlDataAdmissao,
                        it.getCargo(), it.getFormacao(), sqlRemuneracao,
                        it.getEstadoCivil(), it.getEndereco(), it.getCidade(),
                        it.getEstado(), it.getContato());
        }
    }
}
