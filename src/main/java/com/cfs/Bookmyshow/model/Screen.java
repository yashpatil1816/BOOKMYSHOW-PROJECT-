package com.cfs.Bookmyshow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Screen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String screenName;

    private Integer totalseat;

    // ✅ NEW: Screen format type — STANDARD, IMAX, 4DX, 3D, DOLBY, SCREEN_X
    @Column(name = "screen_type")
    private String screenType;

    @OneToMany(mappedBy = "screen")
    private List<Show> shows;

    @ManyToOne
    @JoinColumn(name = "Theater_id", nullable = false)
    private Theater theater;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private List<Seat> seats;
}