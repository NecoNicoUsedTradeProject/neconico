package com.neconico.neconico.dto.store.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Alias("storereviewcarddto")
//상품 문의, 후기 카드
public class StoreReviewCardDto {

    //storereview
    private final Long reviewId;
    private final String writerName;
    private final String accountId;
    private final String content;
    private final LocalDateTime replyCreatedTime;

    //item
    private final Long itemId;
    private final String title;
    private final String price;
    private final String itemImg;
    private final LocalDateTime createdTime;
    private final String status;
    private final String views;

}
