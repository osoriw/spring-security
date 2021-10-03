package org.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springboot.app.models.dao.IUsuarioDao;
import org.springboot.app.models.entity.Role;
import org.springboot.app.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jpaUserDetailService")
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private IUsuarioDao usuarioDao;

	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = usuarioDao.findByUserName(username);

		if (user == null) {
			logger.error("El usuario: " + username + ", no existe.");
			throw new UsernameNotFoundException("El usuario: " + username + ", no existe.");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role : user.getRoles()) {
			logger.info("Role: ".concat(role.getAuthority()));
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		if (authorities.isEmpty()) {
			logger.error("El usuario: " + username + ", no tiene roles asignados.");
			throw new UsernameNotFoundException("El usuario: " + username + ", no tiene roles asignados.");
		}

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), true,
				true, true, true, authorities);
	}

}
