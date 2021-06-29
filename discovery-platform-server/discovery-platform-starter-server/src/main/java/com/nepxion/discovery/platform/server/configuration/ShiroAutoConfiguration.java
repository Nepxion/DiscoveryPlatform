package com.nepxion.discovery.platform.server.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nepxion.discovery.platform.server.filter.ShiroJwtFilter;
import com.nepxion.discovery.platform.server.shiro.JwtCredentialsMatcher;
import com.nepxion.discovery.platform.server.shiro.JwtRealm;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.shiro.AuthRealm;
import com.nepxion.discovery.platform.server.shiro.CredentialsMatcher;

import javax.servlet.Filter;

@Configuration
public class ShiroAutoConfiguration {
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);

        bean.setLoginUrl("/".concat(PlatformConstant.PLATFORM)); // 配置登录的url
        bean.setSuccessUrl("/index"); // 登录成功后要跳转的链接

        Map<String, Filter> filterMap = new HashMap<>(4);
        filterMap.put("jwt", new ShiroJwtFilter());
        bean.setFilters(filterMap);

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/authentication/authenticate", "anon"); // 提供给Desktop的登录接口
        filterChainDefinitionMap.put("/swagger-ui.html", "anon"); // Swagger
        filterChainDefinitionMap.put("/swagger-resources/**", "anon"); // Swagger
        filterChainDefinitionMap.put("/webjars/**", "anon"); // Swagger
        filterChainDefinitionMap.put("/v2/**", "anon"); // Swagger
        filterChainDefinitionMap.put("/css/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/js/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/assets/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/images/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/fonts/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/layuiadmin/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/terminal/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/favicon.ico", "anon"); // 静态资源
        filterChainDefinitionMap.put("/actuator/**", "anon"); // spring-boot-admin
        filterChainDefinitionMap.put(bean.getLoginUrl(), "anon"); // 登录页面
        filterChainDefinitionMap.put("/instances", "anon"); //
        filterChainDefinitionMap.put("/login", "anon"); // 登录页面
        filterChainDefinitionMap.put("/error", "anon"); // 错误逻辑
        filterChainDefinitionMap.put("/logout", "anon"); // 登录逻辑
        filterChainDefinitionMap.put("/do-login", "anon"); // 登录逻辑
        filterChainDefinitionMap.put("/do-quit", "anon"); // 登出逻辑
        filterChainDefinitionMap.put("/**", "jwt,authc");// 需要认证才可以访问
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    // 配置核心安全事务管理器
    @Bean(name = "securityManager")
    @Primary
    @ConditionalOnMissingBean
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm,
                                           @Qualifier("jwtRealm") JwtRealm jwtRealm,
                                           @Qualifier("sessionManager") SessionManager sessionManager,
                                           @Qualifier("authenticator") ModularRealmAuthenticator authenticator) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setAuthenticator(authenticator);
        manager.setRealms(Arrays.asList(authRealm, jwtRealm)); // 设置自定义realm
        manager.setSessionManager(sessionManager);
        return manager;
    }

    @Bean(name = "sessionManager")
    @Primary
    @ConditionalOnMissingBean
    public DefaultWebSessionManager sessionManager(SimpleCookie sessionIdCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie);

        sessionManager.setGlobalSessionTimeout(1000 * 60 * 60 * 6L); // 全局会话超时时间 单位毫秒
        sessionManager.setDeleteInvalidSessions(true);// 是否开启删除无效的session对象 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true); // 是否开启定时调度器进行检测过期session 默认为true

        sessionManager.setSessionValidationInterval(3600000);
        sessionManager.setSessionIdUrlRewritingEnabled(false); // 取消url 后面的 JSESSIONID
        return sessionManager;
    }

    // 配置保存sessionId的cookie 注意：这里的cookie 不是[记住我]的cookie, 记住我需要一个cookie session管理
    // 也需要自己的cookie 默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public SimpleCookie sessionIdCookie() {
        // 这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sidDb2EsAdmin");
        // setCookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        // 只能通过http访问，javascript无法访问
        // 防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        // maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    // 配置自定义的权限登录器
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public AuthRealm authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    // 配置自定义的密码比较器
    @Bean(name = "credentialsMatcher")
    @Primary
    @ConditionalOnMissingBean
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

    @Bean("authenticator")
    @Primary
    @ConditionalOnMissingBean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // 设置多 Realm的认证策略，默认 AtLeastOneSuccessfulStrategy
        AuthenticationStrategy strategy = new FirstSuccessfulStrategy();
        authenticator.setAuthenticationStrategy(strategy);
        return authenticator;
    }

    @Bean("jwtRealm")
    @Primary
    @ConditionalOnMissingBean
    public JwtRealm jwtRealm(@Qualifier("jwtCredentialsMatcher") JwtCredentialsMatcher jwtCredentialsMatcher) {
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(jwtCredentialsMatcher);
        return jwtRealm;
    }

    @Bean("jwtCredentialsMatcher")
    @Primary
    @ConditionalOnMissingBean
    public JwtCredentialsMatcher jwtCredentialsMatcher(){
        return new JwtCredentialsMatcher();
    }

}