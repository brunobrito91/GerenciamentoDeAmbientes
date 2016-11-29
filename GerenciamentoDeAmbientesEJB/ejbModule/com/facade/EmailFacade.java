package com.facade;

import java.util.List;

import javax.ejb.Remote;

import com.model.Email;

@Remote
public interface EmailFacade {
	public abstract void save(Email email);

	public abstract Email update(Email email);

	public abstract void delete(Email email);

	public abstract Email find(int entityID);

	public abstract List<Email> findAll();
}
