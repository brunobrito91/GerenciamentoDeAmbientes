package com.facade;

import java.util.List;

import javax.ejb.Remote;

import com.model.Dispositivo;

@Remote
public interface DispositivoFacade {

	public abstract void save(Dispositivo dispositivo);

	public abstract Dispositivo update(Dispositivo dispositivo);

	public abstract void delete(Dispositivo dispositivo);

	public abstract Dispositivo find(Object entityID);

	public abstract List<Dispositivo> findAll();
}
