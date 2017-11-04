/*
 * This file ("PluginMetadataInjector.java") is part of the molecular-project by Louis.
 * Copyright © 2017 Louis
 *
 * The molecular-project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The molecular-project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with molecular-project.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.molecular.common.plugin.injector;

import com.google.common.base.Strings;
import org.molecular.api.Molecular;
import org.molecular.api.plugin.Plugin;
import org.molecular.api.plugin.PluginContainer;
import org.molecular.common.plugin.PluginLoader;
import org.molecular.common.plugin.analysis.DataTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Louis
 */

public class PluginMetadataInjector {

    public static final PluginMetadataInjector INSTANCE = new PluginMetadataInjector();

    private final Logger logger = LoggerFactory.getLogger("org.molecular.plugin");

    private PluginMetadataInjector() {
    }

    public void inject(PluginContainer container, PluginLoader loader) {
        Collection<DataTrace> plugins = loader.getWatcher().getAnnotationsGlobal(Plugin.class);

        logger.trace("Attempting to inject @Plugin.Metadata classes into {}", container.identifier());
        Collection<DataTrace> instances = loader.getWatcher().getAnnotationsDistrict(Plugin.Metadata.class, container);

        for (DataTrace trace : instances) {
            try {
                Optional<String> optional = InjectorUtils.fetchOwnerPlugin(logger, plugins, trace);
                if (!optional.isPresent()) {
                    logger.debug("Skipping annotation because of missing plugin identification");
                    continue;
                }

                String owner = optional.get();
                if (!container.identifier().equals(owner)) {
                    logger.warn("Skipping metadata injection for {}.{} since it is not for plugin {}", trace.clazz, trace.member, container.identifier());
                    continue;
                }

                PluginContainer plugin = container;
                String value = (String) trace.descriptor.get("value");

                if (!Strings.isNullOrEmpty(value)) {
                    plugin = Molecular.getPluginController().getPlugin(value).orElse(null);
                }

                if (plugin != null) {
                    Class<?> clazz = Class.forName(trace.clazz, true, loader.getPluginClassLoader());
                    Field field = clazz.getDeclaredField(trace.member);

                    if (field == null) {
                        logger.error("PluginMetadataInjector could not find target field for metadata injection [class:{}, field:{}]", trace.clazz, trace.member);
                        continue;
                    }

                    field.setAccessible(true);

                    boolean isStatic = Modifier.isStatic(field.getModifiers());
                    boolean mustBeStatic = !clazz.isInstance(container.instance().orElse(null));

                    if (mustBeStatic && !isStatic) {
                        logger.error("PluginMetadataInjector could not inject metadata in non static field if static is required [not located in plugin instance]");
                        continue;
                    }

                    if (!field.getType().isAssignableFrom(plugin.metadata().getClass())) {
                        logger.error("PluginMetadataInjector could not inject metadata because of non matching field type");
                        continue;
                    }

                    if (!isStatic && !container.instance().isPresent()) {
                        logger.error("PluginMetadataInjector could not inject metadata in non static field if plugin instance of {} is missing", container.identifier());
                        continue;
                    }

                    field.set(isStatic ? null : container.instance().get(), plugin.metadata());
                }
            } catch (Throwable throwable) {
                logger.error("An error occurred trying to inject a plugin metadata into {}.{}", trace.clazz, trace.member, throwable);
            }
        }
    }
}
