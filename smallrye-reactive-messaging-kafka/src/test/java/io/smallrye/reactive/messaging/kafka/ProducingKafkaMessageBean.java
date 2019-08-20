package io.smallrye.reactive.messaging.kafka;

import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class ProducingKafkaMessageBean {

  private AtomicInteger counter = new AtomicInteger();

  @Incoming("data")
  @Outgoing("output-2")
  @Acknowledgment(Acknowledgment.Strategy.MANUAL)
  public KafkaMessage<String, Integer> process(Message<Integer> input) {
    KafkaMessage<String, Integer> message = KafkaMessage.of(
      Integer.toString(input.getPayload()), input.getPayload() + 1).withAck(input::ack);
    message.getHeaders().put("hello", "clement").put("count", Integer.toString(counter.incrementAndGet()));
    return message;
  }

  @Outgoing("data")
  public Publisher<Integer> source() {
    return Flowable.range(0, 10);
  }

}
