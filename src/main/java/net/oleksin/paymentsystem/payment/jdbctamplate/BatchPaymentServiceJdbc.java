package net.oleksin.paymentsystem.payment.jdbctamplate;

import net.oleksin.paymentsystem.payment.BatchPaymentService;
import net.oleksin.paymentsystem.payment.PaymentService;
import net.oleksin.paymentsystem.payment.Payment;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("jdbctamplate")
public class BatchPaymentServiceJdbc implements BatchPaymentService {
    private  final PaymentService paymentService;

    public BatchPaymentServiceJdbc(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Set<Payment> createNewPayments(Set<Payment> payments) {
        Set<Payment> newPayments = new HashSet<>();
        payments.forEach(payment -> {
            newPayments.add(paymentService.createNewPayment(payment));
        });
        return newPayments;
    }
}
