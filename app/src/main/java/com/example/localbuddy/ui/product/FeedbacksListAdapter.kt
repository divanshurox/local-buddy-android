package com.example.localbuddy.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.entity.Feedback
import com.example.localbuddy.avatarUrl
import com.example.localbuddy.convertToDate
import com.example.localbuddy.databinding.ListItemFeedbackBinding
import com.example.localbuddy.imgUrl
import com.example.localbuddy.visible

class FeedbacksListAdapter(val userId: String, val deleteFeedback: (feedbackId: String) -> Unit) :
    ListAdapter<Feedback, FeedbacksListAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(private val binding: ListItemFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(feedback: Feedback) {
            binding.apply {
                userAvatar.avatarUrl(feedback.userAvatar)
                userFeedback.text = feedback.feedback
                userName.text = feedback.username
                datePosted.convertToDate(feedback.createdAt)
                userRating.text = "${feedback.rating.toString()}/5"
                deleteFeedback.apply {
                    visible(userId == feedback.userId)
                    setOnClickListener {
                        cardContent.visible(false)
                        progressBar.visible(true)
                        deleteFeedback(feedback.id)
                    }
                }
            }
            if (!feedback.feedbackImg.isNullOrEmpty()) {
                binding.feedbackImage.apply {
                    visible(true)
                    imgUrl(feedback.feedbackImg)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemFeedbackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Feedback>() {
        override fun areItemsTheSame(oldItem: Feedback, newItem: Feedback): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Feedback, newItem: Feedback): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
}