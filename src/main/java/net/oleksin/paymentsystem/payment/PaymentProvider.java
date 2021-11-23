package net.oleksin.paymentsystem.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.oleksin.s3starter.client.S3ClientWorker;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentProvider {

    private final PaymentService paymentService;
    private final BatchPaymentService batchPaymentService;
    private final S3ClientWorker s3ClientWorker;

    public Payment createNewPayment(Payment payment) {
        Payment newPayment = paymentService.createNewPayment(payment);
        try {
            s3ClientWorker.putObject(getNewByteArrayInputStream(newPayment),
                    newPayment.getClass().getSimpleName(),
                    newPayment.getId());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        s3ClientWorker.listObjects().forEach(log::info);
        return newPayment;
    }

    public List<Payment> createNewPayments(List<Payment> payments) {
        List<Payment> paymentList = batchPaymentService.createNewPayments(payments);
        paymentList.forEach(payment -> {
            try {
                s3ClientWorker.putObject(getNewByteArrayInputStream(payment),
                        payment.getClass().getSimpleName(),
                        payment.getId());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
        s3ClientWorker.listObjects().forEach(log::info);
        return paymentList;
    }

    public List<PaymentJournalDto> getPaymentJournals(Long payerId, Long recipientId, Long srcAccId, Long destAccId) {
        List<PaymentJournalDto> paymentJournalDtos = batchPaymentService.getPaymentJournals(payerId, recipientId, srcAccId, destAccId);
        paymentJournalDtos.forEach(paymentJournalDto -> {
            try {
                s3ClientWorker.putObject(new ByteArrayInputStream(paymentJournalDto.toString().getBytes(StandardCharsets.UTF_8)),
                        paymentJournalDto.getClass().getSimpleName(),
                        paymentJournalDto.getPaymentId());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
        s3ClientWorker.listObjects().forEach(log::info);
        return paymentJournalDtos;
    }

    public static ByteArrayInputStream getNewByteArrayInputStream(Payment payment) {
        return new ByteArrayInputStream(payment.toString().getBytes(StandardCharsets.UTF_8));
    }
}
