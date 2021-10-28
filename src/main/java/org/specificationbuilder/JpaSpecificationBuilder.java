package org.specificationbuilder;

import org.springframework.data.jpa.domain.Specification;

public interface JpaSpecificationBuilder {
    <T> Specification<T> build(Object input);
}