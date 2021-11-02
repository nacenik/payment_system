package net.oleksin.paymentsystem.account;

import net.oleksin.paymentsystem.accounttype.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AccountConverterTest {
    private static final String TEST = "test";

    private AccountConverter accountConverter;

    private Account account;
    private AccountDto accountDto;
    @BeforeEach
    void setUp() {
        accountConverter = new AccountConverter();
        account = Account.builder()
                .id(1L)
                .accountType(AccountType.builder().name(TEST).build())
                .accountNumber("123123")
                .balance(new BigDecimal(555))
                .build();
        accountDto = AccountDto.builder()
                .id(account.getId())
                .accountType(account.getAccountType().getName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    @Test
    void toResponseDtoTest() {
        AccountDto expected = accountConverter.toResponseDto(account);

        assertNotNull(expected);
        assertEquals(accountDto, expected);
    }

    @Test
    void fromRequestDtoTest() {
        Account expected = accountConverter.fromRequestDto(accountDto);

        assertNotNull(expected);
        assertEquals(account, expected);
    }
}