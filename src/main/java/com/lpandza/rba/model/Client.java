package com.lpandza.rba.model;

import com.lpandza.rba.model.enums.CardStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, length = 11)
    private String oib;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus cardStatus;

    public Client(String firstName, String lastName, String oib, CardStatus cardStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.oib = oib;
        this.cardStatus = cardStatus;
    }
}