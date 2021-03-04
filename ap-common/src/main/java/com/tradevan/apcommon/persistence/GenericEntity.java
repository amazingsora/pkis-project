package com.tradevan.apcommon.persistence;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Title: GenericEntity<br>
 * Description: All entity should extends GenericEntity<br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@MappedSuperclass
public abstract class GenericEntity<ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public abstract ID getId();

	public abstract void setId(ID id);

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		GenericEntity<ID> other;
		try {
			other = (GenericEntity<ID>) obj;
		}
		catch (ClassCastException e) {
			return false;
		}

		// check if both are entity (with id)
		if (this.getId() != null && other.getId() != null) {
			return this.getId().equals(other.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		if(this.getId() == null) {
			return super.hashCode();
		}
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", this.getId()).toString();
	}
}
