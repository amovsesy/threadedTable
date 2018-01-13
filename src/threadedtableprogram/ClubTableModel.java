import javax.swing.table.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.JOptionPane.*;

public class ClubTableModel extends AbstractTableModel {

		private Club club;
		private int memberCount;
		private int largestFriendCount;
		
		public ClubTableModel() {
			club = Club.getInstance();
                        initializeClub();
                        getRowCount();
                        getColumnCount();;
		}
		
		public int getColumnCount() {
			return calculateLargestFriendCount() + 2;
		}
		
		public int getRowCount() {
			return club.getMemberList().size();
		}
		
		public Object getValueAt(int r, int c) {
			Member rowMember = (Member) club.getMemberList().get(r);
			
			// column one is dedicated to the original Members, must be subtracted
			int friendIndex = c - 1;
			
			// handle the first column
			if (c == 0)	return rowMember;
			
			// handle all of the cells that are null (too far right to be full)
			if (friendIndex >= rowMember.getFriendList().size()) return null;

			// return friends to be displayed
			return rowMember.getFriendList().get(friendIndex);			
		}
		
		public void setValueAt(Object newMember, int r, int c) {
			Member temp;
			Member rowMember = (Member) club.getMemberList().get(r);
			String tempS = (String)newMember;
			List memberFriends = (ArrayList)rowMember.getFriendList();
			int friendIndex = c - 1;


			if(tempS != "" || tempS != null) {
				//if member exists, find member, else create new member
				if(club.hasMember(tempS)) {
					temp = club.memberForName(tempS);
				}
				else {
			 		temp = new Member(tempS);
					club.addMember(temp);
				}

				if(rowMember.isFriend(temp)) {
					JOptionPane.showMessageDialog(null, temp.toString() + " is already a friend");
				}
				else {
					if(friendIndex < memberFriends.size()) {
						memberFriends.remove(friendIndex);
						rowMember.addFriend(temp);
					}
					else {
					rowMember.addFriend(temp);
					}
				}
			}

			fireTableStructureChanged();
		}

		public boolean isCellEditable(int r, int c) {
			if(c == 0)
			{ return false;}
			else
			{ return true;}
		}

	private void initializeClub() {		
		new Member("A");
		new Member("B");
		new Member("C");
		new Member("D");
		new Member("E");
		new Member("F");
		new Member("G");
		new Member("H");
		new Member("I");
		new Member("J");
		new Member("K");
		new Member("L");
		new Member("M");
		new Member("Z");

		createTwoWayFriendship("D", "H");
		createTwoWayFriendship("J", "B");
		createTwoWayFriendship("C", "K");
		createTwoWayFriendship("D", "M");
		createTwoWayFriendship("F", "J");
		createTwoWayFriendship("H", "G");
		createTwoWayFriendship("F", "I");
		createTwoWayFriendship("G", "A");
		createTwoWayFriendship("B", "F");
		createTwoWayFriendship("A", "D");
	}

	private static Member member(String name) {	
		return Club.getInstance().memberForName(name);
	}

	private static void createTwoWayFriendship(String n1, String n2) {	
		member(n1).addFriend(member(n2));
		member(n2).addFriend(member(n1));
	}	
	
	private int calculateLargestFriendCount() {
		Iterator i = club.getMemberList().iterator();
		int max = 0;
		int current;
		
		while (i.hasNext()) {
			current = ((Member)i.next()).getFriendList().size();
			
			if (current > max) {
				max = current;
			}
		}

		return max;
	}
}