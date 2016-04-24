package al.koop.authserver.token.manager.service

import al.koop.authserver.token.api.model.TokenModel
import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import org.json4s.{DefaultFormats, Extraction}

class TokenService(key: String, algorithm: String) {
  implicit val formats = DefaultFormats

  val header = JwtHeader(algorithm)

  def validate(token: String): Boolean = JsonWebToken.validate(token, key)

  def create(tokenModel : TokenModel) : String = {
    def claimsSet = JwtClaimsSet(Extraction.decompose(tokenModel))
    JsonWebToken(header, claimsSet, key)
  }

  def retrieveClaimset(token: String) : Option[TokenModel] = {
    token match {
      case JsonWebToken(h, claimset, signature) =>
        Extraction.extract(claimset.jvalue)
      case _ =>
        None
    }
  }
}
