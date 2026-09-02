package com.twix.app.user;

import com.twix.app.observer.Observer;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements Observer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    // Setters. Jackson populates the fields directly by reflection, so nothing
    // needed these before; the registration endpoint does, because it stores a
    // trimmed username rather than whatever arrived on the request.
    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("Notification for " + this.getUsername() + ":" + message);
    }
}
