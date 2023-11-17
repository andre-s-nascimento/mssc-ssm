package guru.springframework.msscssm.services;

import static org.junit.jupiter.api.Assertions.*;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PaymentServiceImplTest {

  @Autowired PaymentService paymentService;

  @Autowired PaymentRepository paymentRepository;

  Payment payment;

  @BeforeEach
  void setUp() {
    payment = Payment.builder().amount(new BigDecimal("12.99")).build();
  }

  @Transactional
  @Test
  void preAuth() {
    Payment savedPayment = paymentService.newPayment(payment);

    System.out.println("Should be NEW");
    System.out.println(savedPayment.getState());
    // assertEquals(PaymentState.NEW, savedPayment.getState());

    StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

    Payment preAuthedPayment = paymentRepository.getReferenceById(savedPayment.getId());

    System.out.println("Should be PRE_AUTH");
    System.out.println(sm.getState().getId());
    // assertEquals(PaymentState.PRE_AUTH, sm.getState().getId());
    System.out.println(preAuthedPayment);
  }

  @Transactional
  @RepeatedTest(10)
  void testAuth() {
    Payment savedPayment = paymentService.newPayment(payment);

    StateMachine<PaymentState, PaymentEvent> preAuthSM =
        paymentService.preAuth(savedPayment.getId());

    if (preAuthSM.getState().getId() == PaymentState.PRE_AUTH) {
      System.out.println("Payment is Pre Authorized");

      StateMachine<PaymentState, PaymentEvent> authSM =
          paymentService.authorizePayment(savedPayment.getId());

      // assertEquals(PaymentState.NEW, savedPayment.getState());

      System.out.println("Result of Auth: " + authSM.getState().getId());
    } else {
      System.out.println("Payment failed pre-auth...");
    }
  }
}
