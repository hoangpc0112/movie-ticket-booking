package com.example.theater.services;

import com.example.theater.models.Movie;
import com.example.theater.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovie() {
        return movieRepository.findAll();
    }
}
