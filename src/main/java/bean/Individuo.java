package bean;

import logic.Algoritimo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@EqualsAndHashCode
public class Individuo {

    @Getter @Setter
    private String genes = "";

    @Getter @Setter
    private int aptidao = 0;

    private Random random = new Random();

    //gera um indivíduo aleatório
    public Individuo(int numGenes) {
        genes = "";
        String caracteres = Algoritimo.getCaracteres();

        for (int i = 0; i < numGenes; i++) {
            genes += caracteres.charAt(random.nextInt(caracteres.length()));
            log.info("Novo Individuo " + genes);
        }

        geraAptidao();
    }

    //cria um indivíduo com os genes definidos
    public Individuo(String genes) {
        this.genes = genes;

        //se for mutar, cria um gene aleatório
        if (random.nextDouble() <= Algoritimo.getTaxaDeMutacao()) {
            String caracteres = Algoritimo.getCaracteres();
            String geneNovo = "";
            int posAleatoria = random.nextInt(genes.length());
            for (int i = 0; i < genes.length(); i++) {
                if (i == posAleatoria) {
                    geneNovo += caracteres.charAt(random.nextInt(caracteres.length()));
                    log.info("Posicao Aleatoria " + geneNovo);
                } else {
                    geneNovo += genes.charAt(i);
                    log.info("Posicao não Aleatoria " + geneNovo);
                }

            }
            this.genes = geneNovo;
            log.info("Novo Individuo " + genes);
        }
        geraAptidao();
    }

    //gera o valor de aptidão, será calculada pelo número de bits do gene iguais ao da solução
    private void geraAptidao() {
        String solucao = Algoritimo.getSolucao();
        for (int i = 0; i < solucao.length(); i++) {
            if (solucao.charAt(i) == genes.charAt(i)) {
                log.info("Letra da solucao: " + solucao.charAt(i) + " - " + "Letra do gene: " + genes.charAt(i));
                aptidao++;
            }
        }
        log.info("Aptidão: " + aptidao);
    }
}