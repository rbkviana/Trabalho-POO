package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class DespesaPorPessoa extends Despesa implements Serializable {

    private static final long serialVersionUID = 1L;

    public DespesaPorPessoa(
            int codigo,
            String descricao,
            BigDecimal valor,
            java.util.List<Participante> participantes
    ) {
        super(codigo, descricao, valor, participantes);
    }

    @Override
    public Map<Integer, BigDecimal> calcularRateio() {

        Map<Integer, BigDecimal> resultado = new LinkedHashMap<>();

        for (Participante participante : getParticipantes()) {

            resultado.put(
                    participante.getCodigo(),
                    getValor()
            );
        }

        return resultado;
    }
}
