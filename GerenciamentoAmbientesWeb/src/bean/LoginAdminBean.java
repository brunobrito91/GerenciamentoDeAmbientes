package bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

@ManagedBean
public class LoginAdminBean implements Serializable {
	private static final long serialVersionUID = -7693713951572551532L;

	private boolean loggedIn = false;

	private String username;

	private String password;

	public LoginAdminBean() {
		System.out.println("LoginAdminBean");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void login(ActionEvent event) {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;

		if (username != null && username.equals("admin") && password != null && password.equals("admin")) {
			loggedIn = true;
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);

		} else {
			loggedIn = false;
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Dados incorretos", "Invalid credentials");

		}

		FacesContext.getCurrentInstance().addMessage(null, message);
		context.addCallbackParam("loggedIn", loggedIn);

	}

	public String verificarLogin() {
		if (loggedIn) {
			return "cadastroUsuario.xhtml?faces-redirect=true";
		} else {
			return " ";
		}
	}
}