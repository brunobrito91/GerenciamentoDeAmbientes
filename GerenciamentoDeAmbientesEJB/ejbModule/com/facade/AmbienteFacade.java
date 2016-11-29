package com.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import com.model.Ambiente;
import com.model.Ambiente.MedicaoTemperatura;

@Remote
public interface AmbienteFacade {

	public abstract void save(Ambiente ambiente);

	public abstract Ambiente update(Ambiente ambiente);

	public abstract void delete(Ambiente ambiente);

	public abstract Ambiente find(int entityID);

	public abstract List<Ambiente> findAll();
	
	public abstract List<Ambiente> findAllAtivos();

	public List<MedicaoTemperatura> findMedicoesByDate(int id, Date data);

	public abstract void saveMedicao(int id, MedicaoTemperatura medicao);
	
	public abstract void saveEmail(int id, String email);

	public abstract void deleteEmail(int id, String email);
}