package mesor.r;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RVector;

import java.io.IOException;

/**
 *
 * @author Rubens Oliveira da Cunha Júnior
 */
public class CamadaR {

    static SerieTemporal str = new SerieTemporal("Subunidade", 15);

    // REngine
    static Rengine re;

    // R Enviroment
    static REXP env;

    // REXP de entrada
    static REXP rexpTempos;
    static REXP rexpTipos;
    
    // REXP de saída
    static REXP rexpMle_objs;
    static REXP rexpICsAndParameters;
    static REXP rexpOptimum_RP;
    static REXP rexpOptimum_NHPP;
    static REXP rexpOptimum_KijimaI;
    static REXP rexpOptimum_KijimaII;
    static REXP rexpOptimum;
    static REXP rexpOptimum_InterventionType;

    // REXP diretório de trabalho R
    static REXP rexpWd;
    
    // Nomes das variáveis R usadas no Generic_Code.R
    private static final String R_TEMPOS_ENTRE = "data_base_x";
    private static final String R_TIPOS = "data_base_types";
    private static final String R_MLE_OBJS = "mle_objs";
    private static final String R_DF = "df";

    // Caminho usado para a leitura do arquivo MK_WGRP.R
    public static String R_LOCAL = null;
    public static String R_PATH = null;
    // Caminho usado para salvar arquivo parametersAndICs_table.txt
    public static final String R_USERS_PUBLIC = "C:/Users/Public/";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Inicia a REngine
        rIniciarREngine();

        // Compila o arquivo MK_WGRP.R
        rCompilarArquivoMK_WGRP();
        
        // Importação dos dados vindos do banco e conversão para objetos R
        rGetSeriesTemporais(str);

        // Estudo WGRP e geração dos resultados em tabeça e imagem
        rEstudoWGRP();
        
//        System.out.println("\n** ---------------------------\n\n TENTANDO FINALIZAR O R...");
//        re.end();
        System.out.println("\nPronto...");

        // Encerra a JVM
//        Runtime.getRuntime().exit(1);
//        System.exit(0);
    }

    /**
     * Verifica se as condições necessárias para a inicialização da sessão R
     * foram atendidas e tenta criar a sessão. Ao criá-la, o ambiente R está
     * pronto para uso.
     */
    private static void rIniciarREngine() {
        // Checa se as atuais versões do Java e do JRI são compatíveis.
        if (!Rengine.versionCheck()) {
            System.err.println("** Erro de compatibilidade Java/JRI-REngine.");
            System.exit(1);
        }

        System.out.println("\n-------------------");
        System.out.println("Criando JRI REngine");

        // Argumentos passados para a sessão do R
        String[] args = new String[2];
        args[0] = "--quiet"; // Não exibe mensagem inicial
        args[1] = "--no-save";// Não salva o workspace ao fim da sessão

        // Inicia o Rengine (JRI)
        re = new Rengine(args, false, null);

        // Checa o estado do R, retorna true se está executando
        System.out.println("REngine criada, esperando pelo R...");
        if (!re.waitForR()) {
            System.out.println("Não foi possível carregar R");
        } else {
            System.out.println("REngine pronta.");
            System.out.println("-------------------\n");
        }

        // REXP "env" referencia o global enviroment da sessão R executada
        rEnv();
        // REXP's rexpTempos e rexpTipos 
        rEXPEntrada();
        // REXP referencia o diretório de trabalho
        rexpWd = re.eval("getwd()");
        // String pública com o caminho do diretório de trabalho, com "/"
        R_LOCAL = rexpWd.asString();
        // String pública com o path do diretório de trabalho, com "\\"
        // A classe java Path usa barras duplas invertidas, "\\"
        R_PATH = R_LOCAL.replace("/", "\\");
    }

    /**
     * Compila o arquivo MK_WGRP.R na R session usando o método source(path),
     * onde "path" está localizado no caminho definido em R_LOCAL.
     */
    private static void rCompilarArquivoMK_WGRP() {
        re.eval("source(\"" + R_LOCAL + "/r/MK_WGRP.R\")");
        rEnv();
    }
    
    /**
     * Método privado utilizado no estudo WGRP. Substitui as linhas 104 e 105 do
     * Generci_Code.R
     *
     * Define as variáveis em R que serão usadas nos cálculos, de acordo com os
     * valores calculados em Consulta.SerieTemporal.
     *
     * Em resumo, converte dados das variáveis Java para R.
     *
     * @param st
     */
    private static void rGetSeriesTemporais(SerieTemporal st) {
        RVector rvTempos = new RVector();
        RVector rvTipos = new RVector();

        // Array de String Java com os tempos entre
        String[] strArrayTemposEntre = st.getStringArrayTempos();
        // Array de String Java com os tipos de intervenção
        String[] strArrayTipos = st.getStringArrayTipos();

        // Inicializa as variáveis no ambiente R
        re.eval(R_TEMPOS_ENTRE + " = c()");
        re.eval(R_TIPOS + " = c()");

        rEXPEntrada();
        rEnv();

        // Laço que preenche as variáveis "data_base_x" e "data_base_types"
        int i = 0;
        int len = strArrayTemposEntre.length;
        while (i < len) {
            /**
             * O i+1-ésimo elemento de "data_base_x" recebe o i-ésimo elemento
             * do array de string Java com tempos entre
             *
             * O i+1-ésimo elemento de "data_base_types" recebe o i-ésimo
             * elemento do array de string Java com tipos de intervenção
             *
             * i+1-ésimo pois em R, posição inicial = índice [1] e em Java,
             * posição inicial = índice [0]
             *
             * Em R, "data_base_x[i + 1] <- strArrayTemposEntre[i]"
             */
            rvTempos.add(strArrayTemposEntre[i]);
            rvTipos.add(strArrayTipos[i]);

            re.eval(R_TEMPOS_ENTRE + "[" + String.valueOf(i + 1)
                        + "] <- " + rvTempos.elementAt(i));
            rEXPEntrada();
            rEnv();

            re.eval(R_TIPOS + "[" + String.valueOf(i + 1)
                        + "] <- \"" + rvTipos.elementAt(i) + "\"");
            rEnv();
            rEXPEntrada();
            
            i++;
        }
    }

    /**
     * Realiza as principais operações do estudo WGRP, incluindo a modelagem,
     * escolha dos melhores modelos, criação de arquivos txt com resumo do
     * procedimento e imagem com o gráfico.
     */
    private static void rEstudoWGRP() {
        //
        re.eval("n_x=length(data_base_x)");
        rEnv();
        
        // Cria o objeto mle_objs em R. Executa o cálculo principal da
        // modelagem.
        rexpMle_objs = re.eval(R_MLE_OBJS + 
            " = getMLE_objs(timesBetweenInterventions = " + R_TEMPOS_ENTRE +
            ", interventionsTypes = " + R_TIPOS + ")");
        rEnv();
        
        // Chama método de MK_WGRP.R que cria parametersAndICs_table.txt
        rexpICsAndParameters = re.eval("df=summarizeICsAndParametersTable(" +
                R_MLE_OBJS + ", " + R_TEMPOS_ENTRE + ", \"" + R_LOCAL + "/\")");
        rEnv();

        /**
         * Linhas necessárias, pois a chamada da função getOptimum pela JVM
         * não funciona. Daí, processo descrito nesta função teve que ser
         * realizado linha a linha pela JVM. Problema na função subset()
         *
         */
        re.eval("w = as.vector(subset(df, BIC==min(df$BIC))[1,1])");
        re.eval("str_optimum = paste(\"optimum_\", w, sep=\"\")");
        re.eval("optimum = mle_objs[[sub(\" \",\"\",str_optimum,fixed=TRUE)]]");
        
        // REXP referencia o melhor modelo para o caso.
        rexpOptimum = re.eval("optimum");
        rEnv();
        
        // Continuam as chamadas em R
        re.eval("m=2;");
        re.eval("pmPropagations = c(optimum$propagations, " + 
                    "rep(PROPAGATION$KijimaII, m));");
        re.eval("parameters = getParameters(nSamples=1000, " + 
                    "nInterventions=(n_x+m), a=optimum$a, b=optimum$b, " + 
                    "q=optimum$q, propagations = pmPropagations);");
        re.eval("bSample = bootstrapSample(parameters);");
        re.eval("theoreticalMoments = sampleConditionalMoments(parameters)");

        // Cria o arquivo de imagem
        re.eval("try(png(filename = \"wgrp_plot.png\",width=1024,height=768))");
        re.eval("plot.new();");
        
        re.eval("graphicStudyOfCumulativeTimes(x=data_base_x, " +
                    "bootstrapSample= bSample, conditionalMeans = " + 
                    "theoreticalMoments, parameters=parameters);");
        
        // Finaliza o dispositivo gráfico
        re.eval("dev.off();");
    }
    
    /**
     * Este método deve ser chamado sempre após alguma operação importante em
     * relação às variáveis R, para controle das operações.
     *
     * Lista o nome das variáveis do "Global enviroment" da sessão do R.
     *
     * A REXP "env" é atualizada após trechos importantes do código R. Este
     * método é importante, pois uma vez inicializada uma REXP, ela não sofre
     * alterações como o Global Enviroment sofre sempre que variáveis são
     * criadas ou editadas.
     */
    private static void rEnv() {
        env = re.eval("ls.str()");
    }

    /**
     * Atualiza as referências REXP das variáveis "data_base_x" e
     * "data_base_types".
     * 
     * Deve ser chamado após trechos importantes em R para controle do seu
     * conteúdo no JVM
     */
    private static void rEXPEntrada() {
        rexpTempos = re.eval(R_TEMPOS_ENTRE);
        rexpTipos = re.eval(R_TIPOS);
    }
    
    /**
     * ETAPAS DO ALGORITMO GENÉRICO
     *
     * 1. Configuração dos PATHs de origem e destino dos arquivos .R e .txt.
     * Linhas 2 a 11
     *
     * 2. Ler os arquivos "AuxiliaryThiago.R" e "MK_WGRP.P". Linhas 13 e 14
     *
     * 2. Certificar-se da correta instalação dos pacotes R usados nos cálculos.
     * Linhas 17 a 20
     *
     * 3. Importar os dados Linha 22
     *
     * 4. Formatação dos dados. Linhas 23 a 88
     *
     * 5. Criação de tabelas auxiliares Linhas 90 a 96
     *
     * 6. Criação da tabela final Linhas 97 a 101
     *
     * 7. Estudo WGRP Linhas 103 a 124
     *
     * 7.1 Tempo entre: Variável R "data_base_x" R integer Linha 104
     *
     * 7.2 Tipo de intervenção: Variável R "data_base_types" R factor Linha 105
     *
     * 8. Verificação da aderência dos dados Linhas 126 a 153
     *
     */
    
    // -------------------------------------------------------------------------
    // Métodos em implementação. Testes
    // -------------------------------------------------------------------------
    
    /**
     * TESTE: lê o MK_WGRP.R
     *
     * @deprecated
     */
    private static void teste() {
        Charset charset = Charset.forName("utf-8");
        Path file = Paths.get(mesor.menu.Janela.LOCAL + 
                    "\\r\\MK_WGRP.R");
                
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            int cont = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(cont + ". | " + line);
//                re.eval(line);
                cont++;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        rEnv();
    }
    
    /**
     * TESTE: criar dois ambientes diferentes, um com objetos criados com o
     * MK_WGRP.R e outro com o Generic_Code.R
     * 
     * @deprecated 
     */
    private static void rConfigAmbientes() {}
    
    /**
     * TESTE: Configurar os caminhos para leitura dos arquivos R.
     * @deprecated
     */
    private static void rConfigurarPATHs() {}
}
