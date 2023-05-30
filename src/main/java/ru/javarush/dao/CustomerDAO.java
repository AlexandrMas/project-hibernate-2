package ru.javarush.dao;

import org.hibernate.SessionFactory;
import ru.javarush.domain.Customer;

public class CustomerDAO extends GenericDAO<Customer> {
    public CustomerDAO(SessionFactory sessionFactory) {
        super(Customer.class, sessionFactory);
    }
}
