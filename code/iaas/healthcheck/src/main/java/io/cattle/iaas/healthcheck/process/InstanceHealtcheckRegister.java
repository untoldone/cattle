package io.cattle.iaas.healthcheck.process;

import io.cattle.iaas.healthcheck.service.HealthcheckService;
import io.cattle.iaas.healthcheck.service.HealthcheckService.HealthcheckInstanceType;
import io.cattle.platform.core.addon.InstanceHealthCheck;
import io.cattle.platform.core.constants.InstanceConstants;
import io.cattle.platform.core.model.Instance;
import io.cattle.platform.engine.handler.HandlerResult;
import io.cattle.platform.engine.handler.ProcessPostListener;
import io.cattle.platform.engine.process.ProcessInstance;
import io.cattle.platform.engine.process.ProcessState;
import io.cattle.platform.json.JsonMapper;
import io.cattle.platform.object.util.DataAccessor;
import io.cattle.platform.process.common.handler.AbstractObjectProcessLogic;
import io.cattle.platform.util.type.Priority;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class InstanceHealtcheckRegister extends AbstractObjectProcessLogic implements ProcessPostListener, Priority {
    @Inject
    JsonMapper jsonMapper;

    @Inject
    HealthcheckService healtcheckService;

    @Override
    public HandlerResult handle(ProcessState state, ProcessInstance process) {
        Instance instance = (Instance) state.getResource();
        InstanceHealthCheck healthCheck = DataAccessor.field(instance,
                InstanceConstants.FIELD_HEALTH_CHECK, jsonMapper, InstanceHealthCheck.class);
        
        if (healthCheck != null) {
            healtcheckService.registerForHealtcheck(HealthcheckInstanceType.INSTANCE, instance.getId());
        }
        return null;
    }

    @Override
    public String[] getProcessNames() {
        return new String[] { InstanceConstants.PROCESS_START };
    }

    @Override
    public int getPriority() {
        return Priority.DEFAULT;
    }

}