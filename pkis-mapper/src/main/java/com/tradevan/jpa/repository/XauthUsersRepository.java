package com.tradevan.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tradevan.jpa.entity.XauthUsers;
import com.tradevan.jpa.entity.XauthUsersKey;

@Repository("XauthUsersRepository")
public interface XauthUsersRepository extends JpaRepository<XauthUsers, XauthUsersKey> {
	
}
