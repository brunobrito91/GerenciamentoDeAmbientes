package com.dao;

import javax.ejb.Stateless;

import com.model.Email;

@Stateless
public class EmailDAO extends GenericDAOImpl<Email> {
	public EmailDAO() {
		super(Email.class);
	}

	public void delete(Email email) {
		super.delete(email.getId(), Email.class);
	}
}
