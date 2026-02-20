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
     * Инициализирует тестовые данные
     */
    private void initializeTestData() {
        // Создаем тестовые товары
        Product laptop = new SimpleProduct(
                UUID.randomUUID(),
                "Ноутбук Lenovo IdeaPad",
                75000
        );

        Product phone = new SimpleProduct(
                UUID.randomUUID(),
                "Смартфон Samsung Galaxy",
                35000
        );

        Product headphones = new DiscountedProduct(
                UUID.randomUUID(),
                "Беспроводные наушники Sony",
                20000,
                15
        );

        Product tablet = new DiscountedProduct(
                UUID.randomUUID(),
                "Планшет Apple iPad Pro",
                80000,
                10
        );

        Product usbCable = new FixPriceProduct(
                UUID.randomUUID(),
                "USB-C кабель"
        );

        Product mouse = new FixPriceProduct(
                UUID.randomUUID(),
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
                UUID.randomUUID(),
                "Обзор ноутбука Lenovo IdeaPad",
                "Ноутбук Lenovo IdeaPad обладает мощным процессором Intel Core i7 и длительным временем работы от батареи."
        );

        Article headphonesArticle = new Article(
                UUID.randomUUID(),
                "Тест беспроводных наушников Sony",
                "Наушники Sony показали превосходное качество звука и удобную посадку. Шумоподавление работает отлично."
        );

        Article shoppingGuide = new Article(
                UUID.randomUUID(),
                "Как выбрать электронику",
                "При выборе электроники обращайте внимание на характеристики, бренд и отзывы покупателей."
        );

        // Добавляем статьи в хранилище
        articles.put(laptopArticle.getId(), laptopArticle);
        articles.put(headphonesArticle.getId(), headphonesArticle);
        articles.put(shoppingGuide.getId(), shoppingGuide);
    }

    /**
     * Получает товар по ID
     * @param id идентификатор товара
     * @return товар или null, если не найден
     */
    public Product getProductById(UUID id) {
        return products.get(id);
    }

    /**
     * Получает статью по ID
     * @param id идентификатор статьи
     * @return статья или null, если не найден
     */
    public Article getArticleById(UUID id) {
        return articles.get(id);
    }
}