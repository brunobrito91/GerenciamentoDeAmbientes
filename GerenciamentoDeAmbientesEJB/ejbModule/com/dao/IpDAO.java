package com.dao;

import javax.ejb.Stateless;

import com.model.Ip;

@Stateless
public class IpDAO extends GenericDAOImpl<Ip> {
	public IpDAO() {
		super(Ip.class);
	}

	public void delete(Ip ip) {
		super.delete(ip.getId(), Ip.class);
	}
}
