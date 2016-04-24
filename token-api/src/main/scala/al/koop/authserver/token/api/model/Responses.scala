package al.koop.authserver.token.api.model

case class CreateTokenResponse(token: String, refreshToken: String)