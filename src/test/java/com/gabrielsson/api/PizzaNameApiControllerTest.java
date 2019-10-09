package com.gabrielsson.api;

import com.gabrielsson.service.PizzaNameService;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PizzaNameApiControllerTest {

    @Test
    public void menuPost() {
        PizzaNameApi api = new PizzaNameApiController(new PizzaNameService(Arrays.asList("Test")));
        List<String> ingredients = Arrays.asList(
                "1", "2", "3"
        );
        String name = api.nameGet(new ArrayList<>(ingredients)).getBody();

        Assertions.assertThat(name).isEqualTo("Test");
    }

    @Test
    public void makeSureIdemPotency() {
        PizzaNameApi api = new PizzaNameApiController(new PizzaNameService(Arrays.asList("Test","Test2")));
        List<String> ingredients = Arrays.asList(
                "1", "2", "3"
        );

        List<String> dummyIngredients = Arrays.asList(
                "1", "2"
        );

        List<String> differentOrder = Arrays.asList(
                "3", "1", "2"
        );
        String expected = api.nameGet(new ArrayList<>(ingredients)).getBody();
        String dummy = api.nameGet(new ArrayList<>(dummyIngredients)).getBody();
        String actual = api.nameGet(new ArrayList<>(differentOrder)).getBody();
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}