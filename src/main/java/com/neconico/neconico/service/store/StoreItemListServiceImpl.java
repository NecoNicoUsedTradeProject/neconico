package com.neconico.neconico.service.store;

import com.neconico.neconico.dto.store.StoreItemRequestDto;
import com.neconico.neconico.dto.store.StoreItemSortingDto;
import com.neconico.neconico.mapper.store.StoreItemListMapper;
import com.neconico.neconico.paging.Criteria;
import com.neconico.neconico.paging.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreItemListServiceImpl implements StoreItemListService{

    private final StoreItemListMapper listMapper;

    //Paging 크기
    private final Long pageSize = 10L;
    //아이템 카드 20개 나열
    private final Long cardPerPage = 20L;
    //아이템 카드 외 10개 나열
    private final Long otherCardPerPage = 10L;

    @Override
    public Map<String, Long> createItemListCount(Long userId){
        HashMap<String, Long> itemCountMap = new HashMap<>();
        itemCountMap.put("myItemCount", listMapper.countStoreMyItem(userId));
        itemCountMap.put("purchaseItemCount", listMapper.countStorePurchasedItem(userId));
        itemCountMap.put("favoriteItemCount", listMapper.countStoreFavoriteItem(userId));
        itemCountMap.put("tradeCount", listMapper.countStoreTrade(userId));
        itemCountMap.put("questionCount", listMapper.countStoreQuestion(userId));
        itemCountMap.put("storeReviewCount", listMapper.countStoreReview(userId));
        return itemCountMap;
    }

    @Override
    public Map<String, Object> createMyItemList(Long userId, StoreItemRequestDto request){
        Criteria cri = createCri(request, cardPerPage);
        HashMap<String, Object> itemListMap = new HashMap<>();
        itemListMap.put("pagination",
                new Pagination(cri, listMapper.countStoreMyItem(userId), pageSize));
        itemListMap.put("itemList", listMapper.selectStoreMyItemList(adaptSortingDto(userId, cri)));
        return itemListMap;
    }

    @Override
    public Map<String, Object> createPurchaseItemList(Long userId, StoreItemRequestDto request){
        Criteria cri = createCri(request, otherCardPerPage);
        HashMap<String, Object> itemListMap = new HashMap<>();
        itemListMap.put("pagination",
                new Pagination(cri, listMapper.countStorePurchasedItem(userId), pageSize));
        itemListMap.put("itemList", listMapper.selectStorePurchasedItemList(adaptSortingDto(userId, cri)));
        return itemListMap;
    }

    @Override
    public Map<String, Object> createFavoriteItemList(Long userId, StoreItemRequestDto request){
        Criteria cri = createCri(request, cardPerPage);
        HashMap<String, Object> itemListMap = new HashMap<>();
        itemListMap.put("pagination",
                new Pagination(cri, listMapper.countStoreFavoriteItem(userId), pageSize));
        itemListMap.put("itemList", listMapper.selectStoreFavoriteList(adaptSortingDto(userId, cri)));
        return itemListMap;
    }

    @Override
    public Map<String, Object> createTradeList(Long userId, StoreItemRequestDto request){
        Criteria cri = createCri(request, otherCardPerPage);
        HashMap<String, Object> itemListMap = new HashMap<>();
        itemListMap.put("pagination",
                new Pagination(cri, listMapper.countStoreTrade(userId), pageSize));
        itemListMap.put("itemList", listMapper.selectStoreTradeList(adaptSortingDto(userId, cri)));
        return itemListMap;
    }

    @Override
    public Map<String, Object> createQuestionList(Long userId, StoreItemRequestDto request){
        Criteria cri = createCri(request, otherCardPerPage);
        HashMap<String, Object> itemListMap = new HashMap<>();
        itemListMap.put("pagination",
                new Pagination(cri, listMapper.countStoreQuestion(userId), pageSize));
        itemListMap.put("itemList", listMapper.selectStoreQuestionList(adaptSortingDto(userId, cri)));
        return itemListMap;
    }

    @Override
    public Map<String, Object> createReviewList(Long userId, StoreItemRequestDto request){
        Criteria cri = createCri(request, otherCardPerPage);
        HashMap<String, Object> itemListMap = new HashMap<>();
        itemListMap.put("pagination",
                new Pagination(cri, listMapper.countStoreReview(userId), pageSize));
        itemListMap.put("itemList", listMapper.selectStoreReviewList(adaptSortingDto(userId, cri)));
        return itemListMap;
    }

    private Criteria createCri(StoreItemRequestDto request, Long contentPerPage) {
        Criteria cri = new Criteria();
        cri.setCurrentPage(request.getCurrentPage());
        cri.setContentPerPage(contentPerPage);
        if(request.getSortingColumn().equals("PRICE")){
            cri.setSortingColumn("(replace(price,',','') + 0)");
        } else{
            cri.setSortingColumn(request.getSortingColumn());
        }
        cri.setRequestOrder(request.getRequestOrder());
        return cri;
    }

    // userId + Criteria -> SortingDto
    private StoreItemSortingDto adaptSortingDto(Long userId, Criteria cri) {
        return new StoreItemSortingDto(userId,
                cri.getSortingColumn(),
                cri.getRequestOrder(),
                Long.valueOf(cri.getPageStart()),
                Long.valueOf(cri.getContentPerPage()));
    }

}
