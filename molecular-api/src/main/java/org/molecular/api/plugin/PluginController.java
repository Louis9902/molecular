/*
 * This file ("PluginController.java") is part of the molecular-project by Louis.
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

package org.molecular.api.plugin;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Louis
 */

public interface PluginController {

    Optional<PluginContainer> getPlugin(String identifier);

    Optional<PluginContainer> getPlugin(Object instance);

    Collection<PluginContainer> getPlugins();

    ClassLoader getPluginClassLoader();
}
