package com.neconico.neconico.service.store;

import com.neconico.neconico.dto.file.FileResultInfoDto;
import com.neconico.neconico.dto.store.StoreInfoDto;
import com.neconico.neconico.dto.store.card.StoreInfoCardDto;
import com.neconico.neconico.dto.users.SessionUser;
import com.neconico.neconico.mapper.store.StoreInfoMapper;
import com.neconico.neconico.mapper.store.StoreItemListMapper;
import com.neconico.neconico.mapper.users.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreInfoServiceImpl implements StoreInfoService {

    private final StoreInfoMapper storeInfoMapper;
    private final StoreItemListMapper storeItemListMapper;
    private final UserMapper userMapper;

    @Override
    public StoreInfoCardDto findStoreInfo(SessionUser user) {

        StoreInfoCardDto storeInfo = new StoreInfoCardDto();

        StoreInfoDto storeDto = storeInfoMapper.selectStoreInfoByUser(user.getUserId());
        storeInfo.setStoreInfo(storeDto.getStoreInfo());
        storeInfo.setStoreName(storeDto.getStoreName());
        storeInfo.setStoreImgUrl(storeDto.getStoreImgUrl());

        storeInfo.setCreated(
                calculateCreatedDate(user.getAccountId())
        );

        storeInfo.setSoldCount(storeItemListMapper.countStoreSoldItem(user.getUserId()));

        return storeInfo;
    }

    @Override
    @Transactional
    public void createStoreInfo(StoreInfoDto storeInfoDto) {
        storeInfoMapper.insertStoreInfo(storeInfoDto);
    }

    @Override
    @Transactional
    public void updateStoreInfo(StoreInfoDto storeInfoDto) {
        if (storeInfoDto.getStoreName() != null) {
            if (storeInfoMapper.selectStoreInfoByName(storeInfoDto.getStoreName())) {
                throw new IllegalArgumentException("Exist same storename");
            }
        }
        storeInfoMapper.updateStoreInfo(storeInfoDto);
    }

    @Override
    @Transactional
    public void updateStoreImg(FileResultInfoDto fileResultInfoDto, StoreInfoDto storeInfoDto) throws IOException {

        storeInfoDto.setStoreImgUrl(fileResultInfoDto.getFileUrls());
        storeInfoDto.setStoreImgName(fileResultInfoDto.getFileNames());

        // ???????????? ???, ????????? ????????? ?????? ???????????? ??????????????? ????????? ??????
        storeInfoDto.setStoreName(null);
        storeInfoDto.setStoreInfo(null);

        updateStoreInfo(storeInfoDto);

    }

    public String calculateCreatedDate(String accountId) {
        LocalDate created = userMapper.selectUserByAccountId(accountId).getCreatedDate().toLocalDate();
        LocalDate now = LocalDate.now();

        Long range = ChronoUnit.DAYS.between(created, now);

        return range + "??? ???";
    }

    @Override
    public SessionUser getSessionUserInfoByAccountId(String accountId) {
        return userMapper.selectSessionUserInfoByAccountId(accountId);
    }

    @Override
    public StoreInfoDto findStoreInfoByUserId(Long userId) {
        return storeInfoMapper.selectStoreInfoByUser(userId);
    }



}
