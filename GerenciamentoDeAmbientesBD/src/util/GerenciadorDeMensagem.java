package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import com.facade.AmbienteFacade;
import com.facade.AmbienteFacadeImpl;
import com.facade.DispositivoFacade;
import com.facade.DispositivoFacadeImpl;
import com.facade.UsuarioFacade;
import com.facade.UsuarioFacadeImpl;
import com.model.Ambiente;
import com.model.Ambiente.MedicaoTemperatura;
import com.model.Dispositivo;
import com.model.Usuario;
import com.util.ClientUtility;
import com.util.Lookup;

import email.Email;

public class GerenciadorDeMensagem {
	private DispositivoFacade _dispositivoFacadeImpl;
	private AmbienteFacade _ambienteFacadeImpl;
	private UsuarioFacade _usuarioFacadeImpl;

	private Dispositivo _dispositivo;
	private List<Ambiente> _ambientes;

	private HashSet<Integer> _alerta;

	public GerenciadorDeMensagem(String ip) {

		_dispositivoFacadeImpl = (DispositivoFacade) doLookup(DispositivoFacadeImpl.class, DispositivoFacade.class);

		_ambienteFacadeImpl = (AmbienteFacade) doLookup(AmbienteFacadeImpl.class, AmbienteFacade.class);

		_usuarioFacadeImpl = (UsuarioFacade) doLookup(UsuarioFacadeImpl.class, UsuarioFacade.class);

		_dispositivo = _dispositivoFacadeImpl.find(ip);

		if (_dispositivo == null) {
			_dispositivo = new Dispositivo(ip);
		}
		_alerta = new HashSet<Integer>();
	}

	private <T, P> Object doLookup(Class<T> bean, Class<P> interfac) {
		Object object = null;
		Context context = null;
		String lookupName;

		lookupName = Lookup.getLookupName(bean, interfac);
		Log.logarSaida(lookupName);
		try {
			context = ClientUtility.getInitialContext();
			return context.lookup(lookupName);
		} catch (NamingException e) {
			Log.logarErro(e.getStackTrace().toString());
			e.printStackTrace();
		}

		return object;
	}

	public void GerenciarMensagem(byte[] mensagem) {
		for (int j = 0; j < mensagem.length;) {
			int qtdBytes = mensagem[j + 1];

			switch (mensagem[j]) {
			// Temperatura
			case 'T': {
				Log.logarSaida("-------------------TEMPERATURA--------------------");
				int id = _ambientes.get(mensagem[j + 2]).getId();
				Ambiente ambiente = _ambienteFacadeImpl.find(id);

				Log.logarSaida("Ambiente : " + ambiente.getNome());
				Log.logarSaida("Temperatura igual a " + ambiente.getTemperatura());

				byte temperatura = mensagem[j + 3];

				ambiente.setTemperatura(temperatura);
				verificarAlerta(ambiente);
				_ambienteFacadeImpl.update(ambiente);

				MedicaoTemperatura medicao = new MedicaoTemperatura(String.valueOf(temperatura), new Date());
				_ambienteFacadeImpl.saveMedicao(id, medicao);

				Log.logarSaida("Temperatura atualizada para " + ambiente.getTemperatura());
				Log.logarSaida("--------------------------------------------------");
			}
				break;
			// Status Presença
			case 'P': {
				Log.logarSaida("-------------------STATUS PRESENÇA--------------------");
				int id = _ambientes.get(mensagem[j + 2]).getId();
				Ambiente ambiente = _ambienteFacadeImpl.find(id);

				Log.logarSaida("Ambiente : " + ambiente.getNome());
				Log.logarSaida("Status presença igual a " + ambiente.temPresenca());

				ambiente.setStatusPresenca(mensagem[j + 3] == 1);
				_ambienteFacadeImpl.update(ambiente);

				Log.logarSaida("Status presença atualizada para " + ambiente.temPresenca());
				Log.logarSaida("------------------------------------------------------");
			}
				break;
			// Status Janela
			case 'J': {
				Log.logarSaida("-------------------STATUS JANELA--------------------");
				int id = _ambientes.get(mensagem[j + 2]).getId();
				Ambiente ambiente = _ambienteFacadeImpl.find(id);

				Log.logarSaida("Ambiente : " + ambiente.getNome());
				Log.logarSaida("Status janela igual a " + ambiente.isJanelaAberta());

				ambiente.setStatusJanela(mensagem[j + 3] == 1);
				_ambienteFacadeImpl.update(ambiente);

				Log.logarSaida("Status janela atualizada para " + ambiente.isJanelaAberta());
				Log.logarSaida("----------------------------------------------------");

			}
				break;
			// Status CondicionadorAr
			case 'C': {
				Log.logarSaida("-------------------STATUS CONDICIONADOR DE AR--------------------");
				int id = _ambientes.get(mensagem[j + 2]).getId();
				Ambiente ambiente = _ambienteFacadeImpl.find(id);

				Log.logarSaida("Ambiente : " + ambiente.getNome());
				Log.logarSaida("Status condicionador de ar igual a " + ambiente.isCondicionadorArLigado());

				ambiente.setStatusCondicionadorAr(mensagem[j + 3] == 1);
				_ambienteFacadeImpl.update(ambiente);

				Log.logarSaida("Status condicionador de ar atualizada para " + ambiente.isCondicionadorArLigado());
				Log.logarSaida("-----------------------------------------------------------------");
			}
				break;
			// Inicialização
			case 'I': {
				Log.logarSaida("-------------------INICIALIZAÇÃO--------------------");
				if (_dispositivo.getAmbientes().isEmpty()) {
					int qtdAmbientes = mensagem[j + 2];
					Log.logarSaida("Quantidade de ambientes : " + qtdAmbientes);
					_dispositivo.setTipo((int) mensagem[j + 3]);
					_dispositivoFacadeImpl.save(_dispositivo);

					Log.logarSaida("Dispositivo " + _dispositivo.getIp() + " conectado");

					for (int i = 0; i < qtdAmbientes; i++) {
						Ambiente ambiente = new Ambiente(i, _dispositivo);
						_ambienteFacadeImpl.save(ambiente);
					}
				}
				_dispositivo = _dispositivoFacadeImpl.find(_dispositivo.getIp());
				_ambientes = _dispositivo.getAmbientes();

				Log.logarSaida(_ambientes.size() + " ambientes cadastrados");
				for (Ambiente ambiente : _ambientes) {
					Log.logarSaida(ambiente.getNome() + " - id: " + ambiente.getId());
				}
				Log.logarSaida("----------------------------------------------------");
			}
				break;

			default: {

			}
				break;
			}
			j = j + 2 + qtdBytes;
		}
	}

	private void verificarAlerta(Ambiente ambiente) {
		if (_alerta.contains(ambiente.getId())) {
			if (ambiente.getTemperatura() <= ambiente.getTemperaturaAlerta()) {
				_alerta.remove(ambiente.getId());
			}
		} else {
			if (ambiente.getTemperatura() >= ambiente.getTemperaturaAlerta()) {
				_alerta.add(ambiente.getId());

				String mensagem = new String();
				mensagem = "O ambiente " + ambiente.getId() + "-" + ambiente.getNome()
						+ ", está com a temperatura elevada. Verifique!!";
				ArrayList<Usuario> contatos = new ArrayList<>();

				Log.logarSaida(mensagem);

				for (Usuario usuario : _usuarioFacadeImpl.findAll()) {
					contatos.add(usuario);
				}
				Email email = new Email("GerenciamentoDeAmbientesEptvCentral@eptv.com.br",
						"Gerenciamento De Ambientes Eptv Central", contatos, "Temperatura Elevada", mensagem);
				email.start();
			}
		}
	}

}
