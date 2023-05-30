package ru.javarush.dao;

import org.hibernate.SessionFactory;
import ru.javarush.domain.Staff;

public class StaffDAO extends GenericDAO<Staff> {
    public StaffDAO(SessionFactory sessionFactory) {
        super(Staff.class, sessionFactory);
    }
}
