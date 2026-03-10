package com.cfs.Bookmyshow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
// FIX: Exclude back-reference to prevent infinite loop / StackOverflow in toString/hashCode
@ToString(exclude = {"users"})
@EqualsAndHashCode(exclude = {"users"})
public class roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleid;

    private String name;

    @ManyToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
}