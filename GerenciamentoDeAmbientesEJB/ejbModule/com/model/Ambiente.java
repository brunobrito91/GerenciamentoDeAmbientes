package com.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "buscarTodosAmbientes", query = "SELECT a FROM Ambiente a") })
public class Ambiente implements Serializable {
	private static final long serialVersionUID = -4315162413945160932L;

	@Id
	@GeneratedValue
	private int id;

	private int numero;

	private String nome;
	private int temperaturaAlerta;

	private int temperatura;
	private boolean statusPresenca;
	private boolean statusJanela;
	private boolean statusCondicionadorAr;

	@ManyToOne
	private Dispositivo dispositivo;

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<MedicaoTemperatura> medicoesTemperatura;
	@ElementCollection
	private List<MedicaoStatusPresenca> medicoesStatusPresenca;
	@ElementCollection
	private List<MedicaoStatusJanela> medicoesStatusJanela;
	@ElementCollection
	private List<MedicaoStatusCondicionadorAr> medicoesStatusCondicionadorAr;

	public Ambiente() {
		this.medicoesTemperatura = new HashSet<MedicaoTemperatura>();
		this.medicoesStatusPresenca = new ArrayList<MedicaoStatusPresenca>();
		this.medicoesStatusJanela = new ArrayList<MedicaoStatusJanela>();
		this.medicoesStatusCondicionadorAr = new ArrayList<MedicaoStatusCondicionadorAr>();
	}

	public Ambiente(int numero, Dispositivo dispositivo) {
		this.setNumero(numero);
		this.setNome("Ambiente " + numero);
		this.setTemperaturaAlerta(30);

		this.dispositivo = dispositivo;
		this.dispositivo.addAmbiente(this);

		this.medicoesTemperatura = new HashSet<MedicaoTemperatura>();
		this.medicoesStatusPresenca = new ArrayList<MedicaoStatusPresenca>();
		this.medicoesStatusJanela = new ArrayList<MedicaoStatusJanela>();
		this.medicoesStatusCondicionadorAr = new ArrayList<MedicaoStatusCondicionadorAr>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getTemperaturaAlerta() {
		return temperaturaAlerta;
	}

	public String getTemperaturaStr() {
		return temperatura + " °C";
	}

	public void setTemperaturaAlerta(int temperaturaAlerta) {
		this.temperaturaAlerta = temperaturaAlerta;
	}

	public int getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(int temperatura) {
		this.temperatura = temperatura;
//		MedicaoTemperatura medicao = new MedicaoTemperatura(String.valueOf(temperatura), new Date());
//		medicoesTemperatura.add(medicao);
	}

	public boolean temPresenca() {
		return statusPresenca;
	}

	public String getStatusPresencaStr() {
		if (statusPresenca) {
			return "Sim";
		} else {
			return "Não";
		}
	}

	public void setStatusPresenca(boolean statusPresenca) {
		this.statusPresenca = statusPresenca;
	}

	public boolean isJanelaAberta() {
		return statusJanela;
	}

	public String getStatusJanelaStr() {
		if (statusJanela) {
			return "Aberta";
		} else {
			return "Fechada";
		}
	}

	public void setStatusJanela(boolean statusJanela) {
		this.statusJanela = statusJanela;
	}

	public boolean isCondicionadorArLigado() {
		return statusCondicionadorAr;
	}

	public String getStatusCondicionadorArStr() {
		if (statusCondicionadorAr) {
			return "Ligado";
		} else {
			return "Desligado";
		}
	}

	public void setStatusCondicionadorAr(boolean statusCondicionadorAr) {
		this.statusCondicionadorAr = statusCondicionadorAr;
	}

	public Dispositivo getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

	public Set<MedicaoTemperatura> getMedicoesTemperatura() {
		return medicoesTemperatura;
	}

	public void setMedicoesTemperatura(Set<MedicaoTemperatura> medicoesTemperatura) {
		this.medicoesTemperatura = medicoesTemperatura;
	}

	public List<MedicaoStatusPresenca> getMedicoesStatusPresenca() {
		return medicoesStatusPresenca;
	}

	public void setMedicoesStatusPresenca(List<MedicaoStatusPresenca> medicoesStatusPresenca) {
		this.medicoesStatusPresenca = medicoesStatusPresenca;
	}

	public List<MedicaoStatusJanela> getMedicoesStatusJanela() {
		return medicoesStatusJanela;
	}

	public void setMedicoesStatusJanela(List<MedicaoStatusJanela> medicoesStatusJanela) {
		this.medicoesStatusJanela = medicoesStatusJanela;
	}

	public List<MedicaoStatusCondicionadorAr> getMedicoesStatusCondicionadorAr() {
		return medicoesStatusCondicionadorAr;
	}

	public void setMedicoesStatusCondicionadorAr(List<MedicaoStatusCondicionadorAr> medicoesStatusCondicionadorAr) {
		this.medicoesStatusCondicionadorAr = medicoesStatusCondicionadorAr;
	}

	public boolean getExibirDados() {
		switch (dispositivo.getTipo()) {
		case MonitoracaoEControleTotal: {
			return true;
		}
		case MonitoracaoTotal: {
			return true;
		}
		default: {
			return false;
		}
		}
	}

	public boolean getExibirBotaoLigar() {
		switch (dispositivo.getTipo()) {
		case MonitoracaoEControleTotal: {
			if (!isCondicionadorArLigado()) {
				return true;
			}
		}
		default: {
			return false;
		}
		}
	}

	public boolean getExibirBotaoDesligar() {
		switch (dispositivo.getTipo()) {
		case MonitoracaoEControleTotal: {
			if (isCondicionadorArLigado()) {
				return true;
			}
		}
		default: {
			return false;
		}
		}
	}

	@Embeddable
	public static class MedicaoTemperatura implements Serializable {
		private static final long serialVersionUID = -5060669251371538406L;

		private String temperatura;
		private Date dataMedicao;
		private Long dataLong;

		public MedicaoTemperatura() {

		}

		public MedicaoTemperatura(String temperatura, Date dataMedicao) {
			this.setTemperatura(temperatura);
			this.setDataMedicao(dataMedicao);
			this.setDataLong(dataMedicao.getTime());
		}

		public String getTemperatura() {
			return temperatura;
		}

		public void setTemperatura(String temperatura) {
			this.temperatura = temperatura;
		}

		public Date getDataMedicao() {
			return dataMedicao;
		}

		public void setDataMedicao(Date dataMedicao) {
			this.dataMedicao = dataMedicao;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dataMedicao == null) ? 0 : dataMedicao.hashCode());
			result = prime * result + ((temperatura == null) ? 0 : temperatura.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof MedicaoTemperatura)) {
				return false;
			}
			MedicaoTemperatura other = (MedicaoTemperatura) obj;
			if (dataMedicao == null) {
				if (other.dataMedicao != null) {
					return false;
				}
			} else if (!dataMedicao.equals(other.dataMedicao)) {
				return false;
			}
			if (temperatura == null) {
				if (other.temperatura != null) {
					return false;
				}
			} else if (!temperatura.equals(other.temperatura)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "MedicaoTemperatura [temperatura=" + temperatura + ", dataMedicao=" + dataMedicao + "]";
		}

		public Long getDataLong() {
			return dataLong;
		}

		public void setDataLong(Long dataLong) {
			this.dataLong = dataLong;
		}
	}

	@Embeddable
	public static class MedicaoStatusPresenca implements Serializable {
		private static final long serialVersionUID = -5212067557727116435L;

		private String statusPresenca;
		private Date dataMedicao;

		public MedicaoStatusPresenca() {

		}

		public String getStatusPresenca() {
			return statusPresenca;
		}

		public void setStatusPresenca(String statusPresenca) {
			this.statusPresenca = statusPresenca;
		}

		public Date getDataMedicao() {
			return dataMedicao;
		}

		public void setDataMedicao(Date dataMedicao) {
			this.dataMedicao = dataMedicao;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dataMedicao == null) ? 0 : dataMedicao.hashCode());
			result = prime * result + ((statusPresenca == null) ? 0 : statusPresenca.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof MedicaoStatusPresenca)) {
				return false;
			}
			MedicaoStatusPresenca other = (MedicaoStatusPresenca) obj;
			if (dataMedicao == null) {
				if (other.dataMedicao != null) {
					return false;
				}
			} else if (!dataMedicao.equals(other.dataMedicao)) {
				return false;
			}
			if (statusPresenca == null) {
				if (other.statusPresenca != null) {
					return false;
				}
			} else if (!statusPresenca.equals(other.statusPresenca)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "MedicaoStatusPresenca [statusPresenca=" + statusPresenca + ", dataMedicao=" + dataMedicao + "]";
		}

	}

	@Embeddable
	public static class MedicaoStatusJanela implements Serializable {
		private static final long serialVersionUID = 4029825811039914528L;

		private String statusJanela;
		private Date dataMedicao;

		public MedicaoStatusJanela() {

		}

		public String getStatusJanela() {
			return statusJanela;
		}

		public void setStatusJanela(String statusJanela) {
			this.statusJanela = statusJanela;
		}

		public Date getDataMedicao() {
			return dataMedicao;
		}

		public void setDataMedicao(Date dataMedicao) {
			this.dataMedicao = dataMedicao;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dataMedicao == null) ? 0 : dataMedicao.hashCode());
			result = prime * result + ((statusJanela == null) ? 0 : statusJanela.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof MedicaoStatusJanela)) {
				return false;
			}
			MedicaoStatusJanela other = (MedicaoStatusJanela) obj;
			if (dataMedicao == null) {
				if (other.dataMedicao != null) {
					return false;
				}
			} else if (!dataMedicao.equals(other.dataMedicao)) {
				return false;
			}
			if (statusJanela == null) {
				if (other.statusJanela != null) {
					return false;
				}
			} else if (!statusJanela.equals(other.statusJanela)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "MedicaoStatusJanela [statusJanela=" + statusJanela + ", dataMedicao=" + dataMedicao + "]";
		}
	}

	@Embeddable
	public static class MedicaoStatusCondicionadorAr implements Serializable {
		private static final long serialVersionUID = 1216805328677440075L;

		private String statusCondicionadorAr;
		private Date dataMedicao;

		public MedicaoStatusCondicionadorAr() {

		}

		public String getStatusCondicionadorAr() {
			return statusCondicionadorAr;
		}

		public void setStatusCondicionadorAr(String statusCondicionadorAr) {
			this.statusCondicionadorAr = statusCondicionadorAr;
		}

		public Date getDataMedicao() {
			return dataMedicao;
		}

		public void setDataMedicao(Date dataMedicao) {
			this.dataMedicao = dataMedicao;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dataMedicao == null) ? 0 : dataMedicao.hashCode());
			result = prime * result + ((statusCondicionadorAr == null) ? 0 : statusCondicionadorAr.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof MedicaoStatusCondicionadorAr)) {
				return false;
			}
			MedicaoStatusCondicionadorAr other = (MedicaoStatusCondicionadorAr) obj;
			if (dataMedicao == null) {
				if (other.dataMedicao != null) {
					return false;
				}
			} else if (!dataMedicao.equals(other.dataMedicao)) {
				return false;
			}
			if (statusCondicionadorAr == null) {
				if (other.statusCondicionadorAr != null) {
					return false;
				}
			} else if (!statusCondicionadorAr.equals(other.statusCondicionadorAr)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "MedicaoStatusCondicionadorAr [statusCondicionadorAr=" + statusCondicionadorAr + ", dataMedicao="
					+ dataMedicao + "]";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dispositivo == null) ? 0 : dispositivo.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((medicoesStatusCondicionadorAr == null) ? 0 : medicoesStatusCondicionadorAr.hashCode());
		result = prime * result + ((medicoesStatusJanela == null) ? 0 : medicoesStatusJanela.hashCode());
		result = prime * result + ((medicoesStatusPresenca == null) ? 0 : medicoesStatusPresenca.hashCode());
		result = prime * result + ((medicoesTemperatura == null) ? 0 : medicoesTemperatura.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + numero;
		result = prime * result + (statusCondicionadorAr ? 1231 : 1237);
		result = prime * result + (statusJanela ? 1231 : 1237);
		result = prime * result + (statusPresenca ? 1231 : 1237);
		result = prime * result + temperatura;
		result = prime * result + temperaturaAlerta;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ambiente other = (Ambiente) obj;
		if (dispositivo == null) {
			if (other.dispositivo != null)
				return false;
		} else if (!dispositivo.equals(other.dispositivo))
			return false;
		if (id != other.id)
			return false;
		if (medicoesStatusCondicionadorAr == null) {
			if (other.medicoesStatusCondicionadorAr != null)
				return false;
		} else if (!medicoesStatusCondicionadorAr.equals(other.medicoesStatusCondicionadorAr))
			return false;
		if (medicoesStatusJanela == null) {
			if (other.medicoesStatusJanela != null)
				return false;
		} else if (!medicoesStatusJanela.equals(other.medicoesStatusJanela))
			return false;
		if (medicoesStatusPresenca == null) {
			if (other.medicoesStatusPresenca != null)
				return false;
		} else if (!medicoesStatusPresenca.equals(other.medicoesStatusPresenca))
			return false;
		if (medicoesTemperatura == null) {
			if (other.medicoesTemperatura != null)
				return false;
		} else if (!medicoesTemperatura.equals(other.medicoesTemperatura))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numero != other.numero)
			return false;
		if (statusCondicionadorAr != other.statusCondicionadorAr)
			return false;
		if (statusJanela != other.statusJanela)
			return false;
		if (statusPresenca != other.statusPresenca)
			return false;
		if (temperatura != other.temperatura)
			return false;
		if (temperaturaAlerta != other.temperaturaAlerta)
			return false;
		return true;
	}

	@Override
	public String toString() {
		// return "Ambiente [id=" + getId() + ", nome=" + nome + ",
		// temperaturaAlerta=" + temperaturaAlerta + ", temperatura="
		// + temperatura + ", statusPresenca=" + statusPresenca + ",
		// statusJanela="
		// + statusJanela + ", statusCondicionadorAr=" + statusCondicionadorAr +
		// ", dispositivo=" + dispositivo
		// + ", medicoesTemperatura=" + medicoesTemperatura + ",
		// medicoesStatusPresenca=" + medicoesStatusPresenca
		// + ", medicoesStatusJanela=" + medicoesStatusJanela + ",
		// medicoesStatusCondicionadorAr="
		// + medicoesStatusCondicionadorAr + "]";
		return nome;
	}
}
