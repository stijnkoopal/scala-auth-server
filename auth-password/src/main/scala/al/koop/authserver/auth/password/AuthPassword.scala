package al.koop.authserver.auth.password

import akka.actor.{Actor, Props}
import al.koop.authserver.auth.password.model._
import al.koop.authserver.auth.password.model.rest.LoginRequest
import al.koop.authserver.auth.password.model.rest.marshalling.LoginRequestFormat
import al.koop.authserver.auth.password.service.AuthService
import spray.http.HttpHeaders.RawHeader
import spray.http.StatusCodes.{ServiceUnavailable, NoContent, OK, Unauthorized}
import spray.routing.HttpService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

class AuthPasswordRestActor(authenticationService: AuthService) extends Actor with RestRouting {
  val authService = authenticationService

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(jsonRoute)
}

object AuthPasswordRestActor {
  def props(authenticationService: AuthService) = Props(new AuthPasswordRestActor(authenticationService))
}

trait RestRouting extends HttpService {
  val authService: AuthService

  val jsonRoute = {
    import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

    pathPrefix("auth") {
      // Status
      path("status") {
        get {
          complete {
            OK
          }
        }
      } ~
      path("login") {
        // Create a new login
        post {
          import LoginRequestFormat._
          entity(as[LoginRequest]) { request =>
            onComplete(authService ?(request.email, request.password)) {
              case Failure(ex) => complete(ServiceUnavailable)
              case Success(result) => result match {
                case Successful(token, refreshToken) => respondWithTokens(token, refreshToken) {
                  complete(NoContent)
                }
                case InvalidCredentials() => complete(Unauthorized)
              }
            }
          }
        }
      }
    }
  }

  private def respondWithTokens(token: String, refreshToken: String) =
    respondWithHeaders(RawHeader("X-AUTH-TOKEN", token), RawHeader("X-REFRESH-TOKEN", refreshToken))
}