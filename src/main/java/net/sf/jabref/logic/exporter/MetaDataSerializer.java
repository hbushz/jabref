package net.sf.jabref.logic.exporter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jabref.logic.util.OS;
import net.sf.jabref.logic.util.strings.StringUtil;
import net.sf.jabref.model.MetaData;

public class MetaDataSerializer {

    /**
     * Writes all data in the format <key, serialized data>.
     */
    public static Map<String, String> getSerializedStringMap(MetaData metaData) {
    
        Map<String, String> serializedMetaData = new TreeMap<>();
    
        // first write all meta data except groups
        for (Map.Entry<String, List<String>> metaItem : metaData.getMetaData().entrySet()) {
    
            StringBuilder stringBuilder = new StringBuilder();
            for (String dataItem : metaItem.getValue()) {
                stringBuilder.append(StringUtil.quote(dataItem, ";", '\\')).append(";");
    
                //in case of save actions, add an additional newline after the enabled flag
                if (metaItem.getKey().equals(MetaData.SAVE_ACTIONS)
                        && ("enabled".equals(dataItem) || "disabled".equals(dataItem))) {
                    stringBuilder.append(OS.NEWLINE);
                }
            }
    
            String serializedItem = stringBuilder.toString();
            // Only add non-empty values
            if (!serializedItem.isEmpty() && !";".equals(serializedItem)) {
                serializedMetaData.put(metaItem.getKey(), serializedItem);
            }
        }
    
        // write groups if present. skip this if only the root node exists
        // (which is always the AllEntriesGroup).
        metaData.getGroups().filter(groups -> groups.getNumberOfChildren() > 0).ifPresent(groups -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(OS.NEWLINE);
    
            for (String groupNode : groups.getTreeAsString()) {
                stringBuilder.append(StringUtil.quote(groupNode, ";", '\\'));
                stringBuilder.append(";");
                stringBuilder.append(OS.NEWLINE);
            }
            serializedMetaData.put(MetaData.GROUPSTREE, stringBuilder.toString());
        });
        return serializedMetaData;
    }

}
