package al.koop.authserver.auth.password.model

sealed trait AuthenticationResult
case class Successful(token: String, refreshToken: String) extends AuthenticationResult
case class InvalidCredentials() extends AuthenticationResult
