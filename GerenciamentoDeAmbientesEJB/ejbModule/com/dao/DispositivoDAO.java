package com.dao;

import javax.ejb.Stateless;

import com.model.Dispositivo;

@Stateless
public class DispositivoDAO extends GenericDAOImpl<Dispositivo> {
	public DispositivoDAO() {
		super(Dispositivo.class);
		System.out.println("DispositivoDAO");
	}

	public void delete(Dispositivo dispositivo) {
		System.out.println("DispositivoDAO - delete");
		super.delete(dispositivo.getIp(), Dispositivo.class);
	}
}
