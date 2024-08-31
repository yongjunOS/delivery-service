package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
@Slf4j
public class UserOrderBusiness {

    private final UserOrderService userOrderService;

    private final UserOrderConverter userOrderConverter;

    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;

    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;

    private final StoreService storeService;
    private final StoreConverter storeConverter;




    // 1. 사용자 , 메뉴 id
    // 2. userOrder 생성
    // 3. userOrderMenu 생성
    // 4. 응답 생성

    public UserOrderResponse userOrder(User user, UserOrderRequest body) {
        log.info("Received order request for user {}: {}", user.getId(), body);

        if (body == null || body.getStoreMenuIdList() == null || body.getStoreMenuIdList().isEmpty()) {
            log.error("Invalid order request: body or storeMenuIdList is null or empty");
            throw new ApiException(ErrorCode.BAD_REQUEST, "Invalid order request");
        }

        if (body.getStoreId() == null) {
            log.error("Invalid order request: storeId is null");
            throw new ApiException(ErrorCode.BAD_REQUEST, "Store ID is required");
        }

        try {
            var storeMenuEntityList = body.getStoreMenuIdList()
                    .stream()
                    .map(it -> {
                        try {
                            return storeMenuService.getStoreMenuWithThrow(it);
                        } catch (Exception e) {
                            log.error("Error fetching store menu with id {}: {}", it, e.getMessage());
                            throw new ApiException(ErrorCode.NOT_FOUND, "Store menu not found: " + it);
                        }
                    })
                    .collect(Collectors.toList());

            var userOrderEntity = userOrderConverter.toEntity(user, body.getStoreId(), storeMenuEntityList);

            // 주문
            var newUserOrderEntity = userOrderService.order(userOrderEntity);

            // 맵핑
            var userOrderMenuEntityList = storeMenuEntityList.stream()
                    .map(it -> userOrderMenuConverter.toEntity(newUserOrderEntity, it))
                    .collect(Collectors.toList());

            // 주문내역 기록 남기기
            userOrderMenuEntityList.forEach(userOrderMenuService::order);

            // response
            var response = userOrderConverter.toResponse(newUserOrderEntity);
            log.info("Order processed successfully for user {}: {}", user.getId(), response);
            return response;
        } catch (Exception e) {
            log.error("Error processing order for user {}: {}", user.getId(), e.getMessage());
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "Error processing order: " + e.getMessage());
        }
    }
    public List<UserOrderDetailResponse> current(User user) {

        var userOrderEntityList =  userOrderService.current(user.getId());

        // 주문 1건씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream().map(it ->{
            // 사용자가 주문 메뉴
            var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
            var storeMenuEntityList = userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity ->{
                    var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    return storeMenuEntity;
                })
                .collect(Collectors.toList());

            // 사용자가 주문한 스토어 TODO 리팩토링 필요
            var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

            return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(it))
                .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build()
                ;
        }).collect(Collectors.toList());

        return userOrderDetailResponseList;
    }

    public List<UserOrderDetailResponse> history(User user) {

        var userOrderEntityList =  userOrderService.history(user.getId());

        // 주문 1건씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream().map(it ->{
            // 사용자가 주문 메뉴
            var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
            var storeMenuEntityList = userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity ->{
                    var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    return storeMenuEntity;
                })
                .collect(Collectors.toList());

            // 사용자가 주문한 스토어 TODO 리팩토링 필요
            var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

            return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(it))
                .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build()
                ;
        }).collect(Collectors.toList());

        return userOrderDetailResponseList;
    }

    public UserOrderDetailResponse read(User user, Long orderId) {

        var userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());

        // 사용자가 주문한 메뉴
        var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());

        var storeMenuEntityList = userOrderMenuEntityList.stream()
            .map(userOrderMenuEntity ->{
                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                return storeMenuEntity;
            })
            .collect(Collectors.toList());

        // 사용자가 주문한 스토어 TODO 리팩토링 필요
        var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

        return UserOrderDetailResponse.builder()
            .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
            .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList))
            .storeResponse(storeConverter.toResponse(storeEntity))
            .build()
            ;
    }
}
