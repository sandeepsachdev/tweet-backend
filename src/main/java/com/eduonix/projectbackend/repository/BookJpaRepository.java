package com.eduonix.projectbackend.repository;

import java.util.List;

import com.eduonix.projectbackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookJpaRepository extends JpaRepository<Book,Long> {
	public List<Book> findByName(String book);
}
