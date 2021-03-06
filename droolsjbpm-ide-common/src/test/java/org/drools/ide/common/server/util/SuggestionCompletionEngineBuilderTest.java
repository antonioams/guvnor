/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.ide.common.server.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.drools.ide.common.client.modeldriven.SuggestionCompletionEngine;
import org.drools.ide.common.server.util.SuggestionCompletionEngineBuilder;
import org.drools.lang.dsl.DSLMappingEntry;

public class SuggestionCompletionEngineBuilderTest {
    SuggestionCompletionEngineBuilder builder = new SuggestionCompletionEngineBuilder();

    @Before
    public void setUp() throws Exception {
        this.builder.newCompletionEngine();
    }

    @Test
    public void testAddDSLSentence() {
        final String input = "{This} is a {pattern} considered pretty \\{{easy}\\} by most \\{people\\}. What do you {say}?";
        this.builder.addDSLActionSentence( input );
        this.builder.addDSLConditionSentence( "foo bar" );
        final SuggestionCompletionEngine engine = this.builder.getInstance();

        assertEquals( 1, engine.actionDSLSentences.length );
        assertEquals( 1, engine.conditionDSLSentences.length );

    }

    @Test
    public void testAddSentenceMultipleTypes() {
        final DSLMappingEntry mapping1 = mock(DSLMappingEntry.class, "mapping1");
        final DSLMappingEntry mapping2 = mock(DSLMappingEntry.class, "mapping2");
        final DSLMappingEntry mapping3 = mock(DSLMappingEntry.class, "mapping3");
        final DSLMappingEntry mapping4 = mock(DSLMappingEntry.class, "mapping4");
        
        
        // setting expectations for entry1
        when(mapping1.getSection()).thenReturn(DSLMappingEntry.CONDITION );
        when(mapping1.getMappingKey()).thenReturn("cond");
        
        // setting expectations for entry2
        when(mapping2.getSection()).thenReturn(DSLMappingEntry.CONSEQUENCE );
        when(mapping2.getMappingKey()).thenReturn("cons");

        // setting expectations for entry3
        when(mapping3.getSection()).thenReturn(DSLMappingEntry.ANY );
        when(mapping3.getMappingKey()).thenReturn("any");
        
        // setting expectations for entry4
        when(mapping4.getSection()).thenReturn(DSLMappingEntry.KEYWORD );
        when(mapping4.getMappingKey()).thenReturn("key");
        
        this.builder.addDSLMapping(mapping1);
        this.builder.addDSLMapping(mapping2);
        this.builder.addDSLMapping(mapping3);
        this.builder.addDSLMapping(mapping4);

        final SuggestionCompletionEngine engine = this.builder.getInstance();

        assertEquals( 1, engine.actionDSLSentences.length );
        assertEquals( 1, engine.conditionDSLSentences.length );
        assertEquals( 1, engine.keywordDSLItems.length );
        assertEquals( 1, engine.anyScopeDSLItems.length );

        assertEquals("cond", engine.conditionDSLSentences[0].getDefinition() );
    }

}
