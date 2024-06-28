package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.MyUser;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {

	Optional<MyUser> findByUserName(String userName);

	Optional<MyUser> findByEmailId(String emailId);

}
