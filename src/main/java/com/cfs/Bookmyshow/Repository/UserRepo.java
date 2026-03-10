package com.cfs.Bookmyshow.Repository;

import com.cfs.Bookmyshow.model.Theater;
import com.cfs.Bookmyshow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {


    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByName(String username);


}
