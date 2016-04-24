package al.koop.authserver.auth.password

import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import al.koop.authserver.auth.password.modules._
import spray.can.Http

import scala.concurrent.duration._

object Main extends App {
  val modules = new ActorsImpl with PersistenceImpl with ServiceImpl with ConfigurationImpl with DatabaseInitialisationImpl

  implicit val system = modules.system
  val actor = modules.system.actorOf(AuthPasswordRestActor.props(modules.authService))

  // IO requires an implicit ActorSystem, and ? requires an implicit timeout
  // Bind HTTP to the specified service.
  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(actor, interface = modules.interface, port = modules.port)
}

