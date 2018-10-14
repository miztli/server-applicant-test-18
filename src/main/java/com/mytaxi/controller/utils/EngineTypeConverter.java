package com.mytaxi.controller.utils;

import com.mytaxi.domainvalue.EngineType;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.Optional;

public class EngineTypeConverter extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Optional<EngineType> engineTypeOptional = Arrays.stream(EngineType.values())
                .filter(engineType -> engineType.name().equalsIgnoreCase(text))
                .findAny();
        engineTypeOptional
                .ifPresent(this::setValue);
        engineTypeOptional.orElseThrow(() -> new IllegalArgumentException("Invalid enum type"));
    }
}
