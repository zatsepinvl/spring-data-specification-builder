package org.specificationbuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.specificationbuilder.query.annotation.Equal;
import org.specificationbuilder.query.annotation.GreaterThanOrEqual;
import org.specificationbuilder.query.annotation.In;
import org.specificationbuilder.query.annotation.LessThanOrEqual;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuery {

    @GreaterThanOrEqual("createdAt")
    private Long createdAtFrom;

    @LessThanOrEqual("createdAt")
    private Long createdAtTo;

    @Equal("model")
    private String model;

    @In("name")
    private List<String> names;
}