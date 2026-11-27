
package view;

import controller.PlanejamentoController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TelaPrincipal extends JFrame {

    private final PlanejamentoController controller;

    private TelaParticipantes telaParticipantes;
    private TelaDespesas telaDespesas;
    private TelaResumo telaResumo;

    public TelaPrincipal(PlanejamentoController controller) {

        this.controller = controller;

        configurarJanela();
        montarTela();
    }

    private void configurarJanela() {

        setTitle("Cabe no Bolso - Planejador de Despesas");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarTela() {

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "CABE NO BOLSO",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitulo = new JLabel(
                "Planejador de Despesas Compartilhadas",
                SwingConstants.CENTER
        );

        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel painelTitulo = new JPanel(new GridLayout(2, 1, 5, 5));

        painelTitulo.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 20, 10)
        );

        painelTitulo.add(titulo);
        painelTitulo.add(subtitulo);

        add(painelTitulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(
                new GridLayout(5, 1, 12, 12)
        );

        painelBotoes.setBorder(
                BorderFactory.createEmptyBorder(10, 100, 20, 100)
        );

        JButton botaoParticipantes =
                new JButton("Gerenciar participantes");

        JButton botaoDespesas =
                new JButton("Gerenciar despesas");

        JButton botaoResumo =
                new JButton("Resumo financeiro");

        JButton botaoSalvar =
                new JButton("Salvar planejamento");

        JButton botaoCarregar =
                new JButton("Carregar planejamento");

        botaoParticipantes.addActionListener(e ->
                abrirTelaParticipantes()
        );

        botaoDespesas.addActionListener(e ->
                abrirTelaDespesas()
        );

        botaoResumo.addActionListener(e ->
                abrirTelaResumo()
        );

        botaoSalvar.addActionListener(e ->
                salvarPlanejamento()
        );

        botaoCarregar.addActionListener(e ->
                carregarPlanejamento()
        );

        painelBotoes.add(botaoParticipantes);
        painelBotoes.add(botaoDespesas);
        painelBotoes.add(botaoResumo);
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCarregar);

        add(painelBotoes, BorderLayout.CENTER);

        JLabel rodape = new JLabel(
                "Organize seus gastos. Planeje melhor.",
                SwingConstants.CENTER
        );

        rodape.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 15, 10)
        );

        add(rodape, BorderLayout.SOUTH);
    }

    private void abrirTelaParticipantes() {

        if (telaParticipantes == null ||
                !telaParticipantes.isDisplayable()) {

            telaParticipantes = new TelaParticipantes(controller);
        } else {

            telaParticipantes.atualizarDados();
        }

        telaParticipantes.setVisible(true);
        telaParticipantes.toFront();
    }

    private void abrirTelaDespesas() {

        if (telaDespesas == null ||
                !telaDespesas.isDisplayable()) {

            telaDespesas = new TelaDespesas(controller);
        } else {

            telaDespesas.atualizarDados();
        }

        telaDespesas.setVisible(true);
        telaDespesas.toFront();
    }

    private void abrirTelaResumo() {

        if (telaResumo == null ||
                !telaResumo.isDisplayable()) {

            telaResumo = new TelaResumo(controller);
        }

        telaResumo.setVisible(true);
        telaResumo.toFront();
    }

    private void salvarPlanejamento() {

        try {

            controller.salvarPlanejamento();

            JOptionPane.showMessageDialog(
                    this,
                    "Planejamento salvo com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível salvar o planejamento:\n"
                            + erro.getMessage(),
                    "Erro ao salvar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void carregarPlanejamento() {

        try {

            controller.carregarPlanejamento();

            fecharTelasAbertas();

            JOptionPane.showMessageDialog(
                    this,
                    "Planejamento carregado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException | ClassNotFoundException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar o planejamento:\n"
                            + erro.getMessage(),
                    "Erro ao carregar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fecharTelasAbertas() {

        if (telaParticipantes != null) {
            telaParticipantes.dispose();
            telaParticipantes = null;
        }

        if (telaDespesas != null) {
            telaDespesas.dispose();
            telaDespesas = null;
        }

        if (telaResumo != null) {
            telaResumo.dispose();
            telaResumo = null;
        }
    }
}
