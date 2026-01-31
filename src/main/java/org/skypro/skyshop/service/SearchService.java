package org.skypro.skyshop.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Сервис для поиска товаров и статей.
 */
@Service
public class SearchService {
    private final StorageService storageService;

    /**
     * Конструктор сервиса поиска
     * @param storageService сервис хранения
     */
    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Выполняет поиск по всем объектам
     * @param pattern строка для поиска
     * @return коллекция результатов поиска
     */
    public Collection<SearchResult> search(String pattern) {
        if (pattern == null || pattern.isBlank()) {
            return storageService.getAllSearchables().stream()
                    .map(SearchResult::fromSearchable)
                    .collect(Collectors.toList());
        }

        String lowerPattern = pattern.toLowerCase();

        return storageService.getAllSearchables().stream()
                .filter(item -> item.getSearchTerm().toLowerCase().contains(lowerPattern))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());
    }
}