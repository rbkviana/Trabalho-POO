package service;

import model.Despesa;
import model.Participante;
import model.Planejamento;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResumoService {

    private final Planejamento planejamento;

    public ResumoService(Planejamento planejamento) {
        this.planejamento = planejamento;
    }

    public BigDecimal calcularTotalDespesas() {

        BigDecimal total = BigDecimal.ZERO;

        for (Despesa despesa : planejamento.getDespesas()) {

            for (BigDecimal valor : despesa.calcularRateio().values()) {
                total = total.add(valor);
            }
        }

        return total;
    }

    public Map<Integer, BigDecimal> calcularTotalIndividual() {

        Map<Integer, BigDecimal> totais = new LinkedHashMap<>();

        for (Participante participante : planejamento.getParticipantes()) {

            totais.put(
                    participante.getCodigo(),
                    BigDecimal.ZERO
            );
        }

        for (Despesa despesa : planejamento.getDespesas()) {

            Map<Integer, BigDecimal> rateio = despesa.calcularRateio();

            for (Map.Entry<Integer, BigDecimal> entrada : rateio.entrySet()) {

                int codigo = entrada.getKey();
                BigDecimal valor = entrada.getValue();

                totais.computeIfPresent(
                        codigo,
                        (k, totalAtual) -> totalAtual.add(valor)
                );
            }
        }

        return totais;
    }

    public BigDecimal calcularTotalParticipante(int codigo) {

        return calcularTotalIndividual()
                .getOrDefault(codigo, BigDecimal.ZERO);
    }

    public boolean estaDentroDoOrcamento(int codigo) {

        Participante participante = buscarParticipante(codigo);

        BigDecimal totalGasto = calcularTotalParticipante(codigo);

        return totalGasto.compareTo(participante.getOrcamento()) <= 0;
    }

    public BigDecimal calcularValorExcedido(int codigo) {

        Participante participante = buscarParticipante(codigo);

        BigDecimal totalGasto = calcularTotalParticipante(codigo);

        if (totalGasto.compareTo(participante.getOrcamento()) > 0) {

            return totalGasto.subtract(participante.getOrcamento());
        }

        return BigDecimal.ZERO;
    }

    private Participante buscarParticipante(int codigo) {

        return planejamento.getParticipantes()
                .stream()
                .filter(p -> p.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Participante não encontrado."
                ));
    }
}
