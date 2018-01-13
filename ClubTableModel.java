package cs211m.club;
import javax.swing.table.*;
// import cs211m.club.*;
import java.util.*;

public class ClubTableModel extends AbstractTableModel {
	/*
	 * The serializable class ClubTableModel need to declare a static final
	 * serialVersionUID field of type long.
	 */
	private static final long serialVersionUID = 1L;

	private Club club;

	public ClubTableModel() {
		club = Club.getInstance();
		initializeClub();
		getRowCount();
		getColumnCount();
	}

	public int getColumnCount() {
		// plus 2 for extra columns in addition to the largest friend count:
		// one for the rowMember on the column 0,
		// the other as empty column for the usage of adding new friend
		return calculateLargestFriendCount() + 2;
	}

	public int getRowCount() {
		return club.getMemberList().size();
	}

	public Object getValueAt(int r, int c) {
		Member rowMember = (Member) club.getMemberList().get(r);

		// handle the first column
		if (c == 0)
			return rowMember;

		// column one is dedicated to the original Members, must be subtracted
		int friendIndex = c - 1;

		// handle all of the cells that are null (too far right to be full)
		if (rowMember.getFriendList().size() <= friendIndex)
			return null;

		// return friends to be displayed
		return rowMember.getFriendList().get(friendIndex);
	}

	public boolean isCellEditable(int r, int c) {
		// Column 0 (Member lists) should be un-editable.
		if (c == 0)
			return false;
		return true;
	}

	public void setValueAt(Object memberName, int r, int c) {
		Member newFriendMember = club.memberForName((String) memberName);
		// get the member's name on the column 0
		Member rowMember = (Member) club.getMemberList().get(r);
		// get the overwitten member's name
		Member replacedMember = (Member) getValueAt(r, c);

		// A new member is created and automatically added to the club if the
		// newFriendMember, including empty string, is not an existing Membe.
		if (!club.hasMember(newFriendMember))
			newFriendMember = new Member((String) memberName);

		// member can't be friend to itself
		if (rowMember.getName().equals(newFriendMember.getName()))
			return;

		// Remove the overwritten member and add the new member to the friend
		// list (ArrayList.remove(o) takes care of o == null automatically),
		// and prevent duplicating friend names.
		if (!(newFriendMember.isFriend(rowMember))) {
			rowMember.addFriend(newFriendMember);
			rowMember.removeFriend(replacedMember);
		}

		// Remove teh added Member with empty string (due to deletion or
		// accidental input) from the Club and the friend list.
		if (newFriendMember.getName().length() == 0) {
			club.removeMember(newFriendMember);
			rowMember.removeFriend(newFriendMember);
		}

		// force the table to redraw itself
		fireTableStructureChanged();
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

	// class method
	private static Member member(String name) {
		return Club.getInstance().memberForName(name);
	}

	// class method
	private static void createTwoWayFriendship(String n1, String n2) {
		member(n1).addFriend(member(n2));
		member(n2).addFriend(member(n1));
	}

	private int calculateLargestFriendCount() {
		Iterator i = club.getMemberList().iterator();
		int max = 0;
		int current;

		while (i.hasNext()) {
			current = ((Member) i.next()).getFriendList().size();
			if (current > max) {
				max = current;
			}
		}
		return max;
	}
}