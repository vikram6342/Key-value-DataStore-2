package FileSystem;

public class FileSystemUtils
{
    public static String getKey(String data)
    {
        int keyLenEndIndex = data.indexOf(" ");
        int valueLenEndIndex = data.indexOf(" ", keyLenEndIndex + 1);
        int keyLen = Integer.parseInt(data.substring(0, keyLenEndIndex));
        String key = data.substring(valueLenEndIndex + 1, valueLenEndIndex + keyLen + 1);
        return key;
    }

    public static String getValue(String data)
    {
        int keyLenEndIndex = data.indexOf(" ");
        int valueLenEndIndex = data.indexOf(" ", keyLenEndIndex + 1);
        int valueLength = Integer.parseInt(data.substring(keyLenEndIndex + 1, valueLenEndIndex));
        int keyLen = Integer.parseInt(data.substring(0, keyLenEndIndex));
        String value = data.substring(valueLenEndIndex + keyLen + 2, valueLenEndIndex + keyLen + valueLength + 2);
        return value;
    }
}
