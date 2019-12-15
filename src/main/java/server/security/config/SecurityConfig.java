package server.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import server.security.authentication.StatelessAuthFilter;
import server.security.service.SecurityUserDetailsService;
import server.security.service.TokenAuthService;


@Configuration
@EnableWebSecurity
@ComponentScan("server.security.service")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new StatelessAuthFilter(tokenAuthService), UsernamePasswordAuthenticationFilter.class)
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/gettoken").permitAll()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(securityUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
