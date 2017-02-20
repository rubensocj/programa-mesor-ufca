/**
 * Autor Rubens Oliveira da Cunha Júnior
 */

package intervencao;

import java.text.SimpleDateFormat;
import java.util.Date;
import conexaoJavaSql.Consulta;

public class Interventor {
    
    private String nome;
    private String sexo;
    private String dataNascimento;
    private String dataAdmissao;
    private String cargo;
    private String formacao;
    private String remuneracao;
    private String estadoCivil;
    private String endereco;
    private String cidade;
    
    Consulta consulta = new Consulta();
    SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
    Date sqlData;
    int sqlIntIntervalo, sqlIntDem;
    
    public Interventor() {}
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Interventor(String pNome, String pSexo, String pNasc, String pAdms,
            String pCargo, String pForma, String pRem, String pEst, String pEnd,
            String pCidade) {
        
        setNome(pNome);
        setSexo(pSexo);
        setNascimento(pNasc);
        setAdmissao(pAdms);
        setCargo(pCargo);
        setFormacao(pForma);
        setRemuneracao(pRem);
        setEstadoCivil(pEst);
        setEndereco(pEnd);
        setCidade(pCidade);
    }

    // Métodos set
    public void setNome(String pNome) {
        this.nome = pNome;
    }
    public void setSexo(String pSexo) {
        this.sexo = pSexo;
    }
    public void setNascimento(String pNasc) {
        this.dataNascimento = pNasc;
    }
    public void setAdmissao(String pAdms) {
        this.dataAdmissao = pAdms;
    }
    public void setCargo(String pCargo) {
        this.cargo = pCargo;
    }
    public void setFormacao(String pForma) {
        this.formacao = pForma;
    }
    public void setRemuneracao(String pRem) {
        this.remuneracao = pRem;
    }
    public void setEstadoCivil(String pEst) {
        this.estadoCivil = pEst;
    }
    public void setEndereco(String pEnd) {
        this.endereco = pEnd;
    }
    public void setCidade(String pCidade) {
        this.cidade = pCidade;
    }
    
    // Métodos get
    public String getNome() {
        return nome;
    }
    public String getSexo() {
        return sexo;
    }
    public String getNascimento() {
        return dataNascimento;
    }
    public String getAdmissao() {
        return dataAdmissao;
    }
    public String getCargo() {
        return cargo;
    }
    public String getFormacao() {
        return formacao;
    }
    public String getRemuneracao() {
        return remuneracao;
    }
    public String getEstadoCivil() {
        return estadoCivil;
    }
    public String getEndereco() {
        return endereco;
    }
    public String getCidade() {
        return cidade;
    }
    
    // Exibe o interventor no formato Object[]
    // Usado em JanelaTime 480: modeloTabInterventor.addRow(interventor.toObject());
    public Object[] toObject() {
        Object[] obj = new Object[]{getNome(), getSexo(), getNascimento(),
        getAdmissao(), getCargo(), getFormacao(), getRemuneracao(),
        getEstadoCivil(), getEndereco(), getCidade()};
        return obj;
    }
}