package ru.javarush.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.javarush.domain.Rental;

public class RentalDAO extends GenericDAO<Rental> {
    public RentalDAO(SessionFactory sessionFactory) {
        super(Rental.class, sessionFactory);
    }

    public Rental getAnyUnreturnedRental() {
        String hql = "select r from Rental r where r.returnDate is null";
        Query<Rental> query = getCurrentSession().createQuery(hql, Rental.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
}
