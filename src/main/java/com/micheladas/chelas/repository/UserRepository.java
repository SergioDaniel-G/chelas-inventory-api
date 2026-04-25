package com.micheladas.chelas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.micheladas.chelas.entity.UserAccount;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {

	public UserAccount findByEmail(String email);

	public UserAccount findByEmailAndMobileNumber(String email, String mobileNum);

}
