package ru.javarush.dao;

import org.hibernate.SessionFactory;
import ru.javarush.domain.Actor;

public class ActorDAO extends GenericDAO<Actor> {
    public ActorDAO(SessionFactory sessionFactory) {
        super(Actor.class, sessionFactory);
    }
}
