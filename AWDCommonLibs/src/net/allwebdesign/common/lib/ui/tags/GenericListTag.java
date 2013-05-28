package net.allwebdesign.common.lib.ui.tags;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.jsp.*;


public class GenericListTag extends GenericTag
{

    private Locale locale = null;
    private boolean fullName = false;
    private boolean autoSubmit = false;
    private Integer selected = null;

	

    public GenericListTag()
    {
    	super();
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

    @Override
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
        stringbuffer.append((new StringBuilder()).append("<select name=\"").append(this.getName()).append("\"").toString());
        if(this.getId() != null)
            stringbuffer.append((new StringBuilder()).append(" id=\"").append(this.getId()).append("\"").toString());
        if(this.getClassTag() != null)
            stringbuffer.append((new StringBuilder()).append(" class=\"").append(this.getClassTag()).append("\"").toString());
        
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
            this.getPageContext().getOut().write(stringbuffer.toString());
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
        this.setName(null);
        locale = null;
        fullName = false;
        autoSubmit = false;
        selected = null;
        this.setId(null);
    }
    


	
}

