package net.oleksin.paymentsystem.journal;

import lombok.*;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.person.Person;

import javax.persistence.*;

@Entity
@Table(name = "journals")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "payer_id")
    private Person payer;

    @OneToMany
    @JoinColumn(name = "recipient_id")
    private Person recipient;
}
