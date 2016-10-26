package bean;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Populacao {

    private List<Individuo> individuoList;

    @Getter
    @Setter
    private int tamPopulacao;

    //cria uma população com indivíduos aleatórios
    public Populacao(int numGenes, int tamPop) {
        tamPopulacao = tamPop;
        individuoList = new ArrayList<>(tamPop);

        for (int i = 0; i < tamPop; i++) {
            individuoList.add(new Individuo(numGenes));
        }
    }

    //cria uma população com indivíduos sem valor, será composto posteriormente
    public Populacao(int tamPop) {
        tamPopulacao = tamPop;
        individuoList = new ArrayList<>(tamPop);

        for (int i = 0; i < tamPop; i++) {
            Individuo individuo = null;
            individuoList.add(individuo);
        }
    }

    //coloca um indivíduo em uma certa posição da população
    public void setIndividuo(Individuo individuo, int posicao) {
        individuoList.add(posicao, individuo);
    }

    //coloca um indivíduo na próxima posição disponível da população
    public void setIndividuo(Individuo individuo) {
        for (Individuo indiv : individuoList) {
            if (indiv == null) {
                individuoList.set(individuoList.indexOf(indiv), individuo);
                return;
            }
        }
    }

    //verifica se algum indivíduo da população possui a solução
    public boolean temSolucao(String solucao) {
        Individuo individuo = null;
        for (Individuo indiv : individuoList) {
            if (indiv.getGenes().equals(solucao)) {
                individuo = indiv;
                break;
            }
        }

        if (individuo == null) {
            return false;
        }
        return true;
    }

    //ordena a população pelo valor de aptidão de cada indivíduo,
    // do maior valor para o menor, assim se eu quiser obter o melhor indivíduo desta população,
    // acesso a posição 0 do array de indivíduos
    public void ordenaPopulacao() {
        individuoList = Ordering.natural().reverse()
                .onResultOf(new Function<Individuo, Integer>() {
                    @Override
                    public Integer apply(Individuo request) {
                        return request.getAptidao();
                    }
                })
                .immutableSortedCopy(individuoList);
    }

    //número de indivíduos existentes na população
    public int getNumIndividuos() {
        int num = 0;

        for (Individuo individuo : individuoList) {
            if (individuo != null) {
                num++;
            }
        }
        return num;
    }

    public Individuo getIndivduo(int pos) {
        return individuoList.get(pos);
    }
}