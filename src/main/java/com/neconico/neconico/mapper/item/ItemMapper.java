package com.neconico.neconico.mapper.item;

import com.neconico.neconico.dto.category.CategorySubInfoDto;
import com.neconico.neconico.dto.item.ItemInfoDto;
import com.neconico.neconico.dto.item.ItemInquireInfoDto;
import com.neconico.neconico.dto.item.ItemQuestionInquireDto;
import com.neconico.neconico.dto.item.SearchInfoDto;
import com.neconico.neconico.dto.item.card.ItemCardDto;
import com.neconico.neconico.dto.item.card.ItemCardSearchViewDto;
import com.neconico.neconico.paging.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * main, 검색화면 item card
 * 아이템 등록, 수정, 삭제
 * 해당 item 카드 조회시 나올 item, store, 상품문의, 상품정보
 */
@Mapper
public interface ItemMapper {

    //아이템 조회 시 해당 item 관련 정보
    ItemInquireInfoDto selectItemByItemId(@Param("itemId") Long itemId);

    //아이템 수정 시 해당 item 관련정보
    ItemInfoDto selectItemByItemIdForUpdate(@Param("itemId") Long itemId);

    void insertItems(ItemInfoDto itemInfoDto);

    void updateItemInfo(ItemInfoDto itemInfoDto);

    void deleteItem(@Param("itemId") Long itemId);

    List<ItemCardSearchViewDto> selectItemBySearch(@Param("criteria") Criteria criteria,  //main 검색바 검색
                                                   @Param("search") SearchInfoDto searchInfoDto);

    Long selectTotalItemCount(@Param("search") SearchInfoDto searchInfoDto); //item 총 개수 count

    //main카테고리 조회
    List<ItemCardSearchViewDto> selectItemsByMainCategory(@Param("criteria") Criteria criteria, @Param("subCategoryList") List<CategorySubInfoDto> subCategoryList);

    Long selectTotalItemCountBySubCategoryList(@Param("subCategoryList") List<CategorySubInfoDto> subCategoryList);

    //sub카테고리 조회
    List<ItemCardSearchViewDto> selectItemsBySubCategoryId(@Param("criteria") Criteria criteria,
                                                 @Param("subId") Long subId);

    Long selectTotalItemCountBySubCategoryId(@Param("subId") Long subId);

    //조회수 추가
    void updateItemHits(@Param("itemId") Long itemId);
}
