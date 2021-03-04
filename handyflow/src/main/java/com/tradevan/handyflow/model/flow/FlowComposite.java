package com.tradevan.handyflow.model.flow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * Title: FlowComposite<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2.2
 */
public class FlowComposite extends FlowComponent {

	private List<FlowComponent> flowComponents = new ArrayList<FlowComponent>();
	
	public FlowComposite(FlowComponent parent, String id, String name, String desc) {
		super(parent, id, name, desc);
	}
	
	public void add(FlowComponent component) {
		flowComponents.add(component);
	}
	
	public void remove(FlowComponent component) {
		flowComponents.remove(component);
	}
	
	public FlowComponent getChild(int index) {
		return flowComponents.get(index);
	}
	
	public List<FlowComponent> getChildren() {
		return flowComponents;
	}
	
	@SuppressWarnings("rawtypes")
	public FlowComponent parse(Element element, Map<String, String> attributes) {
		for(Iterator it = element.elementIterator(); it.hasNext(); ) {
			Element inner = (Element) it.next();
			
			if ("flowEvent".equalsIgnoreCase(inner.getName())) {
				FlowEvent flowEvent = 
						new FlowEvent(
								this,
								inner.attributeValue("id"), 
								inner.attributeValue("name"), 
								inner.attributeValue("desc"), 
								FlowEvent.Type.valueOf(inner.attributeValue("type"))
							);
				flowComponents.add(flowEvent);
				flowEvent.parse(inner, attributes);
			}
			else if ("flowTask".equalsIgnoreCase(inner.getName())) {
				FlowTask flowTask = 
						new FlowTask(
								this,
								inner.attributeValue("id"), 
								inner.attributeValue("name"), 
								inner.attributeValue("desc"),
								inner.attributeValue("roles"),
								inner.attributeValue("dept"),
								inner.attributeValue("users"),
								inner.attributeValue("sameUserAs"),
								inner.attributeValue("taskExt"),
								inner.attributeValue("isReviewDeptRole"),
								inner.attributeValue("isProjRole"),
								inner.attributeValue("isSameRolePass"),
								inner.attributeValue("isHighLevelPass"),
								attributes.get("highDeptUserApplyOff")
							);
				flowComponents.add(flowTask);
				flowTask.parse(inner, attributes);
			}
			else if ("flowConditions".equalsIgnoreCase(inner.getName())) {
				FlowConditions flowConditions = 
						new FlowConditions(
								this,
								inner.attributeValue("id"), 
								inner.attributeValue("name"), 
								inner.attributeValue("desc")
							);
				flowComponents.add(flowConditions);
				flowConditions.parse(inner, attributes);
			}
			else if ("flowParallel".equalsIgnoreCase(inner.getName())) {
				FlowParallel flowParallel = 
						new FlowParallel(
								this,
								inner.attributeValue("id"), 
								inner.attributeValue("name"), 
								inner.attributeValue("desc"),
								inner.attributeValue("parallelPassCount")
							);
				flowComponents.add(flowParallel);
				flowParallel.parse(inner, attributes);
			}
			else if ("flowLink".equalsIgnoreCase(inner.getName())) {
				FlowLink flowLink = 
						new FlowLink(
								this,
								inner.attributeValue("id"), 
								inner.attributeValue("name"), 
								inner.attributeValue("desc"), 
								inner.attributeValue("to"), 
								inner.attributeValue("action"),
								inner.attributeValue("isConcurrent")
							);
				flowComponents.add(flowLink);
			}
			else if ("subFlow".equalsIgnoreCase(inner.getName())) {
				SubFlow subFlow = 
						new SubFlow(
								this,
								inner.attributeValue("id"), 
								inner.attributeValue("name"), 
								inner.attributeValue("desc")
							);
				flowComponents.add(subFlow);
				subFlow.parse(inner, attributes);
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		return this;
	}
}
