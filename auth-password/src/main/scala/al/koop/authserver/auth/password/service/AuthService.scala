package al.koop.authserver.auth.password.service

import al.koop.authserver.auth.password.gateway.TokenGateway
import al.koop.authserver.auth.password.model.{AuthenticationResult, InvalidCredentials, Successful}
import al.koop.authserver.auth.password.policy.PasswordPolicy
import al.koop.authserver.auth.password.repository.PrincipalRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Future.successful

trait AuthService {
  def apply(email: String, password: String): Future[AuthenticationResult]
  def ? (userPass: (String, String)): Future[AuthenticationResult] = userPass match {
    case(username, password) => apply(username, password)
  }
}

class AuthServiceImpl(repository: PrincipalRepository, tokenGateway: TokenGateway)(implicit val passwordPolicy: PasswordPolicy) extends AuthService {

  override def apply(email: String, password: String): Future[AuthenticationResult] = {
    repository.findAccountByEmail(email)
      .flatMap {
        case Some(principal) =>
            if (passwordPolicy.isValidPassword(password, principal.password))
              tokenGateway.requestToken(principal.email)
                .map { case (token: String, refreshToken: String) => Successful(token, refreshToken) }
            else
              successful(InvalidCredentials())

        case None => successful(InvalidCredentials())
      }
  }

}
