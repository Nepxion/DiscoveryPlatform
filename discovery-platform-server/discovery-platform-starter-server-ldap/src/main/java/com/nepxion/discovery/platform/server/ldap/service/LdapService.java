package com.nepxion.discovery.platform.server.ldap.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.SearchScope;
import org.springframework.util.ObjectUtils;

import com.nepxion.discovery.platform.server.entity.vo.LdapUserVo;
import com.nepxion.discovery.platform.server.ldap.properties.PlatformLdapProperties;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class LdapService {
    private static final String MEMBER_OF_ATTR_NAME = "memberOf";

    @Autowired
    private LdapTemplate ldapTemplate;
    @Autowired
    private LdapProperties ldapProperties;
    @Autowired
    private PlatformLdapProperties platformLdapProperties;

    /**
     * authenticate with login account and password
     *
     * @param username the login account
     * @param password the login password
     * @return true: login success; false: login failed
     */
    public boolean authenticate(String username, String password) {
        EqualsFilter filter = new EqualsFilter(this.platformLdapProperties.getLoginIdAttrName(), username);
        this.ldapTemplate.setIgnorePartialResultException(true);
        return this.ldapTemplate.authenticate(this.ldapProperties.getBase(), filter.toString(), password);
    }

    /**
     * obtain user information according to login account
     *
     * @param username the login account
     * @return user information
     */
    public LdapUserVo getByUserName(String username) {
        try {
            return this.ldapTemplate.searchForObject(
                    this.ldapProperties.getBase(),
                    query().where(this.platformLdapProperties.getLoginIdAttrName()).is(username).filter().toString(),
                    ctx -> {
                        DirContextAdapter contextAdapter = (DirContextAdapter) ctx;
                        LdapUserVo ldapUserVo = new LdapUserVo();
                        ldapUserVo.setUsername(contextAdapter.getStringAttribute(this.platformLdapProperties.getLoginIdAttrName()));
                        ldapUserVo.setName(contextAdapter.getStringAttribute(this.platformLdapProperties.getNameAttrName()));
                        ldapUserVo.setPhoneNumber(contextAdapter.getStringAttribute(this.platformLdapProperties.getPhoneNumberAttrName()));
                        ldapUserVo.setEmail(contextAdapter.getStringAttribute(this.platformLdapProperties.getMailAttrName()));
                        ldapUserVo.setRemark(contextAdapter.getStringAttribute(this.platformLdapProperties.getTitleAttrName()));
                        return ldapUserVo;
                    });
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    /**
     * search user information according to keyword
     *
     * @param keyword keyword
     * @return return user information which match the keyword
     */
    public List<LdapUserVo> search(String keyword, Integer pageNum, Integer pageSize) {

        int offset = (Math.max(pageNum, 1) - 1) * pageSize;
        int limit = pageSize;

        ContainerCriteria criteria = ldapQueryCriteria().and
                (
                        query().where(this.platformLdapProperties.getLoginIdAttrName()).like("*" + keyword + "*")
                                .or(this.platformLdapProperties.getNameAttrName()).like("*" + keyword + "*")
                );
        List<LdapUserVo> result = ldapTemplate.search(this.ldapProperties.getBase(), criteria.filter().toString(), (AttributesMapper<LdapUserVo>) ctx -> {
            LdapUserVo ldapUserVo = new LdapUserVo();
            if (ctx.get(this.platformLdapProperties.getLoginIdAttrName()) != null) {
                ldapUserVo.setUsername(ctx.get(this.platformLdapProperties.getLoginIdAttrName()).get().toString());
            }
            if (ctx.get(this.platformLdapProperties.getNameAttrName()) != null) {
                ldapUserVo.setName(ctx.get(this.platformLdapProperties.getNameAttrName()).get().toString());
            }
            if (ctx.get(this.platformLdapProperties.getPhoneNumberAttrName()) != null) {
                ldapUserVo.setPhoneNumber(ctx.get(this.platformLdapProperties.getPhoneNumberAttrName()).get().toString());
            }
            if (ctx.get(this.platformLdapProperties.getMailAttrName()) != null) {
                ldapUserVo.setEmail(ctx.get(this.platformLdapProperties.getMailAttrName()).get().toString());
            }
            if (ctx.get(this.platformLdapProperties.getTitleAttrName()) != null) {
                ldapUserVo.setRemark(ctx.get(this.platformLdapProperties.getTitleAttrName()).get().toString());
            }
            return ldapUserVo;
        });

        return result.stream().sorted(Comparator.comparing(LdapUserVo::getUsername)).skip(offset).limit(limit).collect(Collectors.toList());
    }

    private ContainerCriteria ldapQueryCriteria() {
        ContainerCriteria criteria = query().searchScope(SearchScope.SUBTREE)
                .where("objectClass").is(this.platformLdapProperties.getObjectClassAttrName());
        if (this.platformLdapProperties.getMemberOf().length > 0 && !ObjectUtils.isEmpty(this.platformLdapProperties.getMemberOf()[0])) {
            ContainerCriteria memberOfFilters = query().where(MEMBER_OF_ATTR_NAME).is(this.platformLdapProperties.getMemberOf()[0]);
            Arrays.stream(this.platformLdapProperties.getMemberOf()).skip(1)
                    .forEach(filter -> memberOfFilters.or(MEMBER_OF_ATTR_NAME).is(filter));
            criteria.and(memberOfFilters);
        }
        return criteria;
    }
}