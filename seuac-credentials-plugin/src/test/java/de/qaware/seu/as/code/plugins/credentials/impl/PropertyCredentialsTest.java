/*
 *    Copyright 2015 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.seu.as.code.plugins.credentials.impl;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Basic unit test for the PropertyCredentials.
 *
 * @author phxql
 */
public class PropertyCredentialsTest {
    private File file;

    @Before
    public void setUp() throws Exception {
        file = File.createTempFile("seuac-credentials", ".properties");
    }

    @After
    public void tearDown() throws Exception {
        file.delete();
    }

    @Test
    public void test() throws Exception {
        Encryptor encryptor = mock(Encryptor.class);
        when(encryptor.decrypt(any(byte[].class))).thenAnswer(returnsFirstArg());
        when(encryptor.encrypt(any(byte[].class))).thenAnswer(returnsFirstArg());

        PropertyCredentials sut = new PropertyCredentials(file, encryptor);
        sut.set("key", "value");
        sut.save();

        assertThat(sut.get("key"), is("value"));
        assertThat(sut.get("doesnotexist"), is(nullValue()));

        // Load the properties and ensure they contain the key with the base64 encoded value
        Properties properties = new Properties();
        assertThat(file.exists(), is(true));
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            properties.load(fileInputStream);
        } finally {
            fileInputStream.close();
        }

        assertThat(properties.getProperty("key"), is(Base64.encodeBase64String("value".getBytes("UTF-8"))));
    }
}