package ru.javarush.dao;

import org.hibernate.SessionFactory;
import ru.javarush.domain.Country;

public class CountryDAO extends GenericDAO<Country> {
    public CountryDAO(SessionFactory sessionFactory) {
        super(Country.class, sessionFactory);
    }
}
