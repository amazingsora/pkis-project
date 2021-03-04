package com.tradevan.aporg.repository;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserDeptRole;
import com.tradevan.aporg.model.UserProf;



public class UserProfRepositoryTest {

	@Autowired
	private UserProfRepository userProfRepository;
	
	@Autowired
	private AuthProfRepository authProfRepository;
	
	@Autowired
	private DeptProfRepository deptProfRepository;
	
	
	@Autowired
	private RoleProfRepository roleProfRepository;
//	@SuppressWarnings("rawtypes")
//	@Test
//	public void testFindUserIdAndEmailIn() {
//		List<Long> serNos = new ArrayList<Long>();
//		serNos.add(1L);
//		serNos.add(2L);
//		List list = userProfRepository.findByNamedQuery("UserProf.findUserIdAndEmailInSerNos", serNos);
//		assertNotNull(list);
//		list = userProfRepository.findByNamedQuery("UserProf.findUserIdAndEmailInDeptSerNos", 1L);
//		assertNotNull(list);
//		list = userProfRepository.findByNamedQuery("UserProf.findUserIdAndEmailInJobTypeSerNos", 1L);
//		assertNotNull(list);
//		list = userProfRepository.findByNamedQuery("UserProf.findUserIdAndEmailInJobRankSerNos", 1L);
//		assertNotNull(list);
//		list = userProfRepository.findByNamedQuery("UserProf.findUserIdAndEmailInTrainingOrgSerNos", 1L);
//		assertNotNull(list);
//	}
	

	public void testCrossJoinIssus() {
		//user check
		List<UserProf> list =userProfRepository.findAllEntityList();
		for (UserProf userProf : list) {
			
			System.out.println("使用者: "+ userProf.getAppId());
			DeptProf  d=userProf.getDept(); //關聯一
			System.out.println("1.部門: "+ d.getDeptId());
			Set<DeptProf>  u= userProf.getDepts();  //關聯二
			for (DeptProf deptProf : u) {
				System.out.println("2.隸屬其他部門: "+ deptProf.getDeptId());
			}
			Set<RoleProf>  r= userProf.getRoles(); //關聯三
			for (RoleProf roleProf : r) {
				System.out.println("3.隸屬角色: "+ roleProf.getRoleId());
			}
		}
		//check dept 
		List<DeptProf> list2 =deptProfRepository.findAllEntityList();;
		for (DeptProf deptProf : list2) {
			
			System.out.println("部門代號:: "+ deptProf.getDeptId());
			DeptProf  d=deptProf.getUpDept(); //關聯一
			if(null!=d && StringUtils.isNotEmpty(d.getDeptId())) {
				System.out.println("1.上層部門::"+ d.getDeptId());
			}else {
				System.out.println("notfound: getUpDept()");
			}
			
			Set<DeptProf>  aa= deptProf.getSubDepts(); //關聯二
			for (DeptProf deptProf2 : aa) { 
				System.out.println("2.子部門: "+ deptProf2.getDeptId());
			}
			Set<UserProf>  bb= deptProf.getOtherUsers(); //關聯三
			for (UserProf userProf : bb) {
				System.out.println("3.其他部門user: "+ userProf.getUserId());
			}
			Set<UserProf>  cc= deptProf.getUsers(); //關聯四
			for (UserProf userProf : cc) {
				System.out.println("4.部門下user: "+ userProf.getUserId());
			}
			
			Set<RoleProf>  roless= deptProf.getRoles(); //關聯五
			for (RoleProf roleProf : roless) {
				System.out.println("5.部門下角色: "+ roleProf.getRoleId());
			}
		}
		
		
		//check role 
				List<RoleProf> list3 = roleProfRepository.findAllEntityList();;
				for (RoleProf roleProf : list3) {
					
					System.out.println("角色ID: "+ roleProf.getRoleId());
					System.out.println("角色所屬部門: "+ roleProf.getDeptId());
					Set<UserProf>  userProfs = roleProf.getActors();
					for (UserProf userProf : userProfs) {
						System.out.println("1a.角色下有使用者= "+userProf.getUserId());
						System.out.println("1b.使用者所屬部門= "+userProf.getDeptId());
					}
					//roleProf.get
					
				}
		
		
//		deptProfRepository.findAllEntityList();
		UserProf userProf = new UserProf();
//		userProf.setAppId("TEST");
//		userProf
		//userProf.setPhone(0);
		userProf.setAppId("123444356");
		
		//authProfRepository.findAllEntityList();
		//List<UserProf>  list =userProfRepository.findAllEntityList();
		//assertNotNull(list);
		//userProfRepository.findByProperty(new String[] { "USER_ID" }, "dept.id", 1L, "userId"); // dept.id will cause cross join issue
		//userProfRepository.findByProperty(new String[] { "USER_ID" }, "dept.id", 1L, "userId", "dept"); // fix dept.id cause cross join issue 
	}
	
}
