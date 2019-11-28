package com.gabrielsson.configuration;

import com.gabrielsson.service.PizzaNameService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public MetricsService(MeterRegistry meterRegistry, PizzaNameService service) {
        this.meterRegistry = meterRegistry;
        meterRegistry
                .gauge("pizza.menu.name.cache.size",
                        service, PizzaNameService::cacheSize);
    }

    public void count(List<String> ingredients) {

        meterRegistry.counter("pizza.menu.pizza.counter").increment();
        ingredients.forEach(s -> meterRegistry
                .counter("pizza.menu.ingredient.counter",
                        Tags.of(Tag.of("ingredient", s))).increment()
        );
    }
}
