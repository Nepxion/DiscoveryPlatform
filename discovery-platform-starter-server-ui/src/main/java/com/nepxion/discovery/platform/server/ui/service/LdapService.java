package com.nepxion.discovery.platform.server.ui.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.ui.configuration.properties.PlatformLdapProperties;
import com.nepxion.discovery.platform.server.ui.entity.vo.LdapUser;
import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.SearchScope;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class LdapService {
    private static final String MEMBER_OF_ATTR_NAME = "memberOf";
    private static final String MEMBER_UID_ATTR_NAME = "memberUid";
    private final LdapTemplate ldapTemplate;
    private final LdapProperties ldapProperties;
    private final PlatformLdapProperties platformLdapProperties;

    public LdapService(final LdapTemplate ldapTemplate,
                       final LdapProperties ldapProperties,
                       final PlatformLdapProperties platformLdapProperties) {
        this.ldapTemplate = ldapTemplate;
        this.ldapProperties = ldapProperties;
        this.platformLdapProperties = platformLdapProperties;
    }

    /**
     * authenticate with login account and password
     *
     * @param username the login account
     * @param password the login password
     * @return true: login success; false: login failed
     */
    public boolean authenticate(final String username,
                                final String password) {
        final EqualsFilter filter = new EqualsFilter(this.platformLdapProperties.getLoginIdAttrName(), username);
        this.ldapTemplate.setIgnorePartialResultException(true);
        return this.ldapTemplate.authenticate(this.ldapProperties.getBase(), filter.toString(), password);
    }

    /**
     * obtain user information according to login account
     *
     * @param username the login account
     * @return user information
     */
    public LdapUser getByUserName(final String username) {
        try {
            return this.ldapTemplate.searchForObject(
                    this.ldapProperties.getBase(),
                    query().where(this.platformLdapProperties.getLoginIdAttrName()).is(username).filter().toString(),
                    ctx -> {
                        final DirContextAdapter contextAdapter = (DirContextAdapter) ctx;
                        final LdapUser ldapUser = new LdapUser();
                        ldapUser.setUsername(contextAdapter.getStringAttribute(this.platformLdapProperties.getLoginIdAttrName()));
                        ldapUser.setName(contextAdapter.getStringAttribute(this.platformLdapProperties.getNameAttrName()));
                        ldapUser.setPhoneNumber(contextAdapter.getStringAttribute(this.platformLdapProperties.getPhoneNumberAttrName()));
                        ldapUser.setEmail(contextAdapter.getStringAttribute(this.platformLdapProperties.getMailAttrName()));
                        ldapUser.setRemark(contextAdapter.getStringAttribute(this.platformLdapProperties.getTitleAttrName()));
                        return ldapUser;
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
    public List<LdapUser> search(final String keyword,
                                 final Integer pageNum,
                                 final Integer pageSize) {

        final int offset = (Math.max(pageNum, 1) - 1) * pageSize;
        final int limit = pageSize;

        final ContainerCriteria criteria = ldapQueryCriteria().and
                (
                        query().where(this.platformLdapProperties.getLoginIdAttrName()).like("*" + keyword + "*")
                                .or(this.platformLdapProperties.getNameAttrName()).like("*" + keyword + "*")
                );
        final List<LdapUser> result = ldapTemplate.search(this.ldapProperties.getBase(), criteria.filter().toString(), (AttributesMapper<LdapUser>) ctx -> {
            final LdapUser ldapUser = new LdapUser();
            if (null != ctx.get(this.platformLdapProperties.getLoginIdAttrName())) {
                ldapUser.setUsername(ctx.get(this.platformLdapProperties.getLoginIdAttrName()).get().toString());
            }
            if (null != ctx.get(this.platformLdapProperties.getNameAttrName())) {
                ldapUser.setName(ctx.get(this.platformLdapProperties.getNameAttrName()).get().toString());
            }
            if (null != ctx.get(this.platformLdapProperties.getPhoneNumberAttrName())) {
                ldapUser.setPhoneNumber(ctx.get(this.platformLdapProperties.getPhoneNumberAttrName()).get().toString());
            }
            if (null != ctx.get(this.platformLdapProperties.getMailAttrName())) {
                ldapUser.setEmail(ctx.get(this.platformLdapProperties.getMailAttrName()).get().toString());
            }
            if (null != ctx.get(this.platformLdapProperties.getTitleAttrName())) {
                ldapUser.setRemark(ctx.get(this.platformLdapProperties.getTitleAttrName()).get().toString());
            }
            return ldapUser;
        });

        return result.stream().sorted(Comparator.comparing(LdapUser::getUsername)).skip(offset).limit(limit).collect(Collectors.toList());
    }

    private ContainerCriteria ldapQueryCriteria() {
        final ContainerCriteria criteria = query().searchScope(SearchScope.SUBTREE)
                .where("objectClass").is(this.platformLdapProperties.getObjectClassAttrName());
        if (this.platformLdapProperties.getMemberOf().length > 0 && !ObjectUtils.isEmpty(this.platformLdapProperties.getMemberOf()[0])) {
            final ContainerCriteria memberOfFilters = query().where(MEMBER_OF_ATTR_NAME).is(this.platformLdapProperties.getMemberOf()[0]);
            Arrays.stream(this.platformLdapProperties.getMemberOf()).skip(1)
                    .forEach(filter -> memberOfFilters.or(MEMBER_OF_ATTR_NAME).is(filter));
            criteria.and(memberOfFilters);
        }
        return criteria;
    }
}