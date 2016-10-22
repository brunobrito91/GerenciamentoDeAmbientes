package com.dao;

import javax.ejb.Stateless;

import com.model.Usuario;

@Stateless
public class UsuarioDAO extends GenericDAOImpl<Usuario> {
	public UsuarioDAO() {
		super(Usuario.class);
		System.out.println("UsuarioDAO");
	}

	public void delete(Usuario usuario) {
		super.delete(usuario.getId(), Usuario.class);
		System.out.println("UsuarioDAO - delete");
	}
}
