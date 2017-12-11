package main.java;

import akka.actor.Props;
import akka.actor.UntypedActor;
import kamon.Kamon;
import kamon.metric.MinMaxCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouterCreatedActor extends UntypedActor {

  private Logger log = LoggerFactory.getLogger(this.getClass());
  private MinMaxCounter rangeSampler = Kamon.minMaxCounter("RouterCreatedActor");

  @Override
  public void preStart() throws Exception {
    rangeSampler.increment();
    log.info("Started Actor: " + getSelf().path().toSerializationFormat());
  }

  @Override
  public void postStop() throws Exception {
    rangeSampler.decrement();
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Long) {
      context().actorOf(Props.create(UserCreatedActor.class), "userCreated" + message.toString())
          .forward(message, getContext());
    }
  }
}
