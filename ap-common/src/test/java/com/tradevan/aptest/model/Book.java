package com.tradevan.aptest.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.tradevan.apcommon.persistence.GenericEntity;

@Entity
@NamedQueries(value = { 
		@NamedQuery(name = "Book.updateById", query = "UPDATE Book b SET b.title = ?2 WHERE b.id = ?1"),
		@NamedQuery(name = "Book.getTitleById", query = "SELECT b.title FROM Book b WHERE b.id = ?1"),
		@NamedQuery(name = "Book.findByLikeTitle", query = "SELECT b FROM Book b WHERE b.title like ?1 ORDER BY b.id"),
		@NamedQuery(name = "Book.findByLikeTitlePlaceholder", query = "SELECT b FROM Book b WHERE b.title like :title ORDER BY b.id"),
})
public class Book extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private String title;

	public Book() {
	}

	public Book(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Book{" + "id=" + id + ", title='" + title + '\'' + '}';
	}
}
