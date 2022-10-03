package io.github.animeshxd.springsecurity;

// import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    // private final DataSource dataSource;
    private final JdbcTemplate jdbc;
    


    @Autowired
    public SecurityConfiguration(JdbcTemplate jdbc) {
        // this.dataSource = dataSource;
        this.jdbc = jdbc;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        String sql = """
            create table IF NOT EXISTS users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
            create table IF NOT EXISTS authorities (username varchar(50) not null unique,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
            create unique index IF NOT EXISTS ix_auth_username on authorities (username,authority);
            """;
    
        jdbc.execute(sql);
        sql = """
            INSERT INTO users (username, password, enabled) VALUES (?,?,TRUE) ON CONFLICT(username) DO NOTHING ;
            INSERT INTO authorities (username, authority) VALUES (?,?) ON CONFLICT(username) DO NOTHING;
            """;
        jdbc.update(sql,"admin", passwordEncoder().encode("admin"), "admin", "ROLE_ADMIN");
        jdbc.update(sql,"user", passwordEncoder().encode("user"), "user", "ROLE_USER");

        return new JdbcUserDetailsManager(jdbc.getDataSource());
    }

    @Bean
    public SecurityFilterChain securityFIlterChain(HttpSecurity http) throws Exception{
        
        http
            .authorizeRequests(autz -> autz
                .antMatchers("/").permitAll()
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin").hasRole("ADMIN")
                        
            )
            .formLogin()
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
