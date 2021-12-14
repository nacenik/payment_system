package net.oleksin.paymentsystem.account;

import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.person.PersonProvider;
import net.oleksin.paymentsystem.person.PersonRequestDto;
import net.oleksin.paymentsystem.person.PersonResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private static final String BASE_URL = "/api/persons";

    @Mock
    private PersonProvider personProvider;
    @Mock
    private Converter<PersonRequestDto, PersonResponseDto, Person> personConverter;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

  @Test
  void getAccountsByPersonIdTest() throws Exception {
    when(personProvider.getAccountsByPersonId(anyLong()))
            .thenReturn(List.of(Account.builder().id(1L).build(), Account.builder().id(2L).build()));

    mockMvc.perform(get(BASE_URL + "/1/accounts").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    verify(personProvider, times(1))
            .getAccountsByPersonId(anyLong());
  }
}