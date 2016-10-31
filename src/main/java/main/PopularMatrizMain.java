package main;

import lombok.extern.slf4j.Slf4j;
import util.MatrizDinamica;

import java.util.Random;

/**
 * Created by allan on 28/10/16.
 */
@Slf4j
public class PopularMatrizMain {
    public static void main(String[] args) {
        int linhas = 10;
        int colunas = 5;

        // criando uma matriz dinâmica de String, poderia ser de qualquer outra coisa
        MatrizDinamica<String> matriz = MatrizDinamica.randomPopulation(linhas, colunas);

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                String elemento = matriz.get(i, j); // obtendo o elementos
                //log.info(elemento.substring(31, elemento.length()));
                log.info(elemento);
            }
        }
    }
}
