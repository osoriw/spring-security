package org.springboot.app.models.dao;

import org.springboot.app.models.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<User, Long> {

	public User findByUserName(String userName);

}
