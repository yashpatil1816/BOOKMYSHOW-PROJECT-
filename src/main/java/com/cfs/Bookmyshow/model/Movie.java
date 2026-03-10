package com.cfs.Bookmyshow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String language;
    private String genre;
    private String description;
    private String durations;
    private String releaseDate;
    private String PosterUrl;
    private String bannerUrl;
    private Double amount;


    @OneToMany(mappedBy = "movie")
    private List<Show> show;
}