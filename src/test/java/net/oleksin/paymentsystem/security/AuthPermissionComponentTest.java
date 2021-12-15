package net.oleksin.paymentsystem.security;

import net.oleksin.paymentsystem.payment.PaymentRequestDto;
import net.oleksin.paymentsystem.person.Person;
import net.oleksin.paymentsystem.person.PersonService;
import net.oleksin.paymentsystem.security.jwt.JwtUserDetails;
import net.oleksin.paymentsystem.security.user.User;
import net.oleksin.paymentsystem.security.user.role.Role;
import net.oleksin.paymentsystem.security.user.role.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthPermissionComponentTest {
    private static final String EMAIL = "test@test.com";
    private static final String TEST = "test";
    private static final Long ID = 1L;

    @Mock
    private PersonService personService;

    @InjectMocks
    private AuthPermissionComponent authPermissionComponent;

    private JwtUserDetails adminUser;
    private JwtUserDetails simpleUser;

    @BeforeEach
    void setUp() {
        User admin = User.builder()
                .id(ID)
                .created(new Date())
                .updated(new Date())
                .email(EMAIL)
                .password(TEST)
                .status(Status.ACTIVE)
                .roles(List.of(Role.builder().id(ID).roleName("ROLE_ADMIN").build()))
                .person(Person.builder().id(ID).build()).build();

        User user = User.builder()
                .id(ID)
                .created(new Date())
                .updated(new Date())
                .email(EMAIL)
                .password(TEST)
                .status(Status.ACTIVE)
                .roles(List.of(Role.builder().id(ID).roleName("ROLE_USER").build()))
                .person(Person.builder().id(ID).build()).build();
        adminUser = new JwtUserDetails(admin, List.of(new SimpleGrantedAuthority(admin.getRoles().get(0).getRoleName())));
        simpleUser = new JwtUserDetails(user, List.of(new SimpleGrantedAuthority(user.getRoles().get(0).getRoleName())));
    }

    @Test
    void isAdminTrueTest() {
        assertTrue(authPermissionComponent.isAdmin(adminUser));
    }

    @Test
    void isAdminFalseTest() {
        assertFalse(authPermissionComponent.isAdmin(simpleUser));
    }

    @Test
    void belongToBankAccountTrueTest() {
        assertTrue(authPermissionComponent.belongToBankAccount(adminUser, ID));
    }

    @Test
    void belongToBankAccountFalseTest() {
        assertFalse(authPermissionComponent.belongToBankAccount(adminUser, 2L));
    }

    @Test
    void belongToBankAccountWithListFalseTest() {
        when(personService.existByPersonIdAndAccountId(anyLong(), anyLong())).thenReturn(true).thenReturn(false);
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder().sourceAccountId(ID).build();
        PaymentRequestDto paymentRequestDtoNext = PaymentRequestDto.builder().sourceAccountId(2L).build();
        assertTrue(authPermissionComponent.belongToBankAccount(adminUser, List.of(paymentRequestDto, paymentRequestDtoNext)));
    }

    @Test
    void belongToBankAccountWithListTrueTest() {
        when(personService.existByPersonIdAndAccountId(anyLong(), anyLong())).thenReturn(true).thenReturn(true);
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder().sourceAccountId(ID).build();
        PaymentRequestDto paymentRequestDtoNext = PaymentRequestDto.builder().sourceAccountId(2L).build();
        assertFalse(authPermissionComponent.belongToBankAccount(adminUser, List.of(paymentRequestDto, paymentRequestDtoNext)));
    }

    @Test
    void isAdminOrBelongToBankAccountTrueTest() {
        assertTrue(authPermissionComponent.isAdminOrBelongToBankAccount(adminUser, ID));
        assertTrue(authPermissionComponent.isAdminOrBelongToBankAccount(simpleUser, ID));
    }

    @Test
    void isAdminOrBelongToBankAccountFalseTest() {
        assertFalse(authPermissionComponent.isAdminOrBelongToBankAccount(simpleUser, 2L));
    }

    @Test
    void isPersonsAccountTrueTest() {
        when(personService.existByPersonIdAndAccountId(anyLong(), anyLong())).thenReturn(true);
        assertTrue(authPermissionComponent.isPersonsAccount(adminUser, 1L));
    }

    @Test
    void isPersonsAccountFalseTest() {
        when(personService.existByPersonIdAndAccountId(anyLong(), anyLong())).thenReturn(false);
        assertFalse(authPermissionComponent.isPersonsAccount(adminUser, 1L));
    }

    @Test
    void testBelongToBankAccountTrueTest() {
        assertTrue(authPermissionComponent.belongToBankAccount(adminUser, ID));
    }

    @Test
    void testBelongToBankAccountFalseTest() {
        assertFalse(authPermissionComponent.belongToBankAccount(simpleUser, 2L));
    }
}