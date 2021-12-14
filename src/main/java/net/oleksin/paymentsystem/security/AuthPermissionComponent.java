package net.oleksin.paymentsystem.security;

import lombok.RequiredArgsConstructor;
import net.oleksin.paymentsystem.payment.PaymentRequestDto;
import net.oleksin.paymentsystem.person.PersonService;
import net.oleksin.paymentsystem.security.jwt.JwtUserDetails;
import net.oleksin.paymentsystem.security.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("authPermissionComponent")
@RequiredArgsConstructor
public class AuthPermissionComponent {

    private final PersonService personService;

    public boolean isAdmin(UserDetails principal) {
        return retrievePaymentUser(principal)
                .map(User::isAdmin)
                .orElse(false);
    }

    public boolean belongToBankAccount(UserDetails principal, Long personId) {
        return retrievePaymentUser(principal)
                .map(paymentUser -> paymentUser.belongToBankAccount(personId))
                .orElse(false);
    }

    public boolean isAdminOrBelongToBankAccount(UserDetails principal, Long personId) {
        return retrievePaymentUser(principal)
                .map(paymentUser -> paymentUser.isAdminOrBelongToBankAccount(personId))
                .orElse(false);
    }

    public boolean isPersonsAccount(UserDetails principal, Long accountId) {
        return retrievePaymentUser(principal)
                .map(paymentUser -> personService.existByPersonIdAndAccountId(paymentUser.getPerson().getId(), accountId))
                .orElse(false);
    }

    public boolean belongToBankAccount(UserDetails principal, List<PaymentRequestDto> paymentRequestDtoList) {
        return paymentRequestDtoList.stream()
                .map(PaymentRequestDto::getSourceAccountId)
                .anyMatch(id -> !isPersonsAccount(principal, id));
    }

    private Optional<User> retrievePaymentUser(UserDetails principal) {
        if (!(principal instanceof JwtUserDetails)) {
            return Optional.empty();
        }
        return Optional.of((JwtUserDetails) principal)
                .map(JwtUserDetails::getUser);
    }

}
