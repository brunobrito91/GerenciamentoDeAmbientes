package main;

import comunicacao.Servidor;

public class GerenciamentoDeAmbientesBD {
	public static void main(String[] args) {
		Servidor servidor = new Servidor(9876);
		servidor.IniciarServidor();
		// System.out.println("Gerenciamento");
		// GerenciadorDeMensagem gm = new
		// GerenciadorDeMensagem("/101.99.100.190");
		//
		// byte[] msg = new byte[4];
		// msg[0] = 'I';
		// msg[1] = 2;
		// msg[2] = 6;
		// msg[3] = 2;
		//
		// gm.GerenciarMensagem(msg);
		//
		// byte[] msg1 = new byte[4];
		// msg1[0] = 'T';
		// msg1[1] = 2;
		// msg1[2] = 1;
		// msg1[3] = 26;
		//
		// gm.GerenciarMensagem(msg1);
		//
		// for (int i = 0; i < 30; i++) {
		// msg1[0] = 'T';
		// msg1[1] = 2;
		// msg1[2] = 1;
		// msg1[3] = (byte) i;
		//
		// gm.GerenciarMensagem(msg1);
		// }

	}
}
