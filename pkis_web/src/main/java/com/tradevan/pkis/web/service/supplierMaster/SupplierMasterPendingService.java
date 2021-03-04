package com.tradevan.pkis.web.service.supplierMaster;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.xauthframework.web.service.DefaultService;

@Service("SupplierMasterPendingService")
@Transactional(rollbackFor = Exception.class)
public class SupplierMasterPendingService extends DefaultService {
	
}
