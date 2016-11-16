package bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.DefaultMenuItem;

import com.facade.AmbienteFacade;
import com.facade.AmbienteFacadeImpl;
import com.facade.IpFacade;
import com.facade.IpFacadeImpl;
import com.model.Ambiente;
import com.model.Ip;
import com.util.Lookup;

import comunicacao.Cliente;

@ManagedBean
@ViewScoped
public class AmbienteBean implements Serializable {
	private static final long serialVersionUID = 7961266976168256253L;

	private IpFacade _ipFacadeImpl;

	private AmbienteFacade _ambienteFacadeImpl;

	private Ambiente ambiente;

	public AmbienteBean() {
		System.out.println("AmbienteBean");
	}

	@PostConstruct
	public void init() {
		_ambienteFacadeImpl = (AmbienteFacade) Lookup.doLookup(AmbienteFacadeImpl.class, AmbienteFacade.class);
		_ipFacadeImpl = (IpFacade) Lookup.doLookup(IpFacadeImpl.class, IpFacade.class);

		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);

		ambiente = (Ambiente) session.getAttribute("ambienteEscolhido");

	}

	public String exibirPaginaAmbienteEscolhido(ActionEvent event) {
		DefaultMenuItem menuItem = (DefaultMenuItem) ((MenuActionEvent) event).getMenuItem();
		Ambiente ambiente = (Ambiente) menuItem.getValue();
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);

		session.setAttribute("ambienteEscolhido", ambiente);

		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		if (ipAutorizado(ipAddress)) {
			return "ambiente.xhtml?faces-redirect=true";
		}
		return "index.xhtml?faces-redirect=true";
	}

	private boolean ipAutorizado(String ipAddress) {
		List<Ip> ipsCadastrados = _ipFacadeImpl.findAll();
		for (Ip ipCadastrado : ipsCadastrados) {
			if (ipCadastrado.getIp().equals(ipAddress)) {
				return true;
			}
		}
		return false;
	}

	public List<Ambiente> buscarAmbientes() {
		List<Ambiente> ambientes = _ambienteFacadeImpl.findAll();
		return ambientes;
	}

	public String atualizarAmbiente() {
		UsuarioBean usuarioBean = new UsuarioBean();
		if (usuarioBean.Logado()) {
			_ambienteFacadeImpl.update(ambiente);
			addMessage(FacesMessage.SEVERITY_INFO, "Ambiente atualizado com sucesso!");
			return "index.xhtml?faces-redirect=true";
		} else {
			addMessage(FacesMessage.SEVERITY_WARN, "Usuário deve estar logado!");
			return "";
		}
	}

	public void desligarCondicionadorAr(ActionEvent event) {
		UIParameter component = (UIParameter) event.getComponent().findComponent("ambienteSelecionado1");
		Ambiente amb = (Ambiente) component.getValue();

		byte[] mensagem = new byte[4];
		mensagem[0] = 'C';
		mensagem[1] = 2;
		mensagem[2] = (byte) amb.getNumero();
		mensagem[3] = 0;

		Cliente cliente = new Cliente(amb.getDispositivo().getIp().split("/")[1]);
		cliente.enviarMensagem(mensagem);
		cliente.encerrarConexao();

	}

	public void ligarCondicionadorAr(ActionEvent event) {
		UIParameter component = (UIParameter) event.getComponent().findComponent("ambienteSelecionado2");
		Ambiente amb = (Ambiente) component.getValue();

		byte[] mensagem = new byte[4];
		mensagem[0] = 'C';
		mensagem[1] = 2;
		mensagem[2] = (byte) amb.getNumero();
		mensagem[3] = 1;

		Cliente cliente = new Cliente(amb.getDispositivo().getIp().split("/")[1]);
		cliente.enviarMensagem(mensagem);
		cliente.encerrarConexao();

	}

	public List<Ambiente> getAmbientes() {
		return buscarAmbientes();
	}

	public String getNome() {
		return ambiente.getNome();
	}

	public void setNome(String nome) {
		ambiente.setNome(nome);
	}

	public int getTemperaturaAlerta() {
		return ambiente.getTemperaturaAlerta();
	}

	public void setTemperaturaAlerta(int temperaturaAlerta) {
		ambiente.setTemperaturaAlerta(temperaturaAlerta);
	}

	public void addMessage(Severity severity, String mensagem) {
		FacesMessage message = new FacesMessage(severity, mensagem, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
