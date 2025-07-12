package FileSystem;

import java.util.concurrent.ConcurrentHashMap;

public class IndexOperations {

    protected static void updateIndex(ConcurrentHashMap<String, Long> index, String key, Long position)
    {
        index.put(key, position);
    }

    protected static Long getSeekIndex(ConcurrentHashMap<String, Long> index, String key)
    {
        return index.getOrDefault(key, -1L);
    }


}
