package org.specificationbuilder.query.parser;

import lombok.Value;

import java.util.List;

@Value
public class Query {
    /**
     * All fields marked with operation annotations like @Query or @In.
     */
    List<QueryItem> items;
}