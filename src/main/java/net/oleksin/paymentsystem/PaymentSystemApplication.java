package net.oleksin.paymentsystem;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.payment.Payment;
import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.person.PersonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.math.BigDecimal;

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

	
//	@EventListener(ApplicationReadyEvent.class)
//	private void testJpaMethods(){
//		Account account = new Account();
//		account.setAccountType("lol");
//		account.setAccountNumber(12312312L);
//		account.setBalance(new BigDecimal(1234));
//
//		Account account1 = new Account();
//		account1.setAccountType("lol");
//		account1.setAccountNumber(123123213L);
//		account1.setBalance(new BigDecimal(4444));
//
//		Person person = new Person();
//		person.setFirstName("Nikita");
//		person.setLastName("Oleskin");
//		person.getAccounts().add(account);
//		person.getAccounts().add(account1);
//		account.setPerson(person);
//		account1.setPerson(person);
//
//		personService.saveNewPerson(person);
//
//		Payment payment = new Payment();
//		payment.setDestination(account);
//		payment.setSource(account1);
//		payment.setAmount(new BigDecimal(1000));
//		payment.setReason("simple");
//
//		paymentService.createNewPayment(payment);
//	}
}
