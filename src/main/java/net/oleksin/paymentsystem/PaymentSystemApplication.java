package net.oleksin.paymentsystem;

import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.person.PersonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentSystemApplication {
	
	private final PersonService personService;
	private final PaymentService paymentService;

	public PaymentSystemApplication(PersonService personServiceJpa, PaymentService paymentServiceJpa) {
		this.personService = personServiceJpa;
		this.paymentService = paymentServiceJpa;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentSystemApplication.class, args);
	}

}
