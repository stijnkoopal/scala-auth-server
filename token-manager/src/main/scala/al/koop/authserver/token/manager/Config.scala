package al.koop.authserver.token.manager

import com.typesafe.config.ConfigFactory

trait Config {
  private lazy val config = ConfigFactory.load()

  lazy val interface = config.getString("http.interface")
  lazy val port = config.getInt("http.port")

  lazy val tokenSecretKey = config.getString("token.key")
  lazy val tokenAlgorithm = config.getString("token.algorithm")
}
