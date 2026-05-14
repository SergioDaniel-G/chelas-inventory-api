package com.micheladas.chelas.repository;

import com.micheladas.chelas.entity.UserIp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserIpRepository extends JpaRepository<UserIp, Long> {

    // KEY METHOD FOR AUTOMATIC FILTERING/THROTTLING
    boolean existsByEmailAndIpAddressAndStatus(String email, String ipAddress, String status);

    // RETRIEVE ALL LOGIN ATTEMPTS FOR A SPECIFIC USER
    List<UserIp> findByEmailOrderByLoginTimeDesc(String email);

    // DETECT POTENTIAL ATTACKS
    List<UserIp> findByIpAddressOrderByLoginTimeDesc(String ipAddress);
}
