package com.micheladas.chelas.service;

import com.micheladas.chelas.entity.BigBottle;
import com.micheladas.chelas.entity.UserIp;
import com.micheladas.chelas.repository.UserIpRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserIpServiceImpl implements UserIpService{

    private final UserIpRepository userIpRepository;

    public UserIpServiceImpl(UserIpRepository userIpRepository) {

        this.userIpRepository = userIpRepository;
    }

    @Override
    public Page<UserIp> findAll(Pageable pageable) {

        return userIpRepository.findAll(pageable);
    }

    @Override
    public List<UserIp> findAllUserIps() {


        return userIpRepository.findAll();
    }
}
