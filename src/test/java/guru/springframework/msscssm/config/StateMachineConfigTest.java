package guru.springframework.msscssm.config;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

@SpringBootTest
class StateMachineConfigTest {
  @Autowired StateMachineFactory<PaymentState, PaymentEvent> factory;

  @Test
  void testNewStateMachine() {
    StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine(UUID.randomUUID());
    sm.start();

    System.out.println("START " + sm.getState().toString());

    sm.sendEvent(PaymentEvent.PRE_AUTHORIZE);

    System.out.println("AFTER " + PaymentEvent.PRE_AUTHORIZE + " " + sm.getState().toString());

    sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);

    System.out.println("AFTER " + PaymentEvent.PRE_AUTH_APPROVED + " " + sm.getState().toString());

    sm.sendEvent(PaymentEvent.PRE_AUTH_DECLINED);

    System.out.println("AFTER " + PaymentEvent.PRE_AUTH_DECLINED + " " + sm.getState().toString());
  }
}
