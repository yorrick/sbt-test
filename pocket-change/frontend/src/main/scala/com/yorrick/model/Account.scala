package com.yorrick.model

import _root_.java.math.MathContext
import _root_.net.liftweb.mapper._
import net.liftweb.common.Empty


// Create an Account class extending the LongKeyedMapper superclass
// (which is a "mapped" (to the database) trait that uses a Long primary key)
// and mixes in trait IdPK, which adds a primary key called "id".
class Account extends LongKeyedMapper[Account] with IdPK {
  // Define the singleton, as in the "User" class
  def getSingleton = Account

  // Define a many-to-one (foreign key) relationship to the User class
  object owner extends MappedLongForeignKey(this, User) {
    // Change the default behavior to add a database index
    // for this column.
    override def dbIndexed_? = true
  }

  // Define an "access control" field that defaults to false. We’ll
  // use this in the SiteMap chapter to allow the Account owner to
  // share out an account view.
  object is_public extends MappedBoolean(this) {
    override def defaultValue = false
  }

  // Define the field to hold the actual account balance with up to 16
  // digits (DECIMAL64) and 2 decimal places
  object balance extends MappedDecimal(this, MathContext.DECIMAL64, 2)


  object name extends MappedString(this,100)
  object description extends MappedString(this, 300)

  // Define utility methods for simplifying access to related classes. We’ll
  // cover how these methods work in the Mapper chapter
//  def admins = AccountAdmin.findAll(By(AccountAdmin.account, this.id))
//  def addAdmin (user : User) =
//    AccountAdmin.create.account(this).administrator(user).save
//  def viewers = AccountViewer.findAll(By(AccountViewer.account, this.id))
//  def entries = Expense.getByAcct(this, Empty, Empty, Empty)
//  def tags = Tag.findAll(By(Tag.account, this.id))
//  def notes = AccountNote.findAll(By(AccountNote.account, this.id))
}

// The companion object to the above Class
object Account extends Account with LongKeyedMetaMapper[Account] {
  // Define a utility method for locating an account by owner and name
  def findByName (owner : User, name : String) : List[Account] =
    Account.findAll(By(Account.owner, owner.id.is), By(Account.name, name))

//  ... more utility methods ...
}