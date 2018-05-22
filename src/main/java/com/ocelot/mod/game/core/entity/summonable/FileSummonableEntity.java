package com.ocelot.mod.game.core.entity.summonable;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface FileSummonableEntity {
	
	Class<? extends IFileSummonable> value();
}