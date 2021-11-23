package net.oleksin.paymentsystem.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.oleksin.paymentsystem.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentProvider paymentProvider;

    @Mock
    private Converter<PaymentRequestDto, PaymentResponseDto, Payment> paymentConverter;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;
    private PaymentRequestDto paymentRequestDto;
    private Payment payment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
        paymentRequestDto = PaymentRequestDto.builder()
                .sourceAccountId(1L)
                .destinationAccountId(2L)
                .amount(new BigDecimal(100))
                .reason("test")
                .build();
        payment = Payment.builder().id(1L).build();
    }

    @Test
    void createPaymentJsonTest() throws Exception {
        when(paymentProvider.createNewPayment(any())).thenReturn(payment);

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(paymentRequestDto)))
                .andExpect(status().is(201));

        verify(paymentProvider).createNewPayment(any());
    }

    @Test
    void createPaymentXmlTest() throws Exception {
        when(paymentProvider.createNewPayment(any())).thenReturn(payment);

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(mapToXml(paymentRequestDto)))
                .andExpect(status().is(201));

        verify(paymentProvider).createNewPayment(any());
    }

    @Test
    void createPaymentsJsonTest() throws Exception {
        List<PaymentRequestDto> paymentRequestDtos = List.of(paymentRequestDto);
        when(paymentProvider.createNewPayments(any())).thenReturn(List.of(payment));


        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(paymentRequestDtos)))
                .andExpect(status().is(200));

        verify(paymentProvider).createNewPayments(any());
    }

    @Test
    void createPaymentsXmlTest() throws Exception {
        List<PaymentRequestDto> paymentRequestDtos = List.of(paymentRequestDto);
        when(paymentProvider.createNewPayments(any())).thenReturn(List.of(payment));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(mapToXmlList(paymentRequestDtos)))
                .andExpect(status().is(200));

        verify(paymentProvider).createNewPayments(any());
    }

    @Test
    void getPaymentJournalWithFourParamTest() throws Exception {
        when(paymentProvider.getPaymentJournals(anyLong(), anyLong(), anyLong(), anyLong()))
                .thenReturn(List.of(PaymentJournalDto.builder().paymentId(1L).build()));

        mockMvc.perform(get("/api/payments")
                        .param("payerId", "1")
                        .param("recipientId", "2")
                        .param("srcAccId", "3")
                        .param("destAccId", "4"))
                .andExpect(status().is(200));

        verify(paymentProvider).getPaymentJournals(anyLong(), anyLong(), anyLong(), anyLong());
    }

    @Test
    void getPaymentJournalWithOneParamTest() throws Exception {
        when(paymentProvider.getPaymentJournals(anyLong(), any(), any(), any()))
                .thenReturn(List.of(PaymentJournalDto.builder().paymentId(1L).build()));

        mockMvc.perform(get("/api/payments")
                        .param("payerId", "1"))
                .andExpect(status().is(200));

        verify(paymentProvider).getPaymentJournals(anyLong(), any(), any(), any());
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private String mapToXml(Object obj) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(PaymentRequestDto.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(obj, sw);
        return sw.toString();
    }

    private String mapToXmlList(List<PaymentRequestDto> requestDtos) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(PaymentRequestDto[].class);
        JAXBElement<PaymentRequestDto[]> root = new JAXBElement<>(new QName("items"),
                PaymentRequestDto[].class, requestDtos.toArray(new PaymentRequestDto[requestDtos.size()]));
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        marshaller.marshal(root, sw);
        return sw.toString();
    }

}