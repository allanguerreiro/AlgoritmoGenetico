package util;

import lombok.Getter;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by allan on 28/10/16.
 */
public abstract class AbstractMatrizDinamica<T> extends Chromosome {
    @Getter
    MatrizDinamica<String> matrizDinamica = new MatrizDinamica<>();

    @Getter
    Map<Integer, T> elements = new LinkedHashMap<>();

    public AbstractMatrizDinamica(MatrizDinamica<String> representation) throws InvalidRepresentationException {
        this.matrizDinamica = representation;
    }

    public AbstractMatrizDinamica(Map<Integer, T> representation) throws InvalidRepresentationException {
        this.elements = representation;
    }
}
