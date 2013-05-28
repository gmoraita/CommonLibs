package net.allwebdesign.common.lib.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Used when returning amounts from a function in particular in web services.<BR/>
 * Use the below annotations in those cases above the method:<BR/>
 * &#64;XmlElement(required = true)<BR/>
 * &#64;XmlJavaTypeAdapter(BigDecimalAdapter.class)<BR/>
 * &#64;XmlSchemaType(name = "decimal")
 * @author George Moraitakis
 *
 */
public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {
	
	private static final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
	private static final DecimalFormat df = new DecimalFormat("0.00", dfs);
	
	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public BigDecimal unmarshal(final String bigDecimal) throws Exception {
		return (BigDecimal) df.parse(bigDecimal);
	}
	
	/* (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final BigDecimal bigDecimal) throws Exception {
		return df.format(bigDecimal);
	}
}
