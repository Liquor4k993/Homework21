package org.skypro.skyshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование SearchService")
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    private UUID testProductId;
    private UUID testArticleId;
    private Product testProduct;
    private Article testArticle;

    @BeforeEach
    void setUp() {
        testProductId = UUID.randomUUID();
        testArticleId = UUID.randomUUID();
        testProduct = new SimpleProduct(testProductId, "Test Product", 1000);
        testArticle = new Article(testArticleId, "Test Article", "Test content");
    }

    @Test
    @DisplayName("Поиск при пустом хранилище возвращает пустую коллекцию")
    void searchWithEmptyStorageReturnsEmptyCollection() {
        // Arrange
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        // Act
        Collection<SearchResult> results = searchService.search("test");

        // Assert
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Поиск при непустом хранилище без совпадений возвращает пустую коллекцию")
    void searchWithNoMatchesReturnsEmptyCollection() {
        // Arrange
        Collection<Searchable> searchables = Arrays.asList(testProduct, testArticle);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> results = searchService.search("xyz123");

        // Assert
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Поиск с подходящим объектом возвращает коллекцию с результатами")
    void searchWithMatchingObjectReturnsResults() {
        // Arrange
        Collection<Searchable> searchables = Arrays.asList(testProduct, testArticle);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> results = searchService.search("Test");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(SearchResult::getName)
                .containsExactlyInAnyOrder("Test Product", "Test Article");
    }

    @Test
    @DisplayName("Поиск с частичным совпадением возвращает только подходящие объекты")
    void searchWithPartialMatchReturnsOnlyMatchingObjects() {
        // Arrange
        Product product2 = new SimpleProduct(UUID.randomUUID(), "Another Product", 2000);
        Article article2 = new Article(UUID.randomUUID(), "Another Article", "Other content");

        Collection<Searchable> searchables = Arrays.asList(testProduct, testArticle, product2, article2);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> results = searchService.search("Test");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(SearchResult::getName)
                .containsExactly("Test Product", "Test Article");
    }

    @Test
    @DisplayName("Поиск с пустым паттерном возвращает все объекты")
    void searchWithEmptyPatternReturnsAllObjects() {
        // Arrange
        Collection<Searchable> searchables = Arrays.asList(testProduct, testArticle);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> results = searchService.search("");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(SearchResult::getName)
                .containsExactlyInAnyOrder("Test Product", "Test Article");
    }

    @Test
    @DisplayName("Поиск с null паттерном возвращает все объекты")
    void searchWithNullPatternReturnsAllObjects() {
        // Arrange
        Collection<Searchable> searchables = Arrays.asList(testProduct, testArticle);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> results = searchService.search(null);

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(SearchResult::getName)
                .containsExactlyInAnyOrder("Test Product", "Test Article");
    }

    @Test
    @DisplayName("Поиск с учетом регистра: поиск нечувствителен к регистру")
    void searchIsCaseInsensitive() {
        // Arrange
        Collection<Searchable> searchables = Arrays.asList(testProduct, testArticle);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> results = searchService.search("test");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(SearchResult::getName)
                .containsExactlyInAnyOrder("Test Product", "Test Article");
    }

    @Test
    @DisplayName("Поиск возвращает правильные типы контента")
    void searchReturnsCorrectContentTypes() {
        // Arrange
        Collection<Searchable> searchables = Arrays.asList(testProduct, testArticle);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        // Act
        Collection<SearchResult> results = searchService.search("Test");

        // Assert
        assertThat(results).hasSize(2);

        Optional<SearchResult> productResult = results.stream()
                .filter(r -> r.getContentType().equals("PRODUCT"))
                .findFirst();

        Optional<SearchResult> articleResult = results.stream()
                .filter(r -> r.getContentType().equals("ARTICLE"))
                .findFirst();

        assertThat(productResult).isPresent();
        assertThat(productResult.get().getName()).isEqualTo("Test Product");

        assertThat(articleResult).isPresent();
        assertThat(articleResult.get().getName()).isEqualTo("Test Article");
    }
}