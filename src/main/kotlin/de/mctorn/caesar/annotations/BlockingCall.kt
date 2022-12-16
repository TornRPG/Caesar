package de.mctorn.caesar.annotations


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
/**
 * Indicates that a method is blocking
 */
annotation class BlockingCall
