package br.com.test.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class CustomSpecification<E> {

    public static final String PROPERTY_NOT_FOUND = "property-not-found";
    private static final String FIELD_SEPARATOR = ".";
    private static final String REGEX_FIELD_SPLITTER = "\\.";
    private static final String FILTER_RESERVS[] = {"page", "size", "sort"};

    private Class<E> entityBeanType;

    public CustomSpecification() {
        this.entityBeanType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Specification<E> filterWithOptions(final Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            try {
                List<Predicate> predicates = new ArrayList<>();

                Map<String, String> paramsFiltreds = params.entrySet().stream()
                        .filter(param -> !Arrays.asList(FILTER_RESERVS).contains(param.getKey()))
                        .collect(Collectors.toMap(param -> param.getKey(), param -> param.getValue()));

                for (String field : paramsFiltreds.keySet()) {
                    if (field.contains(FIELD_SEPARATOR)) {
                        filterInDepth(params, root, criteriaBuilder, predicates, field);
                    } else {
                        if (this.getEntityBeanType().getDeclaredField(field) != null) {
                            if (this.getEntityBeanType().getDeclaredField(field).getType().equals(String.class)) {
                                predicates.add(criteriaBuilder.like(root.get(field), params.get(field) + "%"));
                            } else {
                                predicates.add(criteriaBuilder.equal(root.get(field), params.get(field)));
                            }
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    private void filterInDepth(Map<String, String> params, Root<E> root, CriteriaBuilder criteriaBuilder,
                                      List<Predicate> predicates, String field) throws NoSuchFieldException {
        String[] compositeField = field.split(REGEX_FIELD_SPLITTER);
        if (compositeField.length == 2) {
            if(Collection.class.isAssignableFrom(this.getEntityBeanType().getDeclaredField(compositeField[0]).getType())) {
                Join<Object, Object> join = root.join(compositeField[0]);
                predicates.add(criteriaBuilder.equal(join.get(compositeField[1]), params.get(field)));
            }
        } else if(this.getEntityBeanType().getDeclaredField(compositeField[0]).getType().getDeclaredField(compositeField[1]) != null) {
            predicates.add(criteriaBuilder.equal(root.get(compositeField[0]).get(compositeField[1]), params.get(field)));
        }
    }

    protected Class<E> getEntityBeanType() {
        return entityBeanType;
    }

}
