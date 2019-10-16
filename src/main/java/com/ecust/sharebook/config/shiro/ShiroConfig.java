package com.ecust.sharebook.config.shiro;


import com.ecust.sharebook.utils.shiro.UserRealm;
import com.ecust.sharebook.utils.Jwt.JwtFilter;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {

	/**
	 * shiro缓存管理器; 需要注入对应的其它的实体类中
	 * ： 1、安全管理器：securityManager
	 * 可见securityManager是整个shiro的核心
	 * @return
	 */

	@Bean
	public EhCacheManager getEhCacheManager() {
		EhCacheManager em = new EhCacheManager();
		em.setCacheManagerConfigFile("classpath:config/ehcache2.xml");
		return em;
	}

	/**
	 * Realm实现
	 *
	 * @return
	 */

	@Bean
	UserRealm userRealm(EhCacheManager cacheManager) {
		UserRealm userRealm = new UserRealm();
		userRealm.setCacheManager(cacheManager);
		return userRealm;
	}

	/**
	 * 注入 securityManager
	 */
	@Bean("securityManager")
	public DefaultWebSecurityManager securityManager(UserRealm userRealm) {

			DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(userRealm);

		// 关闭自带session
		DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
		evaluator.setSessionStorageEnabled(false);

		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		subjectDAO.setSessionStorageEvaluator(evaluator);

		securityManager.setSubjectDAO(subjectDAO);
		return manager;
	}

	@Bean("shiroFilter")
	ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 如果不设置默认会自动寻找Web工程根目录下的"/login.HTML"页面
	    //shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		//shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权界面;
	   //shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		//拦截器
		// 配置不会被拦截的链接 顺序判断
		//filterChainDefinitionMap.put("/wap/**", "anon");
		//filterChainDefinitionMap.put("/h5/**", "anon");
		//filterChainDefinitionMap.put("/h5/**", "anon");
		//filterChainDefinitionMap.put("/taobao/**", "anon");
		//filterChainDefinitionMap.put("/app/**", "anon");
		//filterChainDefinitionMap.put("/css/**", "anon");
		//filterChainDefinitionMap.put("/js/**", "anon");
	//filterChainDefinitionMap.put("/js/plugins/**", "anon");
		//filterChainDefinitionMap.put("/fonts/**", "anon");
		//filterChainDefinitionMap.put("/img/**", "anon");
		//filterChainDefinitionMap.put("/docs/**", "anon");
		//filterChainDefinitionMap.put("/druid/**", "anon");
		//filterChainDefinitionMap.put("/upload/**", "anon");
	//	filterChainDefinitionMap.put("/files/**", "anon");
		//filterChainDefinitionMap.put("/", "anon");
		//filterChainDefinitionMap.put("/blog", "anon");
		//filterChainDefinitionMap.put("/blog/open/**", "anon");
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//		filterChainDefinitionMap.put("/small/tlogin", "anon");
	//	filterChainDefinitionMap.put("/small/login", "anon");
		filterChainDefinitionMap.put("/small/**", "anon");
		LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
		filterMap.put("jwt", new JwtFilter());
		shiroFilterFactoryBean.setFilters(filterMap);
		//<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
		filterChainDefinitionMap.put("/**", "jwt");
		//未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		// 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}


	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor( DefaultWebSecurityManager securityManager ) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

}
