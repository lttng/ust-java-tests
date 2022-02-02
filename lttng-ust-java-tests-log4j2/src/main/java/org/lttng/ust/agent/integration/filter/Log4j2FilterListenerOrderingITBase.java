/*
 * Copyright (C) 2022, EfficiOS Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.lttng.ust.agent.integration.filter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.lttng.ust.agent.utils.Log4j2TestContext;
import org.lttng.ust.agent.utils.Log4j2TestUtils;

/**
 * Implementation of {@link FilterListenerOrderingITBase} for the log4j API.
 */
@TestInstance(Lifecycle.PER_CLASS)
public abstract class Log4j2FilterListenerOrderingITBase extends FilterListenerOrderingITBase {

    private Log4j2TestContext testContext;

    /**
     * Class setup
     */
    @BeforeAll
    public void log4j2ClassSetup() {
        Log4j2TestUtils.testClassSetup(getDomain());
    }

    /**
     * Class cleanup
     */
    @AfterAll
    public static void log4j2ClassCleanup() {
        Log4j2TestUtils.testClassCleanup();
    }

    @Override
    protected void registerAgent() {
        testContext = new Log4j2TestContext("log4j2." + this.getClass().getSimpleName() + ".xml");
        testContext.beforeTest();
    }

    @Override
    protected void deregisterAgent() {
        testContext.afterTest();
    }
}
