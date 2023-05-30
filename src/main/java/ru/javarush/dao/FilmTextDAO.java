package ru.javarush.dao;

import org.hibernate.SessionFactory;
import ru.javarush.domain.FilmText;

public class FilmTextDAO extends GenericDAO<FilmText> {
    public FilmTextDAO(SessionFactory sessionFactory) {
        super(FilmText.class, sessionFactory);
    }
}
