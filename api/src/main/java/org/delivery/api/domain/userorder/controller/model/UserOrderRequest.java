package org.delivery.api.domain.userorder.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 주문 요청")
public class UserOrderRequest {

    @NotNull(message = "상점 ID는 필수입니다.")
    @JsonProperty("storeId")
    @Schema(description = "주문할 상점의 ID", example = "1")
    private Long storeId;

    @NotNull(message = "메뉴 ID 리스트는 필수입니다.")
    @NotEmpty(message = "최소 하나의 메뉴를 선택해야 합니다.")
    @Valid
    @JsonProperty("storeMenuIdList")
    @Schema(description = "주문할 메뉴 ID 리스트", example = "[1, 2, 3]")
    private List<Long> storeMenuIdList;

}