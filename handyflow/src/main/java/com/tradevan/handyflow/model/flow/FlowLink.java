package com.tradevan.handyflow.model.flow;

/**
 * Title: FlowLink<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class FlowLink extends FlowComponent {
	
	private String to;
	private String action;
	private Boolean isConcurrent;
	
	public FlowLink(FlowComponent parent, String id, String name, String desc, String to, String action, String isConcurrent) {
		super(parent, id, name, desc);
		this.to = to;
		this.action = action;
		if (isConcurrent != null) {
			this.isConcurrent = Boolean.valueOf(isConcurrent);
		}
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public Boolean isConcurrent() {
		return isConcurrent;
	}

	public void setIsConcurrent(Boolean isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("flowLink:");
		if (action != null) {
			sb.append(" action=\"").append(action).append("\"");
		}
		if (getId() != null) {
			sb.append(" id=\"").append(getId()).append("\"");
		}
		if (getName() != null) {
			sb.append(" name=\"").append(getName()).append("\"");
		}
		if (isConcurrent() != null) {
			sb.append(" isConcurrent=\"").append(isConcurrent()).append("\"");
		}
		if (getDesc() != null) {
			sb.append(" desc=\"").append(getDesc()).append("\"");
		}
		sb.append(" to=\"").append(to).append("\"");
		return sb.toString();
	}
}
