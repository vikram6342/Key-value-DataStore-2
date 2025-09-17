package ContextUtil;

import javax.servlet.ServletContext;

public class ContextUtil
{
    private static ServletContext servletContext;

    public static void setServletContext(ServletContext servletContext)
    {
        ContextUtil.servletContext = servletContext;
    }

    public static ServletContext getServletContext()
    {
        return ContextUtil.servletContext;
    }

    public static void setContextAttribute(String key, Object value)
    {
        ContextUtil.servletContext.setAttribute(key, value);
    }

    public static Object getContextAttribute(String key)
    {
        return ContextUtil.servletContext.getAttribute(key);
    }

}
