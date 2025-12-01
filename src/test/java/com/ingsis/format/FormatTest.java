package com.ingsis.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FormatTest {

    @Test
    void createAndAccessors() {
        Format f = new Format("owner", "name", "def", true);
        assertEquals("owner", f.getOwnerId());
        assertEquals("name", f.getName());
        assertEquals("def", f.getDefaultValue());
        assertTrue(f.isActive());

        f.setActive(false);
        f.setDefaultValue("x");
        assertFalse(f.isActive());
        assertEquals("x", f.getDefaultValue());

        assertNull(f.getId());
    }

    @Test
    void defaultConstructorAndSetters() {
        Format f = new Format();
        assertNull(f.getOwnerId());
        assertNull(f.getDefaultValue());
        assertFalse(f.isActive());

        f.setDefaultValue("z");
        f.setActive(true);

        assertEquals("z", f.getDefaultValue());
        assertTrue(f.isActive());

        assertNull(f.getId());
    }

    @Test
    void idAccessorViaReflection() throws Exception {
        Format f = new Format("owner", "name", "def", true);
        java.lang.reflect.Field idField = Format.class.getDeclaredField("id");
        idField.setAccessible(true);
        java.util.UUID uuid = java.util.UUID.randomUUID();
        idField.set(f, uuid);
        assertEquals(uuid, f.getId());
    }

    @Test
    void explicitlyInvokeAllAccessorsViaReflection() throws Exception {
        Format f = new Format("owner", "name", "def", true);

        java.lang.reflect.Method m1 = Format.class.getMethod("getOwnerId");
        java.lang.reflect.Method m2 = Format.class.getMethod("getName");
        java.lang.reflect.Method m3 = Format.class.getMethod("getDefaultValue");
        java.lang.reflect.Method m4 = Format.class.getMethod("isActive");

        assertEquals("owner", m1.invoke(f));
        assertEquals("name", m2.invoke(f));
        assertEquals("def", m3.invoke(f));
        assertEquals(true, m4.invoke(f));

        java.lang.reflect.Method s1 = Format.class.getMethod("setDefaultValue", String.class);
        java.lang.reflect.Method s2 = Format.class.getMethod("setActive", boolean.class);
        s1.invoke(f, "newVal");
        s2.invoke(f, false);

        assertEquals("newVal", f.getDefaultValue());
        assertFalse(f.isActive());
    }
}
