package com.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dao.UsuarioDAO;
import com.model.Usuario;

@Stateless
public class UsuarioFacadeImpl implements UsuarioFacade {

	@EJB
	private UsuarioDAO usuarioDao;

	@Override
	public void save(Usuario usuario) {
		System.out.println("Salvando usuario " + usuario.getNome());
		usuarioDao.save(usuario);
	}

	@Override
	public Usuario update(Usuario usuario) {
		System.out.println("Atualizando usuario " + usuario.getNome());
		return usuarioDao.update(usuario);
	}

	@Override
	public void delete(Usuario usuario) {
		System.out.println("Deletando usuario " + usuario.getNome());
		usuarioDao.delete(usuario);
	}

	@Override
	public Usuario find(int entityID) {
		System.out.println("Procurando usuario de id " + entityID);
		return usuarioDao.find(entityID);
	}

	@Override
	public List<Usuario> findAll() {
		System.out.println("Procurando por todos os usuarios ");
		return usuarioDao.findAll();
	}

}
