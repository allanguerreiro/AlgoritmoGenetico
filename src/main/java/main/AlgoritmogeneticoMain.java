package main;

import bean.Populacao;
import logic.Algoritimo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgoritmogeneticoMain {

    public static void main(String[] args) {

        //Define a solução
        Algoritimo.setSolucao("Está foda para o Vasco.");
        //Define os caracteres existentes
        Algoritimo.setCaracteres("!,.:;?áÁãÃâÂõÕôÔóÓéêíÉÊQWERTYUIOPASDFGHJKLÇZXCVBNMqwertyuiopasdfghjklçzxcvbnm1234567890 ");
        //taxa de crossover de 60%
        Algoritimo.setTaxaDeCrossover(0.6);
        //taxa de mutação de 3%
        Algoritimo.setTaxaDeMutacao(0.3);
        //elitismo
        boolean eltismo = true;
        //tamanho da população
        int tamPop = 100;
        //numero máximo de gerações
        int numMaxGeracoes = 10000;

        //define o número de genes do indivíduo baseado na solução
        int numGenes = Algoritimo.getSolucao().length();

        //cria a primeira população aleatórioa
        Populacao populacao = new Populacao(numGenes, tamPop);

        boolean temSolucao = false;
        int geracao = 0;

        log.info("Iniciando... Aptidão da solução: " + Algoritimo.getSolucao().length());

        //loop até o critério de parada
        while (!temSolucao && geracao < numMaxGeracoes) {
            geracao++;

            //cria nova populacao
            populacao = Algoritimo.novaGeracao(populacao, eltismo);

            log.info("Geração " + geracao + " | Aptidão: " + populacao.getIndivduo(0).getAptidao() + " | Melhor: " + populacao.getIndivduo(0).getGenes());

            //verifica se tem a solucao
            temSolucao = populacao.temSolucao(Algoritimo.getSolucao());
        }

        if (geracao == numMaxGeracoes) {
            log.info("Número Maximo de Gerações | " + populacao.getIndivduo(0).getGenes() + " " + populacao.getIndivduo(0).getAptidao());
        }

        if (temSolucao) {
            log.info("Encontrado resultado na geração " + geracao + " | " + populacao.getIndivduo(0).getGenes() + " (Aptidão: " + populacao.getIndivduo(0).getAptidao() + ")");
        }
    }
}