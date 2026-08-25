package com.taroflavoured;

import net.minecraft.client.RecipeBookCategories;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** Temporary diagnostic for the 21.1.235 RecipeBookCategories enum-extension metadata. */
public final class RecipeBookCategoryDiagnostic {
    private RecipeBookCategoryDiagnostic() {
    }

    public static void print() {
        Class<RecipeBookCategories> type = RecipeBookCategories.class;
        System.out.println("=== Taro RecipeBookCategories diagnostic ===");
        System.out.println("class=" + type.getName());
        System.out.println("interfaces=");
        for (Class<?> iface : type.getInterfaces()) {
            System.out.println("  " + iface.getName());
        }
        System.out.println("constructors=");
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            System.out.println("  " + constructor + " modifiers=" + Modifier.toString(constructor.getModifiers()));
            for (java.lang.annotation.Annotation annotation : constructor.getDeclaredAnnotations()) {
                System.out.println("    annotation=" + annotation.annotationType().getName());
            }
        }
        try {
            Method method = type.getDeclaredMethod("getExtensionInfo");
            System.out.println("getExtensionInfo=" + method);
            System.out.println("getExtensionInfo static=" + Modifier.isStatic(method.getModifiers()));
            System.out.println("extensionInfo=" + method.invoke(null));
        } catch (ReflectiveOperationException e) {
            System.out.println("getExtensionInfo failed: " + e);
        }
        System.out.println("implements IExtensibleEnum=" + IExtensibleEnum.class.isAssignableFrom(type));
        System.out.println("enum constants=" + java.util.Arrays.toString(type.getEnumConstants()));
        System.out.println("=== End diagnostic ===");
    }
}
