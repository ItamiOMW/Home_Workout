package com.example.homeworkout.presentation.adapters.user_info_adapter

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.homeworkout.R
import com.example.homeworkout.databinding.ExerciseItemBinding
import com.example.homeworkout.databinding.UserItemBinding
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import javax.inject.Inject

class UserInfoAdapter @Inject constructor(val application: Application) :
    ListAdapter<UserInfoModel, UserInfoViewHolder>(
        DiffUtilItemCallbackUserInfo()
    ) {

    var onItemClicked: ((UserInfoModel) -> Unit)? = null
    var onItemLongClicked: ((UserInfoModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        val item = currentList[position]
        with(holder.binding) {
            tvDate.text = item.date
            tvWeight.text =
                String.format(application.getString(R.string.weight_format), item.weight)
            ivUserPhoto.setImageBitmap(item.photo)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClicked?.invoke(item)
            true
        }

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }
    }

}