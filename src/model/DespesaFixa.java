package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DespesaFixa extends Despesa implements Serializable {

    private static final long serialVersionUID = 1L;

    public DespesaFixa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Participante> participantes
    ) {
        super(codigo, descricao, valor, participantes);
    }

    @Override
    public Map<Integer, BigDecimal> calcularRateio() {

        List<Participante> participantesOrdenados =
                new ArrayList<>(getParticipantes());

        participantesOrdenados.sort(
                Comparator.comparingInt(Participante::getCodigo)
        );

        Map<Integer, BigDecimal> resultado = new LinkedHashMap<>();

        int quantidade = participantesOrdenados.size();

        BigDecimal valorBase = getValor().divide(
                BigDecimal.valueOf(quantidade),
                2,
                java.math.RoundingMode.DOWN
        );

        BigDecimal valorDistribuido = valorBase.multiply(
                BigDecimal.valueOf(quantidade)
        );

        BigDecimal restante = getValor().subtract(valorDistribuido);

        int centavosRestantes = restante
                .movePointRight(2)
                .intValueExact();

        for (int i = 0; i < quantidade; i++) {

            Participante participante = participantesOrdenados.get(i);

            BigDecimal valorIndividual = valorBase;

            if (i < centavosRestantes) {
                valorIndividual = valorIndividual.add(
                        new BigDecimal("0.01")
                );
            }

            resultado.put(
                    participante.getCodigo(),
                    valorIndividual
            );
        }

        return resultado;
    }
}
