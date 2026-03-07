package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование BasketService")
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    private UUID validProductId;
    private UUID invalidProductId;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        validProductId = UUID.randomUUID();
        invalidProductId = UUID.randomUUID();
        testProduct = new SimpleProduct(validProductId, "Test Product", 1000);
    }

    @Test
    @DisplayName("Добавление несуществующего товара выбрасывает NoSuchProductException")
    void addNonExistentProductThrowsException() {
        // Arrange
        when(storageService.getProductById(invalidProductId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchProductException exception = assertThrows(
                NoSuchProductException.class,
                () -> basketService.addProductToBasket(invalidProductId)
        );

        assertThat(exception.getMessage()).contains(invalidProductId.toString());

        // Verify that addProduct was never called
        verify(productBasket, never()).addProduct(any(UUID.class));
    }

    @Test
    @DisplayName("Добавление существующего товара вызывает addProduct у ProductBasket")
    void addExistingProductCallsProductBasketAddProduct() {
        // Arrange
        when(storageService.getProductById(validProductId)).thenReturn(Optional.of(testProduct));

        // Act
        basketService.addProductToBasket(validProductId);

        // Assert
        verify(productBasket, times(1)).addProduct(validProductId);
        verify(productBasket, never()).addProduct(any(UUID.class));
    }

    @Test
    @DisplayName("getUserBasket возвращает пустую корзину, когда ProductBasket пуст")
    void getUserBasketWithEmptyBasketReturnsEmptyBasket() {
        // Arrange
        when(productBasket.getBasketItems()).thenReturn(Collections.emptyMap());

        // Act
        UserBasket userBasket = basketService.getUserBasket();

        // Assert
        assertThat(userBasket.getItems()).isEmpty();
        assertThat(userBasket.getTotal()).isZero();

        // Verify that storageService was never called (no products to look up)
        verify(storageService, never()).getProductById(any(UUID.class));
    }

    @Test
    @DisplayName("getUserBasket возвращает корзину с товарами, когда в ProductBasket есть товары")
    void getUserBasketWithProductsReturnsPopulatedBasket() {
        // Arrange
        UUID productId1 = validProductId;
        UUID productId2 = UUID.randomUUID();
        int quantity1 = 2;
        int quantity2 = 3;
        int price1 = 1000;
        int price2 = 500;

        Product product1 = testProduct;
        Product product2 = new SimpleProduct(productId2, "Another Product", price2);

        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(productId1, quantity1);
        basketMap.put(productId2, quantity2);

        when(productBasket.getBasketItems()).thenReturn(basketMap);
        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));
        when(storageService.getProductById(productId2)).thenReturn(Optional.of(product2));

        // Act
        UserBasket userBasket = basketService.getUserBasket();

        // Assert
        assertThat(userBasket.getItems()).hasSize(2);

        // Проверяем элементы корзины
        List<BasketItem> items = new ArrayList<>(userBasket.getItems());

        BasketItem item1 = items.stream()
                .filter(item -> item.getProduct().getId().equals(productId1))
                .findFirst()
                .orElseThrow();

        BasketItem item2 = items.stream()
                .filter(item -> item.getProduct().getId().equals(productId2))
                .findFirst()
                .orElseThrow();

        assertThat(item1.getQuantity()).isEqualTo(quantity1);
        assertThat(item1.getProduct().getPrice()).isEqualTo(price1);

        assertThat(item2.getQuantity()).isEqualTo(quantity2);
        assertThat(item2.getProduct().getPrice()).isEqualTo(price2);

        // Проверяем общую стоимость (1000*2 + 500*3 = 2000 + 1500 = 3500)
        assertThat(userBasket.getTotal()).isEqualTo(3500);

        // Verify that storageService was called for each product
        verify(storageService, times(1)).getProductById(productId1);
        verify(storageService, times(1)).getProductById(productId2);
    }

    @Test
    @DisplayName("getUserBasket выбрасывает исключение, если продукт не найден в StorageService")
    void getUserBasketThrowsExceptionWhenProductNotFound() {
        // Arrange
        UUID missingProductId = UUID.randomUUID();
        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(missingProductId, 1);

        when(productBasket.getBasketItems()).thenReturn(basketMap);
        when(storageService.getProductById(missingProductId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> basketService.getUserBasket())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("не найден в хранилище");
    }

    @Test
    @DisplayName("Добавление одного и того же товара несколько раз увеличивает количество")
    void addingSameProductMultipleTimesIncreasesQuantity() {
        // Arrange
        when(storageService.getProductById(validProductId)).thenReturn(Optional.of(testProduct));

        // Act - добавляем товар 3 раза
        basketService.addProductToBasket(validProductId);
        basketService.addProductToBasket(validProductId);
        basketService.addProductToBasket(validProductId);

        // Assert
        verify(productBasket, times(3)).addProduct(validProductId);
    }

    @Test
    @DisplayName("Несколько вызовов getUserBasket возвращают одинаковое содержимое")
    void multipleGetUserBasketCallsReturnSameContent() {
        // Arrange
        int quantity = 2;
        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(validProductId, quantity);

        when(productBasket.getBasketItems()).thenReturn(basketMap);
        when(storageService.getProductById(validProductId)).thenReturn(Optional.of(testProduct));

        // Act
        UserBasket firstCall = basketService.getUserBasket();
        UserBasket secondCall = basketService.getUserBasket();

        // Assert
        assertThat(firstCall.getItems()).hasSize(1);
        assertThat(secondCall.getItems()).hasSize(1);
        assertThat(firstCall.getTotal()).isEqualTo(secondCall.getTotal());

        BasketItem item1 = firstCall.getItems().iterator().next();
        BasketItem item2 = secondCall.getItems().iterator().next();

        assertThat(item1.getQuantity()).isEqualTo(item2.getQuantity());
        assertThat(item1.getProduct().getPrice()).isEqualTo(item2.getProduct().getPrice());
    }

    @Test
    @DisplayName("getUserBasket возвращает неизменяемый список элементов")
    void getUserBasketReturnsUnmodifiableList() {
        // Arrange
        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(validProductId, 1);

        when(productBasket.getBasketItems()).thenReturn(basketMap);
        when(storageService.getProductById(validProductId)).thenReturn(Optional.of(testProduct));

        // Act
        UserBasket userBasket = basketService.getUserBasket();

        // Assert
        assertThatThrownBy(() -> userBasket.getItems().add(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("getUserBasket с разными товарами правильно вычисляет общую стоимость")
    void getUserBasketWithDifferentProductsCalculatesCorrectTotal() {
        // Arrange
        UUID productId1 = validProductId;
        UUID productId2 = UUID.randomUUID();
        UUID productId3 = UUID.randomUUID();

        Product product1 = testProduct; // цена 1000
        Product product2 = new SimpleProduct(productId2, "Product 2", 2000);
        Product product3 = new SimpleProduct(productId3, "Product 3", 3000);

        Map<UUID, Integer> basketMap = new HashMap<>();
        basketMap.put(productId1, 1); // 1000
        basketMap.put(productId2, 2); // 4000
        basketMap.put(productId3, 3); // 9000

        when(productBasket.getBasketItems()).thenReturn(basketMap);
        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));
        when(storageService.getProductById(productId2)).thenReturn(Optional.of(product2));
        when(storageService.getProductById(productId3)).thenReturn(Optional.of(product3));

        // Act
        UserBasket userBasket = basketService.getUserBasket();

        // Assert
        assertThat(userBasket.getItems()).hasSize(3);
        assertThat(userBasket.getTotal()).isEqualTo(1000 + 4000 + 9000); // 14000
    }
}