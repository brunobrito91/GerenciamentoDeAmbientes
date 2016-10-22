package comunicacao;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import util.GerenciadorDeMensagem;
import util.Log;

public class Cliente extends Thread {
	private static int numero;
	private Socket _cliente;
	private GerenciadorDeMensagem _gerenciadorDeMensagem;

	public Cliente(Socket cliente) {
		super("Thread Cliente " + numero + " - " + cliente.getInetAddress());
		numero++;
		_cliente = cliente;
		_gerenciadorDeMensagem = new GerenciadorDeMensagem(_cliente.getInetAddress().toString());
	}

	@Override
	public void run() {
		Log.logarSaida(this.getName() +" ativa");
		Log.logarSaida("Cliente " + _cliente.getInetAddress() + " conectado");
		while (_cliente.isConnected()) {
			InputStream inputStream;
			try {
				inputStream = _cliente.getInputStream();
				int tamanhoBuffer = inputStream.available();
				if (tamanhoBuffer > 0) {
					Log.logarSaida("Tamanho do buffer: " + tamanhoBuffer);
					byte[] mensagem = new byte[tamanhoBuffer];
					inputStream.read(mensagem);
					for (int i = 0; i < mensagem.length; i++) {
						Log.logarSaida(_cliente.getInetAddress() + ": " + mensagem[i]);
					}
					_gerenciadorDeMensagem.GerenciarMensagem(mensagem);
				}
			} catch (IOException e) {
				Log.logarErro(e.getStackTrace().toString());
				e.printStackTrace();
			}
			try {
				Cliente.sleep(1000);
			} catch (InterruptedException e) {
				Log.logarErro(e.getStackTrace().toString());
				e.printStackTrace();
			}
		}
		try {
			Log.logarSaida(this.getName() +" desativada");
			Log.logarSaida("Cliente " + _cliente.getInetAddress() + " desconectado");
			Log.logarSaida("Fechando socket...");
			_cliente.close();
			Log.logarSaida("Socket fechado");
		} catch (IOException e) {
			Log.logarErro(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}

}
