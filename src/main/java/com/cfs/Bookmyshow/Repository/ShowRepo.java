package com.cfs.Bookmyshow.Repository;

import com.cfs.Bookmyshow.model.Movie;
import com.cfs.Bookmyshow.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepo extends JpaRepository<Show,Long> {

   List<Show> findByMovieId(Long id);
   List<Show> findByScreenId(Long id);

   List<Show> findByStartTimeBetween(LocalDateTime start,LocalDateTime end);

   List<Show> findByMovieIdAndScreenTheaterCity(Long Id,String city);
}
