package com.nepxion.discovery.platform.server.configuration;

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

import java.util.LinkedHashMap;

@Configuration
public class ShiroAutoConfiguration {
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilter(final SecurityManager manager) {
        final ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);

        bean.setLoginUrl("/".concat(PlatformConstant.PLATFORM)); // 配置登录的url
        bean.setSuccessUrl("/index"); // 登录成功后要跳转的链接

        final LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/css/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/js/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/images/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/fonts/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/layuiadmin/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/terminal/**", "anon"); // 静态资源
        filterChainDefinitionMap.put("/favicon.ico", "anon"); // 静态资源
        filterChainDefinitionMap.put("/actuator/**", "anon"); // spring-boot-admin
        filterChainDefinitionMap.put(bean.getLoginUrl(), "anon"); // 登录页面
        filterChainDefinitionMap.put("/login", "anon"); // 登录逻辑
        filterChainDefinitionMap.put("/quit", "anon"); // 登出逻辑
        filterChainDefinitionMap.put("/**", "authc");// 需要认证才可以访问
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    // 配置核心安全事务管理器
    @Bean(name = "securityManager")
    @Primary
    @ConditionalOnMissingBean
    public SecurityManager securityManager(@Qualifier("authRealm") final AuthRealm authRealm,
                                           @Qualifier("sessionManager") final SessionManager sessionManager) {
        final DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm); // 设置自定义realm
        manager.setSessionManager(sessionManager);
        return manager;
    }

    @Bean(name = "sessionManager")
    @Primary
    @ConditionalOnMissingBean
    public DefaultWebSessionManager sessionManager() {
        final DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie());

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
        final SimpleCookie simpleCookie = new SimpleCookie("sidDb2EsAdmin");
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
    public AuthRealm authRealm(@Qualifier("credentialsMatcher") final CredentialsMatcher matcher) {
        final AuthRealm authRealm = new AuthRealm();
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
        final DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(final SecurityManager manager) {
        final AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
}