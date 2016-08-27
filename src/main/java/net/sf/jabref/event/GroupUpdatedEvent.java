package net.sf.jabref.event;

import net.sf.jabref.model.MetaData;

public class GroupUpdatedEvent {

    private final MetaData metaData;

    /**
     * @param metaData Affected instance
     */
    public GroupUpdatedEvent(MetaData metaData) {
        this.metaData = metaData;
    }

    public MetaData getMetaData() {
        return this.metaData;
    }
}
