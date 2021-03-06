package com.giffing.wicket.spring.boot.starter.configuration.extensions.core.csrf;

import org.apache.wicket.protocol.http.CsrfPreventionRequestCycleListener;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;


/**
 * Enables CSRF protection if the following condition matches.
 * 
 * 1. The {@link CsrfPreventionRequestCycleListener} class is in the classpath.
 * 
 * 2. The property {@link CsrfAttacksPreventionProperties#PROPERTY_PREFIX}.enabled has to be true (default = true)
 *
 * The protection should be enabled by default cause the {@link CsrfPreventionRequestCycleListener} is located
 * in Wickets core project.
 * 
 * @author Marc Giffing
 *
 */
@ApplicationInitExtension
@ConditionalOnProperty(prefix = CsrfAttacksPreventionProperties.PROPERTY_PREFIX, value = "enabled", matchIfMissing = true)
@ConditionalOnClass(value = org.apache.wicket.protocol.http.CsrfPreventionRequestCycleListener.class)
@EnableConfigurationProperties({ CsrfAttacksPreventionProperties.class })
public class CsrfAttacksPreventionConfig implements WicketApplicationInitConfiguration{

	@Autowired
	private CsrfAttacksPreventionProperties properties;
	
	@Override
	public void init(WebApplication webApplication) {
		CsrfPreventionRequestCycleListener listener = new CsrfPreventionRequestCycleListener();
		listener.setConflictingOriginAction(properties.getConflictingOriginAction());
		listener.setErrorCode(properties.getErrorCode());
		listener.setErrorMessage(properties.getErrorMessage());
		listener.setNoOriginAction(properties.getNoOriginAction());
		for (String acceptedOrigin : properties.getAcceptedOrigins()) {
			listener.addAcceptedOrigin(acceptedOrigin);
		}
		webApplication.getRequestCycleListeners().add(listener);
	}

}
