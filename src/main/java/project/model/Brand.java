package project.model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;

public record Brand(
        @JsonProperty("id")
        String id,
        @JsonProperty("name")
        String name,
        @JsonProperty("slug")
        String slug) {
}
