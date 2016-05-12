/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.uuf.api;

import org.wso2.carbon.uuf.exception.InvalidTypeException;
import org.wso2.carbon.uuf.exception.MalformedConfigurationException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Configuration extends HashMap<String, Object> {

    public static final String KEY_APP_CONTEXT = "appContext";
    public static final String KEY_DEFAULT_THEME = "defaultTheme";
    public static final String KEY_ERROR_PAGES = "errorPages";

    public Configuration(Map<?, ?> rawMap) {
        this(rawMap.size());
        for (Entry<?, ?> entry : rawMap.entrySet()) {
            if (entry.getKey() instanceof String) {
                super.put((String) entry.getKey(), entry.getValue());
            } else {
                throw new InvalidTypeException("Configurations must be a Map<String, Object>. Instead found a '" +
                                                       entry.getKey().getClass().getName() + "' key.");
            }
        }
    }

    private Configuration(int initialCapacity) {
        super(initialCapacity);
    }

    public Optional<String> getAppContext() {
        Object appContextObj = get(KEY_APP_CONTEXT);
        if (appContextObj == null) {
            return Optional.<String>empty();
        }
        if (appContextObj instanceof String) {
            String appContext = (String) appContextObj;
            if (appContext.isEmpty()) {
                throw new IllegalArgumentException("App context cannot be empty.");
            }
            if (appContext.charAt(0) == '/') {
                throw new IllegalArgumentException(
                        "App context must start with a '/'. Instead found '" + appContext.charAt(0) + "'.");
            }
            return Optional.of(appContext);
        } else {
            throw new InvalidTypeException(
                    "Value of 'appContext' in the root component configuration must be a string. Instead found '" +
                            appContextObj.getClass().getName() + "'.");
        }
    }

    public String getDefaultThemeName() {
        Object defaultThemeNameObj = get(KEY_DEFAULT_THEME);
        if (defaultThemeNameObj == null) {
            throw new MalformedConfigurationException(
                    "Cannot find the value of 'defaultTheme' in the root component configurations.");
        }
        if (defaultThemeNameObj instanceof String) {
            String defaultThemeName = (String) defaultThemeNameObj;
            if (defaultThemeName.isEmpty()) {
                throw new IllegalArgumentException("Default theme name cannot be empty.");
            }
            return defaultThemeName;
        } else {
            throw new InvalidTypeException(
                    "Value of 'defaultTheme' in the root component configuration must be a string. Instead found '" +
                            defaultThemeNameObj.getClass().getName() + "'.");
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getErrorPages() {
        Object errorPagesObj = get(KEY_ERROR_PAGES);
        if (errorPagesObj == null) {
            return Collections.<String, String>emptyMap();
        }
        if (errorPagesObj instanceof Map) {
            Map<?, ?> errorPagesMap = (Map) errorPagesObj;
            for (Entry<?, ?> entry : errorPagesMap.entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    throw new InvalidTypeException(
                            "Value of 'errorPages' in the root component configuration must be a Map<String, String>." +
                                    " Instead found a '" + entry.getKey().getClass().getName() + "' key.");
                }
                if (!(entry.getValue() instanceof String)) {
                    throw new InvalidTypeException(
                            "Value of 'errorPages' in the root component configuration must be a Map<String, String> " +
                                    "Instead found a '" + entry.getValue().getClass().getName() + "' value.");
                }
            }
            return (Map<String, String>) errorPagesMap;
        } else {
            throw new InvalidTypeException(
                    "Value of 'errorPages' in the root component configuration must be a Map<String, String>. " +
                            "Instead found '" + errorPagesObj.getClass().getName() + "'.");
        }
    }

    public String getAsString(String key) {
        Object value = super.get(key);
        if ((value == null) || (value instanceof String)) {
            return (String) value;
        } else {
            throw new InvalidTypeException(
                    "Value of '" + key + "' must be a string. Instead found '" + value.getClass().getName() + "'.");
        }
    }

    public Object getAsStringOrDefault(String key, String defaultValue) {
        String value = getAsString(key);
        return (value == null) ? defaultValue : value;
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public Object replace(String key, Object value) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        return super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException("Cannot change Configuration.");
    }

    public void merge(Configuration otherConfiguration) {
        for (Entry<String, Object> entry : otherConfiguration.entrySet()) {
            super.putIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    public static Configuration emptyConfiguration() {
        return new Configuration(0);
    }
}