package com.tradevan.handyform.model.form.medicine;

import java.math.BigDecimal;
import java.util.List;

import com.tradevan.handyform.enums.MedCustFormResult;
import com.tradevan.handyform.enums.MedCustFormScoring;
import com.tradevan.handyform.model.form.medicine.elem.MedCustDetailElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustEvalElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustMasterElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustOptionElm;

public abstract class BaseCustMedForm extends BaseMedForm {
	
	protected BigDecimal sumCustFormScoreInternal(List<MedCustMasterElm> masterList, int byReplyN) {
		BigDecimal score = new BigDecimal(0);
		for (MedCustMasterElm master : masterList) {
			for (MedCustDetailElm detail : master.getDetailList()) {
				if (detail.getFormScoring() == MedCustFormScoring.SF) { // 分數欄位
					BigDecimal sc = convertReplyToScore(detail, byReplyN, true);
					if (sc != null) {
						score = score.add(sc);
					}
				}
			}
		}
		return score;
	}
	
	private BigDecimal convertReplyToScore(MedCustDetailElm detail, int byReplyN, boolean forceConvert) {
		// 表單結果 "表單分數"欄位是"多項評量"時，需依多項評量欄位的"計分判斷方式"計算
		if (detail.getIsEval() != null && detail.getIsEval() && detail.getEvalItems() != null) { // 多項評量
			BigDecimal topScore = null, lowScore = null, sumScore = new BigDecimal(0);
			int cntNum = 0;
			for (MedCustEvalElm evalElm : detail.getEvalItems()) {
				if (evalElm.getIsNoScoring() != null && evalElm.getIsNoScoring()) {
					continue; // 20190526: 增加多項評量不計分項目
				}
				String reply = evalElm.getReply();
				switch (byReplyN) {
				case 2:
					reply = evalElm.getReply2();
					break;
				}
				BigDecimal sc = convert2Score(detail.getOptions(), reply, forceConvert);
				if (sc != null && sc.intValue() != Integer.MAX_VALUE) {
					if (topScore == null || sc.compareTo(topScore) == 1) {
						topScore = sc;
					}
					if (lowScore == null || sc.compareTo(lowScore) == -1) {
						lowScore = sc;
					}
					sumScore = sumScore.add(sc);
				}
				if (sc == null || (sc != null && sc.intValue() != Integer.MAX_VALUE)) { // 20190528: 增加多項評量答案"不需評核"判斷
					cntNum++;
				}
			}
			BigDecimal score = null;
			if (detail.getScoringType() != null) {
				switch (detail.getScoringType()) {
				case AVG: // 平均分數
					if (cntNum != 0) {
						score = sumScore.divide(new BigDecimal(cntNum), 1, BigDecimal.ROUND_HALF_UP);
					}
					else {
						score = BigDecimal.ZERO;
					}
					break;
				case TOP: // 最高分數
					score = topScore;
					break;
				case LOW: // 最低分數
					score = lowScore;
					break;
				case SUM: // 分數合計
					score = sumScore;
					break;
				default:
					break;
				}
			}
			else {
				score = sumScore;
			}
			return score;
		}
		else { // (非)多項評量
			String reply = detail.getReply();
			switch (byReplyN) {
			case 2:
				reply = detail.getReply2();
				break;
			case 3:
				reply = detail.getReply3();
				break;
			case 4:
				reply = detail.getReply4();
				break;
			}
			BigDecimal sc = convert2Score(detail.getOptions(), reply, forceConvert);
			if (sc != null && sc.intValue() == Integer.MAX_VALUE) {
				sc = null;
			}
			return sc;
		}
	}
	
	private BigDecimal convert2Score(List<MedCustOptionElm> options, String reply, boolean forceConvert) {
		if (reply != null) {
			if (forceConvert) {
				BigDecimal sc = convert2Score(options, reply);
				if (sc != null) {
					return sc;
				}
			}
			try {
				return new BigDecimal(reply);
			}
			catch (Exception e) {
				BigDecimal sc = convert2Score(options, reply);
				if (sc != null) {
					return sc;
				}
			}
		}
		return null;
	}
	
	private BigDecimal convert2Score(List<MedCustOptionElm> options, String reply) {
		if (reply != null && options != null) {
			for (MedCustOptionElm elm : options) {
				if (reply.equals(elm.getValue())) {
					if (elm.getIsNoScoring() != null && elm.getIsNoScoring()) { // 20190528: 增加多項評量答案"不需評核"判斷
						return new BigDecimal(Integer.MAX_VALUE);
					}
					else if (elm.getScoreConv() != null) {
						return elm.getScoreConv();
					}
				}
			}
		}
		return null;
	}
	
	protected BigDecimal calcScoreInternal(List<MedCustMasterElm> masterList, int byReplyN, Integer masterId, Integer detailId) {
		return calcScoreInternal(masterList, byReplyN, masterId, detailId, false);
	}
	
	protected BigDecimal calcScoreInternal(List<MedCustMasterElm> masterList, int byReplyN, Integer masterId, Integer detailId, boolean force) {
		for (MedCustMasterElm master : masterList) {
			if (masterId != null && !masterId.equals(master.getId())) {
				continue;
			}
			BigDecimal sc = calcDetailScoreInternal(master.getDetailList(), byReplyN, detailId, force);
			if (sc != null) {
				return sc;
			}
		}
		return null;
	}
	
	protected BigDecimal calcDetailScoreInternal(List<MedCustDetailElm> detailList, int byReplyN, Integer detailId) {
		return calcDetailScoreInternal(detailList, byReplyN, detailId, false);
	}
	
	protected BigDecimal calcDetailScoreInternal(List<MedCustDetailElm> detailList, int byReplyN, Integer detailId, boolean force) {
		for (MedCustDetailElm detail : detailList) {
			if (detailId != null && !detailId.equals(detail.getId())) {
				continue;
			}
			if (detail.getFormResult() == MedCustFormResult.FS || detail.getFormResult() == MedCustFormResult.FS_ST || force) { // 表單分數
				if (detail.getIsEval() != null && detail.getIsEval() && detail.getEvalItems() != null) { // 表單結果 "表單分數"欄位是"多項評量"時，需依多項評量欄位的"通過判斷方式"計算
					return convertReplyToScore(detail, byReplyN, true);
				}
				else {
					return convertReplyToScore(detail, byReplyN, false);
				}
			}
		}
		return null;
	}
	
	protected Integer calcSatisfactionInternal(List<MedCustMasterElm> masterList) {
		for (MedCustMasterElm master : masterList) {
			for (MedCustDetailElm detail : master.getDetailList()) {
				if (detail.getFormResult() == MedCustFormResult.S1) { // 受評者滿意度分數
					BigDecimal d = convertReplyToScore(detail, 1, true);
					if (d != null) {
						return d.intValue();
					}
				}
			}
		}
		return null;
	}
	
	protected Integer calcSatisfaction2Internal(List<MedCustMasterElm> masterList) {
		for (MedCustMasterElm master : masterList) {
			for (MedCustDetailElm detail : master.getDetailList()) {
				if (detail.getFormResult() == MedCustFormResult.S2) { // 評量者滿意度分數
					BigDecimal d = convertReplyToScore(detail, 1, true);
					if (d != null) {
						return d.intValue();
					}
				}
			}
		}
		return null;
	}
	
	protected String getPassStatusInternal(List<MedCustMasterElm> masterList, int byReplyN, Integer masterId, Integer detailId) {
		return getPassStatusInternal(masterList, byReplyN, masterId, detailId, false);
	}
	
	protected String getPassStatusInternal(List<MedCustMasterElm> masterList, int byReplyN, Integer masterId, Integer detailId, boolean force) {
		String passStatus = calcPassStatusInternal(masterList, byReplyN, masterId, detailId, force);
		if (passStatus == null) {
			for (MedCustMasterElm master : masterList) {
				if (masterId != null && !masterId.equals(master.getId())) {
					continue;
				}
				passStatus = getDetailPassStatusInternal(master.getDetailList(), byReplyN, detailId);
				if (passStatus != null) {
					return passStatus;
				}
			}
		}
		return passStatus;
	}
	
	private String calcPassStatusInternal(List<MedCustMasterElm> masterList, int byReplyN, Integer masterId, Integer detailId, boolean force) {
		for (MedCustMasterElm master : masterList) {
			if (masterId != null && !masterId.equals(master.getId())) {
				continue;
			}
			for (MedCustDetailElm detail : master.getDetailList()) {
				if (detailId != null && !detailId.equals(detail.getId())) {
					continue;
				}
				// 表單結果 "通過狀態"欄位是"多項評量"時，需依多項評量欄位的"通過判斷方式"計算
				if ((detail.getFormResult() == MedCustFormResult.ST || detail.getFormResult() == MedCustFormResult.FS_ST || force) 
						&& detail.getIsEval() != null && detail.getIsEval() && detail.getEvalItems() != null) {
					int totalCnt = 0, passCnt = 0, noPassCnt = 0;
					for (MedCustEvalElm evalElm : detail.getEvalItems()) {
						if (evalElm.getIsNoScoring() != null && evalElm.getIsNoScoring()) {
							continue; // 20190526: 增加多項評量不計分項目
						}
						String reply = evalElm.getReply();
						switch (byReplyN) {
						case 2:
							reply = evalElm.getReply2();
							break;
						}
						String pass = convert2PassStatus(detail.getOptions(), reply);
						if ("Y".equals(pass)) {
							passCnt++;
						}
						else if ("N".equals(pass)) {
							noPassCnt++;
						}
						
						if (! "X".equals(pass)) {
							totalCnt++;
						}
					}
					
					if (totalCnt > 0 && detail.getPassType() != null) {
						switch (detail.getPassType()) {
						case ALL: // 全部通過
							return totalCnt == passCnt ? "Y" : "N";
						case SP: // 單項通過
							return passCnt > 0 ? "Y" : "N";
						case SNP: // 單項不通過
							return noPassCnt > 0 ? "Y" : "N";
						case MAJOR: // 多數判斷
							return passCnt > noPassCnt ? "Y" : "N";
						default:
							break;
						}
					}
				}
			}
		}
		return null;
	}
	
	private String convert2PassStatus(List<MedCustOptionElm> options, String reply) {
		if (reply != null && options != null) {
			for (MedCustOptionElm elm : options) {
				if (reply.equals(elm.getValue())) {
					if (elm.getIsNoScoring() != null && elm.getIsNoScoring()) { // 20190528: 增加多項評量答案"不需評核"判斷
						return "X";
					}
					else {
						return elm.getPassConv() != null && elm.getPassConv() == true ? "Y" : "N";
					}
				}
			}
		}
		return null;
	}
	
	protected String getDetailPassStatusInternal(List<MedCustDetailElm> detailList, int byReplyN, Integer detailId) {
		for (MedCustDetailElm detail : detailList) {
			if (detailId != null && !detailId.equals(detail.getId())) {
				continue;
			}
			if (detail.getFormResult() == MedCustFormResult.ST || detail.getFormResult() == MedCustFormResult.FS_ST) { // 通過狀態 
				return convertReply2PassStatus(detail, byReplyN);
			}
		}
		return null;
	}
	
	private String convertReply2PassStatus(MedCustDetailElm detail, int byReplyN) {
		String reply = detail.getReply();
		switch (byReplyN) {
		case 2:
			reply = detail.getReply2();
			break;
		case 3:
			reply = detail.getReply3();
			break;
		case 4:
			reply = detail.getReply4();
			break;
		}
		return convert2PassStatus(detail.getOptions(), reply);
	}
}
