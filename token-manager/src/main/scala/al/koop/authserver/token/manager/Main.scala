package al.koop.authserver.token.manager

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import al.koop.authserver.token.manager.service.TokenService
import spray.can.Http

import scala.concurrent.duration._

object Main extends App with Config {
  implicit val system = ActorSystem("TokenManager")
  implicit val tokenService = new TokenService(tokenSecretKey, tokenAlgorithm)

  val actor = system.actorOf(TokenManagerRestRouting.props())

  // IO requires an implicit ActorSystem, and ? requires an implicit timeout
  // Bind HTTP to the specified service.
  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(actor, interface = interface, port = port)
}
