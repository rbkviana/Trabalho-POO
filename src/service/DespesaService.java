package service;

import model.Despesa;
import model.DespesaFixa;
import model.DespesaPorPessoa;
import model.Participante;
import model.Planejamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DespesaService {

    private final Planejamento planejamento;

    public DespesaService(Planejamento planejamento) {
        this.planejamento = planejamento;
    }

    public void cadastrarDespesaFixa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Integer> codigosParticipantes
    ) {

        cadastrarDespesa(
                codigo,
                descricao,
                valor,
                codigosParticipantes,
                true
        );
    }

    public void cadastrarDespesaPorPessoa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Integer> codigosParticipantes
    ) {

        cadastrarDespesa(
                codigo,
                descricao,
                valor,
                codigosParticipantes,
                false
        );
    }

    private void cadastrarDespesa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Integer> codigosParticipantes,
            boolean fixa
    ) {

        validarCodigo(codigo);
        validarDescricao(descricao);
        validarValor(valor);

        boolean codigoExiste = planejamento.getDespesas()
                .stream()
                .anyMatch(d -> d.getCodigo() == codigo);

        if (codigoExiste) {
            throw new IllegalArgumentException(
                    "Já existe uma despesa com esse código."
            );
        }

        List<Participante> participantes =
                buscarParticipantes(codigosParticipantes);

        Despesa despesa = criarDespesa(
                codigo,
                descricao,
                valor,
                participantes,
                fixa
        );

        planejamento.adicionarDespesa(despesa);
    }

    public void editarDespesa(
            int codigo,
            String novaDescricao,
            BigDecimal novoValor,
            List<Integer> novosCodigosParticipantes
    ) {

        validarDescricao(novaDescricao);
        validarValor(novoValor);

        Despesa despesaAtual = buscarPorCodigo(codigo);

        List<Participante> participantes =
                buscarParticipantes(novosCodigosParticipantes);

        boolean fixa = despesaAtual instanceof DespesaFixa;

        Despesa novaDespesa = criarDespesa(
                codigo,
                novaDescricao,
                novoValor,
                participantes,
                fixa
        );

        planejamento.substituirDespesa(codigo, novaDespesa);
    }

    public void excluirDespesa(int codigo) {

        Despesa despesa = buscarPorCodigo(codigo);

        planejamento.removerDespesa(despesa);
    }

    public Despesa buscarPorCodigo(int codigo) {

        return planejamento.getDespesas()
                .stream()
                .filter(d -> d.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Despesa não encontrada."
                ));
    }

    private Despesa criarDespesa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Participante> participantes,
            boolean fixa
    ) {

        if (fixa) {
            return new DespesaFixa(
                    codigo,
                    descricao.trim(),
                    valor.setScale(2),
                    participantes
            );
        }

        return new DespesaPorPessoa(
                codigo,
                descricao.trim(),
                valor.setScale(2),
                participantes
        );
    }

    private List<Participante> buscarParticipantes(
            List<Integer> codigos
    ) {

        if (codigos == null || codigos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione pelo menos um participante."
            );
        }

        Set<Integer> codigosUnicos = new HashSet<>(codigos);

        if (codigosUnicos.size() != codigos.size()) {
            throw new IllegalArgumentException(
                    "Não é permitido repetir participantes."
            );
        }

        List<Participante> participantes = new ArrayList<>();

        for (Integer codigo : codigos) {

            if (codigo == null) {
                throw new IllegalArgumentException(
                        "Código de participante inválido."
                );
            }

            Participante participante = planejamento
                    .getParticipantes()
                    .stream()
                    .filter(p -> p.getCodigo() == codigo)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Participante não encontrado: " + codigo
                    ));

            participantes.add(participante);
        }

        return participantes;
    }

    private void validarCodigo(int codigo) {

        if (codigo <= 0) {
            throw new IllegalArgumentException(
                    "O código deve ser maior que zero."
            );
        }
    }

    private void validarDescricao(String descricao) {

        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A descrição deve ser informada."
            );
        }
    }

    private void validarValor(BigDecimal valor) {

        if (valor == null) {
            throw new IllegalArgumentException(
                    "O valor deve ser informado."
            );
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor da despesa deve ser maior que zero."
            );
        }

        try {
            valor.setScale(2);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException(
                    "O valor deve ter no máximo duas casas decimais."
            );
        }
    }
}
