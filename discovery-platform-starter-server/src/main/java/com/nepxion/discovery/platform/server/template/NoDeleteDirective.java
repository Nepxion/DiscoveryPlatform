package com.nepxion.discovery.platform.server.template;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;

public class NoDeleteDirective extends AuthDirective implements TemplateDirectiveModel {
    @Override
    public void execute(final Environment env,
                        final Map params,
                        final TemplateModel[] loopVars,
                        final TemplateDirectiveBody body) throws TemplateException, IOException {
        if (null != body && !checkPermission(Operation.DELETE)) {
            body.render(env.getOut());
        }
    }
}