package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Arquivo {

	public static void salvar(String log, String nomeArquivo) {
		File arquivo = new File(nomeArquivo);
		if (arquivo.exists()) {
			inserirLog(log, arquivo);
		} else {
			criarArquivo(arquivo);
			salvar(log, nomeArquivo);
		}

	}

	private static void criarArquivo(File arquivo) {
		try {
			arquivo.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void inserirLog(String log, File arquivo) {
		try {
			FileWriter escritor = new FileWriter(arquivo, true);
			escritor.write(log);
			escritor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
