package org.bastanchu.churierpv2.view.common.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ListField(val selected:Boolean = true)
