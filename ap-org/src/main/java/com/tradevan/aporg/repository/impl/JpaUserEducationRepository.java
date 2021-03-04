package com.tradevan.aporg.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.UserEducation;
import com.tradevan.aporg.repository.UserEducationRepository;

@Repository
public class JpaUserEducationRepository extends JpaGenericRepository<UserEducation, Long> implements UserEducationRepository {

}
