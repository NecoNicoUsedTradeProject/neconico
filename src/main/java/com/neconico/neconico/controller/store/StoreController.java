package com.neconico.neconico.controller.store;

import com.neconico.neconico.dto.users.SessionUser;
import com.neconico.neconico.dto.users.UserInfoDto;
import com.neconico.neconico.service.store.StoreInfoService;
import com.neconico.neconico.service.store.StoreItemListService;
import com.neconico.neconico.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreInfoService infoService;
    private final StoreItemListService listService;


    @GetMapping("/store/{accountId}")
    public String store(@PathVariable(name = "accountId") String accountId, Model model) {

        SessionUser user = infoService.getSessionUserInfoByAccountId(accountId);
        model.addAttribute("storeInfo", infoService.findStoreInfo(user));

        HashMap<String, Long> itemCountMap = listService.createItemListCount(user.getUserId());
        itemCountMap.forEach((key, value) -> model.addAttribute(key, value));

        model.addAttribute("userId",user.getUserId());

        return "store/store";
    }


    @GetMapping("/store/mystore")
    public String myStore(Model model, SessionUser user) {

        user.setUserId(7L);
        user.setAccountId("id1");

        model.addAttribute("storeInfo", infoService.findStoreInfo(user));

        HashMap<String, Long> itemCountMap = listService.createItemListCount(user.getUserId());
        itemCountMap.forEach((key, value) -> model.addAttribute(key, value));

        return "store/my_store";
    }

}
