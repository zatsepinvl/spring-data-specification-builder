package org.specificationbuilder.query.parser;

import lombok.Value;

@Value
public class QueryItem {
    QueryOperator operator;
    String key;
    Object value;
}