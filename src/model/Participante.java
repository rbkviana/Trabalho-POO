package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Participante implements Serializable {

    private static final long serialVersionUID = 1L;

    private int codigo;
    private String nome;
    private BigDecimal orcamento;

    public Participante(int codigo, String nome, BigDecimal orcamento) {
        this.codigo = codigo;
        this.nome = nome;
        this.orcamento = orcamento;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(BigDecimal orcamento) {
        this.orcamento = orcamento;
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
                " | Nome: " + nome +
                " | Orçamento: R$ " + orcamento;
    }
}