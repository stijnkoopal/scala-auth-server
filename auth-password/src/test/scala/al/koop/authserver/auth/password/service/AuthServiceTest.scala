package al.koop.authserver.auth.password.service

import al.koop.authserver.auth.password.gateway.TokenGateway
import al.koop.authserver.auth.password.model.{InvalidCredentials, Principal, Successful}
import al.koop.authserver.auth.password.policy.PasswordPolicy
import al.koop.authserver.auth.password.repository.PrincipalRepository
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuthServiceTest extends FlatSpec with Matchers with MockFactory with ScalaFutures {
  implicit val passwordPolicyMock = mock[PasswordPolicy]
  val authRepositoryMock = mock[PrincipalRepository]
  val tokenGatewayMock = mock[TokenGateway]
  val authService = new AuthServiceImpl(authRepositoryMock, tokenGatewayMock)

  it should "return a success when all goes well" in {
    (authRepositoryMock.findAccountByEmail _).expects("user").returning(Future { Some(Principal("user", "pass")) })
    (tokenGatewayMock.requestToken _).expects("user").returning(Future { ("token", "refreshToken") } )
    (passwordPolicyMock.isValidPassword _).expects("pass", "pass").returning(true)

    val authenticationResult = authService ? ("user", "pass")

    authenticationResult.futureValue shouldBe a [Successful]
    authenticationResult.futureValue should equal(Successful("token", "refreshToken"))
  }

  it should "return invalid credentials when password is wrong" in {
    (authRepositoryMock.findAccountByEmail _).expects("user").returning(Future { Some(Principal("user", "invalidpass")) })
    (passwordPolicyMock.isValidPassword _).expects("pass", "invalidpass").returning(false)

    val authenticationResult = authService ? ("user", "pass")

    authenticationResult.futureValue shouldBe a [InvalidCredentials]
  }

  it should "return invalid credentials when principal cannot be found" in {
    (authRepositoryMock.findAccountByEmail _).expects("user").returning(Future { None })

    val authenticationResult = authService ? ("user", "pass")

    authenticationResult.futureValue shouldBe a [InvalidCredentials]
  }

}
