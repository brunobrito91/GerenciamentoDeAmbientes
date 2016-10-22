package bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.facade.UsuarioFacade;
import com.facade.UsuarioFacadeImpl;
import com.model.Usuario;
import com.util.Lookup;

@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = -9162222995892286784L;

	private UsuarioFacade _usuarioFacadeImpl;

	private Usuario usuario;

	public UsuarioBean() {
		System.out.println("UsuarioBean");
	}

	@PostConstruct
	public void init() {
		_usuarioFacadeImpl = (UsuarioFacade) Lookup.doLookup(UsuarioFacadeImpl.class, UsuarioFacade.class);
		usuario = new Usuario();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return usuario.getNome();
	}

	public void setNome(String nome) {
		this.usuario.setNome(nome);
	}

	public String getSobreNome() {
		return usuario.getSobrenome();
	}

	public void setSobreNome(String sobrenome) {
		this.usuario.setSobrenome(sobrenome);
	}

	public String getEmail() {
		return usuario.getEmail();
	}

	public void setEmail(String email) {
		this.usuario.setEmail(email);
	}

	public String getLogin() {
		return usuario.getLogin();
	}

	public void setLogin(String login) {
		this.usuario.setLogin(login);
	}

	public String getSenha() {
		return usuario.getSenha();
	}

	public void setSenha(String senha) {
		this.usuario.setSenha(senha);
	}

	public boolean Logado() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		HttpSession session = (HttpSession) request.getSession();

		if (session.getAttribute("usuarioLogado") == null) {
			return false;
		} else {
			return true;
		}
	}

	public HashMap<String, Usuario> listaDeUsuarios() {
		HashMap<String, Usuario> usuariosCadastrados = new HashMap<String, Usuario>();
		List<Usuario> usuarios = _usuarioFacadeImpl.findAll();
		for (Usuario usuario : usuarios) {
			usuariosCadastrados.put(usuario.getLogin(), usuario);
		}
		return usuariosCadastrados;
	}

	public void Logar() {
		HashMap<String, Usuario> usuariosCadastrados = listaDeUsuarios();

		Usuario user = usuariosCadastrados.get(this.getUsuario().getLogin());
		if (user != null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
			if (user.getSenha().equals(this.getUsuario().getSenha())) {
				session.setAttribute("usuarioLogado", user);
			} else {
				session.invalidate();
			}
		}
	}

	public void Deslogar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		HttpSession session = (HttpSession) request.getSession();
		session.removeAttribute("usuarioLogado");
	}

	public String salvarUsuario() {
		if (usuarioExiste(usuario)) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Usuário já cadastrado!");
			return " ";
		} else {
			_usuarioFacadeImpl.save(usuario);
			addMessage(FacesMessage.SEVERITY_INFO, "Usuário cadastrado com sucesso!");
			return "index.xhtml?faces-redirect=true";
		}
	}

	private boolean usuarioExiste(Usuario usuario) {
		HashMap<String, Usuario> usuariosCadastrados = listaDeUsuarios();
		return usuariosCadastrados.get(usuario.getLogin()) != null;
	}

	public void addMessage(Severity severity, String mensagem) {
		FacesMessage message = new FacesMessage(severity, mensagem, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
