package FileSystem;

import java.nio.charset.StandardCharsets;

public class FileSystemValidationUtils {

    protected static void validateFileObjectCreation(String fileName) throws Exception
    {
        if(fileName == null || fileName.isEmpty())
        {
            throw new Exception("Please enter a valid file name");
        }
        if(fileName.contains("/"))
        {
            throw new Exception("Path traversal is not allowed please remove path regex");
        }
    }

    protected static void validateWrite(String key, String value) throws Exception
    {
        if(key == null || key.isEmpty())
        {
            throw new Exception("Invalid key passed");
        }
        if(value == null || value.isEmpty())
        {
            throw new Exception("Invalid value passed");
        }
        if(key.getBytes(StandardCharsets.UTF_8).length > 10240)
        {
            throw new Exception("Key size is greater than 10KB please make sure the key size is less than that");
        }
        if(value.getBytes(StandardCharsets.UTF_8).length > 2097152)
        {
            throw new Exception("Value size is greater than 2MB please make sure the value size is less than that");
        }
    }

}
