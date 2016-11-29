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
    @Value("${script.engine.service.beans}")
    private String scriptEngineServiceBeansConfiguration;

    @Autowired
    private ApplicationContext context;

    public List<ScriptBinding> provide() {
        List<ScriptBinding> bindings = Lists.newArrayList();
        String[] scriptEngineServicePackages = scriptEngineServicePackagesConfiguration.split(";");
        String[] scriptEngineServiceBeans = scriptEngineServiceBeansConfiguration.split(";");

        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            String fullServiceName = bean.getClass().getCanonicalName();

            for (String whiteListedPackage : scriptEngineServicePackages) {
                if (fullServiceName.startsWith(whiteListedPackage)) {
                    bindings.add(ScriptBinding.builder().name(beanName).object(bean).build());
                }
            }

            for (String whiteListedBean : scriptEngineServiceBeans) {
                if (beanName.equals(whiteListedBean)) {
                    bindings.add(ScriptBinding.builder().name(beanName).object(bean).build());
                }
            }
        }

        return bindings;
    }
}
