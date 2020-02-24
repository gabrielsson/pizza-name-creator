package com.gabrielsson.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.quarkus.cache.CacheResult;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.Tag;
import org.eclipse.microprofile.metrics.annotation.Gauge;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
@RegisterForReflection(fields = false, methods = true)
public class PizzaNameService {
    @Inject
    MetricRegistry registry;

    private final List<String> cities;

    public PizzaNameService() throws IOException {
        this.cities = initCities();
    }

    @CacheResult(cacheName = "pizza-names")
    public String get(List<String> ingredients) {
        registry.counter("pizza.menu.pizza.counter").inc();
        ingredients.forEach(s -> registry
                .counter("pizza.menu.ingredient.generated.counter",
                        new Tag("ingredient", s))
                .inc());
        return getRandomCity();
    }

    private List<String> initCities() throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("META-INF/resources/cities.dat");

        Scanner scanner = new Scanner(resourceAsStream);

        scanner.useDelimiter(Pattern.compile("[\\r\\n;]+"));

        List<String> collect = new ArrayList<>();

        while(scanner.hasNext()) {
            collect.add(scanner.next());
        }
        return collect;
    }


    private String getRandomCity() {
        return cities.stream()
                .skip((int) (cities.size() * Math.random()))
                .findAny()
                .get();
    }
}