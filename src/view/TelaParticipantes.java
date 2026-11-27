package view;

import controller.PlanejamentoController;
import model.Participante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class TelaParticipantes extends JFrame {

    private final PlanejamentoController controller;

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoOrcamento;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaParticipantes(PlanejamentoController controller) {

        this.controller = controller;

        configurarJanela();
        montarTela();
        atualizarTabela();
    }

    private void configurarJanela() {

        setTitle("Gerenciamento de Participantes");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void montarTela() {

        setLayout(new BorderLayout(10, 10));

        JPanel painelFormulario = new JPanel(
                new GridLayout(3, 2, 8, 8)
        );

        painelFormulario.setBorder(
                BorderFactory.createTitledBorder("Dados do participante")
        );

        campoCodigo = new JTextField();
        campoNome = new JTextField();
        campoOrcamento = new JTextField();

        painelFormulario.add(new JLabel("Código:"));
        painelFormulario.add(campoCodigo);

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Orçamento (R$):"));
        painelFormulario.add(campoOrcamento);

        add(painelFormulario, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(
                new Object[]{"Código", "Nome", "Orçamento"},
                0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);

        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabela.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                preencherFormulario();
            }
        });

        JScrollPane painelTabela = new JScrollPane(tabela);

        painelTabela.setBorder(
                BorderFactory.createTitledBorder("Participantes cadastrados")
        );

        add(painelTabela, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        JButton botaoCadastrar = new JButton("Cadastrar");
        JButton botaoEditar = new JButton("Editar");
        JButton botaoExcluir = new JButton("Excluir");
        JButton botaoLimpar = new JButton("Limpar");

        botaoCadastrar.addActionListener(e -> cadastrar());

        botaoEditar.addActionListener(e -> editar());

        botaoExcluir.addActionListener(e -> excluir());

        botaoLimpar.addActionListener(e -> limparCampos());

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoLimpar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void cadastrar() {

        try {

            int codigo = Integer.parseInt(campoCodigo.getText().trim());

            String nome = campoNome.getText();

            BigDecimal orcamento = new BigDecimal(
                    campoOrcamento.getText().trim().replace(",", ".")
            );

            controller.cadastrarParticipante(
                    codigo,
                    nome,
                    orcamento
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Participante cadastrado com sucesso!"
            );

            atualizarTabela();
            limparCampos();

        } catch (NumberFormatException erro) {

            mostrarErro("Informe um código e um orçamento válidos.");

        } catch (IllegalArgumentException erro) {

            mostrarErro(erro.getMessage());
        }
    }

    private void editar() {

        try {

            int codigo = Integer.parseInt(campoCodigo.getText().trim());

            String nome = campoNome.getText();

            BigDecimal orcamento = new BigDecimal(
                    campoOrcamento.getText().trim().replace(",", ".")
            );

            controller.editarParticipante(
                    codigo,
                    nome,
                    orcamento
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Participante atualizado com sucesso!"
            );

            atualizarTabela();
            limparCampos();

        } catch (NumberFormatException erro) {

            mostrarErro("Informe um código e um orçamento válidos.");

        } catch (IllegalArgumentException erro) {

            mostrarErro(erro.getMessage());
        }
    }

    private void excluir() {

        try {

            int linha = tabela.getSelectedRow();

            if (linha == -1) {
                mostrarErro("Selecione um participante na tabela.");
                return;
            }

            int codigo = (int) modeloTabela.getValueAt(linha, 0);

            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este participante?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            controller.excluirParticipante(codigo);

            JOptionPane.showMessageDialog(
                    this,
                    "Participante excluído com sucesso!"
            );

            atualizarTabela();
            limparCampos();

        } catch (IllegalArgumentException erro) {

            mostrarErro(erro.getMessage());
        }
    }

    public void atualizarTabela() {

        modeloTabela.setRowCount(0);

        for (Participante participante :
                controller.getPlanejamento().getParticipantes()) {

            modeloTabela.addRow(new Object[]{
                    participante.getCodigo(),
                    participante.getNome(),
                    participante.getOrcamento()
            });
        }
    }

    private void preencherFormulario() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            return;
        }

        campoCodigo.setText(
                modeloTabela.getValueAt(linha, 0).toString()
        );

        campoNome.setText(
                modeloTabela.getValueAt(linha, 1).toString()
        );

        campoOrcamento.setText(
                modeloTabela.getValueAt(linha, 2).toString()
        );

        campoCodigo.setEditable(false);
    }

    private void limparCampos() {

        campoCodigo.setText("");
        campoNome.setText("");
        campoOrcamento.setText("");

        campoCodigo.setEditable(true);

        tabela.clearSelection();
    }

    private void mostrarErro(String mensagem) {

        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Atenção",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void atualizarDados() {
        atualizarTabela();
    }

}
