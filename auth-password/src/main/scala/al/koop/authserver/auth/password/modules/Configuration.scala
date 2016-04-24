package al.koop.authserver.auth.password.modules

import com.typesafe.config.ConfigFactory

trait Configuration {
  val interface: String
  val port: Int

  val tokenManagerProtocol: String
  val tokenManagerHost: String
  val tokenManagerPort: Int
}

trait ConfigurationImpl extends Configuration {
  private lazy val config = ConfigFactory.load()

  override val interface = config.getString("http.interface")
  override val port = config.getInt("http.port")

  override val tokenManagerProtocol = config.getString("tokenManager.protocol")
  override val tokenManagerHost = config.getString(s"tokenManager.$tokenManagerProtocol.host")
  override val tokenManagerPort = config.getInt(s"tokenManager.$tokenManagerProtocol.port")
}