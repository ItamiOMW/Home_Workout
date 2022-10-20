package com.example.homeworkout.presentation.adapters.user_info_adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.homeworkout.domain.models.UserInfoModel

class DiffUtilItemCallbackUserInfo : DiffUtil.ItemCallback<UserInfoModel>() {

    override fun areItemsTheSame(oldItem: UserInfoModel, newItem: UserInfoModel): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: UserInfoModel, newItem: UserInfoModel): Boolean {
        return oldItem == newItem
    }

}