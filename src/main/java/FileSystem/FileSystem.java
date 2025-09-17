package FileSystem;

import Constants.FileConstants;
import ContextUtil.ContextUtil;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class FileSystem
{
    private static final ConcurrentHashMap<String, IndexPOJO> FILE_INDEX = new ConcurrentHashMap<>();

    public FileSystem() throws Exception
    {
        File file = new File( FileSystem.getActiveFileName());

        if(file.createNewFile())
        {
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
        File file = new File(FileSystem.getActiveFileName());
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
            IndexOperations.updateIndex(FILE_INDEX, key, position);
            ByteBuffer bufferData = ByteBuffer.wrap(String.format("%d %d %s %s %d\n", keyLength, valueLength, key, value, 0).getBytes(StandardCharsets.UTF_8));
            int bytesWritten = fileChannel.write(bufferData);
            System.out.println("Written bytes of size" + bytesWritten);
            fileWritten = true;
        }
        catch(Exception e)
        {
            System.out.println("Exception occurred during writing in file" + e);
        }

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
        File file = new File(FileSystem.getActiveFileName());
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"))
        {
            Long seekIndex = IndexOperations.getSeekIndex(FILE_INDEX, key);
            if(seekIndex == -1)
            {
                System.out.println("The given key does not exist please check the key");
                return "Invalid key";
            }
            randomAccessFile.seek(seekIndex);
            String data = randomAccessFile.readLine();
            String value = FileSystemUtils.getValue(data);

            if(value.isEmpty())
            {
                System.out.println("Value is empty not possible need to check this case");
            }
            return value;

        }
        catch(Exception e)
        {
            System.out.println("Exception occurred" + e);
            throw e;
        }
    }

    public static String getActiveFileName()
    {
        return String.valueOf(ContextUtil.getContextAttribute(FileConstants.ACTIVE_FILE_KEY));
    }

}
