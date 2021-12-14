package net.oleksin.paymentsystem.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.oleksin.paymentsystem.Converter;
import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.account.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

  private static final String BASE_URL = "/api/persons";
  
  @Mock
  private PersonProvider personProvider;
  @Mock
  private Converter<PersonRequestDto, PersonResponseDto, Person> personConverter;
  
  @InjectMocks
  private PersonController personController;

  private MockMvc mockMvc;
  private PersonRequestDto personRequestDto;
  
  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    personRequestDto = PersonRequestDto.builder()
            .firstName("first")
            .lastName("last")
            .accounts(List.of(AccountDto.builder()
                    .id(1L)
                    .accountNumber("123")
                    .accountType("simple")
                    .balance(new BigDecimal(500))
                    .build()))
            .build();
  }
  
  @Test
  void createPersonForJsonTest() throws Exception {
    when(personProvider.createPerson(any())).thenReturn(Person.builder().id(1L).build());
    String json = mapToJson(personRequestDto);

    ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json));
    resultActions.andExpect(status().is(201));
    
    verify(personProvider, times(1))
            .createPerson(any());
  }
  
  @Test
  void getAllPersonsTest() throws Exception {
    when(personProvider.getAllPersons())
            .thenReturn(List.of(Person.builder().id(1L).build(), Person.builder().id(2L).build()));
    
    mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  
    verify(personProvider, times(1))
            .getAllPersons();
  }
  
  @Test
  void getPersonByIdTest() throws Exception {
    when(personProvider.getPersonById(anyLong()))
            .thenReturn(Person.builder().id(1L).build());
  
    mockMvc.perform(get(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  
    verify(personProvider, times(1))
            .getPersonById(anyLong());
  }

  private String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  private String mapToXml(Object obj) throws JAXBException {
    JAXBContext jaxbContext = JAXBContext.newInstance(PersonRequestDto.class);
    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    StringWriter sw = new StringWriter();
    jaxbMarshaller.marshal(obj, sw);
    return sw.toString();
  }
}