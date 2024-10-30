package com.example.theater.repositories;

import com.example.theater.entities.Comment;
import com.example.theater.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository <Comment, Long> {
    List <Comment> findAllByMovie (Movie movie);
}
