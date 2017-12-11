package main.java;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.FromConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;


public class Supervisor extends UntypedActor {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  private ActorRef backendRouter = getContext().actorOf(
      FromConfig.getInstance().props(Props.create(RouterCreatedActor.class)), "router");

  @Override
  public void preStart() {
    getContext().system().scheduler()
        .schedule(Duration.Zero(), Duration.create(1000, TimeUnit.MILLISECONDS),
            () -> backendRouter.tell(System.currentTimeMillis(), getSelf())
            , getContext().system().dispatcher());
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof String) {
      log.info((String) message);
    } else {
      unhandled(message);
    }
  }
}
