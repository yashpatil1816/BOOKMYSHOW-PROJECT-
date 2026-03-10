package com.cfs.Bookmyshow.Repository;

import com.cfs.Bookmyshow.model.Booking;
import com.cfs.Bookmyshow.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepo extends JpaRepository<Movie,Long> {


    List<Movie> findByGenre(String genre);

    List<Movie> findByTitleContaining(String title);//--------------

    List<Movie> findByLanguage(String language);

}
