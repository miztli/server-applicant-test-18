package com.mytaxi.controller.utils;

import com.mytaxi.domainvalue.OnlineStatus;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.Optional;

public class OnlineStatusConverter extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Optional<OnlineStatus> onlineStatusOptional = Arrays.stream(OnlineStatus.values())
                .filter(onlineType -> onlineType.name().equalsIgnoreCase(text))
                .findAny();
        onlineStatusOptional
                .ifPresent(this::setValue);
        onlineStatusOptional.orElseThrow(() -> new IllegalArgumentException("Invalid enum type"));
    }
}
