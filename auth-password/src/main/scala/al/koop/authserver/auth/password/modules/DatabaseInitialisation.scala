package al.koop.authserver.auth.password.modules

import al.koop.authserver.auth.password.db.Tables
import al.koop.authserver.auth.password.util.db.AgnosticDriver.api._
import al.koop.authserver.auth.password.policy.PasswordPolicy
import slick.dbio.DBIO
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

trait DatabaseInitialisationImpl {
  persistence: Persistence with Service  =>

  implicit val db: JdbcProfile#Backend#Database
  val passwordPolicy: PasswordPolicy

  import al.koop.authserver.auth.password.model.Principal

  val principals = TableQuery[Tables.Principals]

  val setup = DBIO.seq (
    // Create the tables, including primary and foreign keys
    principals.schema.create,

    // Insert some principals
    principals += Principal("test", passwordPolicy.hash("test")),
    principals += Principal("test1", passwordPolicy.hash("test1"))

  )
  db.run(setup)
}