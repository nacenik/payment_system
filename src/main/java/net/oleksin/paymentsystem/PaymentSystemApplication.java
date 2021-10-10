package net.oleksin.paymentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentSystemApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentSystemApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	private void testJpaMethods(){
//		Account account = new Account();
//		account.setAccountType(AccountType.simpleCard);
//		account.setAccountNumber("12312312");
//		account.setBalance(new BigDecimal(1234));
//
//		Account account1 = new Account();
//		account1.setAccountType(AccountType.creditCard);
//		account1.setAccountNumber("123123213");
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
//		accountRepository.save(account);
//		accountRepository.save(account1);
//		personService.saveNewPerson(person);
//	}
}
