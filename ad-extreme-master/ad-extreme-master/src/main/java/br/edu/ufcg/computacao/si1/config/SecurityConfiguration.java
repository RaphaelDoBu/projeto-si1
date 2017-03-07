package br.edu.ufcg.computacao.si1.config;

import br.edu.ufcg.computacao.si1.model.Usuario;
import br.edu.ufcg.computacao.si1.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                    .antMatchers("/","/cadastrar-se").permitAll()
                    .antMatchers("/user/**").hasAuthority("USER")
                    .antMatchers("/company/**").hasAuthority("COMPANY")
                    .anyRequest().fullyAuthenticated()
                .and()
            .formLogin()
                    .loginPage("/login").permitAll()
                    .successHandler(new CustomAuthenticationSuccessHandler())
                    .failureUrl("/login?error")
                .and()
            .logout()
                    .logoutUrl("/logout")
                    .deleteCookies("remember-me","JSESSIONID")
                    .logoutSuccessUrl("/login").permitAll()
                .and()
                    .rememberMe();
    }

    /**
     * TODO colocar para o login ser feito por dados consultados no banco de dados
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("company").password("password").roles("COMPANY");


        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select email as username,senha as password, true as enabled from tb_usuario where email=?")
                .authoritiesByUsernameQuery(
                        "select email as username, role from tb_usuario where email=?");
    }

//    @Autowired
//    public final void configAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .userDetailsService(userDetailsService())
//            .passwordEncoder(new BasicEncoder());
//    }
//
    @Bean
    protected UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Autowired
            UsuarioService usuarioService;

            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                Usuario usuario = usuarioService.getByEmail(email).get();
                if(usuario != null){
                    return new User(usuario.getEmail(), usuario.getSenha(), true, true, true, true,
                            AuthorityUtils.createAuthorityList(usuario.getR()));
                }else {
                    throw new UsernameNotFoundException("Não foi possível localizar o usuário" + usuario);
                }
            }
        };
    }
}
