package com.neconico.neconico.paging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaginationTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    @DisplayName("Pagination Test")
    void test(){

          //given
        Criteria cri = new Criteria();
        cri.setCurrentPage(14);
        cri.setContentPerPage(10);
        cri.setSortingColumn("noticeId");
        cri.setRequestOrder("desc");


        //when
        Pagination pagination = new Pagination(cri,300,10);

        //then
        log.info("info log={}",pagination);
        assertThat(pagination.getEndPage()).isEqualTo(20);

    }
}