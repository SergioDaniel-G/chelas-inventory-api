package com.micheladas.chelas.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.micheladas.chelas.entity.UserAccount;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {

	UserAccount findByEmail(String email);

	UserAccount findByEmailAndMobileNumber(String email, String mobileNum);

}
