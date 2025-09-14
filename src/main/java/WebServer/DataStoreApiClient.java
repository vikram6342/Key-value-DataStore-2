package WebServer;

import FileSystem.FileSystem;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class DataStoreApiClient extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
    {
        String key = req.getParameter("key");
        /*
        To do
            Support for multi user data base model // Hope i will do it
         */
        PrintWriter writer = null;
        try
        {
            FileSystem fileSystem = new FileSystem();
            writer = res.getWriter();
            String value = fileSystem.getData(key);
            writer.print(value);

        }
        catch (Exception e)
        {
            if(writer == null)
            {
                return;
            }
            writer.println("Exception occurred when getting data from file" + e);
        }
        finally
        {
            res.setContentType("text/plain");
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    {
        String key = req.getParameter("key");
        String value = req.getParameter("value");
        PrintWriter writer = null;
        try
        {
            writer = res.getWriter();
            FileSystem fileSystem = new FileSystem();
            fileSystem.writeData(key, value);
            writer.println("Successfully written the key value pairs!");
        }
        catch (Exception e)
        {
            if(writer == null)
            {
                return;
            }
            writer.println("Exception occurred during writing file" + e);
        }
        finally
        {
            res.setContentType("text/plain");
        }
    }

}
