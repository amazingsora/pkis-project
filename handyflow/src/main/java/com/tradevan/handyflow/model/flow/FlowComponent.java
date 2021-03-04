package com.tradevan.handyflow.model.flow;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.DocStateLogRepository;

/**
 * Title: FlowComponent<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1.1
 */
public abstract class FlowComponent {
	
	private FlowComponent parent;
	private String id;
	private String name;
	private String desc;
	
	public FlowComponent(FlowComponent parent, String id, String name, String desc) {
		super();
		this.parent = parent;
		this.id = id;
		this.name = name;
		this.desc = desc;
	}

	public void add(FlowComponent component) {
		throw new UnsupportedOperationException();
	}
	
	public void remove(FlowComponent component) {
		throw new UnsupportedOperationException();
	}
	
	public FlowComponent getChild(int i) {
		throw new UnsupportedOperationException();
	}

	public List<FlowComponent> getChildren() {
		throw new UnsupportedOperationException();
	}
	
	public FlowComponent getParent() {
		return parent;
	}
	
	public HandyFlow getRoot() {
		FlowComponent parent = getParent();
		while (true) {
			if (parent.getParent() == null) {
				break;
			}
			else {
				parent = parent.getParent();
			}
		}
		return (HandyFlow) parent;
	}

	public FlowComponent processInternalBefore(DocState docState, OrgService orgService) {
		// Default return self
		return this;
	}
	
	public List<DocState> processInternalAfter(DocState docState, DocStateRepository docStateRepository, DocStateLogRepository docStateLogRepository, OrgService orgService, 
			UserProf beRepresented, UserProf agent, List<UserProf> nextConcurrentUsers) {
		// Default do nothing
		return null;
	}
	
	public List<UserProf> fetchCandidates(DocState docState, OrgService orgService) {
		throw new UnsupportedOperationException();
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}

	public String getValue() {
		throw new UnsupportedOperationException();
	}
	
	public FlowComponent parse(Element element, Map<String, String> attributes) {
		throw new UnsupportedOperationException();
	}
}
