package FileSystem;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class FileSystem {
    private static final String FILE_ROOT_DIR = System.getProperty("user.home") + "/home/dataStore/build/tomcat/apache-tomcat-9.0.107/temp/";
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> FILE_INDEX = new ConcurrentHashMap<>();
    private final String FILE_NAME;
    public FileSystem(String fileName) throws Exception
    {
        FileSystemValidationUtils.validateFileObjectCreation(fileName);
        File file = new File(FileSystem.FILE_ROOT_DIR + fileName);
        FILE_NAME = FileSystem.FILE_ROOT_DIR + fileName;

        if(file.createNewFile())
        {
            ConcurrentHashMap<String, Long> DATA_INDEX = new ConcurrentHashMap<>();
            FILE_INDEX.put(FILE_NAME, DATA_INDEX);
            System.out.println("File does not exist so new file created");
        }
        else
        {
            System.out.println("File exist hence no new file is created");
        }

    }

    public boolean writeData(String key, String value)
    {
        int keyLength, valueLength;
        boolean fileWritten = false;
        keyLength = key.length();
        valueLength = value.length();
        File file = new File(FILE_NAME);
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw"))
        {
            FileChannel fileChannel = randomAccessFile.getChannel();
            FileLock fileLock = fileChannel.tryLock();
            if(fileLock == null)
            {
                System.out.println("Another process has already acquired a lock so skipping the write operation");
                return fileWritten;
            }
            long position = fileChannel.size();
            fileChannel.position(position);
            IndexOperations.updateIndex(FILE_INDEX.get(FILE_NAME), key, position);
            ByteBuffer bufferData = ByteBuffer.wrap(String.format("%d %d %s %s\n", keyLength, valueLength, key, value).getBytes(StandardCharsets.UTF_8));
            int bytesWritten = fileChannel.write(bufferData);
            System.out.println("Written bytes of size" + bytesWritten);
            fileWritten = true;
        }
        catch(Exception e)
        {
            System.out.println("Exception occurred during writing in file" + e);
        }
        FILE_INDEX.get(FILE_NAME).forEach((MapKey, MapValue) -> System.out.println(MapKey + " = " + MapValue));

        return fileWritten;

    }

    public String getData(String key) throws Exception
    {
        if(key == null || key.isEmpty())
        {
            throw new Exception("The given key is not valid");
        }
        if(key.getBytes(StandardCharsets.UTF_8).length > 10240)
        {
            throw new Exception("Invalid key size");
        }
        File file = new File(FILE_NAME);
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"))
        {
            Long seekIndex = IndexOperations.getSeekIndex(FILE_INDEX.get(FILE_NAME), key);
            if(seekIndex == -1)
            {
                System.out.println("The given key does not exist please check the key");
                return "Invalid key";
            }
            randomAccessFile.seek(seekIndex);
            String data = randomAccessFile.readLine();
            int keyLenEndIndex = data.indexOf(" ");
            int valueLenEndIndex = data.indexOf(" ", keyLenEndIndex + 1);
            int valueLength = Integer.parseInt(data.substring(keyLenEndIndex + 1, valueLenEndIndex));
            int keyLen = Integer.parseInt(data.substring(0, keyLenEndIndex));
            String value = data.substring(valueLenEndIndex + keyLen + 2);

            if(value.isEmpty())
            {
                System.out.println("Value is empty not possible need to check this case");
            }
            return value;

        }
        catch(Exception e)
        {
            System.out.println("Exception occurred" + e);
            return "Internal Error occurred";
        }
    }


}
