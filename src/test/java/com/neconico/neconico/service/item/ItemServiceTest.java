package com.neconico.neconico.service.item;

import com.neconico.neconico.dto.category.CategorySubInfoDto;
import com.neconico.neconico.dto.file.FileResultInfoDto;
import com.neconico.neconico.dto.item.ItemInfoDto;
import com.neconico.neconico.dto.item.ItemInquireInfoDto;
import com.neconico.neconico.dto.item.SearchInfoDto;
import com.neconico.neconico.dto.item.card.ItemCardSearchViewDto;
import com.neconico.neconico.dto.store.StoreInfoDto;
import com.neconico.neconico.dto.users.UserJoinDto;
import com.neconico.neconico.paging.Criteria;
import com.neconico.neconico.service.category.CategoryService;
import com.neconico.neconico.service.store.StoreInfoService;
import com.neconico.neconico.service.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ItemServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StoreInfoService storeInfoService;

    private Long userId;

    private List<Long> itemIds = new ArrayList<>();

    @BeforeEach
    void insertUserAndItems() {
        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setAccountId("user1");
        userJoinDto.setAccountPw("1234");
        userJoinDto.setAccountName("user");
        userJoinDto.setGender("M");
        userJoinDto.setBirthdate("980631");
        userJoinDto.setEmail("user" + "@gmail.com");
        userJoinDto.setPhoneNumber("010-1111-1111");
        userJoinDto.setAddress("?????????");
        userJoinDto.setZipNo("01583");
        userJoinDto.setInfoAgreement("check");
        userJoinDto.setCreateDate(LocalDateTime.of(2021, 04, 29, 04, 43));
        userJoinDto.setModifiedDate(LocalDateTime.of(2021, 04, 29, 04, 43));
        userJoinDto.setAuthority("ROLE_USER");
        userService.joinUser(userJoinDto);

        this.userId = userJoinDto.getUserId();
        StoreInfoDto store = new StoreInfoDto(
                userJoinDto.getUserId(),
                userJoinDto.getAccountName(),
                "",
                "user1??? store",
                "");
        storeInfoService.createStoreInfo(store);

        List<CategorySubInfoDto> categorySubInfoDtoList = categoryService.findCategorySubAll();

        for (int i = 1; i <= 10; i++) {
            CategorySubInfoDto categorySubInfoDto = categorySubInfoDtoList.get(i);

            FileResultInfoDto fileResultInfoDto = new FileResultInfoDto(
                    "https://",
                    "2fegd22f-2ffdimgs.png");

            ItemInfoDto itemInfoDto = new ItemInfoDto();
            itemInfoDto.setUserId(this.userId);
            itemInfoDto.setTitle("????????? ??????" + i);
            itemInfoDto.setContent("????????? ??????" + i);
            itemInfoDto.setPrice(i + "0,000");
            //5?????? ?????????, ????????? ??????
            if(i%2 ==0 ) {
                itemInfoDto.setArea("??????????????? ?????????");
            }else{
                itemInfoDto.setArea("????????? ?????????");
            }
            itemInfoDto.setItemStatus("??????");
            itemInfoDto.setHits(0);
            itemInfoDto.setCreatedDate(LocalDateTime.now());
            itemInfoDto.setModifiedDate(LocalDateTime.now());
            itemInfoDto.setSaleStatus("?????? ???");
            itemInfoDto.setShippingPrice("??????");


            itemService.insertItem(fileResultInfoDto, categorySubInfoDto.getCategorySubId(), itemInfoDto);

            //DB??? ???????????? auto_increment??? ????????? itemId??? ???????????? ????????? ???????????? ????????????.
            this.itemIds.add(itemInfoDto.getItemId());
        }
    }


    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition} / {totalRepetitions}")
    @DisplayName("10?????? item??? DB??? ????????? ??? ????????? itemId?????? item????????? ????????????.")
    void item_information_is_retrieved_by_the_created_itemId() throws Exception {
        //given
        Long itemId = getItemId();

        //when
        ItemInquireInfoDto findItemInfo = itemService.findItemByItemId(itemId);

        //then
        assertAll(
                () -> assertThat(findItemInfo.getItemId()).isEqualTo(itemId),
                () -> assertThat(findItemInfo.getStoreInquireInfoDto()).isNotNull()
        );
    }

    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition} / {totalRepetitions}")
    @DisplayName("????????? ????????? ?????? ????????? ??? ???????????? DB??? ??????")
    void changes_are_reflected_in_the_DB() throws Exception {
        //given
        Long itemId = getItemId();


        ItemInfoDto itemInfoDto = itemService.findItemByItemIdForUpdate(itemId); // ?????? DB??? ????????? ???????????????

        FileResultInfoDto fileResultInfoDto = new FileResultInfoDto(
                "https//fdd",
                "2f23f-f3fd-fdn?????????.png"); // ????????? ??????

        String[] currentFiles = itemInfoDto.getItemImgUrls().split(">");

        //when
        itemInfoDto.setTitle("????????????");
        itemInfoDto.setContent("?????? ??????");

        itemService.changeItemInfo(fileResultInfoDto, currentFiles, itemInfoDto);
        ItemInquireInfoDto changeItemInfo = itemService.findItemByItemId(itemId);

        //then
        assertAll(
                () -> assertThat(changeItemInfo.getTitle()).isEqualTo("????????????"),
                () -> assertThat(changeItemInfo.getContent()).isEqualTo("?????? ??????"),
                () -> assertThat(fileResultInfoDto.getFileUrls())
                        .isEqualTo(changeItemInfo.getItemImgUrls())
        );
    }

    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition} / {totalRepetitions}")
    @DisplayName("item ?????? ????????? DB?????? ?????? ???????????????")
    void delete_the_item_from_DB_when_requesting_to_delete_an_item() throws Exception {
        //given
        Long itemId = getItemId();

        //when
        itemService.removeItem(itemId);
        ItemInquireInfoDto findItemInfoDto = itemService.findItemByItemId(itemId);

        //then
        assertThat(findItemInfoDto).isNull();
    }
    @Test
    @DisplayName("???????????? ????????? ?????? ??? ?????? ????????? ??????")
    void search_for_relevant_items_when_searching_for_a_item_transaction_area_name() throws Exception {
        //given
        Criteria criteria = getCriteria(); //url??? ????????? ?????? ?????????

        SearchInfoDto searchInfoDto = getSearchInfoDto("??????");

        //when
        List<ItemCardSearchViewDto> itemInfoDtoList = itemService.searchItems(criteria, searchInfoDto);

        //then
        assertThat(itemInfoDtoList).hasSize(5);
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ??? ?????? ????????? ??????")
    void search_for_relevant_items_when_searching_for_a_item_transaction_title() throws Exception {
        //given
        Criteria criteria = getCriteria(); //url??? ????????? ?????? ?????????

        SearchInfoDto searchInfoDto = getSearchInfoDto("??????");

        //when
        List<ItemCardSearchViewDto> itemInfoDtoList = itemService.searchItems(criteria, searchInfoDto);

        //then
        assertThat(itemInfoDtoList)
                .hasSize(10)
                .anyMatch(i -> i.getTitle().contains("??????"));
    }

    @Test
    @DisplayName("DB??? ????????? item??? ??? ?????? ????????????.")
    void count_the_total_number_of_items_stored_in_the_DB() throws Exception {
        SearchInfoDto searchInfoDto = new SearchInfoDto();
        searchInfoDto.setSearchText("");
        Long totalItemCount = itemService.countTotalItems(searchInfoDto);

        assertThat(totalItemCount).isEqualTo(itemIds.size());
    }

    private Long getItemId() {
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        return this.itemIds.get(randomNumber);
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setCurrentPage(1L);

        return criteria;
    }

    private SearchInfoDto getSearchInfoDto(String searchText) {
        SearchInfoDto searchInfoDto = new SearchInfoDto();
        searchInfoDto.setSearchText(searchText);
        return searchInfoDto;
    }

}