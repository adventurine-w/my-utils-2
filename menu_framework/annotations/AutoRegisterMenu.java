package org.adv.clickerflex.menus.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AutoRegisterMenu {
    SimpleMenuType menuType();
}
