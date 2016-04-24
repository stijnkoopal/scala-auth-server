package al.koop.authserver.auth.password.modules

import akka.actor.ActorSystem

trait Actors {
  implicit val system: ActorSystem
}

trait ActorsImpl extends Actors {
  override implicit val system = ActorSystem("AuthPassword")
}