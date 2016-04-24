package al.koop.authserver.auth.password.policy

import com.github.t3hnar.bcrypt._

trait PasswordPolicy {
  def hash(password: String): String
  def isValidPassword(unhashedPassword: String, hashedPassword: String): Boolean
}

class BCryptPasswordPolicy(val rounds: Int = 13) extends PasswordPolicy {
  override def hash(password: String): String = password.bcrypt(rounds)
  override def isValidPassword(unhashedPassword: String, hashedPassword: String) = unhashedPassword.isBcrypted(hashedPassword)
}
