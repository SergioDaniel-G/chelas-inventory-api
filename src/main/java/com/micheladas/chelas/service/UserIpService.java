package com.micheladas.chelas.service;

import com.micheladas.chelas.entity.BigBottle;
import com.micheladas.chelas.entity.UserIp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserIpService {
    public Page<UserIp> findAll(Pageable pageable);

    List<UserIp> findAllUserIps();
}
