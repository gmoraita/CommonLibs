package net.allwebdesign.common.lib.ui.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.Tag;

public abstract class GenericTag implements Tag
{
    private String classTag = null;
    private String name = null;
    private String id = null;
    private PageContext pageContext;
    private Tag parent;
	

    public GenericTag(){}

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

    public int doStartTag()
        throws JspException
    {
        return 0;
    }

    public int doEndTag()
        throws JspException
    {
        
        return 0;
    }

    public void release()
    {
        dropData();
    }

    private void dropData()
    {
        name = null;
        id = null;
    }
    
    @Override
    public void setPageContext(PageContext pagecontext)
    {
        this.pageContext = pagecontext;
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

	/**
	 * @return the pageContext
	 */
	public PageContext getPageContext() {
		return pageContext;
	}
    

	
}

