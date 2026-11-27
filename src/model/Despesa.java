package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class Despesa implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int codigo;
    private final String descricao;
    private final BigDecimal valor;
    private final List<Participante> participantes;

    public Despesa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Participante> participantes
    ) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.valor = valor;
        this.participantes = List.copyOf(participantes);
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public abstract Map<Integer, BigDecimal> calcularRateio();
}
