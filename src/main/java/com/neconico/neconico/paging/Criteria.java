package com.neconico.neconico.paging;

import lombok.*;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class Criteria {

    private int currentPage;       //현재 페이지
    private int contentPerPage;    //한 페이지에 나올 게시글 수
    private String sortingColumn;  //정렬할 열
    private String requestOrder;   //정렬


    public int getPageStart() {

        return (this.currentPage - 1) * contentPerPage;
    }
}


