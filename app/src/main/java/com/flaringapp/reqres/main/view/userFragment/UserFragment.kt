package com.flaringapp.reqres.main.view.userFragment

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.signature.ObjectKey
import com.flaringapp.reqres.R
import com.flaringapp.reqres.common.Constants
import com.flaringapp.reqres.common.ViewUtils
import com.flaringapp.reqres.main.model.objects.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserFragment : Fragment() {

    private var userId: Int = -1
    private var avatarLink: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null

    var imageView: ImageView? = null
    var firstNameText: TextView? = null
    var lastNameText: TextView? = null
    var emailText: TextView? = null

    private var model: UserFragmentModel? = null

    private val disposables: ArrayList<Disposable> = ArrayList()

    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)

        if (arguments != null) {
            userId = arguments!!.getInt(userIdKey)
            avatarLink = arguments!!.getString(avatarLinkKey)
        } else if (sis != null) {
            userId = sis.getInt(userIdKey)
            avatarLink = sis.getString(avatarLinkKey)
            firstName = sis.getString(firstNameKey)
            lastName = sis.getString(lastNameKey)
            email = sis.getString(emailKey)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(userIdKey, userId)
        outState.putString(firstNameKey, firstName)
        outState.putString(lastNameKey, lastName)
        outState.putString(emailKey, email)
        outState.putString(avatarLinkKey, avatarLink)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UserFragmentModel(context!!)

        imageView = view.findViewById(R.id.user_image)
        firstNameText = view.findViewById(R.id.first_name_text)
        lastNameText = view.findViewById(R.id.last_name_text)
        emailText = view.findViewById(R.id.email_text)

        val emailLayout:View = view.findViewById(R.id.email_layout)
        emailLayout.setOnClickListener {
            if (context != null && email != null) ViewUtils.sendEmail(context!!, email!!)
        }

        loadData()
        loadImage(imageView!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageView = null
        firstNameText = null
        lastNameText = null
        emailText = null

        model?.destroy()
        model = null
    }

    /**
     * Load user data from database
     */
    private fun loadData() {
        if (model == null) return
        disposables += model!!.loadUser(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    setUserData(it)
                    updateData()
                },
                {
                    updateData()
                }
            )
    }

    /**
     * Update use data from internet
     */

    private fun updateData() {
        if (model == null) return
        disposables += model!!.updateUser(userId)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    setUserData(it)
                },
                {
                    val message = it.message
                    context?.let {
                        Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
                    }
                }
            )
    }

    private fun setUserData(user: User) {
        firstName = user.firstName
        lastName = user.lastName
        email = user.email
        avatarLink = user.avatarLink

        firstNameText?.text = firstName
        lastNameText?.text = lastName

        val content = SpannableString(email)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)

        emailText?.text = content
    }

    private fun loadImage(imageView: ImageView) {
        Glide.with(imageView)
            .load(avatarLink)
            .priority(Priority.IMMEDIATE)
            .signature(ObjectKey(avatarLink!!))
            .placeholder(ViewUtils.getPlaceholderDrawable())
            .thumbnail(Constants.thumbnailCompression)
            .transition(DrawableTransitionOptions.withCrossFade(Constants.animDuration))
            .into(imageView)
            .waitForLayout()
    }

    companion object {
        private const val userIdKey = "userId"
        private const val avatarLinkKey = "avatarLink"
        private const val firstNameKey = "firstName"
        private const val lastNameKey = "lastName"
        private const val emailKey = "email"

        fun newInstance(userId: Int, initialAvatarLink: String): UserFragment {
            val fragment = UserFragment()
            val bundle = Bundle()
            bundle.putInt(userIdKey, userId)
            bundle.putString(avatarLinkKey, initialAvatarLink)
            fragment.arguments = bundle
            return fragment
        }
    }

}