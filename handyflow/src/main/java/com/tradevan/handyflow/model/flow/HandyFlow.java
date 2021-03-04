package com.tradevan.handyflow.model.flow;

import com.tradevan.handyflow.exception.FlowSettingException;

/**
 * Title: HandyFlow<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
public class HandyFlow extends FlowComposite {
	
	private String version;

	public HandyFlow(FlowComponent parent, String id, String name, String desc, String version) {
		super(parent, id, name, desc);
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
	
	public FlowEvent getFlowEvent(FlowEvent.Type type) {
		FlowEvent flowEvent = getFlowEventOrNull(type);
		if (flowEvent == null) {
			throw new FlowSettingException("Flow id:" + getId() + " version:" + this.version + " has no FlowEvent by type:" + type);
		}
		return flowEvent;
	}
	
	public FlowEvent getFlowEventOrNull(FlowEvent.Type type) {
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowEvent) {
				FlowEvent event = (FlowEvent) child;
				if (event.getType() == type) {
					return event;
				}
			}
			else if (child instanceof SubFlow) {
				for (FlowComponent subChild : child.getChildren()) {
					if (subChild instanceof FlowEvent) {
						FlowEvent event = (FlowEvent) subChild;
						if (event.getType() == type) {
							return event;
						}
					}
				}
			}
		}
		return null;
	}
	
	public FlowTask getFlowTask(String taskId) {
		FlowTask flowTask = getFlowTaskOrNull(taskId);
		if (flowTask == null) {
			throw new FlowSettingException("Flow id:" + getId() + " version:" + this.version + " has no FlowTask by id:" + taskId);
		}
		return flowTask;
	}
	
	public FlowTask getFlowTaskOrNull(String taskId) {
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowTask) {
				FlowTask task = (FlowTask) child;
				if (taskId.equalsIgnoreCase(task.getId())) {
					return task;
				}
			}
			else if (child instanceof SubFlow) {
				for (FlowComponent subChild : child.getChildren()) {
					if (subChild instanceof FlowTask) {
						FlowTask task = (FlowTask) subChild;
						if (taskId.equalsIgnoreCase(task.getId())) {
							return task;
						}
					}
				}
			}
		}
		return null;
	}
	
	public FlowParallel getFlowParallel(String flowParallelId) {
		FlowParallel flowParallel = getFlowParallelOrNull(flowParallelId);
		if (flowParallel == null) {
			throw new FlowSettingException("Flow id:" + getId() + " version:" + this.version + " has no FlowParallel by id:" + flowParallelId);
		}
		return flowParallel;
	}
	
	public FlowParallel getFlowParallelOrNull(String flowParallelId) {
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowParallel) {
				FlowParallel flowParallel = (FlowParallel) child;
				if (flowParallelId.equalsIgnoreCase(flowParallel.getId())) {
					return flowParallel;
				}
			}
		}
		return null;
	}
	
	public SubFlow getSubFlow(String subFlowId) {
		for (FlowComponent child : getChildren()) {
			if (child instanceof SubFlow) {
				SubFlow subFlow = (SubFlow) child;
				if (subFlowId.equalsIgnoreCase(subFlow.getId())) {
					return subFlow;
				}
			}
		}
		throw new FlowSettingException("Flow id:" + getId() + " version:" + this.version + " has no SubFlow by id:" + subFlowId);
	}
	
	public FlowComponent getFlowComponent(String id) {
		for (FlowComponent child : getChildren()) {
			if (id.equalsIgnoreCase(child.getId())) {
				if (isFlowEvent(child) || isFlowTask(child) || isFlowParallel(child) || isSubFlow(child)) {
					return child;
				}
			}
			else if (isSubFlow(child)) {
				for (FlowComponent subChild : child.getChildren()) {
					if (id.equalsIgnoreCase(subChild.getId())) {
						if (isFlowEvent(subChild) || isFlowTask(subChild)) {
							return subChild;
						}
					}
				}
			}
		}
		throw new FlowSettingException("Flow id:" + getId() + " version:" + this.version + " has no FlowEvent/FlowTask/FlowParallel/SubFlow components by id:" + id);
	}
	
	public FlowComponent getFlowCandidateComponent(String id) {
		String compareId = id;
		for (FlowComponent child : getChildren()) {
			if (compareId.equalsIgnoreCase(child.getId())) {
				if (isFlowTask(child) || isSubFlow(child)) {
					return child;
				}
				else if (isFlowParallel(child)) {
					for (FlowComponent subChild : child.getChildren()) {
						if (isFlowLink(subChild)) {
							FlowLink linkTo = (FlowLink) subChild;
							compareId = linkTo.getTo();
						}
						else if (isFlowConditions(subChild)) {
							for (FlowComponent subSubChild : subChild.getChildren()) {
								if (isFlowLink(subSubChild)) {
									FlowLink linkTo = (FlowLink) subSubChild;
									compareId = linkTo.getTo();
								}
								break;
							}
						}
						break;
					}
				}
			}
			else if (isSubFlow(child)) {
				for (FlowComponent subChild : child.getChildren()) {
					if (compareId.equalsIgnoreCase(subChild.getId())) {
						if (isFlowEvent(subChild) || isFlowTask(subChild)) {
							return subChild;
						}
					}
				}
			}
		}
		throw new FlowSettingException("Flow id:" + getId() + " version:" + this.version + " has no FlowEvent/FlowTask/FlowParallel/SubFlow components by id:" + id);
	}
	
	public boolean isFlowEvent (FlowComponent flowComp) {
		return (flowComp instanceof FlowEvent) ? true : false;
	}
	
	public boolean isFlowTask (FlowComponent flowComp) {
		return (flowComp instanceof FlowTask) ? true : false;
	}
	
	public boolean isFlowParallel (FlowComponent flowComp) {
		return (flowComp instanceof FlowParallel) ? true : false;
	}
	
	public boolean isFlowConditions (FlowComponent flowComp) {
		return (flowComp instanceof FlowConditions) ? true : false;
	}
	
	public boolean isFlowLink (FlowComponent flowComp) {
		return (flowComp instanceof FlowLink) ? true : false;
	}
	
	public boolean isSubFlow (FlowComponent flowComp) {
		return (flowComp instanceof SubFlow) ? true : false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("handyflow: id=\"").append(getId()).append("\"");
		if (getName() != null) {
			sb.append(" name=\"").append(getName()).append("\"");
		}
		if (getDesc() != null) {
			sb.append(" desc=\"").append(getDesc()).append("\"");
		}
		sb.append(" version=\"").append(version).append("\"");
		sb.append("\n");
		for (FlowComponent component : getChildren()) {
			sb.append(component.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
