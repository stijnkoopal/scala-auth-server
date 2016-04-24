package al.koop.authserver.auth.password.util.db

import com.typesafe.config.ConfigFactory
import slick.driver.{H2Driver, JdbcDriver, MySQLDriver}

object AgnosticDriver {
  val api = profile.api
  lazy val profile: JdbcDriver = {
    sys.env.get("DB_ENVIRONMENT") match {
      case Some(e) => ConfigFactory.load().getString(s"$e.slickDriver") match {
        case "scala.slick.driver.H2Driver" => H2Driver
        case "scala.slick.driver.MySQLDriver" => MySQLDriver
      }
      case _ => H2Driver
    }
  }
}