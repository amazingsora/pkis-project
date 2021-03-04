package com.tradevan.handyform.model.exam;

import java.security.SecureRandom;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Examination {
	private List<ExamSection> sections;
	
	@JsonIgnore
	public int calcScore() {
		int score = 0;
		for (ExamSection section : sections) {
			if (section.getPerScore() != null) {
				int perScore = section.getPerScore();
				for (ExamElement elm : section.getQuestions()) {
					if (elm.getReply() != null) {
						if (elm.getReply().equals(elm.getAnswer())) {
							score += perScore;
						}
					}
				}
			}
		}
		return score;
	}

	public List<ExamSection> getSections() {
		return sections;
	}
	public void setSections(List<ExamSection> sections) {
		this.sections = sections;
	}
	
	public String getExamQuestionReply(Integer examSectionId,Integer questionId) {
		
		String reply = "";
		for(ExamSection examSection : sections) {
			if (examSection.getId().equals(examSectionId)) {
				for(ExamElement elm : examSection.getQuestions()) {
					if (elm.getId().equals(questionId)) {
						reply = elm.getReply();
					}
				}
			}
		}
		return reply;
	}

	public Examination processExamRandom() {
		for(ExamSection section : sections) {
			int num = section.getRandom();
			if (num > 0) {
				List<ExamElement> list = section.getQuestions();
				int size = list.size();
				SecureRandom random = new SecureRandom();
				while (size > num) {
					list.remove(random.nextInt(--size));
				}
			}
		}
		return this;
	}
}
