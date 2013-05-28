package net.allwebdesign.common.lib.cache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.util.Assert;


/**
 * Factory class to cache objects
 * @author George Moraitakis
 *
 */
public abstract class Cache {

	private static final Log logger = LogFactory.getLog(Cache.class);

	//~ Instance fields ================================================================================================

	private Ehcache cache;

	//~ Methods ========================================================================================================

	
	public Cache(String cacheName){
		
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		EhCacheFactoryBean b = new EhCacheFactoryBean();
		
		b.setCacheManager(cmfb.getObject());
		b.setCacheName(cacheName);
		b.setEternal(true);
		try {
			b.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		Ehcache cache = b.getObject();
		this.setCache(cache );
	}
	
	
	public void afterPropertiesSet() throws Exception {
       Assert.notNull(cache, "cache mandatory");
	}

	public Ehcache getCache() {
		return cache;
	}

	public Cacheable getFromCache(String key) {
       Element element = cache.get(key);

       if (logger.isDebugEnabled()) {
           logger.debug("Cache hit: " + (element != null) + "; object: " + key);
       }

       if (element == null) {
           return null;
       } else {
           return (Cacheable) element.getObjectValue();
       }
	}

	public void putInCache(Cacheable objectToCache) throws IllegalArgumentException {
       Element element = new Element(objectToCache.getKey(), objectToCache);

       if (logger.isDebugEnabled()) {
           logger.debug("Cache put: " + element.getObjectKey());
       }

       cache.put(element);
	}

	public void removeFromCache(Cacheable cachedObject) {
       if (logger.isDebugEnabled()) {
           logger.debug("Cache remove: " + cachedObject.getKey());
       }

       this.removeFromCache(cachedObject.getKey());
	}

	public void removeFromCache(String key) {
       cache.remove(key);
	}

	
	
	public void resetCache(){
		try{
			this.getCache().removeAll();
		} catch (Exception e){
			Cache.logger.error("CACHE ERROR: There was a problem removing all from the cache");
		}
		
	}
   
	public void setCache(Ehcache cache) {
       this.cache = cache;
	}
	
	
}


