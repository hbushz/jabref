package net.sf.jabref.logic.layout.format;

import net.sf.jabref.logic.layout.LayoutFormatter;
import net.sf.jabref.model.search.RemoveLatexCommands;


public class RemoveLatexCommandsFormatter implements LayoutFormatter {

    private final RemoveLatexCommands removeFormatter = new RemoveLatexCommands();

    @Override
    public String format(String fieldText) {
        return removeFormatter.format(fieldText);
    }

}
