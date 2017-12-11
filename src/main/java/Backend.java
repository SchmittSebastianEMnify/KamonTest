package main.java;

import akka.actor.ActorSystem;
import akka.actor.Props;
import kamon.Kamon;


public class Backend {


  public static void main(String[] args) {
    startup();
  }

  private static void startup() {

    Kamon.loadReportersFromConfig();

    // Create an Akka system
    ActorSystem system = ActorSystem.create("ClusterSystem");

    // Create an actor that handles cluster domain events
    system.actorOf(Props.create(Supervisor.class), "backend");

  }
}
