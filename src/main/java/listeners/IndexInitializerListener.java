package listeners;

import Constants.FileConstants;
import ContextUtil.ContextUtil;
import FileSystem.FileSystem;
import FileSystem.IndexPOJO;
import FileSystem.FileSystemUtils;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class IndexInitializerListener implements ServletContextListener
{
    private final String FILE_ROOT_DIR = System.getProperty("user.home") + "/home/dataStore/build/tomcat/apache-tomcat-9.0.107/temp/";
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        /*
        To do Get the active file name and file size from the file for now using a default file name
         */
        ServletContext servletContext = servletContextEvent.getServletContext();
        ContextUtil.setServletContext(servletContext);
        this.setActiveFileName();
        this.readFileAndUpdateIndex();
    }

    // Need to change the acitve File data to be request specific

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        /*
        To do
        Implement logic to write the last active file id and file size in a file during server shutdown
        Active file name will be used to decide on which file to write
        File size will help to keep track of the file size -> need to maintain threshold for file size
         */
    }

    private void setActiveFileName()
    {
        String activeFileName = FILE_ROOT_DIR + FileConstants.ACTIVE_FILE_PREFIX + "1.txt";
        ContextUtil.setContextAttribute(FileConstants.ACTIVE_FILE_KEY, activeFileName);
    }

    private void readFileAndUpdateIndex() {
        File file = new File(FileSystem.getActiveFileName());
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {

            Class<?> classFile = FileSystem.class;
            Field indexField = classFile.getDeclaredField("FILE_INDEX");
            indexField.setAccessible(true);
            ConcurrentHashMap<String, IndexPOJO> indexMap = (ConcurrentHashMap<String, IndexPOJO>) indexField.get(null);
            String data = null;
            while ((data = randomAccessFile.readLine()) != null) {
                String key = FileSystemUtils.getKey(data);
                Long position = randomAccessFile.getFilePointer();
                IndexPOJO indexPOJO = new IndexPOJO(FILE_ROOT_DIR + FileConstants.ACTIVE_FILE_PREFIX + "1.txt", position - data.length() - 1);
                indexMap.put(key, indexPOJO);
            }

        } catch (Exception e) {
            System.out.println("Exception occurred");
        }
    }
}
