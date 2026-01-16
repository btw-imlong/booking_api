package com.booking.booking_api.Enity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    // Optional: remove the user set if you don't need it
    // @ManyToMany(mappedBy = "roles")
    // private Set<User> users = new HashSet<>();

    // Add this constructor:
    public Role(String name) {
        this.name = name;
    }
}
