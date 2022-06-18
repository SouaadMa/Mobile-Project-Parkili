package com.example.projettdm_parkili.validator

import android.text.TextUtils
import com.example.projettdm_parkili.validator.base.BaseValidator
import com.example.projettdm_parkili.validator.base.ValidateResult

class EmailValidator(val email: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid =
            !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        return ValidateResult(
            isValid,
            if (isValid) "Valid Email format" else "Not a valid email address"
        )
    }
}