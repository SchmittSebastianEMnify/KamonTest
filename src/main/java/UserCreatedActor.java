package main.java;

import akka.actor.UntypedActor;
import kamon.Kamon;
import kamon.metric.MinMaxCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCreatedActor extends UntypedActor {

  private Logger log = LoggerFactory.getLogger(this.getClass());
  private MinMaxCounter rangeSampler = Kamon.minMaxCounter("UserCreatedActor");

  @Override
  public void preStart() throws Exception {
    rangeSampler.increment();
  }

  @Override
  public void postStop() throws Exception {
    rangeSampler.decrement();
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Long) {
      getSender()
          .tell("ok: " + message.toString() + "; RouterCreated: " + context().parent().path().name()
              + "; UserCreated: " + getSelf().path().name(), getSelf());
      getContext().stop(getSelf());
    }
  }
}
