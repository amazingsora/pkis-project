package com.tradevan.handyform.model.exam;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ExamSection {
	private Integer id;
	private String title;
	private String subTitle;
	private Integer perScore;
	private Integer random;
	private List<ExamElement> questions;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public Integer getPerScore() {
		return perScore;
	}
	public void setPerScore(Integer perScore) {
		this.perScore = perScore;
	}
	public Integer getRandom() {
		return random;
	}
	public void setRandom(Integer random) {
		this.random = random;
	}
	public List<ExamElement> getQuestions() {
		return questions;
	}
	public void setQuestions(List<ExamElement> questions) {
		this.questions = questions;
	}
}
