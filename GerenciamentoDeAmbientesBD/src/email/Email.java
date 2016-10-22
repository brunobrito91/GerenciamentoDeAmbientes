package email;

import java.util.ArrayList;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import com.model.Usuario;

import util.Log;

public class Email extends Thread {
	private String fromEmail;
	private String fromNome;
	private ArrayList<Usuario> contatos;
	private String subject;
	private String message;

	public Email(String fromEmail, String fromNome, ArrayList<Usuario> contatos, String subject, String message) {
		this.fromEmail = fromEmail;
		this.fromNome = fromNome;
		this.contatos = contatos;
		this.subject = subject;
		this.message = message;
	}

	@Override
	public void run() {
		try {
			sendEmail(fromEmail, fromNome, contatos, subject, message);
		} catch (EmailException e) {
			Log.logarErro(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}

	public void sendEmail(String fromEmail, String fromNome, ArrayList<Usuario> contatos, String subject,
			String message) throws EmailException {
		SimpleEmail email = new SimpleEmail();
		// email.setDebug(true);

		email.setHostName("101.1.0.91");
		email.setSmtpPort(25);

		for (Usuario usuario : contatos) {
			email.addTo(usuario.getEmail(), usuario.getNome());
		}

		email.setFrom(fromEmail, fromNome);
		email.setSubject(subject);
		email.setMsg(message);
		System.out.println("-------------------E-MAIL--------------------");
		Log.logarSaida("Enviando...");
		email.send();
		Log.logarSaida("Email enviado!");
	}
}
