package net.oleksin.paymentsystem.payment;

import net.oleksin.paymentsystem.account.Account;
import net.oleksin.paymentsystem.exception.AccountNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class AbstractPaymentService {

    protected Payment createNewPayment(Payment payment, Optional<Account> sourceAccountOptional, Optional<Account> destinationAccountOptional) {
        if (sourceAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("Source account not found");
        }
        if (destinationAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("Destination account not found");
        }
        Account sourceAccount = sourceAccountOptional.get();
        Account destinationAccount = destinationAccountOptional.get();

        BigDecimal sourceAmount = sourceAccount.getBalance();
        BigDecimal destinationAmount = destinationAccount.getBalance();

        Payment newPayment = new Payment();
        newPayment.setSource(sourceAccount);
        newPayment.setDestination(destinationAccount);
        newPayment.setReason(payment.getReason());
        newPayment.setAmount(payment.getAmount());

        if(sourceAmount.compareTo(newPayment.getAmount()) < 0) {
            newPayment.setStatus(Status.error);
        } else {
            sourceAccount.setBalance(sourceAmount.subtract(newPayment.getAmount()));
            destinationAccount.setBalance(destinationAmount.add(newPayment.getAmount()));
            newPayment.setStatus(Status.ok);
        }

        newPayment.setTimestamp(LocalDateTime.now());
        return newPayment;
    }

    protected boolean isPaymentNull(Payment payment) {
        return payment.getSource() == null
                || payment.getSource().getId() == null
                || payment.getDestination() == null
                || payment.getDestination().getId() == null
                || payment.getAmount() == null
                || payment.getReason() == null;
    }
}
