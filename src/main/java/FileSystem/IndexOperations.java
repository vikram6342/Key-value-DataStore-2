package FileSystem;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class IndexOperations {

    protected static void updateIndex(ConcurrentHashMap<String, IndexPOJO> indexMap, String key, Long position)
    {

        IndexPOJO indexPOJO = new IndexPOJO(FileSystem.getActiveFileName(), position);
        indexMap.put(key, indexPOJO);
    }

    protected static Long getSeekIndex(ConcurrentHashMap<String, IndexPOJO> indexMap, String key)
    {
        IndexPOJO indexPOJO = indexMap.get(key);
        if(indexPOJO == null)
        {
            return -1L;
        }
        return indexPOJO.getOffset();
    }


}
