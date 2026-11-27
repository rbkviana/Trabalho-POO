package interfaces;

import model.Planejamento;
import java.io.IOException;

public interface Persistencia {

    void salvar(Planejamento planejamento, String caminho)
            throws IOException;

    Planejamento carregar(String caminho)
            throws IOException, ClassNotFoundException;
}
