package com.cfs.Bookmyshow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Theater")
@Data
@NoArgsConstructor
@AllArgsConstructor
// FIX: Exclude bidirectional collection to prevent infinite loop / StackOverflow in toString/hashCode
@ToString(exclude = {"screenList"})
@EqualsAndHashCode(exclude = {"screenList"})
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String Address;
    private String city;
    private String totalScreen;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    private List<Screen> screenList;
}