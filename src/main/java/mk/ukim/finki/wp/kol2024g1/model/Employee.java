package mk.ukim.finki.wp.kol2024g1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String password;

    @ManyToOne
    private Hotel hotel;

    private boolean isManager;
}
