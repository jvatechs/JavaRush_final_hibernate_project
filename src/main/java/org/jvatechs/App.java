package org.jvatechs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.jvatechs.dao.CityDAO;
import org.jvatechs.dao.CountryDAO;
import org.jvatechs.domain.City;
import org.jvatechs.domain.Country;
import org.jvatechs.domain.CountryLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.util.Objects.nonNull;


public class App {
    private final SessionFactory sessionFactory;
    private final RedisClient redisClient;
    private final ObjectMapper mapper;

    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;

    public static void main(String[] args) {
        App app = new App();
        List<City> allCities = app.fetchData(app);
        app.shutdown();
    }
    public App() {
        sessionFactory = prepareRelationalDb();
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);

        redisClient = prepareRedisClient();
        mapper = new ObjectMapper();
    }

    private RedisClient prepareRedisClient() {
        return null;
    }

    private SessionFactory prepareRelationalDb() {
        final SessionFactory sessionFactory;
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/world");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");
        properties.put(Environment.STATEMENT_BATCH_SIZE, "100");

        sessionFactory = new Configuration()
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(CountryLanguage.class)
                .addProperties(properties)
                .buildSessionFactory();
        return sessionFactory;
    }

    private void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }

    private List<City> fetchData(App app) {
        try (Session session = app.sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();
            List<Country> countries = app.countryDAO.getAll();


            int totalCount = app.cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(app.cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }
}
