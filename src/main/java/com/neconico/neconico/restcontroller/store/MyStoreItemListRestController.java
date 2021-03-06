package com.neconico.neconico.restcontroller.store;

import com.neconico.neconico.config.web.LoginUser;
import com.neconico.neconico.dto.store.StoreItemRequestDto;
import com.neconico.neconico.dto.users.SessionUser;
import com.neconico.neconico.service.store.StoreItemListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MyStoreItemListRestController {

    private final StoreItemListService listService;

    @PostMapping("/mystore/list/myItem")
    public Map<String, Object> getMyItemList(@RequestBody StoreItemRequestDto request, @LoginUser SessionUser user) {
        Map<String, Object> itemList = listService.createMyItemList(user.getUserId(), request);
        return itemList;
    }

    @PostMapping("/mystore/list/purchasedItem")
    public Map<String, Object> getPurchasedItemList(@RequestBody StoreItemRequestDto request, @LoginUser SessionUser user) {
        Map<String, Object> itemList = listService.createPurchaseItemList(user.getUserId(), request);
        return itemList;
    }

    @PostMapping("/mystore/list/favoriteItem")
    public Map<String, Object> getFavoriteItemList(@RequestBody StoreItemRequestDto request, @LoginUser SessionUser user) {
        Map<String, Object> itemList = listService.createFavoriteItemList(user.getUserId(), request);
        return itemList;
    }

    @PostMapping("/mystore/list/tradeItem")
    public Map<String, Object> getTradeItemList(@RequestBody StoreItemRequestDto request, @LoginUser SessionUser user) {
        Map<String, Object> itemList = listService.createTradeList(user.getUserId(), request);
        return itemList;
    }

    @PostMapping("/mystore/list/questionItem")
    public Map<String, Object> getQuestionItemList(@RequestBody StoreItemRequestDto request, @LoginUser SessionUser user) {
        Map<String, Object> itemList = listService.createQuestionList(user.getUserId(), request);
        return itemList;
    }


    @PostMapping("/mystore/list/storeReview")
    public Map<String, Object> getStoreReviewLIst(@RequestBody StoreItemRequestDto request, @LoginUser SessionUser user) {
        Map<String, Object> itemList = listService.createReviewList(user.getUserId(), request);
        return itemList;
    }
}
