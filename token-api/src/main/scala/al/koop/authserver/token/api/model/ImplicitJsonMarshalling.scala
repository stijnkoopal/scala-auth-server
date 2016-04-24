package al.koop.authserver.token.api.model

import spray.json.DefaultJsonProtocol

object TokenModelFormat extends DefaultJsonProtocol {
  implicit val tokenModelJson = jsonFormat1(TokenModel)
}

object CreateTokenRequestFormat extends DefaultJsonProtocol {
  implicit val createTokenRequestJson = jsonFormat2(CreateTokenRequest)
}

object CreateTokenResponseFormat extends DefaultJsonProtocol {
  implicit val CreateTokenResponseJson = jsonFormat2(CreateTokenResponse)
}

object RefreshTokenRequestFormat extends DefaultJsonProtocol {
    implicit val refreshTokenRequestJson = jsonFormat1(RefreshTokenRequest)
}

object RemoveTokenRequestFormat extends DefaultJsonProtocol {
    implicit val RemoveTokenRequestJson = jsonFormat1(RemoveRefreshTokenRequest)
}