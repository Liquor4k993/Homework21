package org.skypro.skyshop.service;

import org.skypro.skyshop.model.product.*;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис для хранения товаров и статей в памяти приложения.
 */
@Service
public class StorageService {
    private final Map<UUID, Product> products;
    private final Map<UUID, Article> articles;

    /**
     * Конструктор сервиса хранения.
     * Инициализирует хранилища и заполняет их тестовыми данными.
     */
    public StorageService() {
        this.products = new ConcurrentHashMap<>();
        this.articles = new ConcurrentHashMap<>();
        initializeTestData();
    }

    /**
     * Возвращает коллекцию всех товаров
     * @return коллекция товаров
     */
    public Collection<Product> getAllProducts() {
        return products.values();
    }

    /**
     * Возвращает коллекцию всех статей
     * @return коллекция статей
     */
    public Collection<Article> getAllArticles() {
        return articles.values();
    }

    /**
     * Возвращает коллекцию всех объектов, поддерживающих поиск
     * @return коллекция Searchable объектов
     */
    public Collection<Searchable> getAllSearchables() {
        List<Searchable> searchables = new ArrayList<>();
        searchables.addAll(products.values());
        searchables.addAll(articles.values());
        return searchables;
    }

    /**
     * Получает продукт по ID
     * @param id идентификатор продукта
     * @return Optional с продуктом или пустой Optional
     */
    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    /**
     * Получает статью по ID
     * @param id идентификатор статьи
     * @return Optional со статьей или пустой Optional
     */
    public Optional<Article> getArticleById(UUID id) {
        return Optional.ofNullable(articles.get(id));
    }

    /**
     * Инициализирует тестовые данные
     */
    private void initializeTestData() {
        // Создаем тестовые товары
        Product laptop = new SimpleProduct(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Ноутбук Lenovo IdeaPad",
                75000
        );

        Product phone = new SimpleProduct(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "Смартфон Samsung Galaxy",
                35000
        );

        Product headphones = new DiscountedProduct(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Беспроводные наушники Sony",
                20000,
                15
        );

        Product tablet = new DiscountedProduct(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "Планшет Apple iPad Pro",
                80000,
                10
        );

        Product usbCable = new FixPriceProduct(
                UUID.fromString("55555555-5555-5555-5555-555555555555"),
                "USB-C кабель"
        );

        Product mouse = new FixPriceProduct(
                UUID.fromString("66666666-6666-6666-6666-666666666666"),
                "Игровая мышь Razer"
        );

        // Добавляем товары в хранилище
        products.put(laptop.getId(), laptop);
        products.put(phone.getId(), phone);
        products.put(headphones.getId(), headphones);
        products.put(tablet.getId(), tablet);
        products.put(usbCable.getId(), usbCable);
        products.put(mouse.getId(), mouse);

        // Создаем тестовые статьи
        Article laptopArticle = new Article(
                UUID.fromString("77777777-7777-7777-7777-777777777777"),
                "Обзор ноутбука Lenovo IdeaPad",
                "Ноутбук Lenovo IdeaPad обладает мощным процессором Intel Core i7 и длительным временем работы от батареи."
        );

        Article headphonesArticle = new Article(
                UUID.fromString("88888888-8888-8888-8888-888888888888"),
                "Тест беспроводных наушников Sony",
                "Наушники Sony показали превосходное качество звука и удобную посадку. Шумоподавление работает отлично."
        );

        Article shoppingGuide = new Article(
                UUID.fromString("99999999-9999-9999-9999-999999999999"),
                "Как выбрать электронику",
                "При выборе электроники обращайте внимание на характеристики, бренд и отзывы покупателей."
        );

        // Добавляем статьи в хранилище
        articles.put(laptopArticle.getId(), laptopArticle);
        articles.put(headphonesArticle.getId(), headphonesArticle);
        articles.put(shoppingGuide.getId(), shoppingGuide);
    }
}