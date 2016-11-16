package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

import com.facade.AmbienteFacade;
import com.facade.AmbienteFacadeImpl;
import com.facade.EmailFacade;
import com.facade.EmailFacadeImpl;
import com.model.Ambiente;
import com.model.Email;
import com.util.Lookup;

@ManagedBean
public class ConfiguracaoEmailBean implements Serializable {
	private static final long serialVersionUID = -3669804690692383682L;

	private Email email, emailSelecionado;
	private EmailFacade _emailFacadeImpl;
	private AmbienteFacade _ambienteFacadeImpl;

	private DualListModel<Ambiente> ambientes;
	private List<Ambiente> source;
	private List<Ambiente> target;

	public ConfiguracaoEmailBean() {
		System.out.println("ConfiguracaoEmailBean");
	}

	@PostConstruct
	public void init() {
		_emailFacadeImpl = (EmailFacade) Lookup.doLookup(EmailFacadeImpl.class, EmailFacade.class);
		_ambienteFacadeImpl = (AmbienteFacade) Lookup.doLookup(AmbienteFacadeImpl.class, AmbienteFacade.class);

		email = new Email();
		emailSelecionado = new Email();

		source = getAmbientesAindaNaoAssociados();
		target = emailSelecionado.getAmbientes();

		ambientes = new DualListModel<Ambiente>(source, target);
	}

	private List<Ambiente> getAmbientesAindaNaoAssociados() {
		List<Ambiente> ambientesCadastrados = emailSelecionado.getAmbientes();
		List<Ambiente> ambientesAindaNaoAssociados = new ArrayList<Ambiente>();
		for (Ambiente ambiente : _ambienteFacadeImpl.findAll()) {
			if (!ambientesCadastrados.contains(ambiente)) {
				ambientesAindaNaoAssociados.add(ambiente);
			}
		}
		return ambientesAindaNaoAssociados;
	}

	public String exibirPagina() {
		UsuarioBean usuarioBean = new UsuarioBean();
		if (usuarioBean.getIsAdministrador()) {
			return "sucess";
		}
		return "unsucess";
	}

	public String getEmail() {
		return email.getEmail();
	}

	public void setEmail(String email) {
		this.email.setEmail(email);
	}

	public Email getEmailSelecionado() {
		return emailSelecionado;
	}

	public void setEmailSelecionado(Email emailSelecionado) {
		this.emailSelecionado = emailSelecionado;
	}

	public String salvarEmail() {
		if (EmailExiste(email)) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Email já cadastrado!");
			return " ";
		} else {
			_emailFacadeImpl.save(email);
			addMessage(FacesMessage.SEVERITY_INFO, "Email cadastrado com sucesso!");
//			FacesContext fc = FacesContext.getCurrentInstance();
//			String uri = ((HttpServletRequest) fc.getExternalContext().getRequest()).getRequestURI();
//			String[] str = uri.split("/");
			// return str[2] + "?faces-redirect=true";
			return "";
		}
	}

	private void addMessage(Severity severity, String mensagem) {
		FacesMessage message = new FacesMessage(severity, mensagem, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onRowSelect(SelectEvent event) {
		emailSelecionado = ((Email) event.getObject());
		ambientes.setSource(getAmbientesAindaNaoAssociados());
		ambientes.setTarget(emailSelecionado.getAmbientes());
	}

	private boolean EmailExiste(Email email) {
		List<Email> emailsCadastrados = getListaDeEmails();
		for (Email emailCadastrado : emailsCadastrados) {
			if (emailCadastrado.getEmail().equals(email.getEmail())) {
				return true;
			}
		}
		return false;
	}

	public List<Email> getListaDeEmails() {
		List<Email> emailsCadastrados = _emailFacadeImpl.findAll();
		return emailsCadastrados;
	}

	public DualListModel<Ambiente> getListaDeAmbientes() {
		return ambientes;
	}

	public void setListaDeAmbientes(DualListModel<Ambiente> ambientes) {
		this.ambientes = ambientes;
	}

	public String vincular() {
		System.out.println("####################################");

		List<Ambiente> ambientesVinculados = ambientes.getTarget();
		List<Ambiente> ambientesNaoVinculados = ambientes.getSource();

		System.out.println(emailSelecionado.getEmail());

		for (Ambiente ambiente : ambientesVinculados) {
			boolean contem = false;
			System.out.println("Ambiente vinculado: " + ambiente);
			Set<Email> emails = ambiente.getEmails();
			for (Email email : emails) {
				if (email.equals(emailSelecionado)) {
					contem = true;
					break;
				}
			}
			if (!contem) {
				System.out.println("Não contem o email selecionado");
				_ambienteFacadeImpl.saveEmail(ambiente.getId(), emailSelecionado.getEmail());
			}
		}

		for (Ambiente ambiente : ambientesNaoVinculados) {
			System.out.println("Ambiente não vinculado: " + ambiente);
			Set<Email> emails = ambiente.getEmails();
			for (Email email : emails) {
				if (email.equals(emailSelecionado)) {
					System.out.println("Contem o email selecionado");
					_ambienteFacadeImpl.deleteEmail(ambiente.getId(), emailSelecionado.getEmail());
					break;
				}
			}
		}

		emailSelecionado.setAmbientes(ambientesVinculados);
		_emailFacadeImpl.update(emailSelecionado);

		System.out.println("####################################");
		addMessage(FacesMessage.SEVERITY_INFO, "Associação realizada com sucesso!");
		return "";
	}

}
