package bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;

import com.facade.AmbienteFacade;
import com.facade.AmbienteFacadeImpl;
import com.model.Ambiente;
import com.util.Lookup;

@ManagedBean
@ViewScoped
public class MenuBean implements Serializable {
	private static final long serialVersionUID = -6295120941560014825L;

	private AmbienteFacade _ambienteFacadeImpl;

	private DefaultMenuModel menuModel;

	public MenuBean() {
		System.out.println("MenuBean");
	}

	@PostConstruct
	public void init() {
		_ambienteFacadeImpl = (AmbienteFacade) Lookup.doLookup(AmbienteFacadeImpl.class, AmbienteFacade.class);
		criarMenuPrincipal();
	}

	private void criarMenuPrincipal() {
		menuModel = new DefaultMenuModel();
		DefaultSubMenu subMenu = new DefaultSubMenu("Ambientes");

		List<Ambiente> ambientes = _ambienteFacadeImpl.findAll();

		for (Ambiente ambiente : ambientes) {
			DefaultMenuItem menuItem = new DefaultMenuItem(ambiente);
			menuItem.setUpdate("@all");
			menuItem.setCommand("#{ambienteBean.exibirPaginaAmbienteEscolhido}");
			subMenu.addElement(menuItem);
		}

		menuModel.addElement(subMenu);

		UsuarioBean usuarioBean = new UsuarioBean();
		if (usuarioBean.getIsAdministrador()) {
			DefaultSubMenu subMenuConfiguracoes = new DefaultSubMenu("Configurações");
			
			DefaultMenuItem menuItemEmails = new DefaultMenuItem("Emails");
			menuItemEmails.setCommand("configuracaoEmails.xhtml?faces-redirect=true");
			// menuItemEmails.setCommand("#{configuracaoEmailBean.exibirPagina}");
			subMenuConfiguracoes.addElement(menuItemEmails);

			DefaultMenuItem menuItemIps = new DefaultMenuItem("Ips");
			menuItemIps.setCommand("configuracaoIps.xhtml?faces-redirect=true");
			// menuItemIps.setCommand("#{configuracaoEmailBean.exibirPagina}");
			subMenuConfiguracoes.addElement(menuItemIps);
			
			menuModel.addElement(subMenuConfiguracoes);
		}

	}

	public DefaultMenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(DefaultMenuModel menuModel) {
		this.menuModel = menuModel;
	}
}
