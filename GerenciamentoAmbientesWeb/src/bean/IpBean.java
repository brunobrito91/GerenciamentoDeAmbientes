package bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.facade.IpFacade;
import com.facade.IpFacadeImpl;
import com.model.Ip;
import com.util.Lookup;

@ManagedBean
public class IpBean implements Serializable {
	private static final long serialVersionUID = -2434648284710366239L;

	private IpFacade _ipFacadeImpl;

	private Ip ip;

	public IpBean() {
		System.out.println("IpBean");
	}

	@PostConstruct
	public void init() {
		_ipFacadeImpl = (IpFacade) Lookup.doLookup(IpFacadeImpl.class, IpFacade.class);
		ip = new Ip();
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		ip.setIp(request.getHeader("X-FORWARDED-FOR"));
		if (ip.getIp() == null) {
			ip.setIp(request.getRemoteAddr());
		}
	}

	public String salvarIp() {
		if (IpExiste(ip)) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Ip já cadastrado!");
			return " ";
		} else {
			_ipFacadeImpl.save(ip);
			addMessage(FacesMessage.SEVERITY_INFO, "Ip cadastrado com sucesso!");
			return "";
		}
	}

	public void addMessage(Severity severity, String mensagem) {
		FacesMessage message = new FacesMessage(severity, mensagem, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private boolean IpExiste(Ip ip) {
		List<Ip> ipsCadastrados = getListaDeIps();
		for (Ip ipCadastrado : ipsCadastrados) {
			if (ipCadastrado.getIp().equals(ip.getIp())) {
				return true;
			}
		}
		return false;
	}

	public List<Ip> getListaDeIps() {
		List<Ip> ipsCadastrados = _ipFacadeImpl.findAll();
		return ipsCadastrados;
	}

	public Ip getIp() {
		return ip;
	}

	public void setIp(Ip ip) {
		this.ip = ip;
	}

	public String getIpString() {
		return ip.getIp();
	}

	public void setIpString(String ipString) {
		ip.setIp(ipString);
	}

}
