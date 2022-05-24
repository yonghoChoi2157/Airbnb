package fastcampus.aop.part3.chapter07

import android.content.Context
import android.util.TypedValue
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import fastcampus.aop.part3.chapter07.databinding.ItemHouseBinding
import fastcampus.aop.part3.chapter07.databinding.ItemHouseDetailForViewpagerBinding

class HouseViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(houseModel: HouseModel) {
        if (binding is ItemHouseDetailForViewpagerBinding) {
            binding.titleTextView.text = houseModel.title
            binding.priceTextView.text = houseModel.price

            Glide
                .with(binding.thumbnailIamgeView.context)
                .load(houseModel.imgUrl)
                .into(binding.thumbnailIamgeView)
        } else if (binding is ItemHouseBinding) {
            binding.titleTextView.text = houseModel.title
            binding.priceTextView.text = houseModel.price

            Glide
                .with(binding.thumbnailImageView.context)
                .load(houseModel.imgUrl)
                .transform(
                    CenterCrop(),
                    RoundedCorners(dpToPx(binding.thumbnailImageView.context, 12))
                )
                .into(binding.thumbnailImageView)
        }


    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }
}