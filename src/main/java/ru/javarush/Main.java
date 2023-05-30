package ru.javarush;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import ru.javarush.dao.*;
import ru.javarush.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Main {

    private SessionFactory sessionFactory;
    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;

    public Main() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "qwerty");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.FORMAT_SQL, "true");
        properties.put(Environment.HIGHLIGHT_SQL, "true");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(properties)
                .buildSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }

    public static void main(String[] args) {
        Main main = new Main();

        Customer customer = main.createCustomer();

        main.customerReturnInventoryToStore();

        main.customerRentInventory(customer);

        main.newFilmWasMode();
    }

    private void newFilmWasMode() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Language language = languageDAO.getItems(0, 20).stream().unordered().findAny().get();
            Set<Category> categories = new HashSet<>(categoryDAO.getItems(0, 5));
            Set<Actor> actors = new HashSet<>(actorDAO.getItems(0, 20));
            Film film = new Film();
            film.setActors(actors);
            film.setCategories(categories);
            film.setLanguage(language);
            film.setRating(Rating.NC17);
            film.setSpecialFeatures(Set.of(Feature.TRAILERS, Feature.COMMENTARIES));
            film.setLength((short) 120);
            film.setReplacementCost(BigDecimal.TEN);
            film.setRentalRate(BigDecimal.ZERO);
            film.setDescription("new scary film");
            film.setTitle("scary my-movie");
            film.setRentalDuration((byte) 25);
            film.setOriginalLanguage(language);
            film.setYear(Year.now());
            filmDAO.save(film);
            FilmText filmText = new FilmText();
            filmText.setFilm(film);
            filmText.setId(film.getId());
            filmText.setDescription("new scary film");
            filmText.setTitle("scary my-movie");
            filmTextDAO.save(filmText);
            transaction.commit();
        }
    }

    private void customerRentInventory(Customer customer) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Film film = filmDAO.getFirstAvailableFilmForRent();
            Inventory inventory = new Inventory();
            Store store = storeDAO.getItems(0, 1).get(0);
            inventory.setFilm(film);
            inventory.setStore(store);
            inventoryDAO.save(inventory);
            Staff staff = store.getStaff();
            Rental rental = new Rental();
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setRentalDate(LocalDateTime.now());
            rental.setStaff(staff);
            rentalDAO.save(rental);
            Payment payment = new Payment();
            payment.setCustomer(customer);
            payment.setStaff(staff);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setRental(rental);
            payment.setAmount(BigDecimal.valueOf(33.77));
            paymentDAO.save(payment);
            transaction.commit();
        }
    }

    private Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.getTransaction().begin();
            Store store = storeDAO.getItems(0, 1).get(0);
            City city = cityDAO.getByName("Abha");
            Address address = new Address();
            address.setAddress("1-th street, 22");
            address.setPhone("111-222-333");
            address.setCity(city);
            address.setDistrict("district-1");
            addressDAO.save(address);
            Customer customer = new Customer();
            customer.setStore(store);
            customer.setActive(true);
            customer.setEmail("test@.com");
            customer.setAddress(address);
            customer.setFirstName("Tom");
            customer.setLastName("Koshkin");
            customerDAO.save(customer);
            session.getTransaction().commit();
            return customer;
        }
    }

    private void customerReturnInventoryToStore() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Rental rental = rentalDAO.getAnyUnreturnedRental();
            rental.setReturnDate(LocalDateTime.now());
            rentalDAO.save(rental);
            transaction.commit();
        }
    }
}
