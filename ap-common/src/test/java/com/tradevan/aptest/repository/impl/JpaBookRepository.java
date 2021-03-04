package com.tradevan.aptest.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aptest.model.Book;
import com.tradevan.aptest.repository.BookRepository;

@Repository
public class JpaBookRepository extends JpaGenericRepository<Book, Long> implements BookRepository {

}
