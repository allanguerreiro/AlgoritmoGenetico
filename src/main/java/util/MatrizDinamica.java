package util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by allan on 28/10/16.
 */
@Slf4j
public class MatrizDinamica<T> {
    @Getter
    private final Map<Integer, Map<Integer, T>> elementos = new HashMap<>();

    public void set(int linha, int coluna, T elemento) {
        Map<Integer, T> colunas = getColunas(linha);
        Integer chave = Integer.valueOf(coluna);
        if (elemento != null) {
            colunas.put(chave, elemento);
        } else {
            colunas.remove(chave);
        }
    }

    public T get(int linha, int coluna) {
        Map<Integer, T> colunas = getColunas(linha);
        Integer chave = Integer.valueOf(coluna);
        T elemento = colunas.get(chave);
        return elemento;
    }

    private Map<Integer, T> getColunas(int linha) {
        int chave = Integer.valueOf(linha);
        Map<Integer, T> colunas = elementos.get(chave);
        if (colunas == null) {
            colunas = new HashMap<Integer, T>();
            elementos.put(chave, colunas);
        }
        return colunas;
    }

    public static MatrizDinamica<String> randomPopulation(int linhas, int colunas) {
        MatrizDinamica<String> matriz = new MatrizDinamica<>();
        Random gerador = new Random();
        String elemento = "";
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (i == j) {
                    elemento = "linha: " + i + ", coluna: " + j + " Distância: " + "0";
                    matriz.set(i, j, String.valueOf(elemento)); // inserindo o elemento
                } else {
                    Integer randomElement = gerador.nextInt(26);
                    elemento = "linha: " + i + ", coluna: " + j + " Distância: " + randomElement;
                    matriz.set(i, j, String.valueOf(elemento)); // inserindo o elemento
                }
                log.info(elemento);
            }
        }
        return matriz;
    }
}