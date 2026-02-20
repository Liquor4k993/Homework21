package org.skypro.skyshop.controller;


import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.StorageService;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

/**
 * Контроллер для работы с интернет-магазином.
 * Предоставляет REST API для доступа к товарам, статьям и поиску.
 */
@RestController
@RequestMapping("/api")
public class ShopController {
    private final StorageService storageService;
    private final SearchService searchService;

    /**
     * Конструктор контроллера
     * @param storageService сервис хранения
     * @param searchService сервис поиска
     */
    public ShopController(StorageService storageService, SearchService searchService) {
        this.storageService = storageService;
        this.searchService = searchService;
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
}