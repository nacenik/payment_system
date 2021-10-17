package net.oleksin.paymentsystem.person;

import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {
  
  @Mock
  private PersonService personService;
  @Mock
  private Converter<AccountDto, AccountDto, Account> accountConverter;
  @Mock
  private Converter<PersonRequestDto, PersonResponseDto, Person> personConverter;
  
  @InjectMocks
  private PersonController personController;
  
  private MockMvc mockMvc;
  
  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
  }
  
  @Test
  void createPersonForJsonTest() throws Exception {
    when(personService.saveNewPerson(any())).thenReturn(Person.builder().id(1L).build());
    
    mockMvc.perform(post("/persons").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk());
    
    verify(personService, times(1))
            .saveNewPerson(any());
  }
  
  @Test
  void createPersonForXmlTest() throws Exception {
    when(personService.saveNewPerson(any()))
            .thenReturn(Person.builder().id(1L).build());
    
    mockMvc.perform(post("/persons").contentType(MediaType.APPLICATION_XML_VALUE))
            .andExpect(status().isOk());
    
    verify(personService, times(1))
            .saveNewPerson(any());
  }
  
  @Test
  void getAllPersonsTest() throws Exception {
    when(personService.getAllPersons())
            .thenReturn(List.of(Person.builder().id(1L).build(), Person.builder().id(2L).build()));
    
    mockMvc.perform(get("/persons").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  
    verify(personService, times(1))
            .getAllPersons();
  }
  
  @Test
  void getPersonByIdTest() throws Exception {
    when(personService.getPersonById(anyLong()))
            .thenReturn(Person.builder().id(1L).build());
  
    mockMvc.perform(get("/persons/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  
    verify(personService, times(1))
            .getPersonById(anyLong());
  }
  
  @Test
  void getAccountsByPersonIdTest() throws Exception {
    when(personService.getAccountsByPersonId(anyLong()))
            .thenReturn(Set.of(Account.builder().id(1L).build(), Account.builder().id(2L).build()));
  
    mockMvc.perform(get("/persons/1/accounts").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  
    verify(personService, times(1))
            .getAccountsByPersonId(anyLong());
  }
}