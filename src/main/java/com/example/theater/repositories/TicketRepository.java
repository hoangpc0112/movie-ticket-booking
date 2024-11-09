package com.example.theater.repositories;

import com.example.theater.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository <Ticket, Long> {

    @Query (value = "select seat_no from tickets " + "where movie_title = ? and time = ? and date = ?", nativeQuery = true)
    List <Integer> findAllSeatNoBy (String movieTitle, String time, String date);

    boolean existsBySeatNoAndMovieTitleAndTimeAndDate (int seatNo, String movieTitle, String time, String date);

    Ticket findById (long id);
}
