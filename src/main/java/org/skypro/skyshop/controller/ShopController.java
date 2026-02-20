package org.skypro.skyshop.controller;

import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.UUID;

/**
 * Контроллер для работы с интернет-магазином.
 * Предоставляет REST API для доступа к товарам, статьям, поиску и корзине.
 */
@RestController
@RequestMapping("/api")
public class ShopController {
    private final StorageService storageService;
    private final SearchService searchService;
    private final BasketService basketService;

    /**
     * Конструктор контроллера
     * @param storageService сервис хранения
     * @param searchService сервис поиска
     * @param basketService сервис корзины
     */
    public ShopController(
            StorageService storageService,
            SearchService searchService,
            BasketService basketService) {
        this.storageService = storageService;
        this.searchService = searchService;
        this.basketService = basketService;
    }

    /**
     * Возвращает все товары
     * @return коллекция товаров
     */
    @GetMapping("/products")
    public Collection<Product> getAllProducts() {
        return storageService.getAllProducts();
    }

    /**
     * Возвращает все статьи
     * @return коллекция статей
     */
    @GetMapping("/articles")
    public Collection<Article> getAllArticles() {
        return storageService.getAllArticles();
    }

    /**
     * Выполняет поиск по товарам и статьям
     * @param pattern строка для поиска
     * @return коллекция результатов поиска
     */
    @GetMapping("/search")
    public Collection<SearchResult> search(@RequestParam String pattern) {
        return searchService.search(pattern);
    }

    /**
     * Добавляет продукт в корзину
     * @param id идентификатор продукта
     * @return сообщение об успешном добавлении
     */
    @GetMapping("/basket/{id}")
    public String addProductToBasket(@PathVariable("id") UUID id) {
        basketService.addProductToBasket(id);
        return "Продукт успешно добавлен";
    }

    /**
     * Возвращает содержимое корзины пользователя
     * @return корзина пользователя
     */
    @GetMapping("/basket")
    public UserBasket getUserBasket() {
        return basketService.getUserBasket();
    }
}