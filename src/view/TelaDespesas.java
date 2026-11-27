package view;

import controller.PlanejamentoController;
import model.Despesa;
import model.Participante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TelaDespesas extends JFrame {

    private final PlanejamentoController controller;

    private JTextField campoCodigo;
    private JTextField campoDescricao;
    private JTextField campoValor;

    private JComboBox<String> comboTipo;

    private JList<String> listaParticipantes;
    private DefaultListModel<String> modeloLista;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaDespesas(PlanejamentoController controller) {

        this.controller = controller;

        configurarJanela();
        montarTela();
        atualizarTabela();
    }

    private void configurarJanela() {

        setTitle("Gerenciamento de Despesas");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void montarTela() {

        setLayout(new BorderLayout(10, 10));

        // ==============================
        // FORMULÁRIO
        // ==============================

        JPanel painelFormulario = new JPanel(
                new GridLayout(4, 2, 8, 8)
        );

        painelFormulario.setBorder(
                BorderFactory.createTitledBorder("Dados da despesa")
        );

        campoCodigo = new JTextField();
        campoDescricao = new JTextField();
        campoValor = new JTextField();

        comboTipo = new JComboBox<>(
                new String[]{
                        "Despesa fixa",
                        "Despesa por pessoa"
                }
        );

        painelFormulario.add(new JLabel("Código:"));
        painelFormulario.add(campoCodigo);

        painelFormulario.add(new JLabel("Descrição:"));
        painelFormulario.add(campoDescricao);

        painelFormulario.add(new JLabel("Valor (R$):"));
        painelFormulario.add(campoValor);

        painelFormulario.add(new JLabel("Tipo:"));
        painelFormulario.add(comboTipo);

        add(painelFormulario, BorderLayout.NORTH);

        // ==============================
        // SELEÇÃO DE PARTICIPANTES
        // ==============================

        modeloLista = new DefaultListModel<>();

        listaParticipantes = new JList<>(modeloLista);

        listaParticipantes.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );

        JScrollPane painelParticipantes =
                new JScrollPane(listaParticipantes);

        painelParticipantes.setBorder(
                BorderFactory.createTitledBorder(
                        "Selecione os participantes envolvidos"
                )
        );

        painelParticipantes.setPreferredSize(
                new Dimension(250, 130)
        );

        atualizarListaParticipantes();

        // ==============================
        // TABELA DE DESPESAS
        // ==============================

        modeloTabela = new DefaultTableModel(
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
                BorderFactory.createTitledBorder(
                        "Despesas cadastradas"
                )
        );

        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));

        painelCentral.add(painelParticipantes, BorderLayout.WEST);
        painelCentral.add(painelTabela, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        // ==============================
        // BOTÕES
        // ==============================

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

    // ==============================
    // CADASTRAR DESPESA
    // ==============================

    private void cadastrar() {

        try {

            int codigo = Integer.parseInt(
                    campoCodigo.getText().trim()
            );

            String descricao = campoDescricao.getText();

            BigDecimal valor = new BigDecimal(
                    campoValor.getText().trim().replace(",", ".")
            );

            List<Integer> codigos =
                    obterCodigosSelecionados();

            if (comboTipo.getSelectedIndex() == 0) {

                controller.cadastrarDespesaFixa(
                        codigo,
                        descricao,
                        valor,
                        codigos
                );

            } else {

                controller.cadastrarDespesaPorPessoa(
                        codigo,
                        descricao,
                        valor,
                        codigos
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Despesa cadastrada com sucesso!"
            );

            atualizarTabela();
            limparCampos();

        } catch (NumberFormatException erro) {

            mostrarErro(
                    "Informe um código e um valor válidos."
            );

        } catch (IllegalArgumentException erro) {

            mostrarErro(erro.getMessage());
        }
    }

    // ==============================
    // EDITAR DESPESA
    // ==============================

    private void editar() {

        try {

            int linha = tabela.getSelectedRow();

            if (linha == -1) {

                mostrarErro(
                        "Selecione uma despesa na tabela."
                );

                return;
            }

            int codigo = Integer.parseInt(
                    campoCodigo.getText().trim()
            );

            String descricao = campoDescricao.getText();

            BigDecimal valor = new BigDecimal(
                    campoValor.getText().trim().replace(",", ".")
            );

            List<Integer> codigos =
                    obterCodigosSelecionados();

            controller.editarDespesa(
                    codigo,
                    descricao,
                    valor,
                    codigos
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Despesa atualizada com sucesso!"
            );

            atualizarTabela();
            limparCampos();

        } catch (NumberFormatException erro) {

            mostrarErro(
                    "Informe um código e um valor válidos."
            );

        } catch (IllegalArgumentException erro) {

            mostrarErro(erro.getMessage());
        }
    }

    // ==============================
    // EXCLUIR DESPESA
    // ==============================

    private void excluir() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {

            mostrarErro(
                    "Selecione uma despesa na tabela."
            );

            return;
        }

        int codigo = (int) modeloTabela.getValueAt(linha, 0);

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir esta despesa?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            controller.excluirDespesa(codigo);

            JOptionPane.showMessageDialog(
                    this,
                    "Despesa excluída com sucesso!"
            );

            atualizarTabela();
            limparCampos();

        } catch (IllegalArgumentException erro) {

            mostrarErro(erro.getMessage());
        }
    }

    // ==============================
    // ATUALIZAR TABELA
    // ==============================

    public void atualizarTabela() {

        modeloTabela.setRowCount(0);

        for (Despesa despesa :
                controller.getPlanejamento().getDespesas()) {

            String tipo = despesa.getClass().getSimpleName();

            String participantes = despesa.getParticipantes()
                    .stream()
                    .map(Participante::getNome)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            modeloTabela.addRow(new Object[]{
                    despesa.getCodigo(),
                    despesa.getDescricao(),
                    despesa.getValor(),
                    tipo,
                    participantes
            });
        }
    }

    // ==============================
    // ATUALIZAR LISTA DE PARTICIPANTES
    // ==============================

    private void atualizarListaParticipantes() {

        modeloLista.clear();

        for (Participante participante :
                controller.getPlanejamento().getParticipantes()) {

            modeloLista.addElement(
                    participante.getCodigo()
                            + " - "
                            + participante.getNome()
            );
        }
    }

    private List<Integer> obterCodigosSelecionados() {

        List<Integer> codigos = new ArrayList<>();

        for (String item : listaParticipantes.getSelectedValuesList()) {

            int codigo = Integer.parseInt(
                    item.substring(0, item.indexOf(" - "))
            );

            codigos.add(codigo);
        }

        return codigos;
    }

    // ==============================
    // PREENCHER FORMULÁRIO
    // ==============================

    private void preencherFormulario() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            return;
        }

        campoCodigo.setText(
                modeloTabela.getValueAt(linha, 0).toString()
        );

        campoDescricao.setText(
                modeloTabela.getValueAt(linha, 1).toString()
        );

        campoValor.setText(
                modeloTabela.getValueAt(linha, 2).toString()
        );

        String tipo = modeloTabela.getValueAt(linha, 3).toString();

        if (tipo.equals("DespesaFixa")) {

            comboTipo.setSelectedIndex(0);

        } else {

            comboTipo.setSelectedIndex(1);
        }

        campoCodigo.setEditable(false);

        Despesa despesa = controller.getPlanejamento()
                .getDespesas()
                .stream()
                .filter(d -> d.getCodigo() == Integer.parseInt(
                        campoCodigo.getText()
                ))
                .findFirst()
                .orElse(null);

        if (despesa != null) {

            listaParticipantes.clearSelection();

            for (int i = 0; i < modeloLista.size(); i++) {

                String item = modeloLista.getElementAt(i);

                int codigo = Integer.parseInt(
                        item.substring(0, item.indexOf(" - "))
                );

                boolean selecionado = despesa.getParticipantes()
                        .stream()
                        .anyMatch(p -> p.getCodigo() == codigo);

                if (selecionado) {
                    listaParticipantes.addSelectionInterval(i, i);
                }
            }
        }
    }

    // ==============================
    // LIMPAR CAMPOS
    // ==============================

    private void limparCampos() {

        campoCodigo.setText("");
        campoDescricao.setText("");
        campoValor.setText("");

        campoCodigo.setEditable(true);

        comboTipo.setSelectedIndex(0);

        listaParticipantes.clearSelection();

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
        atualizarListaParticipantes();
        atualizarTabela();
    }

}
