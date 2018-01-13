import java.util.*;

/***
 *  Class: Member
 *  Author: Jason Schatz
 *
 *  Member objects automatically add themselves to the Singleton instance
 *  of Club when they are instantiated - this allows Club to serve as a 
 *  name directory for Members, looking up Members objects by thier names.
 *
 *  Member objects keep track of a set of friend members.  They can also determine
 *  Whether or not a Member is an aquaintance - an aquaintance is defined as a friend, 
 *  or the friend of a friend.
 */

public class Member 
{
  private List friends;
  private String name;
  private Club club;
  private Member aquaintance;

  public Member(String n) 
  {
    friends = new ArrayList();
    name = n;
    club = Club.getInstance();

    club.addMember(this);
  }

  public boolean addFriend(Member m) 
  {
    if (isFriend(m)) 
    {
      return false;
    }

    friends.add(m);
    return true;
  }

  public boolean isFriend(Member m) 
  {
    return friends.contains(m);
  }

  public boolean isFriend(String name) 
  {
    return isFriend(club.memberForName(name));
  }


  //  A member is an aquaintance if it is a friend, or the 
  //  friend of a friend.

  public boolean isAquaintance(Member m) 
  {
    Iterator i = m.getFriendList().iterator();
    
    while(i.hasNext())
    {
      aquaintance = (Member)i.next();
      if(friends.contains(aquaintance))
      {
        return true;
      }
    }
    if(friends.contains(m) || m.getFriendList().contains(this))
    {
      return true;
    }
    
    return false;
  }

  public boolean isAquaintance(String name) 
  {
    if(isFriend(club.memberForName(name)))
    {
      return true;
    }
    
    return false;
  }

  public String getName() 
  {
    return name;
  }
	
  public List getFriendList() 
  {
    return friends;
  }

  public String toString() 
  {
    return name;
  }
}