package com.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.util.ETipoDispositivo;

@Entity
@NamedQueries({ @NamedQuery(name = "buscarTodosDispositivos", query = "SELECT d FROM Dispositivo d") })
public class Dispositivo implements Serializable {
	private static final long serialVersionUID = -2132878628934026083L;

	@Id
	private String ip;
	private ETipoDispositivo tipo;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dispositivo")
	private List<Ambiente> ambientes;

	public Dispositivo() {
		this.ambientes = new ArrayList<Ambiente>();
	}

	public Dispositivo(String ip) {
		this.ip = ip;
		this.ambientes = new ArrayList<Ambiente>();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public ETipoDispositivo getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		switch (tipo) {
		case 0:
			this.tipo = ETipoDispositivo.MonitoracaoSimples;
			break;
		case 1:
			this.tipo = ETipoDispositivo.MonitoracaoTotal;
			break;
		case 2:
			this.tipo = ETipoDispositivo.MonitoracaoEControleTotal;
			break;

		default:
			this.tipo = ETipoDispositivo.Nenhum;
			break;
		}
	}

	public List<Ambiente> getAmbientes() {
		return ambientes;
	}

	public void setAmbientes(List<Ambiente> ambientes) {
		this.ambientes = ambientes;
	}

	public void addAmbiente(Ambiente ambiente) {
		this.ambientes.add(ambiente);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ambientes == null) ? 0 : ambientes.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Dispositivo other = (Dispositivo) obj;
		if (ambientes == null) {
			if (other.ambientes != null)
				return false;
		} else if (!ambientes.equals(other.ambientes))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Dispositivo [ip=" + ip + ", ambientes=" + ambientes + "]";
	}
}
