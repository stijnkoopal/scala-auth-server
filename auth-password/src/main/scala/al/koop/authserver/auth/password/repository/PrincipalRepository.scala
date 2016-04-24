package al.koop.authserver.auth.password.repository

import al.koop.authserver.auth.password.db.Tables
import al.koop.authserver.auth.password.util.db.AgnosticDriver.api._
import al.koop.authserver.auth.password.model.Principal
import slick.driver.JdbcProfile

import scala.concurrent.Future

trait PrincipalRepository {
  def findAccountByEmail(email: String): Future[Option[Principal]]
}

class DbPrincipalRepository()(implicit val db: JdbcProfile#Backend#Database) extends TableQuery(new Tables.Principals(_)) with PrincipalRepository {

  def findAccountByEmail(email: String): Future[Option[Principal]] =
    db.run(
      this.filter(_.email === email).result.headOption
    )
}
