package org.springboot.app;

import org.springboot.app.auth.handler.LoginSuccessHandler;
import org.springboot.app.models.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginSuccessHandler successHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JpaUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
				/* .antMatchers("/ver/**").hasAnyRole("USER") */
				/* .antMatchers("/uploads/**").hasAnyRole("USER") */
				/* .antMatchers("/form/**").hasAnyRole("ADMIN") */
				/* .antMatchers("/eliminar/**").hasAnyRole("ADMIN") */
				/* .antMatchers("/factura/**").hasAnyRole("ADMIN") */
				.anyRequest().authenticated().and().formLogin().successHandler(successHandler).loginPage("/login")
				.permitAll().and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/error_403");

	}

	/**
	 * Autenticación basada en JPA
	 */
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

}
