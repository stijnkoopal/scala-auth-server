package al.koop.authserver.auth.password.modules

import al.koop.authserver.auth.password.gateway.{RealTokenGateway, TokenGateway}
import al.koop.authserver.auth.password.policy.{BCryptPasswordPolicy, PasswordPolicy}
import al.koop.authserver.auth.password.repository.{DbPrincipalRepository, PrincipalRepository}
import al.koop.authserver.auth.password.service.{AuthService, AuthServiceImpl}

import scala.concurrent.ExecutionContext.Implicits.global

trait Service {
  val principalRepository: PrincipalRepository
  val tokenGateway: TokenGateway
  val authService: AuthService
  implicit val passwordPolicy: PasswordPolicy
}

trait ServiceImpl extends Service { this: Persistence with Configuration with Actors =>
  override lazy val principalRepository = new DbPrincipalRepository
  override lazy val tokenGateway = new RealTokenGateway(tokenManagerProtocol, tokenManagerHost, tokenManagerPort)

  override implicit val passwordPolicy = new BCryptPasswordPolicy
  override lazy val  authService: AuthService = new AuthServiceImpl(principalRepository, tokenGateway)
}
