package com.neconico.neconico.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Alias("itemQuestionResponse")
public class ItemQuestionResponseDto {

    private Long itemId;
    private Long itemQuestionId;
    private String storeName;
    private String storeImgPath;
    private String content;
    private LocalDateTime createdDate;
}
