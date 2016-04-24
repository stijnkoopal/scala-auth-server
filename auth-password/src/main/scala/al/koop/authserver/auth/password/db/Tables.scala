package al.koop.authserver.auth.password.db

import al.koop.authserver.auth.password.model.Principal
import al.koop.authserver.auth.password.util.db.AgnosticDriver.api._

object Tables {
  class Principals(tag: Tag) extends Table[Principal](tag, "Principals") {
    def email = column[String]("email", O.PrimaryKey)
    def password = column[String]("password")

    def * = (email, password) <> (Principal.tupled, Principal.unapply _)
  }
}
