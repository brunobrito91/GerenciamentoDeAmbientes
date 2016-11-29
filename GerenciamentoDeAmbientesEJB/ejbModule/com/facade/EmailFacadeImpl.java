package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.EmailDAO;
import com.model.Email;

@Stateless
public class EmailFacadeImpl implements EmailFacade {

	@EJB
	private EmailDAO emailDAO;

	@Override
	public void save(Email email) {
		emailDAO.save(email);
	}

	@Override
	public Email update(Email email) {
		return emailDAO.update(email);
	}

	@Override
	public void delete(Email email) {
		emailDAO.delete(email);
	}

	@Override
	public Email find(int entityID) {
		return emailDAO.find(entityID);
	}

	@Override
	public List<Email> findAll() {
		return emailDAO.findAll();
	}

}
