package com.gabrielsson.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
public class CitiesConfiguration {

    @Bean
    public List<String> cities() throws IOException {
        File cityFile = ResourceUtils.getFile("classpath:cities.dat");

        Scanner scanner = new Scanner(cityFile);

        scanner.useDelimiter(Pattern.compile("[\\r\\n;]+"));

        List<String> collect = scanner.tokens().collect(Collectors.toList());
        return collect;
    }
}
