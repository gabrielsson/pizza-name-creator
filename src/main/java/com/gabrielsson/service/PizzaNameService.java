package com.gabrielsson.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PizzaNameService {

    private final List<String> cities;
    private final Cache<String, String> cache;

    public PizzaNameService(List<String> cities) {
        this.cities = cities;

        cache = Caffeine.newBuilder()
                .softValues()
                .maximumSize(40000)
                .build();
    }

    public String get(List<String> ingredients) {
        String key = createIngredientKey(ingredients);

        String name = cache.get(key, this::createPizzaName);
        return name;
    }

    private String createIngredientKey(List<String> ingredients) {
        return ingredients.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining());
    }

    private String createPizzaName(String key) {
        String c = getRandomCity();

        while(cacheContains(c)) {
            c += " " + getRandomCity();
        }
        return c;
    }

    private boolean cacheContains(String c) {
        return cache.asMap().values().contains(c);
    }

    private String getRandomCity() {
        return cities.stream()
                .skip((int) (cities.size() * Math.random()))
                .findAny()
                .get();
    }
}
