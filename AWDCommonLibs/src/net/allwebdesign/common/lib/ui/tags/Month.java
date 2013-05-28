package net.allwebdesign.common.lib.ui.tags;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.Tag;

public class Month extends GenericTag
{
    private String classTag;
    private String name;
    private Locale locale;
    private boolean fullName;
    private boolean autoSubmit;
    private Integer selected;
    private String id;
    private PageContext pageContext;
    private Tag parent;
	

    public Month()
    {
        name = null;
        locale = null;
        fullName = false;
        autoSubmit = false;
        selected = null;
        id = null;
        classTag = null;
    }

    public void setId(String s)
    {
        id = s;
    }

    public String getId()
    {
        return id;
    }

    public void setName(String s)
    {
        name = s;
    }

    public String getName()
    {
        return name;
    }

    public void setLocale(Locale locale1)
    {
        locale = locale1;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setFullName(boolean flag)
    {
        fullName = flag;
    }

    public boolean getFullName()
    {
        return fullName;
    }

    public void setAutoSubmit(boolean flag)
    {
        autoSubmit = flag;
    }

    public boolean getAutoSubmit()
    {
        return autoSubmit;
    }

    public void setSelected(int i)
    {
        selected = new Integer(i);
    }

    public int getSeleted()
    {
        return selected.intValue();
    }

    public int doStartTag()
        throws JspException
    {
        return 0;
    }

    public int doEndTag()
        throws JspException
    {
        StringBuffer stringbuffer = new StringBuffer("");
        GregorianCalendar gregoriancalendar = new GregorianCalendar(2001, 0, 1);
        int j;
        if(selected == null)
            j = 1;
        else
            j = selected.intValue();
        SimpleDateFormat simpledateformat;
        if(locale == null)
        {
            if(fullName)
                simpledateformat = new SimpleDateFormat("MMMM");
            else
                simpledateformat = new SimpleDateFormat("MMM");
        } else
        if(fullName)
            simpledateformat = new SimpleDateFormat("MMMM", locale);
        else
            simpledateformat = new SimpleDateFormat("MMM", locale);
        stringbuffer.append((new StringBuilder()).append("<select name=\"").append(name).append("\"").toString());
        if(id != null)
            stringbuffer.append((new StringBuilder()).append(" id=\"").append(id).append("\"").toString());
        if(classTag != null)
            stringbuffer.append((new StringBuilder()).append(" class=\"").append(classTag).append("\"").toString());
        
        if(autoSubmit)
            stringbuffer.append(" onChange=\"this.submit();\"");
        stringbuffer.append(">\n");
        for(int i = 1; i <= 12; i++)
        {
            stringbuffer.append("<option value=\"");
            stringbuffer.append((new StringBuilder()).append("").append(i).toString());
            stringbuffer.append("\"");
            if(i == j)
                stringbuffer.append(" selected=\"selected\"");
            stringbuffer.append(">");
            stringbuffer.append(simpledateformat.format(gregoriancalendar.getTime()));
            stringbuffer.append("</option>\n");
            gregoriancalendar.add(2, 1);
        }

        stringbuffer.append("</select>\n");
        try
        {
            pageContext.getOut().write(stringbuffer.toString());
        }
        catch(IOException ioexception)
        {
            throw new JspException((new StringBuilder()).append("IO Error: ").append(ioexception.getMessage()).toString());
        }
        dropData();
        return 6;
    }

    public void release()
    {
        dropData();
    }

    private void dropData()
    {
        name = null;
        locale = null;
        fullName = false;
        autoSubmit = false;
        selected = null;
        id = null;
    }
    
    @Override
    public void setPageContext(PageContext pagecontext)
    {
        pageContext = pagecontext;
    }

    public void setParent(Tag tag)
    {
        parent = tag;
    }

    public Tag getParent()
    {
        return parent;
    }

	/**
	 * @return the classTag
	 */
	public String getClassTag() {
		return classTag;
	}

	/**
	 * @param classTag the classTag to set
	 */
	public void setClassTag(String classTag) {
		this.classTag = classTag;
	}
    

	
}

