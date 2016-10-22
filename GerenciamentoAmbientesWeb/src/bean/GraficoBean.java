package bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;

import com.facade.AmbienteFacade;
import com.facade.AmbienteFacadeImpl;
import com.model.Ambiente;
import com.model.Ambiente.MedicaoTemperatura;
import com.util.Lookup;

@ManagedBean
@ViewScoped
public class GraficoBean implements Serializable {
	private static final long serialVersionUID = 3211116565573441502L;

	private AmbienteFacade _ambienteFacadeImpl;

	private Ambiente ambiente;
	private Date data;

	private JSONArray json;
	private int alerta;

	private int temperaturaMin;
	private int temperaturaMax;
	private int temperaturaMed;
	private String dataTemperaturaAlerta;
	private int numeroOcorrencias;

	public GraficoBean() {
		System.out.println("GraficoBean");
	}

	@PostConstruct
	public void init() {
		_ambienteFacadeImpl = (AmbienteFacade) Lookup.doLookup(AmbienteFacadeImpl.class, AmbienteFacade.class);
		json = new JSONArray();

		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);

		setAmbiente((Ambiente) session.getAttribute("ambienteEscolhido"));
		setData(new Date());
		setAlerta(ambiente.getTemperaturaAlerta());
		criarModelo();
	}

	public void criarModelo() {
		if (ambiente != null) {
			temperaturaMax = 0;
			temperaturaMin = 20;
			temperaturaMed = 0;
			numeroOcorrencias = 0;

			List<MedicaoTemperatura> listaMedicao = _ambienteFacadeImpl.findMedicoesByDate(ambiente.getId(), data);

			// RequestContext.getCurrentInstance().execute("drawChart()");
			if (listaMedicao.isEmpty()) {
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.update("form:display");
				requestContext.execute("PF('dlgNadaAExibir').show()");
			} else {
				json = new JSONArray(listaMedicao.toArray());
				for (MedicaoTemperatura medicaoTemperatura : listaMedicao) {
					int temperatura = Integer.parseInt(medicaoTemperatura.getTemperatura());

					temperaturaMed = temperaturaMed + temperatura;
					if (temperatura >= ambiente.getTemperaturaAlerta()) {
						numeroOcorrencias = numeroOcorrencias + 1;
						dataTemperaturaAlerta = new SimpleDateFormat("HH:mm:ss")
								.format(medicaoTemperatura.getDataMedicao());
					}
					if (temperatura > temperaturaMax) {
						temperaturaMax = temperatura;
					}
					if (temperatura < temperaturaMin) {
						temperaturaMin = temperatura;
					}
				}
				temperaturaMed = temperaturaMed / listaMedicao.size();
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.update("form:display");
				requestContext.execute("PF('dlgResumo').show()");
			}
		}
	}

	public int getTemperaturaMin() {
		return temperaturaMin;
	}

	public void setTemperaturaMin(int temperaturaMin) {
		this.temperaturaMin = temperaturaMin;
	}

	public int getTemperaturaMax() {
		return temperaturaMax;
	}

	public void setTemperaturaMax(int temperaturaMax) {
		this.temperaturaMax = temperaturaMax;
	}

	public int getTemperaturaMed() {
		return temperaturaMed;
	}

	public void setTemperaturaMed(int temperaturaMed) {
		this.temperaturaMed = temperaturaMed;
	}

	public String getDataTemperaturaAlerta() {
		return dataTemperaturaAlerta;
	}

	public void setDataTemperaturaAlerta(String dataTemperaturaAlerta) {
		this.dataTemperaturaAlerta = dataTemperaturaAlerta;
	}

	public int getNumeroOcorrencias() {
		return numeroOcorrencias;
	}

	public void setNumeroOcorrencias(int numeroOcorrencias) {
		this.numeroOcorrencias = numeroOcorrencias;
	}

	public Ambiente getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(Ambiente ambiente) {
		this.ambiente = ambiente;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public JSONArray getJson() {
		return json;
	}

	public void setJson(JSONArray json) {
		this.json = json;
	}

	public int getAlerta() {
		return alerta;
	}

	public void setAlerta(int alerta) {
		this.alerta = alerta;
	}
}
