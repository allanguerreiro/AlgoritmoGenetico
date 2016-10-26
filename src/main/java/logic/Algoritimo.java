package logic;

import bean.Individuo;
import bean.Populacao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Algoritimo {

    @Getter
    @Setter
    private static String solucao;
    @Getter
    @Setter
    private static double taxaDeCrossover;
    @Getter
    @Setter
    private static double taxaDeMutacao;
    @Getter
    @Setter
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
            List<Individuo> pais = selecaoTorneio(populacao);
            List<Individuo> filhos = new ArrayList<>(2);

            //verifica a taxa de crossover, se sim realiza o crossover,
            // se não, mantém os pais selecionados para a próxima geração
            if (random.nextDouble() <= taxaDeCrossover) {
                filhos = crossover(pais);
            } else {
                filhos.add(new Individuo(pais.get(0).getGenes()));
                filhos.add(new Individuo(pais.get(1).getGenes()));
            }

            //adiciona os filhos na nova geração
            novaPopulacao.setIndividuo(filhos.get(0));
            novaPopulacao.setIndividuo(filhos.get(1));
        }

        //ordena a nova população
        novaPopulacao.ordenaPopulacao();
        return novaPopulacao;
    }

    public static List<Individuo> crossover(List<Individuo> individuoList) {

        //sorteia o ponto de corte
        int pontoCorte1 = random.nextInt((individuoList.get(0).getGenes().length() / 2) - 2) + 1;
        int pontoCorte2 = random.nextInt((individuoList.get(0).getGenes().length() / 2) - 2) + individuoList.get(0).getGenes().length() / 2;

        List<Individuo> filhos = new ArrayList<>(2);

        //pega os genes dos pais
        String genePai1 = individuoList.get(0).getGenes();
        String genePai2 = individuoList.get(1).getGenes();

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
        filhos.add(new Individuo(geneFilho1));
        filhos.add(new Individuo(geneFilho2));

        return filhos;
    }

    public static List<Individuo> selecaoTorneio(Populacao populacao) {
        Populacao populacaoIntermediaria = new Populacao(3);

        //seleciona 3 indivíduos aleatóriamente na população
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(random.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(random.nextInt(populacao.getTamPopulacao())));
        populacaoIntermediaria.setIndividuo(populacao.getIndivduo(random.nextInt(populacao.getTamPopulacao())));

        log.info("População Intermediária --> Genes: " + populacaoIntermediaria.getIndivduo(0).getGenes());
        log.info("População Intermediária --> Aptidão: " + populacaoIntermediaria.getIndivduo(0).getAptidao());

        //ordena a população
        populacaoIntermediaria.ordenaPopulacao();
        List<Individuo> pais = new ArrayList<>(2);

        //seleciona os 2 melhores deste população
        pais.add(populacaoIntermediaria.getIndivduo(0));
        pais.add(populacaoIntermediaria.getIndivduo(1));

        return pais;
    }
}