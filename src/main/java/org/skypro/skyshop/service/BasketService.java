package org.skypro.skyshop.service;

import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с корзиной пользователя.
 */
@Service
public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    /**
     * Конструктор сервиса корзины
     * @param productBasket компонент корзины (session scope)
     * @param storageService сервис хранения
     */
    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    /**
     * Добавляет продукт в корзину по ID
     * @param id идентификатор продукта
     * @throws IllegalArgumentException если продукт не найден
     */
    public void addProductToBasket(UUID id) {
        // Проверяем существование продукта
        Product product = storageService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продукт с ID " + id + " не найден"));

        // Добавляем в корзину
        productBasket.addProduct(id);
    }

    /**
     * Возвращает корзину пользователя
     * @return корзина пользователя
     */
    public UserBasket getUserBasket() {
        // Получаем содержимое корзины
        Map<UUID, Integer> basketItems = productBasket.getBasketItems();

        // Преобразуем в список BasketItem
        List<BasketItem> items = basketItems.entrySet().stream()
                .map(entry -> {
                    UUID productId = entry.getKey();
                    Integer quantity = entry.getValue();

                    // Получаем продукт из хранилища
                    Product product = storageService.getProductById(productId)
                            .orElseThrow(() -> new IllegalStateException(
                                    "Продукт с ID " + productId + " не найден в хранилище, но присутствует в корзине"
                            ));

                    return new BasketItem(product, quantity);
                })
                .collect(Collectors.toList());

        return new UserBasket(items);
    }
}