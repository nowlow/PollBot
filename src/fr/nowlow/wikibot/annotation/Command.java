package fr.nowlow.wikibot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	public String name();
	public Executor type() default Executor.ALL;
	public String description() default "";
	
	public enum Executor{
		USER, CONSOLE, ALL;
	}
}
