package com.gabrielsson;

import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.Tag;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Path("/name")
public class PizzaNameResource {
    private final List<String> cities;
    private final Map<String, String> cache;

    @Inject
    MetricRegistry registry;

    public PizzaNameResource() {
        try {
            this.cities = initCities();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cache = new HashMap<>(10000);
    }

    @GET
    @Path("/{name}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public String nameGet(@PathParam String name) {
        return this.getPizzaName(Optional.ofNullable(name).map(Arrays::asList).orElse(new ArrayList<>()));
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> name(ArrayList<ArrayList<String>> ingredients) {
        return ingredients.stream()
                .map(list -> this.getPizzaName(Optional.ofNullable(list).orElse((new ArrayList<>()))))
                .collect(Collectors.toList());
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

    private String getPizzaName(List<String> ingredients) {
        registry.counter("pizza.menu.pizza.counter").inc();
        ingredients.forEach(s -> registry
                .counter("pizza.menu.ingredient.generated.counter",
                        new Tag("ingredient", s))
                .inc());
        String key = createIngredientKey(ingredients);
        String name = cache.computeIfAbsent(key, this::createPizzaName);
        return name;
    }

    @Gauge(absolute = true, name="pizza.name.cache",unit = MetricUnits.NONE, description = "Current size of pizza name cache")
    public long cacheSize() {
        return cache.size();
    }

    private String createIngredientKey(List<String> ingredients) {
        return ingredients.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining());
    }

    private String createPizzaName(String key) {
        String c = getRandomCity();

        while (cacheContains(c)) {
            c += " " + getRandomCity();
        }
        return c;
    }

    private boolean cacheContains(String c) {
        return cache.values().contains(c);
    }

    private String getRandomCity() {
        return cities.stream()
                .skip((int) (cities.size() * Math.random()))
                .findAny()
                .get();
    }}