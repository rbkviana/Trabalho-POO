package dao;

import interfaces.Persistencia;
import model.Planejamento;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArquivoPersistencia implements Persistencia {

    @Override
    public void salvar(Planejamento planejamento, String caminho)
            throws IOException {

        Path arquivo = Path.of(caminho);
        Path pasta = arquivo.getParent();

        if (pasta != null) {
            Files.createDirectories(pasta);
        }

        try (ObjectOutputStream saida = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(arquivo.toFile())
                ))) {

            saida.writeObject(planejamento);
        }
    }

    @Override
    public Planejamento carregar(String caminho)
            throws IOException, ClassNotFoundException {

        Path arquivo = Path.of(caminho);

        try (ObjectInputStream entrada = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(arquivo.toFile())
                ))) {

            Object objeto = entrada.readObject();

            if (!(objeto instanceof Planejamento)) {
                throw new IOException(
                        "O arquivo não contém um planejamento válido."
                );
            }

            return (Planejamento) objeto;
        }
    }
}
