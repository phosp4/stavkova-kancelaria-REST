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
}
