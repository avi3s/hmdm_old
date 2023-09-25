/*
 *
 * Headwind MDM: Open Source Android MDM Software
 * https://h-mdm.com
 *
 * Copyright (C) 2019 Headwind Solutions LLC (http://h-sms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hmdm.license.guice;

import com.google.common.collect.ImmutableSet;
import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.ProvisionException;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.spi.Message;
import com.hmdm.license.guice.module.LicenseConfigModule;
import com.hmdm.license.guice.module.LicenseLiquibaseModule;
import com.hmdm.license.guice.module.LicensePersistenceModule;
import com.hmdm.license.guice.module.LicenseRestModule;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>A <code>Guice</code> initializer for servlet context for <code>License Server</code> application.</p>
 *
 * @author isv
 */
public class LicenseServerInitializer extends GuiceServletContextListener {

    private ServletContext context;
    
    private Injector injector;

    /**
     * <p>Constructs new <code>LicenseServerInitializer</code> instance. This implementation does nothing.</p>
     */
    public LicenseServerInitializer() {
    }

    protected Injector getInjector() {
        try {
            this.injector = Guice.createInjector(this.getModules());
        } catch (ProvisionException e){
            handleException(e);
        } catch (CreationException e){
            handleException(e);
        } catch (Exception e){
            handleException(e);
        }
        return injector;
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.context = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);
    }

    private List<Module> getModules() {
        List<Module> modules = new LinkedList<>();
        
        modules.add(new LicensePersistenceModule(this.context));
        modules.add(new LicenseLiquibaseModule(this.context));
        modules.add(new LicenseConfigModule(this.context));
        modules.add(new LicenseRestModule());

        return modules;
    }


    /**
     * <p>Logs the specified exception.</p>
     *
     * @param e an exception to log.
     */
    @SuppressWarnings("unchecked")
    private void handleException(ProvisionException e) {
        try {
            Field messagesField = ProvisionException.class.getDeclaredField("messages");
            messagesField.setAccessible(true);
            ImmutableSet<Message> messages = (ImmutableSet<Message>) messagesField.get(e);
            messages.iterator().forEachRemaining(message -> System.out.println("[LICENSE-SERVER-INITIALIZER]: ERROR: " + message.getMessage()));
            messagesField.setAccessible(false);
        } catch (Exception e1) {
            System.out.println("Failed to access [messages] field: " + e1);
        }
    }

    /**
     * <p>Logs the specified exception.</p>
     *
     * @param e an exception to log.
     */
    private void handleException(Exception e) {
        try {
            System.out.println("[LICENSE-SERVER-INITIALIZER] : ERROR : ");
            e.printStackTrace();
        } catch (Exception e1) {
            if (e.getCause() != null) {
                System.out.println("[LICENSE-SERVER-INITIALIZER] : ERROR : " + e.getCause().getMessage());
                e.getCause().printStackTrace();
            } else {
                System.out.println("[LICENSE-SERVER-INITIALIZER] : ERROR OF TYPE : " + e.getClass().getName());
            }
        }
    }

    /**
     * <p>Logs the specified exception.</p>
     *
     * @param e an exception to log.
     */
    private void handleException(CreationException e) {
        try {
            e.getErrorMessages().forEach(m-> System.out.println("[LICENSE-SERVER-INITIALIZER] : ERROR : " +  m.getMessage()));
        } catch (Exception e1) {
            if (e.getCause() != null) {
                System.out.println("[LICENSE-SERVER-INITIALIZER] : ERROR : " + e.getCause().getMessage());
                e.getCause().printStackTrace();
            } else {
                System.out.println("[LICENSE-SERVER-INITIALIZER] : ERROR OF TYPE : " + e.getClass().getName());
            }
        }
    }

}
