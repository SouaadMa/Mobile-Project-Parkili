package com.example.projettdm_parkili

import android.R
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projettdm_parkili.databinding.ActivityMainBinding
import com.example.projettdm_parkili.models.User
import com.example.projettdm_parkili.retrofit.EndPoint
import com.example.projettdm_parkili.utils.getUserId
import com.example.projettdm_parkili.utils.isLogin
import com.example.projettdm_parkili.utils.saveUserID
import com.example.projettdm_parkili.utils.saveUserName
import com.example.projettdm_parkili.validator.EmailValidator
import com.example.projettdm_parkili.validator.EmptyValidator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var mGoogleSignInClient : GoogleSignInClient
    val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isLogin(this)) {
            openHomeActivity()
        }

        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        var button_signup = binding.buttonSignup
        button_signup.setOnClickListener {
            openSignUpActivity()
        }

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)

        val signInButton = binding.ivGoogleSignIn
        //signInButton.setSize(SignInButton.SIZE_STANDARD)

        signInButton.setOnClickListener {
            signInGoogle()
        }



        var button_signin = binding.buttonSignin
        button_signin.setOnClickListener {
            val email = binding.editTextTextPersonName.text.toString()
            val emailEmptyValidation = EmptyValidator(email).validate()
            val validEmailValidation = EmailValidator(email).validate()
            val pwd = binding.editTextTextPassword.text.toString()
            val pwdEmptyValidator = EmptyValidator(pwd).validate()

            var errormsg =
                if (!emailEmptyValidation.isSuccess)
                    emailEmptyValidation.message
                else if (!validEmailValidation.isSuccess)
                    validEmailValidation.message
                else if (!pwdEmptyValidator.isSuccess)
                    pwdEmptyValidator.message
                else null

            if(errormsg != null) Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show()
            else {
                callSignIn(this)
            }
        }

    }

    fun openSignUpActivity(){

        var intent = Intent(this, SignUpActivity::class.java )
        startActivity(intent)

    }

    fun callSignIn(ctx : Context) {
        CoroutineScope(Dispatchers.IO).launch  {
            val resp = signIn()
            withContext(Dispatchers.Main) {
                if (resp.isSuccessful && resp.body() != null) {
                    saveUserName(ctx, resp.body()?.fullname!!)
                    saveUserID(ctx, resp.body()?.userId!!)
                    //load user reservations in local storage
                    loadUserReservations(ctx)
                    openHomeActivity()
                } else {
                    Log.d("io", "resp is not successful")
                    val errormsg = "Could not login"
                    showErrorMsg(errormsg!!)
                }
            }
        }
    }

    private fun loadUserReservations(ctx : Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = EndPoint.createInstance().getUserReservations(getUserId(ctx))
            withContext(Dispatchers.Main) {
                if(response.isSuccessful && response.body() != null) {
                    var dao = AppDatabase.buildDatabase(ctx)?.getReservationDao()
                    dao?.addReservations(response.body()!!)
                    Log.d("loadreservations", "Loaded user reservations")
                }
                else {
                    Log.d("loadreservations", "Couldn't load user reservations")
                }
            }
        }
    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl

                Toast.makeText(this, "Email : $personEmail", Toast.LENGTH_LONG).show()
                openHomeActivity()
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }

    suspend fun signIn() : Response<User> {

        return EndPoint.createInstance().login(
            User(
                email = binding.editTextTextPersonName.text.toString(),
                pwd = binding.editTextTextPassword.text.toString()
            )
        )
    }

    fun showErrorMsg(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    fun openHomeActivity(){

        var intent = Intent(this, NavHomeActivity::class.java )
        startActivity(intent)
        finish()

    }
}