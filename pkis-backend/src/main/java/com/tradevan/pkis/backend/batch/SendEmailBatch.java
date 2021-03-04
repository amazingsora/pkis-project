package com.tradevan.pkis.backend.batch;

import com.tradevan.pkis.backend.service.SendEmailService;

public class SendEmailBatch extends CommonBatch {
	
	private SendEmailService service = new SendEmailService();
	
	public static void main(String[] args) throws Exception {
		SendEmailBatch main = new SendEmailBatch();
		String arg = "";
		if (args.length > 0) {
			arg = args[0];
		}
		main.init(SendEmailBatch.class);
		main.runLoop(arg, main.getClass().getSimpleName());
	}
	
	private void init() throws Exception {
		service = (SendEmailService) ctx.getBean("SendEmailService");
	}

	@Override
	protected String process(String arg) throws Exception {
		String errorMsg = "";
		try {
			// init
			this.init();
			errorMsg = service.sendEmailSpring();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			logger.info("####### SendEmailBatch END #######");
		}
		
		return errorMsg;
	}
}
