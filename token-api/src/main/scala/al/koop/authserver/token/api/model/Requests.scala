package al.koop.authserver.token.api.model

case class CreateTokenRequest(identityId: String, authMethod: String)
case class RefreshTokenRequest(refreshToken: String)
case class RemoveRefreshTokenRequest(refreshToken: String)
