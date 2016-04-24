package al.koop.authserver.token.manager

import akka.actor.{Actor, Props}
import al.koop.authserver.token.api.model.{CreateTokenResponse, _}
import al.koop.authserver.token.manager.service.TokenService
import spray.http.StatusCodes
import spray.routing.HttpService

class TokenManagerRestRouting(implicit tokenServiceImplicit: TokenService) extends Actor with RestRouting {
  val tokenService = tokenServiceImplicit

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(jsonRoute)
}

object TokenManagerRestRouting {
  def props() (implicit tokenServiceImplicit: TokenService) = Props(new TokenManagerRestRouting())
}

trait RestRouting extends HttpService {
 val tokenService: TokenService

  val jsonRoute = {
    import spray.httpx.SprayJsonSupport.{sprayJsonMarshaller, sprayJsonUnmarshaller}

    pathPrefix("tokens") {
      // Status
      path("status") {
        get {
          complete {
            StatusCodes.OK
          }
        }
      } ~
      // Return the content of the given token
      path(Segment) { token =>
        get {
          import TokenModelFormat._
          complete {
            tokenService retrieveClaimset token match {
              case None => StatusCodes.NotFound
              case Some(model) => StatusCodes.OK -> model
            }
          }
        }
      } ~
      // Create a new token
      post {
        import CreateTokenRequestFormat._
        import CreateTokenResponseFormat._
        entity(as[CreateTokenRequest]) { request =>
          complete {
            StatusCodes.Created -> CreateTokenResponse(tokenService.create(TokenModel(request.identityId)), "refresh")
          }
        }
      } ~
      // Refresh a token
      patch {
        import RefreshTokenRequestFormat._
        entity(as[RefreshTokenRequest]) { request =>
          complete {
            StatusCodes.NotImplemented
          }
        }
      } ~
      // Remove a refresh token
      delete {
        import RemoveTokenRequestFormat._
        entity(as[RemoveRefreshTokenRequest]) { request =>
          complete {
            StatusCodes.NotImplemented
          }
        }
      }
    }
  }
}