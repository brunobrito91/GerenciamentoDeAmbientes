package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.IpDAO;
import com.model.Ip;

@Stateless
public class IpFacadeImpl implements IpFacade {

	@EJB
	private IpDAO ipDAO;

	@Override
	public void save(Ip ip) {
		ipDAO.save(ip);
	}

	@Override
	public Ip update(Ip ip) {
		return ipDAO.update(ip);
	}

	@Override
	public void delete(Ip ip) {
		ipDAO.delete(ip);
	}

	@Override
	public Ip find(int entityID) {
		return ipDAO.find(entityID);
	}

	@Override
	public List<Ip> findAll() {
		return ipDAO.findAll();
	}

}
