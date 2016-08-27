package net.sf.jabref.model.groups;

import java.util.List;

import net.sf.jabref.model.ParseException;

/**
 * Converts string representation of groups to a parsed {@link GroupTreeNode}.
 */
public class GroupsParser {

    public static GroupTreeNode importGroups(List<String> orderedData, String keywordSeparator, String allEntriesName)
            throws ParseException {
        GroupTreeNode cursor = null;
        GroupTreeNode root = null;
        for (String string : orderedData) {
            // This allows to read databases that have been modified by, e.g., BibDesk
            string = string.trim();
            if (string.isEmpty()) {
                continue;
            }

            int spaceIndex = string.indexOf(' ');
            if (spaceIndex <= 0) {
                throw new ParseException("Expected \"" + string + "\" to contain whitespace");
            }
            int level = Integer.parseInt(string.substring(0, spaceIndex));
            AbstractGroup group = AbstractGroup.fromString(string.substring(spaceIndex + 1), keywordSeparator,
                    allEntriesName);
            GroupTreeNode newNode = GroupTreeNode.fromGroup(group);
            if (cursor == null) {
                // create new root
                cursor = newNode;
                root = cursor;
            } else {
                // insert at desired location
                while (level <= cursor.getLevel()) {
                    cursor = cursor.getParent().get();
                }
                cursor.addChild(newNode);
                cursor = newNode;
            }
        }
        return root;
    }
}
