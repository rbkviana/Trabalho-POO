package service;

import java.math.BigDecimal;
import model.Participante;
import model.Planejamento;

public class ParticipanteService {

    private final Planejamento planejamento;

    public ParticipanteService(Planejamento planejamento) {
        this.planejamento = planejamento;
    }

    public void cadastrarParticipante(
            int codigo,
            String nome,
            BigDecimal orcamento
    ) {

        validarCodigo(codigo);
        validarNome(nome);
        validarOrcamento(orcamento);

        boolean codigoExiste = planejamento.getParticipantes()
                .stream()
                .anyMatch(p -> p.getCodigo() == codigo);

        if (codigoExiste) {
            throw new IllegalArgumentException(
                    "Já existe um participante com esse código."
            );
        }

        Participante participante = new Participante(
                codigo,
                nome.trim(),
                orcamento.setScale(2)
        );

        planejamento.adicionarParticipante(participante);
    }

    public void editarParticipante(
            int codigo,
            String novoNome,
            BigDecimal novoOrcamento
    ) {

        validarNome(novoNome);
        validarOrcamento(novoOrcamento);

        Participante participante = buscarPorCodigo(codigo);

        participante.setNome(novoNome.trim());
        participante.setOrcamento(novoOrcamento.setScale(2));
    }

    public void excluirParticipante(int codigo) {

        Participante participante = buscarPorCodigo(codigo);

        boolean possuiVinculo = planejamento.getDespesas()
                .stream()
                .anyMatch(d -> d.getParticipantes()
                        .stream()
                        .anyMatch(p -> p.getCodigo() == codigo));

        if (possuiVinculo) {
            throw new IllegalArgumentException(
                    "Não é possível excluir um participante " +
                            "que possui despesas vinculadas."
            );
        }

        planejamento.removerParticipante(participante);
    }

    public Participante buscarPorCodigo(int codigo) {

        return planejamento.getParticipantes()
                .stream()
                .filter(p -> p.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Participante não encontrado."
                ));
    }

    private void validarCodigo(int codigo) {

        if (codigo <= 0) {
            throw new IllegalArgumentException(
                    "O código deve ser maior que zero."
            );
        }
    }

    private void validarNome(String nome) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O nome deve ser informado."
            );
        }
    }

    private void validarOrcamento(BigDecimal orcamento) {

        if (orcamento == null) {
            throw new IllegalArgumentException(
                    "O orçamento deve ser informado."
            );
        }

        if (orcamento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "O orçamento não pode ser negativo."
            );
        }

        try {
            orcamento.setScale(2);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(
                    "O orçamento deve ter no máximo duas casas decimais."
            );
        }
    }
}
