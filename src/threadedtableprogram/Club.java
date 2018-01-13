import java.util.*;

/***
 *  The Club class uses List to keep track of a set of members.  It
 *  also serves as a directory so that member objects can be retrieved
 *  by name.
 *
 *  The class implements the Singleton design patter - Any class can get
 *  access to a single instance of a club object by invoking the getInstance() method
 *  Look at the Member class constructor, and the ClubTesters main function to
 *  see examples.  Notice that by using the getInstance method, all Members automaticaly
 *  add themselves to the same club.
 */

public class Club 
{
  
  private static Club instance;
  private List members;

  // Returns the same instance of Club, regardless of
  // where it is being invoked.

  public static Club getInstance() 
  {
    if (instance == null) 
    {
      instance = new Club();
    }

    return instance;
  }

  public Club() 
  {
    members = new ArrayList();
  }

  // Returns true if the members set is modified, otherwise
  // returns false

  public boolean addMember(Member m) 
  {
    if (members.contains(m)) 
    {
      return false;
    }

    members.add(m);
    return true;
  }

  // Returns a Member object if one exists with a name
  // that matches the name argument, otherwise returns
  // null

  public Member memberForName(String name) 
  {
    Iterator i = members.iterator();
    Member m;

    while(i.hasNext()) 
    {
      m = (Member) i.next();
      if (m.getName().equals(name)) 
      {
        return m;
      }
    }

    return null;
  }

  public boolean hasMember(String name) 
  {
    return memberForName(name) != null;
  }

  public boolean hasMember(Member m) 
  {
    return members.contains(m);
  }
	
  public List getMemberList() 
  {
    return members;
  }
}