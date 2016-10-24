package logic;

import bean.Individuo;
import bean.Populacao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Algoritimo {

    @Getter @Setter
    private static String solucao;
    @Getter @Setter
    private static double taxaDeCrossover;
    @Getter @Setter
    private static double taxaDeMutacao;
    @Getter @Setter
    private static String caracteres;

    private static Random random = new Random();

    public static Populacao novaGeracao(Populacao populacao, boolean elitismo) {
        //nova população do mesmo tamanho da antiga
        Populacao novaPopulacao = new Populacao(populacao.getTamPopulacao());

        //se tiver elitismo, mantém o melhor indivíduo da geração atual
        if (elitismo) {
            novaPopulacao.setIndividuo(populacao.getIndivduo(0));
        }

        //insere novos indivíduos na nova população, até atingir o tamanho máximo
        while (novaPopulacao.getNumIndividuos() < novaPopulacao.getTamPopulacao()) {
            //seleciona os 2 pais por torneio
            Individuo[] pais = selecaoTorneio(populacao);

            Individuo[] filhos = new Individuo[2];

            //verifica a taxa de crossover, se sim realiza o crossover, se não, mantém os pais selecionados para a próxima geração
            if (random.nextDouble() <= taxaDeCrossover) {
                filhos = crossover(pais[1], pais[0]);
            } else {
                filhos[0] = new Individuo(pais[0].getGenes());
                filhos[1] = new Individuo(pais[1].getGenes());
            }

            //adiciona os filhos na nova geração
            novaPopulacao.setIndividuo(filhos[0]);
            novaPopulacao.setIndividuo(filhos[1]);
        }

        //ordena a nova população
        novaPopulacao.ordenaPopulacao();
        return novaPopulacao;
    }

    public static Individuo[] crossover(Individuo individuo1, Individuo individuo2) {

        //sorteia o ponto de corte
        int pontoCorte1 = random.nextInt((individuo1.getGenes().length()/2) -2) + 1;
        int pontoCorte2 = random.nextInt((individuo1.getGenes().length()/2) -2) + individuo1.getGenes().length()/2;

        Individuo[] filhos = new Individuo[2];

        //pega os genes dos pais
        String genePai1 = individuo1.getGenes();
        String genePai2 = individuo2.getGenes();

        String geneFilho1;
        String geneFilho2;

        //realiza o corte, 
        geneFilho1 = genePai1.substring(0, pontoCorte1);
        geneFilho1 += genePai2.substring(pontoCorte1, pontoCorte2);
        geneFilho1 += genePai1.substring(pontoCorte2, genePai1.length());
        
        geneFilho2 = genePai2.substring(0, pontoCorte1);
        geneFilho2 += genePai1.substring(pontoCorte1, pontoCorte2);
        geneFilho2 += genePai2.substring(pontoCorte2, genePai2.length());

        //cria o novo indivíduo com os genes dos pais
        filhos[0] = new Individuo(geneFilho1);
        filhos[1] = new Individuo(geneFilho2);

        return filhos;
    }

    public static Individuo[] selecaoTorneio(Populacao populacao) {

        Populacao populacaoIntermediaria = new Populacao(3);

        //seleciona 3 indivíduos aleatóriamente na população
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(random.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(random.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(random.nextInt(populacao.getTamPopulacao())));

        log.info("População Intermediária --> Genes: " + populacaoIntermediaria.getIndivduo(0).getGenes());
        log.info("População Intermediária --> Aptidão: " + populacaoIntermediaria.getIndivduo(0).getAptidao());

        //ordena a população
        populacaoIntermediaria.ordenaPopulacao();

        Individuo[] pais = new Individuo[2];

        //seleciona os 2 melhores deste população
        pais[0] = populacaoIntermediaria.getIndivduo(0);
        pais[1] = populacaoIntermediaria.getIndivduo(1);

        return pais;
    }
}