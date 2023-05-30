package ru.javarush.dao;

import org.hibernate.SessionFactory;
import ru.javarush.domain.Category;

public class CategoryDAO extends GenericDAO<Category> {
    public CategoryDAO(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }
}
