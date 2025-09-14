package FileSystem;

public class IndexPOJO
{
    private String fileName;
    private Long offset;

    public IndexPOJO(String fileName, Long offset)
    {
        this.fileName = fileName;
        this.offset = offset;
    }

    public String getFileName()
    {
        return this.fileName;
    }

    public Long getOffset()
    {
        return this.offset;
    }

    public void setOffset(Long offset)
    {
        if(offset == null || offset < 0)
        {
            System.out.println("The offset value is invalid please enter the proper value");
            return;
        }
        this.offset = offset;
    }

    public void setFileName(String fileName)
    {
        if(fileName == null || fileName.isEmpty())
        {
            System.out.println("The file name is invalid please enter the proper value");
            return;
        }
        this.fileName = fileName;
    }
}
