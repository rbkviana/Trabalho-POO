package controller;

import dao.ArquivoPersistencia;
import interfaces.Persistencia;
import model.Planejamento;
import service.DespesaService;
import service.ParticipanteService;
import service.ResumoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PlanejamentoController {

    private Planejamento planejamento;

    private ParticipanteService participanteService;
    private DespesaService despesaService;
    private ResumoService resumoService;

    private final Persistencia persistencia;
    private final String caminhoArquivo;

    public PlanejamentoController() {

        this.persistencia = new ArquivoPersistencia();
        this.caminhoArquivo = "dados/planejamento.dat";

        this.planejamento = new Planejamento();

        inicializarServicos();
    }

    private void inicializarServicos() {

        participanteService =
                new ParticipanteService(planejamento);

        despesaService =
                new DespesaService(planejamento);

        resumoService =
                new ResumoService(planejamento);
    }

    public Planejamento getPlanejamento() {
        return planejamento;
    }

    // ==============================
    // PARTICIPANTES
    // ==============================

    public void cadastrarParticipante(
            int codigo,
            String nome,
            BigDecimal orcamento) {

        participanteService.cadastrarParticipante(
                codigo, nome, orcamento
        );
    }

    public void editarParticipante(
            int codigo,
            String nome,
            BigDecimal orcamento) {

        participanteService.editarParticipante(
                codigo, nome, orcamento
        );
    }

    public void excluirParticipante(int codigo) {

        participanteService.excluirParticipante(codigo);
    }

    // ==============================
    // DESPESAS
    // ==============================

    public void cadastrarDespesaFixa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Integer> participantes) {

        despesaService.cadastrarDespesaFixa(
                codigo, descricao, valor, participantes
        );
    }

    public void cadastrarDespesaPorPessoa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Integer> participantes) {

        despesaService.cadastrarDespesaPorPessoa(
                codigo, descricao, valor, participantes
        );
    }

    public void editarDespesa(
            int codigo,
            String descricao,
            BigDecimal valor,
            List<Integer> participantes) {

        despesaService.editarDespesa(
                codigo, descricao, valor, participantes
        );
    }

    public void excluirDespesa(int codigo) {

        despesaService.excluirDespesa(codigo);
    }

    // ==============================
    // RESUMO FINANCEIRO
    // ==============================

    public BigDecimal calcularTotalDespesas() {

        return resumoService.calcularTotalDespesas();
    }

    public BigDecimal calcularTotalParticipante(int codigo) {

        return resumoService.calcularTotalParticipante(codigo);
    }

    public boolean estaDentroDoOrcamento(int codigo) {

        return resumoService.estaDentroDoOrcamento(codigo);
    }

    public BigDecimal calcularValorExcedido(int codigo) {

        return resumoService.calcularValorExcedido(codigo);
    }

    public Map<Integer, BigDecimal> calcularTotalIndividual() {

        return resumoService.calcularTotalIndividual();
    }

    // ==============================
    // PERSISTÊNCIA
    // ==============================

    public void salvarPlanejamento() throws IOException {

        persistencia.salvar(
                planejamento,
                caminhoArquivo
        );
    }

    public void carregarPlanejamento()
            throws IOException, ClassNotFoundException {

        this.planejamento = persistencia.carregar(caminhoArquivo);
        inicializarServicos();
    }
}
