package org.delivery.api.domain.userorder.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.api.common.annotation.UserSession;
import org.delivery.api.common.api.Api;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.business.UserOrderBusiness;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-order")
@Slf4j
public class UserOrderApiController {

    private final UserOrderBusiness userOrderBusiness;


    // 사용자 주문
    @PostMapping("")
    public Api<UserOrderResponse> userOrder(
            @Valid @RequestBody UserOrderRequest userOrderRequest,
            @Parameter(hidden = true) @UserSession User user
    ) {
        log.info("Received order request: {}", userOrderRequest);

        if (userOrderRequest == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Invalid request body");
        }

        var response = userOrderBusiness.userOrder(user, userOrderRequest);
        return Api.OK(response);
    }
    // 현재 진행중인 주문건
    @GetMapping("/current")
    public Api<List<UserOrderDetailResponse>> current(
            @Parameter(hidden = true)
            @UserSession
            User user
    ) {
        var response = userOrderBusiness.current(user);
        return Api.OK(response);
    }


    // 과거 주문 내역
    @GetMapping("/history")
    public Api<List<UserOrderDetailResponse>> history(
            @Parameter(hidden = true)
            @UserSession
            User user
    ) {
        var response = userOrderBusiness.history(user);
        return Api.OK(response);
    }


    // 주문 1건에 대한 내역
    @GetMapping("/id/{orderId}")
    public Api<UserOrderDetailResponse> read(
            @Parameter(hidden = true)
            @UserSession User user,

            @PathVariable Long orderId
    ) {
        var response = userOrderBusiness.read(user, orderId);
        return Api.OK(response);
    }
}
