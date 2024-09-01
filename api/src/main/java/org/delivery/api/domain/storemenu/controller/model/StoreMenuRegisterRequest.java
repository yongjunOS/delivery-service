package org.delivery.api.domain.storemenu.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreMenuRegisterRequest {

    @NotNull
    @JsonProperty("store_id")
    private Long storeId;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

}
