package net.sf.jabref.logic.groups;

import net.sf.jabref.model.groups.AllEntriesGroup;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AllEntriesGroupTest {

    @Test
    public void testToString() {
        assertEquals("AllEntriesGroup:", new AllEntriesGroup("").toString());
    }

}