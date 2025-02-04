package org.example.stavkova_kancelaria_web;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.example.stavkova_kancelaria_web.sportevent.SportEventDAO;
import org.example.stavkova_kancelaria_web.user.UserDAO;
import org.example.stavkova_kancelaria_web.ticket.TicketDAO;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

@Configuration
public class Config {

    @Value("${spring.datasource.url}")
    private String dbJdbc;

    @Bean
    public SportEventDAO sportEventDAO() {
        return new SportEventDAO(dslContext());
    }

    @Bean
    public UserDAO userDAO() {
        return new UserDAO(dslContext());
    }
    @Bean
    public TicketDAO ticketDao() {
        return new TicketDAO(dslContext());
    }

    @Bean
    public DSLContext dslContext() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(dbJdbc);
        return DSL.using(dataSource, SQLDialect.SQLITE);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173"); // Allow your frontend origin
        config.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, etc.)
        config.addAllowedHeader("*"); // Allow all headers
        config.setAllowCredentials(true); // Allow cookies or credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply to all endpoints
        return new CorsFilter(source);
    }
}
