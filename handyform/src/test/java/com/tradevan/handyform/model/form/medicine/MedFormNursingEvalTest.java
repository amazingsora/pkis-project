package com.tradevan.handyform.model.form.medicine;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.model.form.medicine.elem.MedCustMasterElm;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: MedFormNursingEvalTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public class MedFormNursingEvalTest {

	private MedFormNursingEval medFormNursingEval;
	
	private String fileName = "MedFormNursingEvalSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormNursingEval = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormNursingEval.class);
	}
	
	@Test
	public void testGetTotalCount() {
		
		assertEquals(8, medFormNursingEval.getTotalCount());
	}
	
	@Test
	public void testGetPassCount() {
		
		assertEquals(4, medFormNursingEval.getPassCount());
	}
	
	@Test
	public void testGetPassStatus() {
		
		assertEquals("N", medFormNursingEval.getPassStatus());
	}
	
	@Test
	public void testGetNursingEvalNeedAdvising() {
		
		for (int x = 0; x < medFormNursingEval.getMasterList().size(); x++) {
			MedCustMasterElm elm = medFormNursingEval.getMasterList().get(x);
			if (x == 0) {
				assertEquals("3/4", elm.getNursingEvalNeedAdvising());
			}
			else if (x == 1) {
				assertEquals("1/4", elm.getNursingEvalNeedAdvising());
			}
		}
	}
	
	@Test
	public void testGetNursingEvalAdvised() {
		
		for (int x = 0; x < medFormNursingEval.getMasterList().size(); x++) {
			MedCustMasterElm elm = medFormNursingEval.getMasterList().get(x);
			if (x == 0) {
				assertEquals("2/3", elm.getNursingEvalAdvised());
			}
			else if (x == 1) {
				assertEquals("1/1", elm.getNursingEvalAdvised());
			}
		}
	}
	
	@Test
	public void testGetNursingEvalDone() {
		
		for (int x = 0; x < medFormNursingEval.getMasterList().size(); x++) {
			MedCustMasterElm elm = medFormNursingEval.getMasterList().get(x);
			if (x == 0) {
				assertEquals("1/3", elm.getNursingEvalDone());
			}
			else if (x == 1) {
				assertEquals("2/4", elm.getNursingEvalDone());
			}
		}
	}
}
