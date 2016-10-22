package com.facade;

import java.util.List;

import javax.ejb.Remote;

import com.model.Usuario;

@Remote
public interface UsuarioFacade {
	public abstract void save(Usuario usuario);

	public abstract Usuario update(Usuario usuario);

	public abstract void delete(Usuario usuario);

	public abstract Usuario find(int entityID);

	public abstract List<Usuario> findAll();
}
