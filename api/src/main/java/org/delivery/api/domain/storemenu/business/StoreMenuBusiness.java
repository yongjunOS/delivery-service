package org.delivery.api.domain.storemenu.business;


import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class StoreMenuBusiness {


    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;


    public StoreMenuResponse register(
            StoreMenuRegisterRequest request
    ){
        // 요청 객체의 값을 확인
        System.out.println("Request - storeId: " + request.getStoreId());
        System.out.println("Request - name: " + request.getName());
        System.out.println("Request - amount: " + request.getAmount());
        System.out.println("Request - thumbnailUrl: " + request.getThumbnailUrl());
        // req -> entity -> save -> response
        var entity = storeMenuConverter.toEntity(request);
        var newEntity = storeMenuService.register(entity);
        var response = storeMenuConverter.toResponse(newEntity);
        return response;
    }

    public List<StoreMenuResponse> search(
            Long storeId
    ){
        var list = storeMenuService.getStoreMenuByStoreId(storeId);

        return list.stream()
                .map(it ->{
                    return storeMenuConverter.toResponse(it);
                })
                //.map(storeMenuConverter::toResponse)
                .collect(Collectors.toList());
    }
}