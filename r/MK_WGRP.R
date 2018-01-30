#rm(list=ls()) # nÃ£o precisa, pois jÃ¡ foi declarado em "AuxiliaryThiago.R".
#PATH = "C:/Users/Thiago Luiz/Google Drive/MESOR/" #TIAGO # nÃ£o precisa, pois jÃ¡ foi declarado em "Generic_Code.R".
#source('C:/cloud/GoogleDrive/UFCA/Pesquisa/R/MyRCodes/Auxiliary/Auxiliary.R')
#source(paste(PATH, "_Orientandos/ThiagoRodrigues/AuxiliaryThiago.R", sep=""))
set.seed(0)
#CONSTANTS
isUnderDebug=FALSE
INTERVENTION_TYPE=list(PM="PM", CM="CM")
PROPAGATION = list(KijimaI = 1, KijimaII = 0)#KijimaI:propagation=1; KijimaII:propagation=0... see v function
PM_TIMES = "PM TIMES"; TIMES_RELIABILITY = "PM TIMES RELIABILITY"; PM_RELIABILITIES = "PMs RELIABILITIES"
nearZero = 1e-100; boundsNames = c("min","max")
aBounds = list(nearZero, -1); bBounds = list(nearZero, 5); 
qBounds = list(0, 1);  names(aBounds) = boundsNames; names(bBounds) = boundsNames;names(qBounds) = boundsNames; 
FORMALISM = list(RP="RP", NHPP="NHPP", KIJIMA_I = "Kijima I", KIJIMA_II = "Kijima II", INTERVENTION_TYPE = "Intervention type-based", GENERIC_PROPAGATION = "Generic propagation-based")
#PACKAGES
includePackage<-function(packName){
  #VERIFYING REQUIRED PACKAGES
  #strPackName <- as.character(packName)
  pack <- NULL
  if(require(packName, character.only = TRUE)==FALSE){
    packages<-available.packages()
    sum_<-sum(packages[,1]==packName)
    if(sum_==0){
      pack<-download.packages(packName, destdir="C:/Users/firminpr/Documents/R/win-library/2.15");
    }
    install.packages(packName)
    require(packName, character.only = TRUE)
  }
}
getParameters=function(nSamples=0, nInterventions=NULL, a=NULL, b=NULL, q=NULL, propagations=NULL, reliabilityQuantiles=NULL
                       , previousVirtualAge=0, interventionsTypes=NULL, formalism=FORMALISM$RP){
  parameters = list(nSamples=nSamples
                    , nInterventions=nInterventions
                    , a=a, b=b, q=q
                    , propagations=propagations, reliabilityQuantiles=reliabilityQuantiles
                    , previousVirtualAge=previousVirtualAge
                    , interventionsTypes=interventionsTypes, formalism=formalism); 
  return(parameters)
}
#INFORMATION CRITERIA
ic_wgrp=function(optimumObj, x){
  nPar=-1;	n=length(x);
  formalism = optimumObj$parameters$formalism
  if(formalism==FORMALISM$RP)nPar=2
  else if(formalism==FORMALISM$NHPP)nPar=2
  else if(formalism==FORMALISM$KIJIMA_I)nPar=3
  else if(formalism==FORMALISM$KIJIMA_II)nPar=3
  else if(formalism==FORMALISM$INTERVENTION_TYPE) nPar=5
  else if(formalism==FORMALISM$GENERIC_PROPAGATION) nPar=3+n
  AIC = -2*optimumObj$optimum$value
  AIC = AIC + 2*nPar
  
  AICc = AIC + 2*nPar*(nPar+1)/(n-nPar-1)
  
  BIC = -2*optimumObj$optimum$value + nPar*log(n)
  
  ret = list(AIC=AIC, AICc = AICc, BIC=BIC)
  return(ret)
}
#VIRTUAL AGE FUNCTION
v <- function(propagation, q, currentVirtualAge, x){
  # Propagation = teta
  KijimaI = currentVirtualAge + q*x
  KijimaII = q*(currentVirtualAge + x)
  virtualAge = propagation*KijimaI + (1-propagation)*KijimaII
  
  #if(model==KijimaI){
  #virtualAge = currentVirtualAge + q*x
  #}
  #else if(model==KijimaII){
  #virtualAge = q*(currentVirtualAge + x)
  #}
  #else...FOR OTHER FUNCTIONS...
  result = list(propagation, virtualAge)
  names(result) = c("propagation","virtualAge")
  result
}
getSampleVirtualAges = function(x, q, propagations){
  n = length(x);
  virtualAges=numeric(n);
  previousVirtualAge = 0;
  if(length(propagations)==0)propagations=rep(-1,n)
  for (i in 1:n){
    virtualAges[i] = v(propagations[i], q, previousVirtualAge, x[i])$virtualAge
    previousVirtualAge = virtualAges[i]
  }
  virtualAges
}
#INVERSE GENERATION OF WGRP SAMPLES
qwgrp<-function(n, a, b, q, propagations, reliabilityQuantiles=NULL, previousVirtualAge=0){
  x=numeric(n); virtualAges=numeric(n);
  if(length(reliabilityQuantiles)==0)
    reliabilityQuantiles = runif(n)
  u = reliabilityQuantiles
  for (i in 1:n){
    while(u[i]==0) u[i]=runif(1)
    aux = a*((previousVirtualAge/a)^b-log(u[i]))^(1/b) - previousVirtualAge
    x[i] = 0
    if(is.finite(aux)){
      if(aux>0)
        x[i]=aux
      else{
        g=1
      }
    }else{
      g=1
    }
    virtualAges[i] = v(propagations[i], q, previousVirtualAge, x[i])$virtualAge
    previousVirtualAge = virtualAges[i]
  }
  #write.csv(cbind(x, virtualAges), file="C:/firminpr/My Dropbox/CompetitiveRisks/GRP/grp_paper2/Simulation/trash2.csv")
  ret = list(reliabilityQuantiles, x, virtualAges)
  names(ret)=c("reliabilityQuantiles", "times", "virtualAges")
  ret
}
#WGRP DENSITY
dwgrp<-function(x, a, b, v, log){
  n = length(x)
  result = rep(-Inf, n)
  tryCatch({
    for(i in 1:n){
      if((x[i]+v)>0 & a>0 & b>0){
        result[i] = log(b) - b*log(a) + (b-1)*log(x[i]+v) + (v/a)^b - ((x[i]+v)/a)^b
      }
    }
    if(!log){
      result = exp(result)
    }
  }, warning = function(war){
    print(paste("dwgrp_WARNING:  ",war))
  }, error = function(err){
    print(paste("dwgrp_ERROR:  ",err))
  })
  result
}
#WGRP CUMULATIVE DISTRIBUTION FUNCTION
pwgrp<-function(x, a, b, v, lower.tail = TRUE, log=FALSE){
  n=length(x)
  result = rep(-1, n); a_ = rep(a,n); b_=rep(b,n); 
  tryCatch({
    result = (v/a_)^b_ - ((v+x)/a_)^b_
    if(!log){
      result = exp(result)
      if(lower.tail){
        result = 1 - result;
      }
    } else if(lower.tail){
      result = log(1 - exp(result));
    }
  }, warning = function(war){
    print(paste("pwgrp_WARNING:  ",war))
  }, error = function(err){
    print(paste("pwgrp_ERROR:  ",err))
  })
  result
}
#WGRP LIKELIHOOD
lwgrp<-function(x, a, b, q, propagations, log){
  n = length(x); virtualAges = getSampleVirtualAges(x, q, propagations); previousVirtualAge = 0
  l=-Inf
  tryCatch({
    if(a>0 & b>0){
      l = 0; 
      #write.csv(cbind(x, virtualAges), file="C:/firminpr/My Dropbox/CompetitiveRisks/GRP/grp_paper2/Simulation/trash.csv")
      for(i in 1:n){
        l = l + dwgrp(x[i], a, b, previousVirtualAge,log=TRUE)
        previousVirtualAge = virtualAges[i]
      }
    }
  }, warning = function(war){
    print(paste("lwgrp_WARNING: (a,b,q)= (",a,",",b, ",", q,")" ,war))
  }, error = function(err){
    print(paste("lwgrp_ERROR: (a,b,q)= (",a,",",b, ",", q,")" , err))
  })
  res = l
  if(!log)
    res = exp(l)
  res
}
#WGRP MAXIMUM LIKELIHOOD ESTIMATION
mle_wrgp<-function(x, pParameters){
  set.seed(0)
  includePackage("GenSA"); includePackage("stats")
  n = length(x);
  #par[1]= b, par[2]=q
  getVirtualAgesAnd_a = function(b, q, propagations){
    virtualAges = getSampleVirtualAges(x, q, propagations)
    sum = 0; previousVirtualAge = 0; 
    for(i in 1:n){
      sum = sum + (x[i] + previousVirtualAge)^b - previousVirtualAge^b
      previousVirtualAge = virtualAges[i]
    }
    sum = sum/n;
    a = sum^(1/b)
    ret = list(a, virtualAges)
    names(ret) = c("a", "virtualAges")
    ret
  }
  objetvieFunction = function(parameters){
    b = parameters[1]; propagations = rep(0, n); 
    q=Inf
    if(pParameters$formalism==FORMALISM$RP)q=0
    else if(pParameters$formalism==FORMALISM$NHPP)q=1
    else if(pParameters$formalism==FORMALISM$KIJIMA_I){q=parameters[2]; propagations = rep(PROPAGATION$KijimaI, n)}
    else if(pParameters$formalism==FORMALISM$KIJIMA_II){q=parameters[2]; propagations = rep(PROPAGATION$KijimaII, n)}
    else if(pParameters$formalism==FORMALISM$INTERVENTION_TYPE){
      q=parameters[2]; 
      propagations = ifelse(pParameters$interventionsTypes==INTERVENTION_TYPE$PM, parameters[3], parameters[4])}
    else if(pParameters$formalism==FORMALISM$GENERIC_PROPAGATION){q=parameters[2]; propagations=parameters[3:(n+2)]}
    log_likelihood = -Inf
    if(b>0){
      a = getVirtualAgesAnd_a(b, q, propagations)$a	
      if(is.finite(a)){	
        log_likelihood = lwgrp(x, a, b, q, propagations, log=TRUE)
      }
    }
    ret=-log_likelihood
    ret
  }	
  #  control<-list(maxit = 1000, nb.stop.improvement = 50, smooth = TRUE, max.call = 3000, max.time = 10, temperature = 150, verbose = FALSE);	
  control<-list(maxit = 2000, nb.stop.improvement = 50, smooth = TRUE, max.call = 3000, max.time = 20, temperature = 150, verbose = FALSE);	
  b = 1; q = 0
  lower = c(bBounds$min); upper = c(bBounds$max); par= c(b)
  if(pParameters$formalism==FORMALISM$RP)q=0
  else if(pParameters$formalism==FORMALISM$NHPP)q=1
  else{
    lower = c(lower, qBounds$min); upper = c(upper, qBounds$max); par=c(par, pParameters$q, pParameters$propagations); 
    if(pParameters$formalism==FORMALISM$INTERVENTION_TYPE){ lower = c(lower, rep(0,2)); upper = c(upper, rep(1,2)); }
    else if(pParameters$formalism==FORMALISM$GENERIC_PROPAGATION){lower = c(lower, rep(0,n)); upper = c(upper, rep(1,n));}
  }
  opt=GenSA(par=as.vector(par), lower = lower, upper = upper, fn = objetvieFunction, control=control); b = opt$par[1]; 
  opt$value=-opt$value;
  propagations = NULL;
  if(pParameters$formalism!=FORMALISM$RP & pParameters$formalism!=FORMALISM$NHPP){ 
    q = opt$par[2]
    if(pParameters$formalism==FORMALISM$KIJIMA_I){propagations = rep(PROPAGATION$KijimaI, n)}
    else if(pParameters$formalism==FORMALISM$KIJIMA_II){propagations = rep(PROPAGATION$KijimaII, n)}
    else if(pParameters$formalism==FORMALISM$INTERVENTION_TYPE) propagations = ifelse(pParameters$interventionsTypes==INTERVENTION_TYPE$PM, opt$par[3], opt$par[4])
    else if(pParameters$formalism==FORMALISM$GENERIC_PROPAGATION) propagations= opt$par[3:length(opt$par)]
  }
  #opt = nlm(f=objetvieFunction, p=par, iterlim =300, gradtol = 1e-50, steptol = 1e-50, check.analyticals = FALSE); b =opt$estimate[1]; q = opt$estimate[2];
  a_vs = getVirtualAgesAnd_a(b, q, propagations); a = a_vs$a; virtualAges = a_vs$virtualAges
  pParameters$a=a; pParameters$b=b; pParameters$q=q; pParameters$propagations=propagations; 
  par = list(a=a, b=b, q=q, propagations=propagations, virtualAges=virtualAges, optimum=opt, parameters=pParameters)
  print(paste("l(ponto estimado, ",pParameters$formalism, ", a=",round(a, 4), ", b=", round(b, 4),", q=", round(q, 4), ")=", opt$value))
  par
}
#WGRP ESTIMATION VIA MINIMUM MAX CUMULATIVE TIMES DISTANCE 
mmd_wrgp<-function(x, propagations){
  includePackage("GenSA"); includePackage("stats")
  n = length(x);
  cumX = numeric(n); cumX[1] = x[1]
  for (i in 2:n){
    cumX[i] = cumX[i-1]+x[i]
  }
  #par[1]= a, par[2]= b, par[3]=q
  par = c(1, 1, 1)
  objetvieFunction = function(parameters){
    a = parameters[1]; b = parameters[2]; q=parameters[3]; 
    rq = rep(0.5, n); rq_x = qwgrp(n, a=a, b=b, q=q, propagations = propagations, rq)$times		
    cumQ = numeric(n); cumQ[1] = rq_x[1]
    max_d = -Inf; mse=0;ret=0
    for(i in 2:n){
      cumQ[i] = cumQ[i-1]+rq_x[i]
      diff = abs(cumQ[i] - cumX[i])
      if(diff > max_d){
        max_d = diff
      }
      mse = mse + diff^2
    }
    mse = mse/n
    #ret = ret + mse
    ret = ret + max_d
    ret
  }	
  control<-list(maxit = 1000
                #,threshold.stop = 0
                ,nb.stop.improvement = 20
                ,smooth = TRUE
                ,max.call = 30000
                ,max.time = 60
                ,temp = 3000
                ,verbose = FALSE
  );	lower = c(aBounds$min, bBounds$min, qBounds$min); upper = c(aBounds$max, bBounds$max, qBounds$max); 
  opt=GenSA(par=as.vector(par), lower = lower, upper = upper, fn = objetvieFunction, control=control); a = opt$par[1]; b = opt$par[2]; q = opt$par[3];
  #opt = nlm(f=objetvieFunction, p=par, iterlim =300, gradtol = 1e-50, steptol = 1e-50, check.analyticals = FALSE); b =opt$estimate[1]; q = opt$estimate[2];
  virtualAges = getSampleVirtualAges(x, q, propagations)
  par = list(a, b, q, virtualAges, opt)
  names (par)=c("a", "b", "q", "virtualAges", "optimum")
  par
}
#WGRP KS GOODNESS-OF-FIT
ks_wgrp <- function(x, a, b, q, v=NULL, models){
  n = length(x); Rx = rank(x)/n; Fx = numeric(n); maxD = 0; 
  if(length(v)==0){ 
    v = getSampleVirtualAges(x, q, models)
  }
  print(ks.test(x, "pwgrp", a=a, b=b, v=v))
  currentVirtualAge = 0; 
  for(i in 1:n){
    Fx[i] = pwgrp(x[i], a, b, currentVirtualAge)
    currentVirtualAge = v[i]
    diff = abs(Fx[i] - Rx[i])
    if(diff > maxD){
      maxD = diff
    }
  }
  #npvalue=0
  #if(maxD>(1/(2*n)) & maxD<=(1/n)){
  #npvalue=
  #}
  #pvalue=1-npvalue
  data = as.data.frame(cbind(x, Rx, Fx))
  oData = data[do.call(order, data),]
  plot(oData$x, oData$Fx, col="black", type="l", lwd=2, ylab="Cumulative distribution", xlab="x"
       , main="Rx (gray) vs WGRP Fx (black)"
       ,sub=paste( "Fx with parameters (a, b, q)=("
                   , round(a, 2), ", ", round(b, 2), ", ", round(q, 2), ")"))
  points(oData$x, oData$Rx, type="l", lwd=2, col="gray")
  
  maxD
}
#CONDITIONAL EXPECTATION...
conditionalMoments=function(parameters, v){
  a=parameters$a; b=parameters$b; x_aux = 1/b; q1 = (v/a)^b; q2= 0
  incompleteGamma = function (x){
    integralLowerBound=1/b
    pgamma(q=x, shape=integralLowerBound, lower=FALSE)*gamma(integralLowerBound)
  }
  upperGamma1 = incompleteGamma(q1); upperGamma2 = incompleteGamma(q2)
  gamma1 = gamma(1+1/b); Ex = 0; 	Vx = 0
  #aux = exp((v/a)^b) * (a/b) * (b*gamma1 + upperGamma1 - upperGamma2)
  l_aux = (v/a)^b; 
  l_aux = l_aux + log(a) - log(b)
  l_aux2 =  b*gamma1 + upperGamma1 - upperGamma2;
  l_aux2 =  log(l_aux2);#perhaps this abs(.) call is not correct...
  l_aux = l_aux + l_aux2;
  aux = exp(l_aux)
  if(is.finite(aux)){
    if(aux>0){
      Ex=aux
    }
  }
  R_Ex = pwgrp(Ex, parameters$a, parameters$b, v, lower.tail=FALSE)
  ret = list(Ex, Vx, R_Ex); names(ret)=c("mean", "var", "R_mean")
  ret
}
sampleConditionalMoments<-function(parameters){
  n=parameters$nInterventions; q = parameters$q;
  Ex=numeric(n); Vx=numeric(n); R_Ex=numeric(n); virtualAges=numeric(n);
  currentVirtualAge = parameters$previousVirtualAge;
  for (i in 1:n){
    condMom = conditionalMoments(parameters, currentVirtualAge)
    Ex[i] = condMom$mean
    Vx[i] = condMom$var
    R_Ex[i] = condMom$R_mean
    currentVirtualAge = v(parameters$propagations[i], q, currentVirtualAge, Ex[i])$virtualAge
    virtualAges[i] = currentVirtualAge
  }
  ret = list(Ex, Vx, R_Ex, virtualAges)
  names(ret)=c("mean", "var", "R_mean", "virtualAges")
  ret
}
bootstrapSample = function(parameters){
  sampleMatrix = matrix(nrow=parameters$nSamples, ncol=parameters$nInterventions)#each row is a system interventions history involving nInterventions interventions
  for(i in 1:parameters$nSamples){
    sample = qwgrp(n=parameters$nInterventions, parameters$a, parameters$b, parameters$q, parameters$propagations, NULL, parameters$previousVirtualAge)
    sampleMatrix[i,]=sample$times
  }
  sampleMatrix
}
bootstrapConditionalMoments = function(sampleMatrix){
  nInterventions = ncol(sampleMatrix)
  sampleMeans= numeric(length=nInterventions)
  sampleVars= numeric(length=nInterventions)
  for(j in 1:nInterventions){
    sampleMeans[j] = mean(sampleMatrix[,j])
    sampleVars[j] = var(sampleMatrix[,j])
  }
  ret = as.data.frame(cbind(sampleMeans, sampleVars))
  ret
}
sampleGraphicStudy<-function(sample, title, add, border, breaks, lty){
  #if(breaks=="")
  #	breaks = "Sturges"
  hist(sample, main = title, add=add, border=border, freq=FALSE, breaks=breaks, lty=lty, right = FALSE)
  #f<-function(x){dwgrp(x, a,b,20,FALSE)}
  #curve(expr=f, from=min(sample), to=max(sample), add=TRUE)	
}
comparingHistograms=function(parameters){
  aux1 = c(KijimaII, KijimaII); models = rep(aux1, each=parameters$nInterventions/2)
  sample1 = rwgrp(nInterventions, parameters$a, parameters$b, parameters$q, models); 
  aux2 = c(KijimaI, KijimaI); models = rep(aux2, each=parameters$nInterventions/2)
  sample2 = rwgrp(nInterventions, parameters$a, parameters$b, parameters$q, models); 
  aux3 = c(KijimaI, KijimaII); models = rep(aux3, each=parameters$nInterventions/2)
  sample3 = rwgrp(nInterventions, parameters$a, parameters$b, parameters$q, models); 
  aux4 = c(KijimaII, KijimaI); models = rep(aux4, each=parameters$nInterventions/2)
  sample4 = rwgrp(nInterventions, parameters$a, parameters$b, parameters$q, models); 
  aux=list(aux2, aux4, aux1, aux3)
  sample=list(sample2, sample4, sample1, sample3)
  border=c("gray", "red", "black", "yellow")
  breaks=seq(from=min(c(sample2, sample4, sample1, sample3))
             , to=max(c(sample2, sample4, sample1, sample3))
             , length.out=round(sqrt(parameters$nInterventions)))
  
  par(mfrow=c(1,1))
  #sub = paste(
  for(i in 1:4)
    sampleGraphicStudy(sample[[i]], paste(aux[[i]][1], aux[[i]][2], sep=", "), add=as.logical(i-1), border=border[i], breaks=breaks, lty=(i+1))
}
graphicStudyOfBootstraping<-function(parameters, bootstrap, theoreticalMoments){
  main=""#"Mean time between interventions"
  sub=(paste("(nSamples, nInterventions, a, b, q)="
             , paste("(", paste(parameters$nSamples, parameters$nInterventions
                                , parameters$a, parameters$b, parameters$q, sep=","), ")")))
  plot(bootstrap$sampleMeans, col="gray", main = main, type="b", xlab="intervention"
       , ylab="theoretical (black) and sampled (gray) means", sub=sub)
  points(theoreticalMoments$mean, col="black", type="b")
}
graphicStudyOfCumulativeTimes=function(x=NULL, bootstrapSample=NULL, conditionalMeans = NULL, parameters){
  #png(filename = "wgrp_plot.png", width = 1024, height = 768)
  quantiles=c(.975, .025); cumX = NULL
  col = list(NULL); legend = list(NULL); lwd = list(NULL); lty = list(NULL)
  rq_x=NULL; n_rq_x = length(parameters$reliabilityQuantiles);cumQ=NULL
  if(n_rq_x>0){
    i=1; 
    rq_x = qwgrp(parameters$nInterventions, parameters$a, parameters$b, parameters$q, parameters$propagations, parameters$reliabilityQuantiles)$times
    col = as.list(c(col, quantile="red"))
    legend = as.list(c(legend, quantile=paste("WGRP ", parameters$reliabilityQuantiles[1], "-based Reliability quantiles")))
    lwd = as.list(c(lwd, quantile=4))
    lty = as.list(c(lty, quantile=2))
    cumQ = numeric(n_rq_x); cumQ[i] = rq_x[i]
    for(i in 2:n_rq_x){
      cumQ[i] = cumQ[i-1]+rq_x[i]
    }
  }
  n_cm = length(conditionalMeans); cumCm = NULL; Ql = NULL; Qu = NULL; 
  if(n_cm>0){
    n_cm = length(conditionalMeans$mean)
    i=1
    #col = as.list(c(col, quantiles="blue"))
    col = as.list(c(col, cm="blue"))
    #legend = as.list(c(legend, quantiles="WGRP 95% mean-based confidence intervals "))
    legend = as.list(c(legend, cm="WGRP conditional means "))
    #lwd = as.list(c(lwd, quantiles=4))
    lwd = as.list(c(lwd, cm=4))
    #lty = as.list(c(lty, quantiles=4))
    lty = as.list(c(lty, cm=2))
    cumCm = numeric(n_cm); cumCm[i] = conditionalMeans$mean[i]
    #previousVirtualAge = parameters$previousVirtualAge;	
    Ql = numeric(n_cm); Ql[i] = qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations=parameters$propagations[i], reliabilityQuantiles = quantiles[1], previousVirtualAge =  parameters$previousVirtualAge)$times
    Qu = numeric(n_cm); Qu[i] = qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations=parameters$propagations[i], reliabilityQuantiles = quantiles[2], previousVirtualAge =  parameters$previousVirtualAge)$times
    for (i in 2:n_cm){
      #previousVirtualAge = v(propagation=parameters$propagations[i-1], q=parameters$q, currentVirtualAge=previousVirtualAge, x=conditionalMeans[i-1])$virtualAge
      Ql[i] = cumCm[i-1]+qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations=parameters$propagations[i-1], reliabilityQuantiles = quantiles[1], previousVirtualAge = conditionalMeans$virtualAges[i-1])$times
      Qu[i] = cumCm[i-1]+qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations=parameters$propagations[i-1], reliabilityQuantiles = quantiles[2], previousVirtualAge = conditionalMeans$virtualAges[i-1])$times
      cumCm[i] = cumCm[i-1] + conditionalMeans$mean[i]
    }
  }
  n_x=length(x); bs_nrow =0;	mean_cumBS = NULL; sQl = NULL; sQu = NULL
  if(n_x>0){
    i=1; 
    col = as.list(c(col, x="black"))
    legend = as.list(c(legend, x="Observed series "))
    lwd = as.list(c(lwd, x=4))
    lty = as.list(c(lty, x=1))
    cumX = numeric(n_x);
    cumX[i] = x[i]
    for(i in 2:n_x){
      cumX[i] = cumX[i-1]+x[i]
    }
  }
  bs_ncol=0; bs_nrow=0; cumBS = NULL
  if(length(bootstrapSample)>0){
    i=1; 
    col = as.list(c(col, sample="gray"))
    col = as.list(c(col, sQ="black"))
    col = as.list(c(col, sMean="black")); 
    legend = as.list(c(legend, sample="WGRP samples "))
    legend = as.list(c(legend, sQ="WGRP samples 95% confidence intervals "))
    legend = as.list(c(legend, sMean="WGRP samples means ")); 
    lwd = as.list(c(lwd, sample=1))
    lwd = as.list(c(lwd, sQ=4))
    lwd = as.list(c(lwd, sMean=4)); 
    lty = as.list(c(lty, sample=4))
    lty = as.list(c(lty, sQ=3))
    lty = as.list(c(lty, sMean=2)); 
    bs_nrow = nrow(bootstrapSample); 
    bs_ncol=ncol(bootstrapSample); pos = round(quantiles*bs_nrow); if(pos[2]==0) pos[2]=1;
    cumBS = matrix(nrow=bs_nrow, ncol=bs_ncol); cumBS[,i] = bootstrapSample[,i]
    mean_cumBS = numeric(bs_ncol); mean_cumBS[i] = mean(cumBS[,i])
    sQl = numeric(bs_ncol); sQu = numeric(bs_ncol); 
    sorted_cbs = sort(cumBS[,i])
    sQu[i] = sorted_cbs[pos[1]]#qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations = parameters$propagations[i], quantiles[1], previousVirtualAge)$times
    sQl[i] = sorted_cbs[pos[2]]#qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations = parameters$propagations[i], quantiles[2], previousVirtualAge)$times
    for(i in 2:bs_ncol){
      cumBS[,i] = cumBS[,i-1]+bootstrapSample[,i]
      mean_cumBS[i] = mean(cumBS[,i])
      sorted_cbs = sort(cumBS[,i])
      sQu[i] = sorted_cbs[pos[1]]#qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations = parameters$propagations[i-1], quantiles[1], previousVirtualAge)$times
      sQl[i] = sorted_cbs[pos[2]]#qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations = parameters$propagations[i-1], quantiles[2], previousVirtualAge)$times
    }
  }
  min = min(cumX, cumQ, cumBS, Ql, Qu, rq_x, cumCm);max = max(cumX, cumQ, cumBS, Ql, Qu, rq_x, cumCm);
  txt_propagation = paste(" (a, b, q)=(", round(parameters$a, 2), ", ", round(parameters$b, 2), ", ", round(parameters$q, 2) , ")")
  #plot(x=c(1, max(n_x,bs_ncol, n_rq_x, n_cm) ), y=c(min, max), col="white", ylab="Cumulative times", xlab="intervention index"
  #	, sub=paste("the WGRP propagation was based on", txt_propagation))
  plot(x=c(1, max(n_x,bs_ncol, n_rq_x, n_cm) ), y=c(min, max), col="white", ylab="Cumulative times", xlab="intervention index"
       , sub=paste("", txt_propagation)) #the WGRP propagation was based on
  if(bs_nrow>0){
    min_=min(20, bs_nrow)
    index = sample(1:bs_nrow, min_)
    min3 = bs_ncol
    if(n_x>0) min3=n_x		
    for(i in 1:bs_nrow){
      points(cumBS[index[i],][1:min3], type="l", col=col$sample, lty = lty$sample, lwd=lwd$sample, pch=19)
    }
    points(sQl, type="l",		 col=col$sQ,  lwd=lwd$sQ, lty=lty$sQ, pch=19)
    points(sQu, type="l",		 col=col$sQ,  lwd=lwd$sQ, lty=lty$sQ, pch=19)
    points(mean_cumBS, type="l", col=col$sMean, lwd=lwd$sMean, lty=lty$sMean, pch=19)
    if(n_x>0 & bs_ncol>n_x){
      points(x=c(n_x,n_x), y=c(min, max), type="l", lty = 4, col="green", main="", lwd=1)
    }
  }
  if(n_rq_x>0){
    points(cumQ, type="l", col=col$quantile, main="", lwd=lwd$quantile, lty=lty$quantile)
  }
  if(n_x>0){
    points(cumX, type="l", col=col$x, main="", lwd=lwd$x, lty=lty$x)
  }
  if(n_cm>0){
    #points(Ql, type="l",		 col=col$quantiles,  lwd=lwd$quantiles, lty=lty$quantiles, pch=19)
    #points(Qu, type="l",		 col=col$quantiles,  lwd=lwd$quantiles, lty=lty$quantiles, pch=19)
    points(cumCm, type="l", col=col$cm, lwd=lwd$cm, lty=lty$cm, pch=19)
  }
  #	legend(x=1, y=max, col=c("black", "blue", "gray", "red"),bg="white", merge=TRUE
  #		, lty=c(1, 3, 3, 4), lwd=c(4, 4, 1, 2), pch=c(-1, -1, -1, -1)
  #		, legend=c("Observed series ", "WGRP propagation Mean ","WGRP samples ","WGRP 95% Confidence Interval "))
  col = col[-1]; lty=lty[-1]; lwd=lwd[-1];legend=legend[-1]
  nLegend = length(col); c_col = character(nLegend); c_lty = numeric(nLegend); c_lwd = numeric(nLegend); c_legend = character(nLegend)
  for(i in 1:nLegend){
    c_col[i] = col[[i]]
    c_lty[i] = lty[[i]]
    c_lwd[i] = lwd[[i]]
    c_legend[i] = legend[[i]]
  }
  legend(x=0, y=max, col=c_col, lty=c_lty, lwd=c_lwd, legend=c_legend, pch=rep(-1, length(lwd)), bg="white", merge=TRUE, bty = "n", cex = 0.9)
  #legend(x=1, y=max, col=c_col, lty=c_lty, lwd=c_lwd, legend=c_legend, pch=rep(-1, length(lwd)), bg="white", merge=TRUE)
  res = list(cumX, cumQ, cumCm, mean_cumBS, sQl, sQu, Ql, Qu); 
  names(res)=c("cumTimes", "cumQuantile", "cumCondMeans", "cumSampleMeans", "sampleLowerBound", "sampleUpperBound", "lowerBound", "upperBound")
  #auxText = paste(Sys.time(), " - (", parameters$a, ", ", parameters$b, ", ", parameters$q, ")", sep = "")
  #mtext(text = auxText, side = 3)
  res
  #dev.off();
}

# -----------------------------------------
graphicStudyForPreventiveMaintainence<-function(parameters, theoreticalMoments, theoreticalReliabilityQuantiles, graphicType){
  #par(mfrow=c(1,1))
  prSuccess_meanBasedPM = 0; prSuccess_reliaBasedPM = 0;
  for (i in 1:parameters$nInterventions){
    prSuccess_meanBasedPM = prSuccess_meanBasedPM + log(theoreticalMoments$R_mean[i])
    prSuccess_reliaBasedPM = prSuccess_reliaBasedPM + log(theoreticalReliabilityQuantiles$reliabilityQuantiles[i])
  }
  prSuccess_meanBasedPM = exp(prSuccess_meanBasedPM)
  prSuccess_reliaBasedPM = exp(prSuccess_reliaBasedPM)
  #PLOTING PM TIMES
  if(graphicType==PM_TIMES){
    main=paste("Mean (black) and ",theoreticalReliabilityQuantiles$reliabilityQuantiles[1], "reliability quantile (gray) times between interventions")
    sub=(paste("(nInterventions, a, b, q)="
               , paste("(", paste(parameters$nInterventions
                                  , parameters$a, parameters$b, parameters$q, sep=","), ")")))
    min = round(c(min(c(theoreticalMoments$mean, theoreticalReliabilityQuantiles$times))), 2)
    max = round(c(max(c(theoreticalMoments$mean, theoreticalReliabilityQuantiles$times))), 2)
    plot(rep(c(min, max), each=parameters$nInterventions/2), main = main, type="b", xlab="intervention"
         , ylab="mean (black) and reliability (gray) times", sub=sub, col="white")
    points(theoreticalReliabilityQuantiles$times, main = main, type="b", xlab="intervention"
           , ylab="mean (black) and reliability (gray) times", sub=sub, col="gray"
           , yaxp = c(min,max, n=3) , lty=1, lwd=1.5)
    points(theoreticalMoments$mean, type="b", xlab="intervention"
           #, ylab="sampled (black) and theoretical (gray) means"
           , sub=sub, col="black", lty=2, lwd=1.5)
  }
  #PLOTING PM TIMES RELIABILITY
  else if(graphicType==TIMES_RELIABILITY){
    main=paste("Reliabilit of the mean (black) and ",theoreticalReliabilityQuantiles$reliabilityQuantiles[1], "quantile (gray)", "times between interventions")
    sub=(paste("(nInterventions, a, b, q)="
               , paste("(", paste(parameters$nInterventions
                                  , parameters$a, parameters$b, parameters$q, sep=","), ")")))
    min = round(c(min(c(theoreticalMoments$R_mean, theoreticalReliabilityQuantiles$reliabilityQuantiles))), 2)
    max = round(c(max(c(theoreticalMoments$R_mean, theoreticalReliabilityQuantiles$reliabilityQuantiles))), 2)
    plot(rep(c(min, max), each=parameters$nInterventions/2), main = main, type="b", xlab="intervention"
         , ylab="mean (black) and reliability (gray) times", sub=sub, col="white")
    points(rep(theoreticalReliabilityQuantiles$reliabilityQuantile, parameters$nInterventions), main = main, type="b", xlab="intervention"
           , ylab="mean (black) and reliability (gray) times", sub=sub, col="gray"
           , yaxp = c(min,max, n=3) , lty=1, lwd=1.5)
    points(theoreticalMoments$R_mean, type="b", xlab="intervention"
           #, ylab="sampled (black) and theoretical (gray) means"
           , sub=sub, col="black", lty=2, lwd=1.5)
    text(x=2, y=0.5, pos=4
         , paste("Probabability of PM policy success (Mean-based, Reliability-based) = ("
                 , round(prSuccess_meanBasedPM, 4), ", ", round(prSuccess_reliaBasedPM, 4), ")"), cex=1)
  }
  #PLOTING PMs RELIABILITIES
  else if (graphicType==PM_RELIABILITIES){
    main=paste("Reliabilit of the mean (black) and ",theoreticalReliabilityQuantiles$reliabilityQuantiles[1], "quantile (gray)", "times between interventions")
    sub=(paste("(nInterventions, a, b, q)="
               , paste("(", paste(parameters$nInterventions
                                  , parameters$a, parameters$b, parameters$q, sep=","), ")")))
    plot(x=c(0, sum(theoreticalMoments$mean))
         , y=c(pwgrp(sum(theoreticalMoments$mean), parameters$a, parameters$b, theoreticalMoments$virtualAges[parameters$nInterventions], lower.tail=FALSE), 1.1)
         , col="white", ylab="WGRP Reliability", xlab="actual time", main=main, sub=sub)
    f = function(x){
      res = theoreticalReliabilityQuantiles$reliabilityQuantiles[1]*x/x
      res
    }
    lwd=2.0
    #curve(f(x), from = 0, to=sum(theoreticalMoments$mean), col="gray", add=TRUE, lwd=lwd)
    actualTime=0;virtualTime=0
    for (i in 1:parameters$nInterventions){
      nextTime = actualTime + theoreticalMoments$mean[i]
      curve(pwgrp(x-actualTime#+theoreticalMoments$virtualAges[i]
                  , parameters$a, parameters$b, virtualTime, lower.tail=FALSE)
            , col="black", from = actualTime, to=nextTime, add=TRUE, lwd=lwd)
      actualTime = nextTime
      points(x=c(actualTime, actualTime), lty=2, type="l", col="black", lwd=lwd
             , y=c(0, pwgrp(0, parameters$a, parameters$b, virtualTime, lower.tail=FALSE)))
      virtualTime = theoreticalMoments$virtualAges[i]
    }
    #plot(x=c(0, sum(theoreticalReliabilityQuantiles$times))
    #, y=c(pwgrp(sum(theoreticalReliabilityQuantiles$times), parameters$a, parameters$b, theoreticalReliabilityQuantiles$times[parameters$nInterventions], lower.tail=FALSE), 1)
    #, col="white", ylab="WGRP Reliability", xlab="actual time", main=main, sub=sub)
    actualTime=0;virtualTime=0
    for (i in 1:parameters$nInterventions){
      nextTime = actualTime + theoreticalReliabilityQuantiles$times[i]
      curve(pwgrp(x-actualTime#+theoreticalReliabilityQuantiles$virtualAges[i]
                  , parameters$a, parameters$b, virtualTime, lower.tail=FALSE)
            , col="gray", from = actualTime, to=nextTime, add=TRUE, lwd=lwd)
      actualTime = nextTime
      points(x=c(actualTime, actualTime), lty=2, type="l", col="gray", lwd=lwd
             , y=c(0, pwgrp(0, parameters$a, parameters$b, virtualTime, lower.tail=FALSE)))
      virtualTime=theoreticalReliabilityQuantiles$virtualAges[i]
    }
    text(x=2, y=1.06, pos=4
         , paste("Probabability of PM policy success (Mean-based, Reliability-based) = ("
                 , round(prSuccess_meanBasedPM, 4), ", ", round(prSuccess_reliaBasedPM, 4), ")"), cex=1, bg="gray")
  }
}
comparingGraphics=function(parameters, qs, bs, graphicType){
  n_qs = length(qs)
  n_bs = length(bs)
  par(mfrow=c(2,1))
  for(i in 1:n_qs){
    parameters$q = qs[i]
    for(j in 1:n_bs){
      parameters$b = bs[j]
      theoreticalMoments = sampleConditionalMoments(parameters)
      theoreticalReliabilityQuantiles = qwgrp(parameters$reliabilityQuantile, parameters$a, parameters$b, parameters$q, parameters$propagations)
      graphicStudyForPreventiveMaintainence(parameters, theoreticalMoments, theoreticalReliabilityQuantiles, graphicType=graphicType)
    }
  }
}
computeForecastingTable=function(forecasting, initialTime){
  n = length(forecasting$cumTimes)
  m = length(forecasting$cumSampleMeans)-n
  intervention = paste(seq(from=(n+1), to=(n+m)), colapse=" & "); intervention = c("intervention & ", intervention, " \\")
  lower = paste(round(forecasting$sampleLowerBound[(n+1):(n+m)]+initialTime, 2), colapse=" & "); lower  = c("2.5% quantile & ", lower , " \\")
  mean = paste(round(forecasting$cumSampleMeans[(n+1):(n+m)]+initialTime, 2), colapse=" & "); mean = c("mean & ", mean, " \\")
  upper = paste(round(forecasting$sampleUpperBound[(n+1):(n+m)]+initialTime, 2), colapse=" & "); upper = c("97.5% quantile & ", upper, " \\")
  print(intervention); print(lower); print(mean); print(upper)
  ret = rbind(intervention, lower, mean, upper)
  write.table(ret, row.names=FALSE, col.names=FALSE, file="C:/cloud/Dropbox/CompetitiveRisks/GRP/grp_paper2/ventured_version/ICs_table.txt")
  ret
}
computeInformationsCriteriaAndMLETable=function(ICs){
  n = length(forecasting$cumTimes)
  m = length(forecasting$cumSampleMeans)-n
  intervention = paste(seq(from=(n+1), to=(n+m)), colapse=" & "); intervention = c("intervention & ", intervention, " \\")
  lower = paste(round(forecasting$sampleLowerBound[(n+1):(n+m)]+initialTime, 2), colapse=" & "); lower  = c("2.5% quantile & ", lower , " \\")
  mean = paste(round(forecasting$cumSampleMeans[(n+1):(n+m)]+initialTime, 2), colapse=" & "); mean = c("mean & ", mean, " \\")
  upper = paste(round(forecasting$sampleUpperBound[(n+1):(n+m)]+initialTime, 2), colapse=" & "); upper = c("97.5% quantile & ", upper, " \\")
  print(intervention); print(lower); print(mean); print(upper)
  ret = rbind(intervention, lower, mean, upper)
  write.table(ret, row.names=FALSE, col.names=FALSE, file="C:/firminpr/My Dropbox/CompetitiveRisks/GRP/grp_paper2/table.txt")
  ret
}
getReliabilityBasedMSE <- function(x, parameters, reliability=.995){
  propagation = parameters$propagations
  i=1
  trueCumulative = NULL; trueCumulative[i] = x[i];
  virtualAge_oneStepAhead = NULL; virtualAge_oneStepAhead[i] = v(propagation[i], q, 0, x[i])
  virtualAge_iStepsAhead = NULL; virtualAge_iStepsAhead[i] = v(propagation[i], q, 0, x[i])
  estimatedCumulative_oneStepAhead = NULL; estimatedCumulative_oneStepAhead[i]=x[i]#the contorn condition is estimated[1] = true[1] # alpha*(-log(reliability))^(1/beta)#for x=0, the virtual age equals zero
  estimatedCumulative_iStepsAhead = NULL; estimatedCumulative_iStepsAhead[i]=x[i]#the contorn condition is estimated[1] = true[1] # alpha*(-log(reliability))^(1/beta)#for x=0, the virtual age equals zero
  for(i in 2:length(x)){
    trueCumulative[i] = trueCumulative[i-1] + x[i]
    forecast_i_oneStepAhead <- qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations =  propagations[i-1]
                                     , reliabilityQuantile=reliability, previousVirtualAge = virtualAge_oneStepAhead[i-1])# alpha*((virtualAge_oneStepAhead[i-1]/alpha)^(beta)-log(reliability))^(1/beta) - virtualAge_oneStepAhead[i-1]
    forecast_i_iStepsAhead <- qwgrp(n=1, a=parameters$a, b=parameters$b, q=parameters$q, propagations =  propagations[i-1]
                                    , reliabilityQuantile=reliability, previousVirtualAge = virtualAge_iStepsAhead[i-1])# alpha*((virtualAge_iStepsAhead[i-1]/alpha)^(beta)-log(reliability))^(1/beta) - virtualAge_iStepsAhead[i-1]
    estimatedCumulative_iStepsAhead[i] = estimatedCumulative_iStepsAhead[i-1] +  forecast_i_iStepsAhead
    estimatedCumulative_oneStepAhead[i] = trueCumulative[i-1] +  forecast_i_oneStepAhead
    virtualAge_oneStepAhead[i] = v(propagation[i], q, virtualAge_oneStepAhead[i-1], x[i]) #virtualAge_oneStepAhead[i-1] + ifelse(propagation[i]==0, qCorr*x[i],qPrev*x[i])
    virtualAge_iStepsAhead[i] = v(propagation[i], q, virtualAge_iStepsAhead[i-1], x[i])#qPrev*estimatedCumulative_iStepsAhead[i]
  }
  MSE = (trueCumulative[1:72]-estimatedCumulative_oneStepAhead[1:72])^2
  MSE = mean(MSE)
  ret = list(MSE=MSE, trueCumulative = trueCumulative, estimatedCumulative_iStepsAhead=estimatedCumulative_iStepsAhead, estimatedCumulative_oneStepAhead=estimatedCumulative_oneStepAhead)
  return (ret)
}
#CEEERMA paper
computeCEERMAcurve <- function(beta=0.1, alpha=103.3, qPrev=0.45, qCorr=0.26, reliability=.995, reliability2=-1, r_1=-1, r_2=-1){
  t <- c(220, 13, 1, 6, 25, 5, 3, 6, 6, 2, 7, 1, 5, 25, 3, 5, 32, 3, 1, 12, 36, 1, 11, 10, 4, 1, 1, 32, 14, 1, 12, 7, 28, 10, 24, 8, 1, 1, 1, 19, 2, 1, 1, 13, 6, 3, 6, 2, 12, 1, 3, 7, 2, 12, 12, 117, 3, 4, 2, 2, 30, 97, 65, 47, 7, 18, 8, 80, 61, 11, 28, 12, 13, 24, 3, 10, 4, 85, 28, 5, 76, 49, 4, 32, 17);
  j <- c(1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1);
  #prev <- NULL
  Real <- NULL
  Accum <- NULL
  R_Accum1 <- NULL
  R_Accum2 <- NULL
  AccumBetween <- NULL
  AccumBetween[1] <- 0
  Accum[1] <- t[1]
  #R_Accum1[1] <- t[1]
  #R_Accum2[1] <- t[1]
  Real[1] <- t[1]
  AccumBetween[1] <- AccumBetween[1] + Accum[1]
  for(i in 2:72){
    if(j[i-1] == 0){
      Accum[i] <- Accum[i-1] + qCorr * t[i]
      AccumBetween[i] <- AccumBetween[i-1] + Accum[i]
    }
    else if(j[i-1] == 1){
      #Accum[i] <- qPrev*(Accum[i-1] + t[i])
      Accum[i] <- Accum[i-1] + qPrev * t[i]
      AccumBetween[i] <- AccumBetween[i-1] + Accum[i]
    }
  }
  for(i in 2:85){
    if(j[i-1] == 0){
      #Real[i] <- Real[i-1] + qCorr * t[i]
      Real[i] <- Real[i-1] + t[i]
      R_95 <- alpha*((R_Accum1[i-1]/alpha)^(beta)-log(r_1))^(1/beta) - R_Accum1[i-1]
      R_99_9 <- alpha*((R_Accum2[i-1]/alpha)^(beta)-log(r_2))^(1/beta) - R_Accum2[i-1]
      R_Accum1[i] <- R_Accum1[i - 1] + qCorr*R_95
      R_Accum2[i] <- R_Accum2[i - 1] + qCorr*R_99_9
    }
    else if(j[i-1] == 1){
      #Real[i] <- Real[i-1] + qPrev * t[i]
      Real[i] <- Real[i - 1] + t[i]
      R_95 <- alpha*((R_Accum1[i-1]/alpha)^(beta)-log(r_1))^(1/beta) - R_Accum1[i-1]
      R_99_9 <- alpha*((R_Accum2[i-1]/alpha)^(beta)-log(r_2))^(1/beta) - R_Accum2[i-1]
      R_Accum1[i] <- R_Accum1[i - 1] + qPrev*R_95
      R_Accum2[i] <- R_Accum2[i - 1] + qPrev*R_99_9
    }
  }
  R_Accum1[72] <- Real[72]
  R_Accum2[72] <- Real[72]
  for(i in 73:85){
    if(j[i-1] == 0){
      R_95 <- alpha*((R_Accum1[i-1]/alpha)^(beta)-log(r_1))^(1/beta) - R_Accum1[i-1]
      R_99_9 <- alpha*((R_Accum2[i-1]/alpha)^(beta)-log(r_2))^(1/beta) - R_Accum2[i-1]
      R_Accum1[i] <- R_Accum1[i-1] + qCorr*R_95
      R_Accum2[i] <- R_Accum2[i-1] + qCorr*R_99_9
    }
    else if(j[i - 1] == 1){
      R_95 <- alpha*((R_Accum1[i-1]/alpha)^(beta)-log(r_1))^(1/beta) - R_Accum1[i-1]
      R_99_9 <- alpha*((R_Accum2[i-1]/alpha)^(beta)-log(r_2))^(1/beta) - R_Accum2[i-1]
      R_Accum1[i] <- R_Accum1[i-1] + qPrev*R_95
      R_Accum2[i] <- R_Accum2[i-1] + qPrev*R_99_9
    }
  }
  
  #reliability = .9965
  #for(i in (length(t)+1):prediction){ 
  Accum[72] <- Real[72]
  for(i in 73:85){ 
    #prev <- alpha*((Accum[i-1]/alpha)^(beta)-log(reliability))^(1/beta) - Accum[i-1]
    prev <- alpha*((Real[i-1]/alpha)^(beta)-log(reliability))^(1/beta) - Real[i-1]
    teste <- prev
    U <- runif(1)
    #if (U < 0.35){
    Accum[i] <- Accum[i-1] + qPrev*prev
    AccumBetween[i] <- AccumBetween[i-1] + Accum[i]
    #}
    #else if(U >= 0.35)
    #{
    #z <- ((0.99)^(1/beta))*Accum[i-1]
    #Accum[i] <- qPred*(Accum[i-1] + z)
    #AccumBetween[i] <- AccumBetween[i-1] + Accum[i]
    #}
  }
  newAccum <- NULL
  newAccum2 <- NULL
  newAccum[1] <- Real[1]#alpha*((Real[1]/alpha)^(beta)-log(reliability))^(1/beta)
  newAccum2[1] <- Real[1]
  
  for(i in 2:85){
    newprev <- alpha*(((qPrev*newAccum[i-1])/alpha)^(beta)-log(reliability))^(1/beta) - qPrev*newAccum[i-1]
    newprev2 <- alpha*(((qPrev*newAccum2[i-1])/alpha)^(beta)-log(reliability2))^(1/beta) - qPrev*newAccum2[i-1]
    newAccum[i] <- newAccum[i-1] + newprev
    newAccum2[i] <- newAccum2[i-1] + newprev2
  }
  
  #Predicted <- NULL
  #teste <- 0
  #for(l in 1:iteration){
  #	t[l] <- alpha*(-log(0.95))^(1/beta)
  #	teste <- teste + t[l]
  #}
  #teste <- teste/iteration
  #Predicted[1] <- teste
  #	for(k in 2:prediction){
  #		teste <- 0
  #		if(j[k-1] == 0)
  #		{for(l in 1:iteration){
  #			t[k] <- alpha*((qPrev*Predicted[i-1]/alpha)^(beta)-log(0.95))^(1/beta) - qPrev*Predicted[i-1]
  #			uso <- t[k]
  #			if(uso < 0){
  #				t[k] <- 0
  #			}
  #			teste <- teste + t[k]
  #		}
  #		teste <- teste/iteration
  #		Predicted[k] <- Predicted[k-1] + teste
  #		}
  #	else if(j[k-1] == 1)
  #	{			
  #		for(l in 1:iteration){
  #			t[k] <- alpha*((qPrev*Predicted[i-1]/alpha)^(beta)-log(0.95))^(1/beta) - qPrev*Predicted[i-1]
  #			uso <- t[k]
  #			if(uso < 0){
  #				t[k] <- 0
  #			}
  #			teste <- teste + t[k]
  #		}
  #		teste <- teste/iteration
  #		Predicted[k] <- Predicted[k-1] + teste
  #}
  #}
  
  
  #plot(x = c(1,prediction), y = c(0,max(AccumBetween)), xlab = "Intervention Index", ylab = "Accumulated times (solid: observed; dashed: predicted)", col = "white")
  #Original plot(x = c(1,85), y = c(0,max(AccumBetween)), xlab = "Intervention Index", ylab = "Accumulated times (solid: observed; dashed: predicted)", col = "white")
  plot(x = c(1,85), y = c(0,max(Real)), xlab = "Intervention Index", ylab = "Accumulated times (solid: observed; dashed: predicted)", col = "white")
  #points(x = c(1:85), y = AccumBetween[1:85], type = "l", lty = 1, lwd = 1, col = "black")
  points(x = c(1:85), y = Real[1:85], type = "l", lty = 1, lwd = 2, col = "black")
  #points(x = c(1:85), y = newAccum[1:85], type = "l", lty = 1, lwd = 2, col = "black") 95% reliability whole prediction
  points(x = c(1:85), y = newAccum2[1:85], type = "l", lty = 2, lwd = 2, col = "black") # 94,42$ reliability whole prediction
  #points(x = c(73:85), y = R_Accum1[73:85], type = "l", lty = 1, lwd = 2, col = "gray") #upper% interval reliability
  #points(x = c(73:85), y = R_Accum2[73:85], type = "l", lty = 1, lwd = 2, col = "green") #lower% interval reliability
  #points(x = c(1:79), y = AccumBetween[1:79], type = "l", lty = 1, lwd = 3, col = "black")
  #points(x = c(1:72), y = Accum[73:85], type = "l", lty = 1, lwd = 3, col = "black")
  #points(x = c(85:prediction), y = AccumBetween[85:prediction], type = "l", lty = 2, lwd = 1, col = "grey")
  #points(x = c(79:85), y = AccumBetween[79:85], type = "l", lty = 2, lwd = 4, col = "red")
  points(x = c(73:85), y = Accum[73:85], type = "b", lty = 3, lwd = 2, col = "black")
  #	points(x = c(1:prediction), y = Predicted, type = "l", lty = 2, lwd = 1, col = "blue")
  savePlot(filename = "C:/cloud/Dropbox/CompetitiveRisks/CEERMA/Papers/Revis?o 7/Figures/Figure5_16_10",type = "jpeg")
  write(Accum, "C:\\Users\\Ricardo\\Dropbox\\GRP\\grp_paper2\\Competing_Risks\\Last_Predicted.txt")
  write(newAccum2, "C:\\Users\\Ricardo\\Dropbox\\GRP\\grp_paper2\\Competing_Risks\\Whole_Prediction.txt")
  #	write(Predicted, "C:\\Users\\Ricardo\\Dropbox\\GRP\\grp_paper2\\Competing_Risks\\Competing_Risks\\Predicted.txt")
}
BPMLE <- function(mu, a, x, bp){
  sum <- NULL
  sum <- 0
  aux_sum <- NULL
  aux_sum <- 0
  for(i in 1:length(x)){
    for(j in i:length(x)){
      aux_sum <- aux_sum + x[j]
    }
    cond <- 0
    if(i > 1){
      cond <- 1
    }
    sum <- sum + (mu*a*(aux_sum^a))*(exp(-mu*(aux_sum^a) - mu*((aux_sum - x[length(x)])^a)))*((1-bp)^(length(x) - i)*bp^(cond))
  }
  sum
}
getReliabilityBasedMSE <- function(beta=0.1, alpha=103.3, qPrev=0.45, qCorr=0.26, reliability=.995){
  t <- c(220, 13, 1, 6, 25, 5, 3, 6, 6, 2, 7, 1, 5, 25, 3, 5, 32, 3, 1, 12, 36, 1, 11, 10, 4, 1, 1, 32, 14, 1, 12, 7, 28, 10, 24, 8, 1, 1, 1, 19, 2, 1, 1, 13, 6, 3, 6, 2, 12, 1, 3, 7, 2, 12, 12, 117, 3, 4, 2, 2, 30, 97, 65, 47, 7, 18, 8, 80, 61, 11, 28, 12, 13, 24, 3, 10, 4, 85, 28, 5, 76, 49, 4, 32, 17);
  j <- c(1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1);
  i=1
  trueCumulative = NULL; trueCumulative[i] = t[i];
  virtualAge_oneStepAhead = NULL; virtualAge_oneStepAhead[i] = ifelse(j[i]==0, qCorr*trueCumulative[i],qPrev*trueCumulative[i])#alpha*((Real[1]/alpha)^(beta)-log(reliability))^(1/beta)
  virtualAge_iStepsAhead = NULL; virtualAge_iStepsAhead[i] = ifelse(j[i]==0, qCorr*trueCumulative[i],qPrev*trueCumulative[i])#alpha*((Real[1]/alpha)^(beta)-log(reliability))^(1/beta)
  estimatedCumulative_oneStepAhead = NULL; estimatedCumulative_oneStepAhead[i]=t[i]#the contorn condition is estimated[1] = true[1] # alpha*(-log(reliability))^(1/beta)#for t=0, the virtual age equals zero
  estimatedCumulative_iStepsAhead = NULL; estimatedCumulative_iStepsAhead[i]=t[i]#the contorn condition is estimated[1] = true[1] # alpha*(-log(reliability))^(1/beta)#for t=0, the virtual age equals zero
  for(i in 2:length(t)){
    trueCumulative[i] = trueCumulative[i-1] + t[i]
    forecast_i_oneStepAhead <- alpha*((virtualAge_oneStepAhead[i-1]/alpha)^(beta)-log(reliability))^(1/beta) - virtualAge_oneStepAhead[i-1]
    forecast_i_iStepsAhead <- alpha*((virtualAge_iStepsAhead[i-1]/alpha)^(beta)-log(reliability))^(1/beta) - virtualAge_iStepsAhead[i-1]
    estimatedCumulative_iStepsAhead[i] = estimatedCumulative_iStepsAhead[i-1] +  forecast_i_iStepsAhead
    estimatedCumulative_oneStepAhead[i] = trueCumulative[i-1] +  forecast_i_oneStepAhead
    virtualAge_oneStepAhead[i] = virtualAge_oneStepAhead[i-1] + ifelse(j[i]==0, qCorr*t[i],qPrev*t[i])
    virtualAge_iStepsAhead[i] = qPrev*estimatedCumulative_iStepsAhead[i]
  }
  MSE = (trueCumulative[1:72]-estimatedCumulative_oneStepAhead[1:72])^2
  MSE = mean(MSE)
  ret = list(MSE=MSE, trueCumulative = trueCumulative, estimatedCumulative_iStepsAhead=estimatedCumulative_iStepsAhead, estimatedCumulative_oneStepAhead=estimatedCumulative_oneStepAhead)
  return (ret)
}
#GETING THE OPTIMAL RELIABILITY INTRINSIC TO THE LANGSETH DATASET
getReliabilityBasedForecasts = function(){
  f=function(x){
    MSE=NULL
    for (i in 1:length(x))
      MSE[i] = getReliabilityBasedMSE(beta=0.1, alpha=103.3, qPrev=0.45, qCorr=0.26, reliability=x[i])$MSE
    return (MSE)
  }
  reliabilityCurve=curve(f, from=0.95, to=1, xlab="reliability", ylab="MSE(reliability)")
  write.table(reliabilityCurve, row.names=FALSE, col.names=TRUE, file="C:/cloud/Dropbox/CompetitiveRisks/CEERMA/Papers/Revis?o 7/Figures/reliabilityBased_MSE.txt", sep="\t")
  savePlot(filename = "C:/cloud/Dropbox/CompetitiveRisks/CEERMA/Papers/Revis?o 7/Figures/Figure 5_reliabilityBasedForecastsMSE",type = "jpeg")
  optimalReliability_index = which(reliabilityCurve$y==min(reliabilityCurve$y))
  optimalReliability = reliabilityCurve$x[optimalReliability_index]
  
  reliabilityBasedForecastProperties = getReliabilityBasedMSE(reliability=optimalReliability)
  series = data.frame(true = reliabilityBasedForecastProperties$trueCumulative
                      ,one_stepAheadForecast= reliabilityBasedForecastProperties$estimatedCumulative_oneStepAhead)
  #,i_stepsAheadForecast = reliabilityBasedForecastProperties$estimatedCumulative_iStepsAhead)
  write.table(series, row.names=FALSE, col.names=TRUE, file="C:/cloud/Dropbox/CompetitiveRisks/CEERMA/Papers/Revis?o 7/Figures/forecasts.txt", sep="\t")
  return(series)
}
plotReliabilityBasedForecasts=function(series){
  #legend = c("True", "Forecast: one step ahead", "Forecast: i steps ahead")
  legend = c("True", "Forecast: next intervention time")
  col =c("black", "gray", "gray")
  pch = c(20,21, 19)
  lty=c("solid", "longdash", "dotdash")
  lwd=c(2,1.5,1.5)
  type=c("o","o", "o")
  series = series[,1:2]
  from=1; to=nrow(series)
  min_y = min(series[from:to,]); max_y = max(series[from:to,])
  plot(x = c(1,nrow(series)), y = c(0,max(series)), xlab = "Intervention index (i)", ylab = "Accumulated times", col = "white")
  lines(x=c(72,72), y=c(min_y, max_y), type="l")
  for(k in ncol(series):1){
    lines(type = type[k], x=from:to, y=series[from:to,k], col=col[k], pch = pch[k], lty=lty[k], lwd=lwd[k])
  }
  legend(x=to-round(.6*(to-from)), y=max_y, legend=legend, col=col, pch=pch, lty=lty, lwd=lwd, bg ="white", text.font = 6,	cex=.8, pt.cex = 1)
  savePlot(filename = "C:/cloud/Dropbox/CompetitiveRisks/CEERMA/Papers/Revis?o 7/Figures/Figure 6_reliabilityBasedForecasts",type = "jpeg")
}
getMLE_objs=function(timesBetweenInterventions, interventionsTypes){
  x=timesBetweenInterventions; j=interventionsTypes
  parameters_RP = getParameters(b=1, formalism=FORMALISM$RP); optimum_RP = mle_wrgp(x, pParameters=parameters_RP);
  parameters_NHPP = getParameters(b=1, formalism=FORMALISM$NHPP); optimum_NHPP = mle_wrgp(x, pParameters=parameters_NHPP);
  
  best = list(b=optimum_RP$b, q=0); if(optimum_RP$optimum$value < optimum_NHPP$optimum$value){best$b=optimum_NHPP$b; best$q=1}
  parameters_KijimaI = getParameters(b=best$b, q=best$q, formalism=FORMALISM$KIJIMA_I); optimum_KijimaI = mle_wrgp(x, pParameters=parameters_KijimaI);
  parameters_KijimaII = getParameters(b=best$b, q=best$q, formalism=FORMALISM$KIJIMA_II); optimum_KijimaII = mle_wrgp(x, pParameters=parameters_KijimaII);
  
  best = list(b=optimum_KijimaI$b, q=optimum_KijimaI$q, propagations = rep(PROPAGATION$KijimaI, 2))
  if(optimum_KijimaI$optimum$value < optimum_KijimaII$optimum$value){
    best = list(b=optimum_KijimaII$b, q=optimum_KijimaII$q, propagations = rep(PROPAGATION$KijimaII, 2))
  }
  parameters_InterventionType = getParameters(b=best$b, q=best$q, interventionsTypes=j, propagations=best$propagations
                                              , formalism=FORMALISM$INTERVENTION_TYPE); optimum_InterventionType = mle_wrgp(x, pParameters=parameters_InterventionType);
  #parameters_GenericPropagation = getParameters(b=1, q=0, propagations=rep(0, n_x), formalism=FORMALISM$GENERIC_PROPAGATION); optimum_GenericPropagation = mle_wrgp(x, pParameters=parameters_GenericPropagation);
  mle_objs = list(optimum_RP=optimum_RP
                  , optimum_NHPP=optimum_NHPP
                  , optimum_KijimaI=optimum_KijimaI
                  , optimum_KijimaII=optimum_KijimaII
                  , optimum_InterventionType=optimum_InterventionType
                  #, optimum_GenericPropagation=optimum_GenericPropagation
  )
  return(mle_objs)
}
summarizeICsAndParametersTable=function(mle_objs, x, wd){
  df = data.frame()
  #n=length(x)
  m = length(mle_objs)
  for(i in 1:m){
    mle_i = mle_objs[[i]]
    NM_i = mle_i$parameters$formalism
    IC_i = ic_wgrp(mle_i, x)
    row_i = data.frame(Formalism=NM_i, AIC=IC_i$AIC, AICc=IC_i$AICc, BIC = IC_i$BIC, alpha=mle_i$a, beta=mle_i$b, q=mle_i$q, y_prev=NA, y_corr=NA)
    if(NM_i==FORMALISM$KIJIMA_I) {row_i$y_prev = 1; row_i$y_corr=1}
    else if(NM_i==FORMALISM$KIJIMA_II) {row_i$y_prev = 0; row_i$y_corr=0}
    else if(NM_i==FORMALISM$INTERVENTION_TYPE) {row_i$y_prev = mle_i$optimum$par[3]; row_i$y_corr=mle_i$optimum$par[4]}
    df=rbind(df, row_i)
  }
  #w = subset(df, BIC==min(df$BIC))$Formalism
  #str_optimum = paste("optimum_", w, sep="")
  #optimum = mle_objs[[str_optimum]]
  #write.table(df, row.names=FALSE, col.names=TRUE, file= paste(PATH, "parametersAndICs_table.txt", sep=""), sep="\t")
  write.table(df, row.names=FALSE, col.names=TRUE, file= paste(wd, "parametersAndICs_table.txt", sep=""), sep=",")
  #View(df)
  #---------- escolher optimum ----------#  
  #str_optimum = (paste("optimum_",subset(df,BIC==min(df$BIC))[1,1],sep=""))
  #optimum = mle_objs[names(mle_objs)==str_optimum]
  return(df)
}

getOptimum = function(mle_objs, df) {
  #---------- escolher optimum ----------#
  w = as.vector(subset(df, BIC==min(df$BIC))[1,1])
  str_optimum = paste("optimum_", w, sep="")
  # Substitui o " " do string por "", para fazer "Kijima I" = "KijimaI"
  str_optimum = gsub(pattern = " ", replacement = "", x=str_optimum)
  optimum = mle_objs[[str_optimum]]
  #optimum = mle_objs[["optimum_NHPP"]] #Teste
  return(optimum)
}