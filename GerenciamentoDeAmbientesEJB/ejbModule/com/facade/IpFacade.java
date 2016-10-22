package com.facade;

import java.util.List;

import javax.ejb.Remote;

import com.model.Ip;

@Remote
public interface IpFacade {
	public abstract void save(Ip ip);

	public abstract Ip update(Ip ip);

	public abstract void delete(Ip ip);

	public abstract Ip find(int entityID);

	public abstract List<Ip> findAll();
}
