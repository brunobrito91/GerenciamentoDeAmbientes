package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public static void logarSaida(String log) {
		logar(log, "saida.txt");
	}

	public static void logarErro(String log) {
		logar(log, "erro.txt");
	}

	private static void logar(String log, String nomeArquivo) {
		Date data = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		log = format.format(data) + " - " + log + "\n";
		System.out.println(log);
		Arquivo.salvar(log, nomeArquivo);
	}

}
