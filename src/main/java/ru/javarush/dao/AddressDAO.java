package ru.javarush.dao;

import org.hibernate.SessionFactory;
import ru.javarush.domain.Address;

public class AddressDAO extends GenericDAO<Address> {
    public AddressDAO(SessionFactory sessionFactory) {
        super(Address.class, sessionFactory);
    }
}
