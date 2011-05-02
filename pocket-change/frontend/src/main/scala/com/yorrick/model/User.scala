package com.yorrick.model

// Import all of the mapper classes
import _root_.net.liftweb.mapper._
import net.liftweb.util.Log
import net.liftweb.common.Logger

// Create a User class extending the Mapper base class
// MegaProtoUser, which provides default fields and methods
// for a site user.
class User extends MegaProtoUser[User] {
  def getSingleton = User // reference to the companion object below
  def allAccounts : List[Account] =
    Account.findAll(By(Account.owner, this.id))
}

// Create a "companion object" to the User class (above).
// The companion object is a "singleton" object that shares the same
// name as its companion class. It provides global (i.e. non-instance)
// methods and fields, such as find, dbTableName, dbIndexes, etc.
// For more, see the Scala documentation on singleton objects
object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users" // define the DB table name

  // Just for testing purposes. In production we remove this
  override def skipEmailValidation = true

  // Provide our own login page template .
  override def loginXhtml =
    <lift:surround with="default" at="content">
      { super.loginXhtml }
    </lift:surround>

  // Provide our own signup page template.
  override def signupXhtml(user: User) =
    <lift:surround with="default" at="content">
      { super.signupXhtml(user) }
    </lift:surround>

//  /**
//   * Overridden to add logging of activation url to be able to activate
//   * without sending mail.
//   */
//  override def signupMailBody(user: User, validationLink: String) = {
//    println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\nSending activation mail to " + validationLink)
//    super.signupMailBody(user, validationLink)
//  }

}
