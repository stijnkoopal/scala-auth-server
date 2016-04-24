package al.koop.authserver.auth.password.modules

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait Persistence {
  implicit val db: JdbcProfile#Backend#Database
}

trait PersistenceImpl extends Persistence {
  private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("database")
  override implicit val db: JdbcProfile#Backend#Database = dbConfig.db
}