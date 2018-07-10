# Generic_Code.R
#
# Altere o nome do usuário do pc no PATH5 para o do seu pc, linha 12
# Crie o PATH4 e compile usando source, linha 13 e 14
# O trecho de código da série de Thiago está separado caso ele precise usá-lo. Para outros cases não é necessário
# Em WGRP Study, mude as variáveis data_base_x e data_base_types para as do seu case, linhas 124 e 125
# Execute as linhas 123 a 163
# ---

# Compilar o novo MK_WGRP.R ----
# Alterar nome de usuário para cada pc
PATH5 = "C:/Users/Rubens Jr/Google Drive/MESOR/_Orientandos/RubensOliveira/"
PATH4 = paste(PATH5, "MK_WGRP.R", sep="")
source(PATH4, print.eval = TRUE)

# Thiago ----
#rm(list=ls())#NÃ£o Ã© necessÃ¡rio, pois o cÃ³digo AuxiliaryThiago.R jÃ¡ farÃ¡ isso
#ENDERE?O DA PASTA MESOR
PATH = "C:/Users/Rubens Jr/Google Drive/MESOR/_Orientandos/ThiagoRodrigues/" #TIAGO
#PATH = "C:/cloud/GoogleDrive/UFCA/Pesquisa/MESOR/_Orientandos/ThiagoRodrigues/" #Paulo Renato

#local onde salvar a tabela dos par?metros
PATH2 = paste(PATH, "parametersAndICs_table.txt", sep="")

#onde se encontra o c?digo MK_WGRP.R e o "AuxiliaryThiago.R"
PATH3 = paste(PATH, "AuxiliaryThiago.R", sep="")
PATH4 = "C:/Users/Rubens Jr/Documents/NetBeansProjects/Programa/r/MK_WGRP.R"
#lendo o c?digo Auxiliary e mk_wgrp
source(PATH3, print.eval = TRUE)
source(PATH4)

# Carregando e instalando pacotes
includePackage("MASS") 
#includePackage("fitdistrplus")
includePackage("readxl")
includePackage("zoo")

data_base <- read_excel(paste(PATH,"/case_gas/queima_perda.xlsx", sep="")) #Imporatando os dados (Thiago)
data_base$data= as.yearmon(data_base$data, format="%m-%Y") #Convertendo a coluna data em formato date*

#Criando vetor num?rico dos meses
mes = format(data_base$data, format="%m")
mes = as.numeric(mes)
data_base$mes = mes
year = format(data_base$data, format="%Y")
year = as.numeric(year)
data_base$year = as.numeric(year)
boxplot(data_base$dados~data_base$year)

#Criando vetores auxiliares e para os quantis 75 e 95
#par(mfrow=c(1,2))
trashold_75 = NULL; 
trashold_95= NULL
extremeEventsIndexes = NULL;
extremeEventsIndexes_75 = NULL;
extremeEventsIndexes_95 = NULL;
#aux = extremeEventsIndexes
#Inserindo valores nos vetores criados anteriormente
getThiagoDataset = function(data_base){#Para tratamento das heterogeneidade temporal dos dados de Thiago
  tbYears = table(data_base$year)
  years = as.numeric(names(tbYears))
  nYears = length(tbYears)
  #i=1
  for(i in 1:nYears){
    year_i = years[i]
    aux_table = subset(data_base, data_base$year==year_i)#View(aux_table)
    trashold_75_year = quantile(aux_table$dados, .75)
    trashold_95_year = quantile(aux_table$dados, .95)
    trashold_75 = c(trashold_75, trashold_75_year)
    trashold_95= c(trashold_95, trashold_95_year)
    
    indexes_year_75 = which(data_base$year==year_i & data_base$dados >= trashold_75_year & data_base$dados < trashold_95_year)
    extremeEventsIndexes_75 = c(extremeEventsIndexes_75, indexes_year_75)
    
    indexes_year_95 = which(data_base$year==year_i & data_base$dados >= trashold_95_year)
    extremeEventsIndexes_95 = c(extremeEventsIndexes_95, indexes_year_95)
    
    # plot(aux_table$dados, type = "l", axes=FALSE)
    # abline(h=trashold_75_mes)
    # abline(h=trashold_95_mes)
    # axis(2)
    # axis(1, labels = paste(aux_table$data, sep=""),at=seq(1, nrow(aux_table)))
  }
  vectors = list(extremeEventsIndexes_75 = extremeEventsIndexes_75, extremeEventsIndexes_95 = extremeEventsIndexes_95)
  return (vectors)
}
getVIviDataset = function(data_base){#Para tratamento das heterogeneidade temporal dos dados de Vivi
  #???
}
getRuteDataset = function(data_base){#Para tratamento das heterogeneidade temporal dos dados de Rute
  #???
}
aux = getThiagoDataset(data_base)#trocar pela de VIvi e Rute, quando oportuno
#Ordenando os ?ndicies dos eventos extremos
extremeEventsIndexes_75 = sort(aux$extremeEventsIndexes_75)
extremeEventsIndexes_95 = sort(aux$extremeEventsIndexes_95)

#Analisando o tamanho do vetor de cada quantil
n_75 = length(extremeEventsIndexes_75)
n_95 = length(extremeEventsIndexes_95)
type_75 = rep(x = "Q75", n_75)
type_95 = rep(x = "Q95", n_95)
extremeEventsIndexes = c(extremeEventsIndexes_75, extremeEventsIndexes_95)
type = c(type_75, type_95)

#Criando a tabela com os ?ndices e o tipo de quantil
db = data.frame(extremeEventsIndexes, type)
View(db)
#Ordenando a tabela em rela??o aos ?ndices dos quantis
db = db[order(db$extremeEventsIndexes),]
#eixo x=      --- eixo y= indices dos eventos extremos
plot(db$extremeEventsIndexes)
#Calculando os tempos entre os eventos extremos
n = length(db$extremeEventsIndexes)
x = db$extremeEventsIndexes[2:n] - db$extremeEventsIndexes[1:(n-1)]
table = data.frame (x, type=db$type[2:n])
View(table)

# WGRP STUDY ----

# Alterar as variáveis para cada case
# data_base_x : tempos entre intervenção
# data_base_types : tipos de intervenção
data_base_x = c(1:40)
data_base_types = c("P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C","P", "C")

#engine <- function(data_base_x, data_base_types) {

# Tamanho da série
n_x = length(data_base_x)

# Calcula mle_objs
mle_objs = getMLE_objs(timesBetweenInterventions=data_base_x, interventionsTypes=data_base_types) # Importante

# Cria o objeto da tabela dos parametros no R e o arquivo txt em "C:/Users/Public" - pode ser alterado este path
df = summarizeICsAndParametersTable(mle_objs, data_base_x, "C:/Users/Public/") #resultado do getMLE. Precisa ser exibido (tabela df)

# Lixo
#optimum_RP = mle_objs$optimum_RP ##
#optimum_NHPP = mle_objs$optimum_NHPP
#optimum_KijimaI = mle_objs$optimum_KijimaI
#optimum_KijimaII = mle_objs$optimum_KijimaII
#optimum_InterventionType = mle_objs$optimum_InterventionType
#optimum = optimum_KijimaII ## --------------------- selecionar melhor modelo automaticamente

# Seleciona o Otimum chamando a função getOptimum
optimum = getOptimum(mle_objs, df)

# Verifica se o propagations é nulo (RP ou NHPP).
# Se for, atribui um vetor (1,1,1,...,1)
if(is.null(optimum$propagations)==TRUE){optimum$propagations = rep(1, n_x)}

# m=2 previsao p/ t+1 e t+2
m=2; pmPropagations = c(optimum$propagations, rep(PROPAGATION$KijimaII, m)); parameters = getParameters(nSamples=1000, nInterventions=(n_x+m), a=optimum$a, b=optimum$b, q=optimum$q, propagations = pmPropagations); 

# Valores simulados, linhas cinzas tracejadas
bSample = bootstrapSample(parameters);#theoreticalMoments = sampleConditionalMoments(parameters);
#rq = rep(0.5, n+m); rq_x = qwgrp(n+m, a=parameters$a, b=parameters$b, q=parameters$q, models = parameters$models, rq)$times

#valores medio teoricos, linha azul
theoreticalMoments = sampleConditionalMoments(parameters)

# Gera o gráfico
forecasting = graphicStudyOfCumulativeTimes(x=data_base_x, bootstrapSample= bSample, conditionalMeans = theoreticalMoments, parameters=parameters);

#}
computeForecastingTable(forecasting, 0, paste(PATH5, "TableIC.txt", sep = ""));
#optimum$optimum$value
# mle_objs$optimum_InterventionType$parameters
# optimum_InterventionType

# Fim do WGRP study

# Verificando Ader?ncia aos Dados ----
#Realizando a transforma??o de W

timebetween = data_base_x
a<-optimum$parameters$a
Bta <- optimum$parameters$b
q <- optimum$parameters$q
V = optimum$virtualAges
W = numeric(n_x)
i=1
W[i] = (timebetween[i] + 0)^Bta - (0)^Bta 
for(i in 2:n_x){
  W[i] = (timebetween[i] + V[i-1])^Bta - (V[i-1])^Bta 
}
#Substituindo os zeros no vetor W
W[W==0]=1e-100

histogram = hist(W, freq=FALSE, main = "")
mean_ = mean(W)
lambda = 1/mean_
dExp = function(x){
  dexp(x, rate=lambda)
}
curve(expr = dExp, add=TRUE, col="blue", type = "l")
ks = ks.test(W, "pexp", rate = lambda)
text(x = mean(c(min(W), max(W))),
     y = 0.8*max(histogram$density),
     labels = paste("p*=", round(ks$p.value, 3), ";Exp", "rate=",round(lambda, 4)))
# a = fitdist(W,"exp", "mge")
# lambda= a$estimate[1]
#   shape= Bta
#   scale= a
#   dWeibull = function(x){
#     dweibull(x, shape, scale)
#   }
#  curve(expr = dWeibull, add=TRUE, col="red")
#  ks1 = ks.test(W, "pweibull",  shape , scale)
#  text(x = mean(c(min(W), max(W))),
#       y = max(histogram$density),
#       labels = paste("p*=", round(ks1$p.value, 3), "; Wei", "(shape, scale)=(",round(shape, 3), ",", round(scale, 1),")"))
