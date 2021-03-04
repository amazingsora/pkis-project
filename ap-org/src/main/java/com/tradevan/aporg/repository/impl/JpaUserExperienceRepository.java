package com.tradevan.aporg.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.UserExperience;
import com.tradevan.aporg.repository.UserExperienceRepository;

@Repository
public class JpaUserExperienceRepository extends JpaGenericRepository<UserExperience, Long> implements UserExperienceRepository {
	
}
