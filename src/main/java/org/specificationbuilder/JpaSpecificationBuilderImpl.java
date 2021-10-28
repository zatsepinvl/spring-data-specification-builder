package org.specificationbuilder;

import lombok.RequiredArgsConstructor;
import org.specificationbuilder.query.parser.QueryParser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class JpaSpecificationBuilderImpl implements JpaSpecificationBuilder {
    private final QueryParser parser;

    @Override
    public <T> Specification<T> build(Object input) {
        List<Specification<T>> specs = new ArrayList<>();

        parser.parse(input).getItems().stream()
                .<Specification<T>>map(item -> {
                    String key = item.getKey();
                    Object value = item.getValue();
                    switch (item.getOperator()) {
                        case EQUAL:
                            return (root, cq, cb) -> cb.equal(root.get(key), value);
                        case IN:
                            return (root, cq, cb) -> root.get(key).in(value);
                        case GREATER_THAN_OR_EQUAL:
                            return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get(key), (Comparable) value);
                        case LESS_THAN_OR_EQUAL:
                            return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get(key), (Comparable) value);
                        default:
                            throw new RuntimeException("Unsupported query operator: " + item.getOperator());
                    }
                })
                .forEach(specs::add);

        if (specs.size() == 0) {
            throw new RuntimeException("Can not build query due to no field is marked with operator annotation.");
        }

        Specification<T> initialSpec = Specification.where(specs.get(0));
        return specs.stream()
                .skip(1)
                .reduce(initialSpec, Specification::and);
    }
}