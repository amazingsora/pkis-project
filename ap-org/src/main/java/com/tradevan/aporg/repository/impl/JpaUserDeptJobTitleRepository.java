package com.tradevan.aporg.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.UserDeptJobTitle;
import com.tradevan.aporg.repository.UserDeptJobTitleRepository;

/**
 * Title: JpaUserDeptJobTitleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaUserDeptJobTitleRepository extends JpaGenericRepository<UserDeptJobTitle, Long> implements UserDeptJobTitleRepository {
	
}
