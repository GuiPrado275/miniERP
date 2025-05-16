package com.projetominiERP.miniERP.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = Trip.TABLE_NAME) //table for database
@AllArgsConstructor  //constructor
@NoArgsConstructor   //constructor empty
@Data
public class Trip {

    public static final String TABLE_NAME = "trip";

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id is random (id)
    private Long id;

    @ManyToOne //Many tasks for one user
    @JoinColumn(name = "user_id", nullable = false, updatable = false) //this is for make reference of "user_id"
    private User user;

    @Column(name = "status", nullable = false)
    @NotBlank(message = "Status can't be null.")
    private boolean finished;

    @Column(name = "start_date",nullable = false)
    @NotNull(message = "Start date is required.")
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull(message = "End date is required.")
    private Date endDate;

    @Column(name = "origin", length = 50, nullable = false)
    @Size(min = 1, max = 50, message = "Origin must be between 1 and 20 characters.")
    @NotBlank(message = "Origin cannot be blank.")
    private String origin;

    @Column(name = "destination", length = 50, nullable = false)
    @Size(min = 1, max = 50, message = "Destination must be between 1 and 20 characters.")
    @NotBlank(message = "Destination cannot be blank.")
    private String destination;

    @Column(name = "km_value", nullable = false)
    @NotNull(message = "KM value is required.")
    private Double kmValue;

    @Column(name = "km", nullable = false)
    @NotNull(message = "KM is required.")
    private Double km;

    @Column(name = "travel_time", nullable = false)
    @NotNull(message = "Travel time is required.")
    private Double travelTime;

    @Column(name = "travel_cost", nullable = false)
    @NotNull(message = "Travel cost is required.")
    private Double travelCost;

}
