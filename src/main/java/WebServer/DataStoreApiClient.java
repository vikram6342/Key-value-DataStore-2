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
        String fileName = req.getParameter("fileName");
        /*
        To do
        1. Need to move the file name logic it should be in header
        2. Need to check whether the user who made the req have access to that particular file
         */
        PrintWriter writer = null;
        try
        {
            FileSystem fileSystem = new FileSystem(fileName);
            writer = res.getWriter();
            String value = fileSystem.getData(key);
            writer.println(value);

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
        String fileName = req.getParameter("fileName");
        PrintWriter writer = null;
        try
        {
            writer = res.getWriter();
            FileSystem fileSystem = new FileSystem(fileName);
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
