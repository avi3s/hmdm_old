package com.hmdm.license;

import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;

/**
 * <p>A configuration for HMDM License server application.</p>
 *
 * @author isv
 */
public class HMDMLicenseApplication extends ResourceConfig {

    /**
     * <p>Constructs new <code>HMDMLicenseApplication</code> instance and initializes the Guice-HK2 bridge.</p>
     */
    @Inject
    public HMDMLicenseApplication(final ServiceLocator serviceLocator) {
        packages("com.hmdm.license");
        register(new ContainerLifecycleListener() {
            public void onStartup(Container container) {
                ServletContainer servletContainer = (ServletContainer) container;
                GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
                GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
                Injector injector = (Injector) servletContainer.getServletContext().getAttribute(Injector.class.getName());
                guiceBridge.bridgeGuiceInjector(injector);
            }

            public void onReload(Container container) {
            }

            public void onShutdown(Container container) {
            }
        });
    }

}
