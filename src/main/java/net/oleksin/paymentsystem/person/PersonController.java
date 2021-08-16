package net.oleksin.paymentsystem.person;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {
  
  private final PersonService personService;
  
  public PersonController(PersonService personService) {
    this.personService = personService;
  }
  
  @RequestMapping(name = "/",
          produces = { MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public String welcome() {
    return "Welcome to RestTemplate Example.";
  }
  
  @GetMapping("/person")
  public List<Person> getPerson() {
    List<Person> personSet =  personService.getAllPersons();
    return personSet;
  }
  
  @PostMapping(value = "/savePerson")
  public Long savePerson(Person person) {
    return personService.saveNewPerson(person);
  }
}
