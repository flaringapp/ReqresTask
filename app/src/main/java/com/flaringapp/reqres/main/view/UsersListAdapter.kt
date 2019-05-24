package com.flaringapp.reqres.main.view

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.signature.ObjectKey
import com.flaringapp.reqres.R
import com.flaringapp.reqres.common.Constants
import com.flaringapp.reqres.common.ViewUtils
import com.flaringapp.reqres.main.model.objects.ListUser
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class UsersListAdapter(
    private var models: ArrayList<ListUser> = ArrayList()
): RecyclerView.Adapter<UserViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private val clickPublisher = PublishSubject.create<ListUser>()
    val clickEvent: Observable<ListUser> = clickPublisher

    fun updateModels(models: List<ListUser>) {
        applyAndAnimateRemovals(models)
        applyAndAnimateAdditions(models)
        applyAndAnimateMovedItems(models)
    }

    fun addModel(student: ListUser) {
        addItem(models.size, student)
    }

    fun removeModel(student: ListUser) {
        val index = models.indexOf(student)
        if (index >= 0 ) {
            removeItem(index)
        }
    }

    fun updateModel(student: ListUser) {
        models.indexOfFirst { it.id == student.id }.let {
            if (it >= 0) {
                models[it] = student
                notifyItemChanged(it)
            }
        }
    }

    private fun applyAndAnimateRemovals(newModels: List<ListUser>) {
        for (i in models.size - 1 downTo 0) {
            val model = models[i]
            if (!newModels.contains(model)) {
                removeItem(i)
            }
        }
    }

    private fun applyAndAnimateAdditions(newModels: List<ListUser>) {
        var i = 0
        val count = newModels.size
        while (i < count) {
            val model = newModels[i]
            if (!models.contains(model)) {
                addItem(i, model)
            }
            i++
        }
    }

    private fun applyAndAnimateMovedItems(newModels: List<ListUser>) {
        for (toPosition in newModels.indices.reversed()) {
            val model = newModels[toPosition]
            val fromPosition = models.indexOf(model)
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition)
            }
        }
    }

    private fun removeItem(position: Int): ListUser {
        val model = models.removeAt(position)
        notifyItemRemoved(position)
        return model
    }

    private fun addItem(position: Int, model: ListUser) {
        models.add(position, model)
        notifyItemInserted(position)
    }

    private fun moveItem(fromPosition: Int, toPosition: Int) {
        val model = models.removeAt(fromPosition)
        models.add(toPosition, model)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val model = models[position]

        val fullNameText = model.firstName + " " + model.lastName

        holder.nameTextView.text = fullNameText

        holder.container.setOnClickListener { clickPublisher.onNext(model) }

        Glide.with(holder.imageView)
            .load(model.avatarLink)
            .signature(ObjectKey(model.avatarLink))
            .transform(MultiTransformation<Bitmap>(
                CenterCrop(),
                RoundedCornersTransformation(
                    ViewUtils.dp(holder.imageView.context, Constants.roundedCorners).toInt(),
                    0,
                    RoundedCornersTransformation.CornerType.ALL)
                )
            ).placeholder(ViewUtils.getPlaceholderDrawable())
            .thumbnail(Constants.thumbnailCompression)
            .transition(DrawableTransitionOptions.withCrossFade(Constants.animDuration))
            .into(holder.imageView)
            .waitForLayout()
    }

}