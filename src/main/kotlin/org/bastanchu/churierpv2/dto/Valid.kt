package org.bastanchu.churierpv2.dto

import jakarta.validation.ConstraintViolation

interface Valid {

    fun validate(): List<ConstraintViolation<*>>

}