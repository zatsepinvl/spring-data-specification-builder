package org.specificationbuilder.query.parser;

import org.specificationbuilder.query.annotation.Equal;
import org.specificationbuilder.query.annotation.GreaterThanOrEqual;
import org.specificationbuilder.query.annotation.In;
import org.specificationbuilder.query.annotation.LessThanOrEqual;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.specificationbuilder.query.parser.QueryOperator.*;

@Service
class QueryParserImpl implements QueryParser {

    @Override
    public Query parse(Object input) {
        try {
            return doParse(input);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Unable to parse query object" + input + ": " + e.getMessage(), e);
        }
    }

    private Query doParse(Object input) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        List<QueryItem> items = new ArrayList<>();
        for (Field field : input.getClass().getDeclaredFields()) {
            PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), input.getClass());
            Object value = descriptor.getReadMethod().invoke(input);
            if (value == null) {
                continue;
            }
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Equal) {
                    Equal operator = (Equal) annotation;
                    String key = operator.value();
                    items.add(new QueryItem(EQUAL, key, value));

                } else if (annotation instanceof GreaterThanOrEqual) {
                    GreaterThanOrEqual operator = (GreaterThanOrEqual) annotation;
                    String key = operator.value();
                    if (value instanceof Comparable<?>) {
                        items.add(new QueryItem(GREATER_THAN_OR_EQUAL, key, value));
                    } else {
                        throw new IllegalArgumentException(format(
                                "Field %s marked as %s in %s class must implement Comparable.",
                                field.getName(), GreaterThanOrEqual.class, input.getClass()
                        ));
                    }

                } else if (annotation instanceof LessThanOrEqual) {
                    LessThanOrEqual operator = (LessThanOrEqual) annotation;
                    String key = operator.value();
                    if (value instanceof Comparable<?>) {
                        items.add(new QueryItem(LESS_THAN_OR_EQUAL, key, value));
                    } else {
                        throw new IllegalArgumentException(format(
                                "Field %s marked as %s in %s class must implement Comparable.",
                                field.getName(), LessThanOrEqual.class, input.getClass()
                        ));
                    }

                } else if (annotation instanceof In) {
                    In in = (In) annotation;
                    String key = in.value();
                    items.add(new QueryItem(IN, key, value));
                }
            }
        }
        return new Query(items);
    }
}