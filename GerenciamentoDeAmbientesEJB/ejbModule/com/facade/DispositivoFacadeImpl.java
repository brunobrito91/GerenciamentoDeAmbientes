package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.DispositivoDAO;
import com.model.Dispositivo;

@Stateless
public class DispositivoFacadeImpl implements DispositivoFacade {

	@EJB
	private DispositivoDAO dispositivoDao;

	@Override
	public void save(Dispositivo dispositivo) {
		System.out.println("Salvando dispositivo " + dispositivo.getIp());
		dispositivoDao.save(dispositivo);
	}

	@Override
	public Dispositivo update(Dispositivo dispositivo) {
		System.out.println("Atualizando dispositivo " + dispositivo.getIp());
		return dispositivoDao.update(dispositivo);
	}

	@Override
	public void delete(Dispositivo dispositivo) {
		System.out.println("Deletando dispositivo " + dispositivo.getIp());
		dispositivoDao.delete(dispositivo);
	}

	@Override
	public Dispositivo find(Object entityID) {
		System.out.println("Procurando dispositivo de id " + entityID);
		return dispositivoDao.find(entityID);
	}

	@Override
	public List<Dispositivo> findAll() {
		System.out.println("Procurando por todos os dispositivos ");
		return dispositivoDao.findAll();
	}

}
