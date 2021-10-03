package org.springboot.app;

import javax.sql.DataSource;

import org.springboot.app.auth.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private DataSource dataSource;	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
		/*.antMatchers("/ver/**").hasAnyRole("USER")*/
		/*.antMatchers("/uploads/**").hasAnyRole("USER")*/
		/*.antMatchers("/form/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/factura/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
		.and()
		    .formLogin()
		        .successHandler(successHandler)
		        .loginPage("/login")
		    .permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");

	}

	/**
	 * Autenticación basada en JDBC
	 */
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {

		build.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
				.usersByUsernameQuery("select username, password, active from users where username=?")
				.authoritiesByUsernameQuery(
						"select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");

	}

	/**
	 * Autenticación basada en memoria
	 */
	/*@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		PasswordEncoder encoder = this.passwordEncoder;
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);

		build.inMemoryAuthentication().withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
				.withUser(users.username("andres").password("12345").roles("USER"));

	}*/

	
	
}
