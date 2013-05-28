package net.allwebdesign.common.lib.ui;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * An extention of Spring's UrlBasedViewResolver for ZK.
 * @author George Moraitakis
 *
 */
public class ZulViewResolver extends UrlBasedViewResolver implements Ordered {
    
	private String patternToMatch;
	
	
	
	
	/**
	 * Loads the view
	 * @param the name of the view to load
	 * @param the locale
	 */
	protected View loadView(String viewName, Locale locale) throws Exception {
        AbstractUrlBasedView view = buildView(viewName);
        View viewObj = (View)     getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
            if (viewObj instanceof JstlView) {
                JstlView jv = (JstlView) viewObj;
                if (jv.getBeanName().indexOf(this.patternToMatch) != -1) {
                    return viewObj;
                }
            }

            return null;
        }





	/**
	 * Set the pattern to match
	 * @param patternToMatch the patternToMatch to set
	 */
	public void setPatternToMatch(String patternToMatch) {
		this.patternToMatch = patternToMatch;
	}





	/**
	 * Get the pattern to match
	 * @return the patternToMatch
	 */
	public String getPatternToMatch() {
		return patternToMatch;
	}
}
