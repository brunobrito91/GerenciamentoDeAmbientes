package comunicacao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.Log;

public class Servidor {
	private ServerSocket _servidor;

	public Servidor(int port) {
		try {
			_servidor = new ServerSocket(port);
		} catch (IOException e) {
			Log.logarErro(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}

	public void IniciarServidor() {
		Log.logarSaida("Servidor iniciado");
		while (true) {
			try {
				if (_servidor != null) {
					Socket socketCliente;
					Log.logarSaida("Aguardando...");
					socketCliente = _servidor.accept();

					Cliente cliente = new Cliente(socketCliente);
					cliente.start();
				} else {
					break;
				}
			} catch (IOException e) {
				Log.logarErro(e.getStackTrace().toString());
				e.printStackTrace();
			}
			try {
				Log.logarSaida("Dormindo...");
				Thread.sleep(1000);
				Log.logarSaida("Acordado...");
			} catch (InterruptedException e) {
				Log.logarErro(e.getStackTrace().toString());
				e.printStackTrace();
			}
		}
	}

}
