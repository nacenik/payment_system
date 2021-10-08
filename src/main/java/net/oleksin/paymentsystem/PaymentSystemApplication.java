package net.oleksin.paymentsystem;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountType;
import net.oleksin.paymentsystem.account.springdatajpa.AccountRepository;
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
	private final AccountRepository accountRepository;

	public PaymentSystemApplication(PersonService personServiceJpa, PaymentService paymentServiceJpa, AccountRepository accountRepository) {
		this.personService = personServiceJpa;
		this.paymentService = paymentServiceJpa;
		this.accountRepository = accountRepository;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentSystemApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	private void testJpaMethods(){
		Account account = new Account();
		account.setAccountType(AccountType.simpleCard);
		account.setAccountNumber("12312312");
		account.setBalance(new BigDecimal(1234));

		Account account1 = new Account();
		account1.setAccountType(AccountType.creditCard);
		account1.setAccountNumber("123123213");
		account1.setBalance(new BigDecimal(4444));

		Person person = new Person();
		person.setFirstName("Nikita");
		person.setLastName("Oleskin");
		person.getAccounts().add(account);
		person.getAccounts().add(account1);
		account.setPerson(person);
		account1.setPerson(person);

		accountRepository.save(account);
		accountRepository.save(account1);
		personService.saveNewPerson(person);
	}
}
