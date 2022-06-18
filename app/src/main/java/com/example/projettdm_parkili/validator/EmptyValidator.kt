package com.example.projettdm_parkili.validator

import com.example.projettdm_parkili.R
import com.example.projettdm_parkili.validator.base.BaseValidator
import com.example.projettdm_parkili.validator.base.ValidateResult

class EmptyValidator(val input: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid = input.isNotEmpty()
        return ValidateResult(
            isValid,
            if (isValid) "Not empty" else "Empty required field"
        )
    }
}