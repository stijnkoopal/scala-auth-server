package al.koop.authserver.auth.password.gateway

import akka.actor.ActorSystem
import al.koop.authserver.token.api.model._
import spray.client.pipelining._
import spray.http._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

trait TokenGateway {
  def requestToken(id: String): Future[(String, String)]
}

class RealTokenGateway(val protocol: String, interface: String, port: Int)(implicit val system: ActorSystem) extends TokenGateway {
  override def requestToken(id: String): Future[(String, String)] = {
    import spray.httpx.SprayJsonSupport.{sprayJsonMarshaller, sprayJsonUnmarshaller}
    import CreateTokenRequestFormat._
    import CreateTokenResponseFormat._

    val createTokenRequest = CreateTokenRequest(id, "pass")
    val pipeline: HttpRequest => Future[CreateTokenResponse] = sendReceive ~> unmarshal[CreateTokenResponse]

    pipeline(Post(s"$protocol://$interface:$port/tokens", createTokenRequest)).map(response => (response.token, response.refreshToken))
  }
}
