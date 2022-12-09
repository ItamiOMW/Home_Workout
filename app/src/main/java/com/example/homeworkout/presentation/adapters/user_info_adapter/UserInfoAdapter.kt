package com.example.homeworkout.presentation.adapters.user_info_adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.homeworkout.*
import com.example.homeworkout.databinding.UserItemBinding
import com.example.homeworkout.domain.models.UserInfoModel
import java.sql.Date
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
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
            tvDate.text = longToTime(item.date).format(DateTimeFormatter.ISO_LOCAL_DATE)
            tvWeight.text =
                String.format(application.getString(R.string.weight_format), item.weight)
            Glide.with(application).load(item.photo).into(ivUserPhoto)
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