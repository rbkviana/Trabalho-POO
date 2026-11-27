package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Planejamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Participante> participantes;
    private final List<Despesa> despesas;

    public Planejamento() {
        this.participantes = new ArrayList<>();
        this.despesas = new ArrayList<>();
    }

    public List<Participante> getParticipantes() {

        return Collections.unmodifiableList(participantes);
    }

    public List<Despesa> getDespesas() {

        return Collections.unmodifiableList(despesas);
    }

    public void adicionarParticipante(Participante participante) {
        participantes.add(participante);
    }

    public void removerParticipante(Participante participante) {
        participantes.remove(participante);
    }

    public void adicionarDespesa(Despesa despesa) {
        despesas.add(despesa);
    }

    public void substituirDespesa(int codigo, Despesa novaDespesa) {

        for (int i = 0; i < despesas.size(); i++) {

            if (despesas.get(i).getCodigo() == codigo) {

                despesas.set(i, novaDespesa);
                return;
            }
        }
    }

    public void removerDespesa(Despesa despesa) {
        despesas.remove(despesa);
    }
}
