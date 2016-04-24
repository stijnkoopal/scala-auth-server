package al.koop.authserver.auth.password.model.rest.marshalling

import al.koop.authserver.auth.password.model.rest.LoginRequest
import spray.json.DefaultJsonProtocol

object LoginRequestFormat extends DefaultJsonProtocol {
  implicit val loginRequestJson = jsonFormat2(LoginRequest)
}