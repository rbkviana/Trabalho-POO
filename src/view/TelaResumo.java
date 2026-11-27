package view;

import controller.PlanejamentoController;
import model.Despesa;
import model.Participante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;

public class TelaResumo extends JFrame {

    private final PlanejamentoController controller;

    private JLabel labelTotalGeral;

    private JTable tabelaDespesas;
    private DefaultTableModel modeloDespesas;

    private JTable tabelaParticipantes;
    private DefaultTableModel modeloParticipantes;

    public TelaResumo(PlanejamentoController controller) {

        this.controller = controller;

        configurarJanela();
        montarTela();
        atualizarResumo();
    }

    private void configurarJanela() {

        setTitle("Resumo Financeiro");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void montarTela() {

        setLayout(new BorderLayout(10, 10));

        // ==============================
        // TOTAL GERAL
        // ==============================

        JPanel painelTotal = new JPanel(new GridLayout(2, 1, 5, 5));

        painelTotal.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 15, 10)
        );

        JLabel titulo = new JLabel(
                "TOTAL GERAL DAS DESPESAS",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        labelTotalGeral = new JLabel(
                "R$ 0,00",
                SwingConstants.CENTER
        );

        labelTotalGeral.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        painelTotal.add(titulo);
        painelTotal.add(labelTotalGeral);

        add(painelTotal, BorderLayout.NORTH);

        // ==============================
        // TABELA DE DESPESAS
        // ==============================

        modeloDespesas = new DefaultTableModel(
                new Object[]{
                        "Código",
                        "Descrição",
                        "Valor",
                        "Tipo",
                        "Participantes"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabelaDespesas = new JTable(modeloDespesas);

        JScrollPane painelDespesas = new JScrollPane(tabelaDespesas);

        painelDespesas.setBorder(
                BorderFactory.createTitledBorder(
                        "Despesas do planejamento"
                )
        );

        // ==============================
        // TABELA DE PARTICIPANTES
        // ==============================

        modeloParticipantes = new DefaultTableModel(
                new Object[]{
                        "Código",
                        "Participante",
                        "Orçamento",
                        "Total gasto",
                        "Valor excedido",
                        "Situação"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabelaParticipantes = new JTable(modeloParticipantes);

        JScrollPane painelParticipantes =
                new JScrollPane(tabelaParticipantes);

        painelParticipantes.setBorder(
                BorderFactory.createTitledBorder(
                        "Resumo individual dos participantes"
                )
        );

        // ==============================
        // ORGANIZAÇÃO CENTRAL
        // ==============================

        JPanel painelCentral = new JPanel(new GridLayout(2, 1, 10, 10));

        painelCentral.add(painelDespesas);
        painelCentral.add(painelParticipantes);

        add(painelCentral, BorderLayout.CENTER);

        // ==============================
        // BOTÕES
        // ==============================

        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        JButton botaoAtualizar = new JButton("Atualizar resumo");

        JButton botaoFechar = new JButton("Fechar");

        botaoAtualizar.addActionListener(e -> atualizarResumo());

        botaoFechar.addActionListener(e -> dispose());

        painelBotoes.add(botaoAtualizar);
        painelBotoes.add(botaoFechar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    // ==============================
    // ATUALIZAR RESUMO
    // ==============================

    public void atualizarResumo() {

        atualizarTabelaDespesas();

        atualizarTabelaParticipantes();

        BigDecimal totalGeral =
                controller.calcularTotalDespesas();

        labelTotalGeral.setText(
                formatarMoeda(totalGeral)
        );
    }

    private void atualizarTabelaDespesas() {

        modeloDespesas.setRowCount(0);

        for (Despesa despesa :
                controller.getPlanejamento().getDespesas()) {

            String tipo = despesa.getClass().getSimpleName();

            String participantes = despesa.getParticipantes()
                    .stream()
                    .map(Participante::getNome)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            modeloDespesas.addRow(new Object[]{
                    despesa.getCodigo(),
                    despesa.getDescricao(),
                    formatarMoeda(despesa.getValor()),
                    tipo,
                    participantes
            });
        }
    }

    private void atualizarTabelaParticipantes() {

        modeloParticipantes.setRowCount(0);

        Map<Integer, BigDecimal> totais =
                controller.calcularTotalIndividual();

        for (Participante participante :
                controller.getPlanejamento().getParticipantes()) {

            int codigo = participante.getCodigo();

            BigDecimal totalGasto = totais.getOrDefault(
                    codigo,
                    BigDecimal.ZERO
            );

            boolean dentroDoOrcamento =
                    controller.estaDentroDoOrcamento(codigo);

            BigDecimal excedido =
                    controller.calcularValorExcedido(codigo);

            String situacao = dentroDoOrcamento
                    ? "Dentro do orçamento"
                    : "Orçamento excedido";

            modeloParticipantes.addRow(new Object[]{
                    codigo,
                    participante.getNome(),
                    formatarMoeda(participante.getOrcamento()),
                    formatarMoeda(totalGasto),
                    formatarMoeda(excedido),
                    situacao
            });
        }
    }

    private String formatarMoeda(BigDecimal valor) {

        return String.format(
                java.util.Locale.forLanguageTag("pt-BR"),
                "R$ %.2f",
                valor
        );
    }
}
