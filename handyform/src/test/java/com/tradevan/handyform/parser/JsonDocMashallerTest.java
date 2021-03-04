package com.tradevan.handyform.parser;

import org.junit.Test;

import com.tradevan.handyform.marshaller.JsonDocMarshaller;
import com.tradevan.handyform.model.exam.Examination;
import com.tradevan.handyform.model.form.medicine.MedFormAssessment;
import com.tradevan.handyform.model.form.medicine.MedFormCaseLog;
import com.tradevan.handyform.model.form.medicine.MedFormCbd;
import com.tradevan.handyform.model.form.medicine.MedFormCustomMulti;
import com.tradevan.handyform.model.form.medicine.MedFormCustomSingle;
import com.tradevan.handyform.model.form.medicine.MedFormDops;
import com.tradevan.handyform.model.form.medicine.MedFormEval360;
import com.tradevan.handyform.model.form.medicine.MedFormMeeting;
import com.tradevan.handyform.model.form.medicine.MedFormMilestone;
import com.tradevan.handyform.model.form.medicine.MedFormMiniCex;
import com.tradevan.handyform.model.form.medicine.MedFormNursingEval;
import com.tradevan.handyform.model.form.medicine.MedFormOtherNoSc;
import com.tradevan.handyform.model.form.medicine.MedFormOtherSc;
import com.tradevan.handyform.model.form.medicine.MedFormResidentAssess;
import com.tradevan.handyform.model.form.medicine.MedFormStudyEval;
import com.tradevan.handyform.model.form.medicine.elem.MedCustMasterElm;
import com.tradevan.handyform.model.ques.QuesSatisfaction;
import com.tradevan.handyform.model.ques.QuesVote;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: JsonDocParserTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class JsonDocMashallerTest {
	private JsonDocMarshaller mashaller = new JsonDocMarshaller();
	
	@Test
	public void testQuesSatisfaction() {
		testMarshalUnmarshalInternal("QuesSatisfactionSample", QuesSatisfaction.class);
		testMarshalUnmarshalInternal("QuesSatisfactionSampleReply", QuesSatisfaction.class);
	}
	
	private <T> void testMarshalUnmarshalInternal(String fileName, Class<T> clazz) {
		try {
			System.out.println(fileName + ":");
			String jsonStr = FormTestUtil.getInstance().getTestJsonStr(fileName, clazz);	
			T doc = mashaller.unmarshal(jsonStr, clazz);
			System.out.println(mashaller.marshal(doc));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testQuesVote() {
		testMarshalUnmarshalInternal("QuesVoteSample", QuesVote.class);
		testMarshalUnmarshalInternal("QuesVoteSampleReply", QuesVote.class);
	}
	
	@Test
	public void testExamNormal() {
		testMarshalUnmarshalInternal("ExamNormalSample", Examination.class);
		testMarshalUnmarshalInternal("ExamNormalSampleReply", Examination.class);
	}
	
	@Test
	public void testExamRandom() {
		testMarshalUnmarshalInternal("ExamRandomSample", Examination.class);
		testMarshalUnmarshalInternal("ExamRandomSampleReply", Examination.class);
	}
	
	@Test
	public void testMedFormAssessment() {
		testMarshalUnmarshalInternal("MedFormAssessmentSample", MedFormAssessment.class);
		testMarshalUnmarshalInternal("MedFormAssessmentSampleReply", MedFormAssessment.class);
	}
	
	@Test
	public void testMedFormCaseLog() {
		testMarshalUnmarshalInternal("MedFormCaseLogSample", MedFormCaseLog.class);
		testMarshalUnmarshalInternal("MedFormCaseLogSampleReply", MedFormCaseLog.class);
		testMarshalUnmarshalInternal("MedFormCaseLogSampleReply2", MedFormCaseLog.class);
	}
	
	@Test
	public void testMedFormCbd() {
		testMarshalUnmarshalInternal("MedFormCbdSample", MedFormCbd.class);
		testMarshalUnmarshalInternal("MedFormCbdSampleReply", MedFormCbd.class);
	}
	
	@Test
	public void testMedFormDops() {
		testMarshalUnmarshalInternal("MedFormDopsSample", MedFormDops.class);
		testMarshalUnmarshalInternal("MedFormDopsSampleReply", MedFormDops.class);
	}
	
	@Test
	public void testMedFormEval360() {
		testMarshalUnmarshalInternal("MedFormEval360Sample", MedFormEval360.class);
		testMarshalUnmarshalInternal("MedFormEval360SampleReply", MedFormEval360.class);
	}
	
	@Test
	public void testMedFormMilestone() {
		testMarshalUnmarshalInternal("MedFormMilestoneSample", MedFormMilestone.class);
		testMarshalUnmarshalInternal("MedFormMilestoneSampleReply", MedFormMilestone.class);
	}
	
	@Test
	public void testMedFormMiniCex() {
		testMarshalUnmarshalInternal("MedFormMiniCexSample", MedFormMiniCex.class);
		testMarshalUnmarshalInternal("MedFormMiniCexSampleReply", MedFormMiniCex.class);
	}
	
	@Test
	public void testMedFormOtherSc() {
		testMarshalUnmarshalInternal("MedFormOtherScSample", MedFormOtherSc.class);
		testMarshalUnmarshalInternal("MedFormOtherScSampleReply", MedFormOtherSc.class);
	}
	
	@Test
	public void testMedFormOtherNoSc() {
		testMarshalUnmarshalInternal("MedFormOtherNoScSample", MedFormOtherNoSc.class);
		testMarshalUnmarshalInternal("MedFormOtherNoScSampleReply", MedFormOtherNoSc.class);
	}
	
	@Test
	public void testMedFormStudyEval() {
		testMarshalUnmarshalInternal("MedFormStudyEvalSample", MedFormStudyEval.class);
		testMarshalUnmarshalInternal("MedFormStudyEvalSampleReply", MedFormStudyEval.class);
	}
	
	@Test
	public void testMedFormMeeting() {
		testMarshalUnmarshalInternal("MedFormMeetingSample", MedFormMeeting.class);
		testMarshalUnmarshalInternal("MedFormMeetingSampleReply", MedFormMeeting.class);
	}
	
	@Test
	public void testMedFormCustomSingle() {
		testMarshalUnmarshalInternal("MedFormCustomSingleSample1", MedFormCustomSingle.class);
		testMarshalUnmarshalInternal("MedFormCustomSingleSample1Reply", MedFormCustomSingle.class);
		testMarshalUnmarshalInternal("MedFormCustomSingleSample2", MedFormCustomSingle.class);
		testMarshalUnmarshalInternal("MedFormCustomSingleSample2Reply", MedFormCustomSingle.class);
		testMarshalUnmarshalInternal("MedFormCustomSingleSample3", MedFormCustomSingle.class);
		testMarshalUnmarshalInternal("MedFormCustomSingleSample3Reply", MedFormCustomSingle.class);
		testMarshalUnmarshalInternal("MedFormCustomSingleSample4", MedFormCustomSingle.class);
		testMarshalUnmarshalInternal("MedFormCustomSingleSample4Reply", MedFormCustomSingle.class);
	}
	
	@Test
	public void testMedFormCustomMulti() {
		testMarshalUnmarshalInternal("MedFormCustomMultiSample", MedFormCustomMulti.class);
		testMarshalUnmarshalInternal("MedFormCustomMultiSampleReply", MedFormCustomMulti.class);
		testMarshalUnmarshalInternal("MedFormCustomMultiSampleReplyM1", MedCustMasterElm.class);
		testMarshalUnmarshalInternal("MedFormCustomMultiSampleReplyM2", MedCustMasterElm.class);
	}
	
	@Test
	public void testMedFormResidentAssess() {
		testMarshalUnmarshalInternal("MedFormResidentAssessSample", MedFormResidentAssess.class);
		testMarshalUnmarshalInternal("MedFormResidentAssessSampleReply", MedFormResidentAssess.class);
	}
	
	@Test
	public void testMedFormNursingEval() {
		testMarshalUnmarshalInternal("MedFormNursingEvalSample", MedFormNursingEval.class);
		testMarshalUnmarshalInternal("MedFormNursingEvalSampleReply", MedFormNursingEval.class);
	}
}
