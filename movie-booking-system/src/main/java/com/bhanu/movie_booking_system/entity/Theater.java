package com.bhanu.movie_booking_system.entity;

import com.bhanu.movie_booking_system.entity.Enum.State;
import com.bhanu.movie_booking_system.entity.Enum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "theaters")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;
    @Column(nullable = false)
    private String theaterName;
    @Column(nullable = false)
    private String city;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false,unique = true,length = 10)
    private String contactNumber;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "theater")
    private List<Screen> screens;
}
