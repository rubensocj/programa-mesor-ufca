/**
 * Autor Rubens Oliveira da Cunha Júnior
 */

package mesor.intervencao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import mesor.sql.Consulta;

public class Interventor {
    
    private String nome;
    private String sexo;
    private String dataNascimento;
    private String dataAdmissao;
    private String cargo;
    private String formacao;
    private String especializacao;
    private String remuneracao;
    private String estadoCivil;
    private String endereco;
    private String cidade;
    private String estado;
    private String contato;
    
    private int idBD = 0;   // Parâmetros do banco de dados: id.
    
    Consulta consulta = new Consulta();
    
    private SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
    private Date sqlData;    
    private final SimpleDateFormat formatoSQLDate;
    private Date sqlDataNasc, sqlDataAdm;
    int sqlIntIntervalo, sqlIntDem;
    
    private float fRem = 0.0f;
    private int fCont = 0;
    
    public Interventor() {
        this.formatoSQLDate = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Interventor(String pNome, String pSexo, String pNasc, String pAdms,
            String pCargo, String pForma, String pEsp, String pRem, String pEst,
            String pEnd, String pCidade, String pEstado, String pContato) {
        
        this.formatoSQLDate = new SimpleDateFormat("yyyy-MM-dd");
        
        setNome(pNome);
        setSexo(pSexo);
        setNascimento(pNasc);
        setAdmissao(pAdms);
        setCargo(pCargo);
        setFormacao(pForma);
        setEspecializacao(pEsp);
        setRemuneracao(pRem);
        setEstadoCivil(pEst);
        setEndereco(pEnd);
        setCidade(pCidade);
        setEstado(pEstado);
        setContato(pContato);
    }

    // Métodos set
    public void setNome(String pNome) {
        this.nome = pNome;
    }
    public void setSexo(String pSexo) {
        if(pSexo.equals("Selecionar...")) pSexo = "";
        this.sexo = pSexo;
    }
    public void setNascimento(String pNasc) {
        if(pNasc.contains(" ")) {
            this.dataNascimento = "";
        } else {
            this.dataNascimento = pNasc.substring(6,10).concat(
            "-" + pNasc.substring(3,5)).concat(
            "-" + pNasc.substring(0,2));
        }
    }
    public void setAdmissao(String pAdms) {
        if(pAdms.contains(" ")) {
            this.dataAdmissao = "";
        } else {
            this.dataAdmissao = pAdms.substring(6,10).concat(
            "-" + pAdms.substring(3,5)).concat(
            "-" + pAdms.substring(0,2));
        }
    }
    public void setCargo(String pCargo) {
        this.cargo = pCargo;
    }
    public void setFormacao(String pForma) {
        this.formacao = pForma;
    }
    public void setEspecializacao(String pEsp) {
        this.especializacao = pEsp;
    }
    public void setRemuneracao(String pRem) {
        if(!pRem.equals("0,00")) {
            String[] s = pRem.split(",");
            if(pRem.contains(".")) {
                s[0] = s[0].replaceAll("\\.", "");
            }
            fRem = Float.parseFloat(s[0].concat(".").concat(s[1]));
        }
        this.remuneracao = pRem;
    }
    public void setEstadoCivil(String pEst) {
        if(pEst.equals("Selecionar...")) pEst = "";
        this.estadoCivil = pEst;
    }
    public void setEndereco(String pEnd) {
        this.endereco = pEnd;
    }
    public void setCidade(String pCidade) {
        this.cidade = pCidade;
    }
    public void setEstado(String pEstado) {
        if(pEstado.length() > 2) pEstado = "";
        this.estado = pEstado;
    }
    public void setContato(String pContato) {
        this.contato = pContato;
//        fCont = (int) Integer.parseInt(this.contato);
    }
    public void setIdBD(int id) {
        this.idBD = id;
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
    public String getEspecializacao() {
        return especializacao;
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
    public String getEstado() {
        return estado;
    }
    public int getContato() {
        return fCont;
    }
    public int getIdBD() {
        return idBD;
    }
    
    // Exibe o interventor no formato Object[]
    // Usado em JanelaTime 480: modeloTabInterventor.addRow(interventor.toObject());
    public Object[] toObject() {
        Object[] obj = new Object[]{getNome(), getSexo(), getNascimento(),
        getAdmissao(), getCargo(), getFormacao(), getRemuneracao(),
        getEstadoCivil(), getEndereco(), getCidade(), getEstado(),
        getContato()};
        return obj;
    }
    
    /**
     * 
     * @param dataString
     * @return Um Date formatado.
     * @throws ParseException 
     */
    private Date converterData(String dataString) throws ParseException {
        sqlData = null;
        
        if(dataString.equals("")) {
            sqlData = null; // Se não houver data de aquisição, sqlData = null.
        } else {
            sqlData = formatoSQLDate.parse(dataString);
        }
        
        return sqlData;
    }
    
    /**
     * Insere informações no banco de dados.
     */
    public void sqlInserir() {
        sqlDataNasc = null;
        sqlDataAdm = null;
        
        try {
            sqlDataNasc = converterData(this.dataNascimento);
            sqlDataAdm = converterData(this.dataAdmissao);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        int n;
        // Insere na entidade "unidade".
        n = Consulta.insertInterventor(this.nome, this.sexo, sqlDataNasc,
                sqlDataAdm, this.cargo, this.formacao, this.especializacao,
                this.fRem, this.estadoCivil, this.endereco, this.cidade,
                this.estado, this.contato);
    }
    
    /**
     * Altera informações no banco de dados.
     */
    public void sqlAlterar() {
        sqlDataNasc = null;
        sqlDataAdm = null;
        
        try {
            sqlDataNasc = converterData(this.dataNascimento);
            sqlDataAdm = converterData(this.dataAdmissao);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        int n;
        // Insere na entidade "interventor".
        n = Consulta.updateInterventor(this.nome, this.sexo, sqlDataNasc,
                sqlDataAdm, this.cargo, this.formacao, this.especializacao,
                this.fRem, this.estadoCivil, this.endereco, this.cidade,
                this.estado, this.contato);
    }
    
    /**
     * Deleta informações do banco de dados.
     */
    public void sqlExcluir() {
        int n;
        n = Consulta.deleteInterventor(this.idBD);
    }
}