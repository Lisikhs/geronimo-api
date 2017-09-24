package com.geronimo.controller.resource.converter;

import com.geronimo.controller.resource.AbstractResource;
import com.geronimo.model.AbstractEntity;
import org.modelmapper.ModelMapper;

public interface ResourceConverter<E extends AbstractEntity, D extends AbstractResource> {
    D convert(E entity);

    E convert(D dto);

    ModelMapper getModelMapper();

    void setModelMapper(ModelMapper modelMapper);
}
