package comunicacao;

import java.io.IOException;
import java.net.Socket;

public final class Cliente {
	private Socket cliente;

	public Cliente(String ipServidor) {
		try {
			cliente = new Socket(ipServidor, 6789);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void enviarMensagem(byte[] mensagem) {
		if (cliente != null) {
			if (cliente.isConnected()) {
				try {
					cliente.getOutputStream().write(mensagem);
				} catch (IOException e) {
				}
			} else {
				System.out.println("Não foi possivel enviar a mensagem. Cliente não está conectado");
			}
		} else {
			System.out.println("Cliente null :(");
		}
	}

	public void encerrarConexao() {
		try {
			cliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
