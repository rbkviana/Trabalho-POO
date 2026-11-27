import controller.PlanejamentoController;
import view.TelaPrincipal;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            PlanejamentoController controller =
                    new PlanejamentoController();

            TelaPrincipal tela =
                    new TelaPrincipal(controller);

            tela.setVisible(true);
        });
    }
}
