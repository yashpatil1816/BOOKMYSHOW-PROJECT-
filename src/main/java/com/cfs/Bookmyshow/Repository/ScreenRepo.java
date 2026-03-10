package com.cfs.Bookmyshow.Repository;

import com.cfs.Bookmyshow.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepo extends JpaRepository<Screen,Long> {

    List<Screen> findByTheaterId(Long Id);


}
