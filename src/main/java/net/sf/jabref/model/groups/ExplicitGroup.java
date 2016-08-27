package net.sf.jabref.model.groups;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import net.sf.jabref.logic.groups.GroupDescriptions;
import net.sf.jabref.model.ParseException;
import net.sf.jabref.model.entry.FieldName;
import net.sf.jabref.model.util.ModelStringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Select explicit bibtex entries. It is also known as static group.
 *
 * @author jzieren
 */
public class ExplicitGroup extends KeywordGroup {

    public static final String ID = "ExplicitGroup:";

    private final List<String> legacyEntryKeys = new ArrayList<>();

    private static final Log LOGGER = LogFactory.getLog(ExplicitGroup.class);


    public ExplicitGroup(String name, GroupHierarchyType context, String keywordSeparator)
            throws ParseException {
        super(name, FieldName.GROUPS, name, true, false, context, keywordSeparator);
    }

    public static ExplicitGroup fromString(String s, String keywordSeparator) throws ParseException {
        if (!s.startsWith(ExplicitGroup.ID)) {
            throw new IllegalArgumentException("ExplicitGroup cannot be created from \"" + s + "\".");
        }
        QuotedStringTokenizer tok = new QuotedStringTokenizer(s.substring(ExplicitGroup.ID.length()),
                AbstractGroup.SEPARATOR, AbstractGroup.QUOTE_CHAR);

        String name = tok.nextToken();
        int context = Integer.parseInt(tok.nextToken());
        ExplicitGroup newGroup = new ExplicitGroup(name, GroupHierarchyType.getByNumber(context), keywordSeparator);
        newGroup.addLegacyEntryKeys(tok);
        return newGroup;
    }

    /**
     * Called only when created fromString.
     * JabRef used to store the entries of an explicit group in the serialization, e.g.
     *  ExplicitGroup:GroupName\;0\;Key1\;Key2\;;
     * This method exists for backwards compatibility.
     */
    private void addLegacyEntryKeys(QuotedStringTokenizer tok) {
        while (tok.hasMoreTokens()) {
            String key = ModelStringUtil.unquote(tok.nextToken(), AbstractGroup.QUOTE_CHAR);
            addLegacyEntryKey(key);
        }
    }

    public void addLegacyEntryKey(String key) {
        this.legacyEntryKeys.add(key);
    }

    @Override
    public AbstractGroup deepCopy() {
        try {
            ExplicitGroup copy = new ExplicitGroup(getName(), getContext(), keywordSeparator);
            copy.legacyEntryKeys.addAll(legacyEntryKeys);
            return copy;
        } catch (ParseException exception) {
            // this should never happen, because the constructor obviously succeeded in creating _this_ instance!
            LOGGER.error("Internal error in ExplicitGroup.deepCopy(). "
                    + "Please report this on https://github.com/JabRef/jabref/issues", exception);
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExplicitGroup)) {
            return false;
        }
        ExplicitGroup other = (ExplicitGroup) o;
        return Objects.equals(getName(), other.getName()) && Objects.equals(getHierarchicalContext(),
                other.getHierarchicalContext()) && Objects.equals(getLegacyEntryKeys(), other.getLegacyEntryKeys());
    }

    /**
     * Returns a String representation of this group and its entries.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ExplicitGroup.ID).append(
                ModelStringUtil.quote(getName(), AbstractGroup.SEPARATOR, AbstractGroup.QUOTE_CHAR)).
                append(AbstractGroup.SEPARATOR).append(getContext().ordinal()).append(AbstractGroup.SEPARATOR);

        // write legacy entry keys in well-defined order for CVS compatibility
        Set<String> sortedKeys = new TreeSet<>();
        sortedKeys.addAll(legacyEntryKeys);

        for (String sortedKey : sortedKeys) {
            sb.append(ModelStringUtil.quote(sortedKey, AbstractGroup.SEPARATOR, AbstractGroup.QUOTE_CHAR)).append(
                    AbstractGroup.SEPARATOR);
        }
        return sb.toString();
    }

    /**
     * Remove all stored cite keys, resulting in an empty group.
     */
    public void clearLegacyEntryKeys() {
        legacyEntryKeys.clear();
    }

    @Override
    public String getDescription() {
        return GroupDescriptions.getDescriptionForPreview();
    }

    @Override
    public String getShortDescription(boolean showDynamic) {
        return GroupDescriptions.getShortDescription(this, showDynamic);
    }

    public List<String> getLegacyEntryKeys() {
        return legacyEntryKeys;
    }

    @Override
    public String getTypeId() {
        return ExplicitGroup.ID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean isDynamic() {
        return false;
    }
}
