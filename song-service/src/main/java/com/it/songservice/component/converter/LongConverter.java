package com.it.songservice.component.converter;

import com.it.commonservice.model.BaseEntity;
import org.dozer.CustomConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public
class LongConverter implements CustomConverter {

    /**
     * Instantiates a new Client converter.
     */
    public LongConverter() {
    }

    @Override
    public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if (source == null)
            return null;
        else if (source instanceof Set) {
            return ((Set) source).stream().map(s -> {
                return convert(dest, s, destinationClass, s.getClass());
            }).collect(Collectors.toSet());
        } else if (source instanceof BaseEntity) {
            return ((BaseEntity) source).getId();
        } else return null;
    }
}
