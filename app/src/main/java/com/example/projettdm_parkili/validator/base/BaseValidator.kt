package com.example.projettdm_parkili.validator.base

import com.example.projettdm_parkili.R

abstract class BaseValidator : IValidator {
    companion object {
        fun validate(vararg validators: IValidator): ValidateResult {
            validators.forEach {
                val result = it.validate()
                if (!result.isSuccess)
                    return result
            }
            return ValidateResult(true, "Validation Success")
        }
    }
}