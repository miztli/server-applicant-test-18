package com.mytaxi.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mytaxi.domainobject.ManufacturerDO;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManufacturerDTO {

    @JsonIgnore
    Long id;

    @NotNull(message = "Name can not be null!")
    String name;

    @NotNull(message = "Description can not be null!")
    String description;

    public ManufacturerDTO() {
    }

    public ManufacturerDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static ManufacturerDTOBuilder newBuilder() { return new ManufacturerDTOBuilder(); }

    public static class ManufacturerDTOBuilder {
        private Long id;
        private String name;
        private String description;

        public ManufacturerDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ManufacturerDTOBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ManufacturerDTOBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ManufacturerDTO createManufacturerDTO() {
            return new ManufacturerDTO(id, name, description);
        }
    }
}
