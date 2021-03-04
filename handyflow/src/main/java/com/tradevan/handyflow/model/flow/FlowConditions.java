package com.tradevan.handyflow.model.flow;

import com.tradevan.handyflow.exception.FlowException;

/**
 * Title: FlowConditions<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
public class FlowConditions extends FlowComposite {
	
	public FlowConditions(FlowComponent parent, String id, String name, String desc) {
		super(parent, id, name, desc);
	}
	
	public FlowLink getFlowLink(LinkAction action) {
		FlowLink flowLink = getFlowLinkInternal(action);
		if (flowLink != null) {
			return flowLink;
		}
		else {
			throw new FlowException("FlowConditions has no FlowLink match action:" + action);
		}
	}
	
	private FlowLink getFlowLinkInternal(LinkAction action) {
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowLink) {
				FlowLink flowLink = (FlowLink) child;
				if (action == null || action.getAction() == null || action.getAction().equalsIgnoreCase(flowLink.getAction())) {
					return flowLink;
				}
			}
		}
		return null;
	}
	
	public boolean checkFlowLink(LinkAction action) {
		FlowLink flowLink = getFlowLinkInternal(action);
		if (flowLink != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public FlowLink getFlowLinkByTo(String to) {
		FlowLink rtnFlowLink = null;
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowLink) {
				FlowLink flowLink = (FlowLink) child;
				if (rtnFlowLink == null) {
					rtnFlowLink = flowLink;
				}
				if (to == null || "".equals(to) || to.equalsIgnoreCase(flowLink.getTo())) {
					return flowLink;
				}
			}
		}
		return rtnFlowLink;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("flowConditions:");
		if (getId() != null) {
			sb.append(" id=\"").append(getId()).append("\"");
		}
		if (getName() != null) {
			sb.append(" name=\"").append(getName()).append("\"");
		}
		if (getDesc() != null) {
			sb.append(" desc=\"").append(getDesc()).append("\"");
		}
		for (FlowComponent component : getChildren()) {
			sb.append("\n");
			sb.append(component.toString());
		}
		return sb.toString();
	}
}
