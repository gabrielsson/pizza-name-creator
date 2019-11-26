package com.gabrielsson.api;

import com.gabrielsson.service.PizzaNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PizzaNameApiController implements PizzaNameApi {

    private static final Logger log = LoggerFactory.getLogger(PizzaNameApiController.class);

    private final PizzaNameService pizzaNameService;

    public PizzaNameApiController(PizzaNameService pizzaNameService) {
        this.pizzaNameService = pizzaNameService;
    }

    @Override
    public ResponseEntity<String> nameGet(ArrayList<String> ingredients) {
        return new ResponseEntity<>(pizzaNameService.get(Optional.ofNullable(ingredients).orElse((new ArrayList<>()))), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> namesPost(ArrayList<ArrayList<String>> ingredients) {
        return  new ResponseEntity<>(ingredients.stream()
                .map(list->pizzaNameService.get(Optional.ofNullable(list).orElse((new ArrayList<>()))))
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
