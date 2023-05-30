package ru.javarush.dao;

import org.hibernate.SessionFactory;
import ru.javarush.domain.Inventory;

public class InventoryDAO extends GenericDAO<Inventory> {
    public InventoryDAO(SessionFactory sessionFactory) {
        super(Inventory.class, sessionFactory);
    }
}
