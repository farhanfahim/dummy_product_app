package com.app.dummyproduct.ui.fragment.auth

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.dummyproduct.R
import com.app.dummyproduct.base.BaseFragment
import com.app.dummyproduct.callback.ActionCallback
import com.app.dummyproduct.constants.Constant
import com.app.dummyproduct.data.events.BaseEvent
import com.app.dummyproduct.data.events.OnLoginEvent
import com.app.dummyproduct.databinding.FragmentSignInBinding
import com.app.dummyproduct.ui.activity.HomeActivity
import com.app.dummyproduct.utils.StaticMethods
import com.app.dummyproduct.utils.Utils
import com.app.dummyproduct.utils.gone
import com.app.dummyproduct.utils.isNetworkAvailable
import com.app.dummyproduct.viewmodel.SignInViewModel

class SignInFragment : BaseFragment(), ActionCallback {

    private lateinit var binding: FragmentSignInBinding
    lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        viewModel.loginEvent.observe(this, loginObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            binding<FragmentSignInBinding>(
                inflater,
                R.layout.fragment_sign_in,
                container
            ).apply {
                lifecycleOwner = viewLifecycleOwner
                callback = this@SignInFragment
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                if (binding.usernameLayout.isErrorEnabled) {
                    binding.usernameError.gone()
                    binding.usernameLayout.isErrorEnabled = false
                }


                if (cs.length > 4) {

                    Utils.setSuccessIcon(
                        binding.usernameErrorIcon,
                        binding.etUsername,
                        binding.usernameLayout,
                    )


                } else {
                    binding.etUsername.setBackgroundResource(R.drawable.drawable_edittext)
                    binding.usernameError.gone()
                }
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {

            }

            override fun afterTextChanged(arg0: Editable) {

            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                if (binding.passwordLayout.isErrorEnabled) {
                    binding.passwordError.gone()
                    binding.passwordLayout.isErrorEnabled = false
                }

                if (cs.length >= 6) {
                    if (!StaticMethods.isValidPassword(cs.toString())) {
                        Utils.setErrorIcon(
                            binding.passwordErrorIcon,
                            binding.etPassword,
                            binding.passwordLayout,
                            "Password must be 6 characters, 1 capital, 1 numeric"
                        )
                    } else {
                        Utils.setSuccessIcon(
                            binding.passwordErrorIcon,
                            binding.etPassword,
                            binding.passwordLayout
                        )
                    }
                } else {
                    binding.etPassword.setBackgroundResource(R.drawable.drawable_edittext)
                    binding.passwordErrorIcon.gone()
                }
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {

            }

            override fun afterTextChanged(arg0: Editable) {

            }
        })

    }

    companion object {
        private const val TAG = "SignInFragment"

        @JvmStatic
        fun newInstance() = SignInFragment()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View) {
        when (view.id) {

            R.id.sign_in_btn -> {
                dismissTouch()
                checkValidations()
            }

        }
    }

    private fun checkValidations() {
        StaticMethods.hideSoftKeyboard(requireActivity())
        if (binding.etUsername.text.toString().isEmpty()) {
            Utils.setErrorIcon(
                binding.usernameErrorIcon,
                binding.etUsername,
                binding.usernameLayout,
                "Username is required"
            )
        } else if (binding.etPassword.text.toString().isEmpty()) {
            Utils.setErrorIcon(
                binding.passwordErrorIcon,
                binding.etPassword,
                binding.passwordLayout,
                "Password is required"
            )
        } else if (binding.etPassword.text.toString().length < 6) {
            Utils.setErrorIcon(
                binding.passwordErrorIcon,
                binding.etPassword,
                binding.passwordLayout,
                "Password is really short please enter atleast 6 character"
            )
        } else if (!StaticMethods.isValidPassword(binding.etPassword.text.toString())) {
            Utils.setErrorIcon(
                binding.passwordErrorIcon,
                binding.etPassword,
                binding.passwordLayout,
                "Password must be 6 characters, 1 capital, 1 numeric"
            )
        } else {
            dismissTouch()
            callLoginApi()
        }
    }

    private fun callLoginApi() {

        if(isNetworkAvailable(requireContext())) {
            Utils.onStartAnimationCall(binding.signInBtn)

            val hashMap = HashMap<String, String>()
            hashMap["username"] = binding.etUsername.text.toString().trim()
            hashMap["password"] = binding.etPassword.text.toString().trim()
            viewModel.onLogin(hashMap)
        }else{
            snack(Constant.ErrorMessageNoConnectivity)
        }
    }


    private val loginObserver = Observer<BaseEvent<OnLoginEvent>> {
        when (val event = it.getEventIfNotHandled()) {
            is OnLoginEvent.StartLoading -> {}

            is OnLoginEvent.StopLoading -> {}

            is OnLoginEvent.OnLoginData -> {

                Log.d("Farhan", event.model!!.email)
                enableTouch()
                Utils.onSuccessCall(binding.signInBtn, requireActivity())
                Utils.onStartAnimationCall(binding.signInBtn)
                StaticMethods.hideSoftKeyboard(requireActivity())
                binding.etUsername.clearFocus()
                binding.etPassword.clearFocus()
                changeActivity(HomeActivity::class.java)
                requireActivity().finish()

            }

            is OnLoginEvent.Error -> {
                Log.d("Farhan", "${event.error}")

                enableTouch()
                Utils.onErrorCall(binding.signInBtn, requireActivity())
                snack(event.error!!)
            }

            is OnLoginEvent.Exception -> {
                Log.d("Farhan", "${event.exception?.message.toString()}")

                enableTouch()
                Utils.onErrorCall(binding.signInBtn, requireActivity())
                snack(event.exception?.message.toString())
            }
            else -> {

            }
        }
    }


}