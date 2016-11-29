package com.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.AmbienteDAO;
import com.model.Ambiente;
import com.model.Ambiente.MedicaoTemperatura;

@Stateless
public class AmbienteFacadeImpl implements AmbienteFacade {

	@EJB
	private AmbienteDAO ambienteDao;

	@Override
	public void save(Ambiente ambiente) {
		System.out.println("Salvando ambiente " + ambiente.getNome());
		ambienteDao.save(ambiente);
	}

	@Override
	public Ambiente update(Ambiente ambiente) {
		System.out.println("Atualizando ambiente " + ambiente.getNome());
		return ambienteDao.update(ambiente);
	}

	@Override
	public void delete(Ambiente ambiente) {
		System.out.println("Deletando ambiente " + ambiente.getNome());
		ambienteDao.delete(ambiente);
	}

	@Override
	public Ambiente find(int entityID) {
		System.out.println("Procurando ambiente de id " + entityID);
		Ambiente ambiente = ambienteDao.find(entityID);
//		System.out.println("Hibernate.initialize");
//		System.out.println("Tamanho Antes : " + ambiente.getMedicoesTemperatura().size());
//		Hibernate.initialize(ambiente.getMedicoesTemperatura());
//		System.out.println("Tamanho Depois : " + ambiente.getMedicoesTemperatura().size());

		return ambiente;
	}

	@Override
	public List<Ambiente> findAll() {
		System.out.println("Procurando por todos os ambientes");
		return ambienteDao.findAll();
	}
	
	@Override
	public List<Ambiente> findAllAtivos() {
		System.out.println("Procurando por todos os ambientes ativos");
		return ambienteDao.findAllAtivos();
	}

	public List<MedicaoTemperatura> findMedicoesByDate(int id, Date data) {
		System.out.println("Procurando por todas as medições de temperatura para o Ambiente de id " + id);
		return ambienteDao.findMedicoesByDate(id, data);
	}

	@Override
	public void saveMedicao(int id, MedicaoTemperatura medicao) {
		System.out.println("Salvando medição de temperatura para o Ambiente de id " + id);
		ambienteDao.saveMedicao(id, medicao);
	}

	@Override
	public void saveEmail(int id, String email) {
		System.out.println("Salvando email para o Ambiente de id " + id);
		ambienteDao.saveEmail(id, email);
	}

	@Override
	public void deleteEmail(int id, String email) {
		System.out.println("Deletando email para o Ambiente de id " + id);
		ambienteDao.deleteEmail(id, email);
	}

}
