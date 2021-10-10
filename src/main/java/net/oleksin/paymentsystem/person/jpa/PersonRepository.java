package net.oleksin.paymentsystem.person.jpa;

import net.oleksin.paymentsystem.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
