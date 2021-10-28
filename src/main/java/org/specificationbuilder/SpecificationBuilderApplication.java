package org.specificationbuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static java.util.Arrays.asList;

@SpringBootApplication
@RequiredArgsConstructor
public class SpecificationBuilderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpecificationBuilderApplication.class, args);
    }

    private final ProductRepository repository;
    private final JpaSpecificationBuilder specificationBuilder;

    @Override
    public void run(String... args) {
        // Init database
        Product iphone1 = new Product("p1", "iPhone 1", "old", 0L);
        Product iphone2 = new Product("p2", "iPhone 2", "old", 1L);
        Product iphone3 = new Product("p3", "iPhone 3", "old", 3L);
        Product iphone4 = new Product("p4", "iPhone 4", "new", 4L);
        Product iphone4s = new Product("p5", "iPhone 4S", "new", 5L);
        Product iphone4xs = new Product("p5", "iPhone 4XS", "new", 6L);
        Product iphone4xxs = new Product("p5", "iPhone 4XXS", "new", 7L);
        List<Product> products = asList(iphone1, iphone2, iphone3, iphone4, iphone4s, iphone4xs, iphone4xxs);
        repository.saveAll(products);

        // Build spec from query
        ProductQuery query = new ProductQuery(0L, 6L, "new", asList("iPhone 4", "iPhone 4XS", "iPhone 4XXS"));
        Specification<Product> spec = specificationBuilder.build(query);

        // Query products
        List<Product> queryResult = repository.findAll(spec);
        System.out.println(queryResult);
    }
}