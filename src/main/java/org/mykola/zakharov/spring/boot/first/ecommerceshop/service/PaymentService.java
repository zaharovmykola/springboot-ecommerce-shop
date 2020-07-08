package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.PaymentHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Payment;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.PaymentModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.PaymentResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentHibernateDAO dao;

    public PaymentResponseModel pay(Payment payment) {
        dao.save(payment);
        PaymentResponseModel response =
                PaymentResponseModel.builder()
                .status("success")
                .message("Payment successfull with amount : " + payment.getAmount())
                .build();
        return response;
    }

    public PaymentResponseModel getTx(String vendor) {
        List<Payment> payments = dao.findByVendor(vendor);
        List<PaymentModel> paymentModels = payments.stream().map((p) ->
            PaymentModel.builder()
                .id(p.getId())
                .transactionId(p.getTransactionId())
                .vendor(p.getVendor())
                .paymentDateString((new SimpleDateFormat("dd/mm/yyyy HH:mm:ss a")).format(p.getPaymentDate()))
                .amount(p.getAmount())
                .build()
        ).collect(Collectors.toList());
        return PaymentResponseModel.builder()
            .status("success")
            .payments(paymentModels)
            .build();
    }
}
