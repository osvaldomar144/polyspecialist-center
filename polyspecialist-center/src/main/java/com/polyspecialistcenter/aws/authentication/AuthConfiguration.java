package com.polyspecialistcenter.aws.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.polyspecialistcenter.aws.model.Credentials;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired DataSource datasource;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            //authorization paragraph: qui definiamo chi puo accedere a cosa
            .authorizeRequests()
            
            //chiunque (autenticato o no) puo' accedere alle pagine index, login, register, ai css e alle immagini
            .antMatchers(HttpMethod.GET, "/", "/index", "/login", "/register", "/css/**", "/images/**").permitAll()
            
            //chiunque (autenticato o no) puo' mandare richieste POST al punto di accesso
            .antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
            
            .antMatchers(HttpMethod.GET, "/profile/**").hasAnyAuthority(Credentials.GENERIC_USER_ROLE, Credentials.ADMIN_ROLE)
            .antMatchers(HttpMethod.POST, "/profile/**").hasAnyAuthority(Credentials.GENERIC_USER_ROLE, Credentials.ADMIN_ROLE)
            
            //solo gli utenti autenticati con ruolo admin possono accedere a risorse con path /admin/
            .antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
            .antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
            //.anyRequest().authenticated()  //togliendo questo non serve autenticazione per andare sulle altre pagine

            //login paragraph: qui definiamo come è gestita l'autenticazione
            //usiamo il protocollo formlogin
            .and().formLogin()
            
            //la pagina di login si trova a /login
            //NOTA: Spring gestisce il post di login automaticamente
            .loginPage("/login")
            
            //se il login ha successo, si viene rediretti al path /default
            .defaultSuccessUrl("/default")

            
			//google
			.and().oauth2Login()
			.loginPage("/login")
			.defaultSuccessUrl("/")

            //logout paragraph: qui definiamo il logout
            .and().logout()
            
            //il logout è attivato con una richiesta GET a "/logout"
            .logoutUrl("/logout")
            
            //in caso di successo, si viene reindirizzati all'index
            .logoutSuccessUrl("/")
            
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .clearAuthentication(true).permitAll();
    }
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.jdbcAuthentication()  //autenticazione su database
				
				.dataSource(this.datasource)
				
				.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?") //query per ottenere le credenziali per nome utente e ruolo corrispondente
				
				.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");  //query per ottenere username e password
		
	}
	
    /**
     * This method defines a "passwordEncoder" Bean.
     * The passwordEncoder Bean is used to encrypt and decrpyt the Credentials passwords.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	
}
