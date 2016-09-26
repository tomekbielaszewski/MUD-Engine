package org.grizz.game.service.script;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemScriptBindingProvider {
    @Value("${script.engine.service.packages}")
    private String scriptEngineServicePackagesConfiguration;

    @Autowired
    private ApplicationContext context;

    public List<ScriptBinding> provide() {
        List<ScriptBinding> bindings = Lists.newArrayList();
        String[] scriptEngineServicePackages = scriptEngineServicePackagesConfiguration.split(";");

        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            String canonicalName = bean.getClass().getCanonicalName();

            for (String servicePackage : scriptEngineServicePackages) {
                if (canonicalName.startsWith(servicePackage)) {
                    bindings.add(ScriptBinding.builder().name(beanName).object(bean).build());
                }
            }
        }

        return bindings;
    }
}
