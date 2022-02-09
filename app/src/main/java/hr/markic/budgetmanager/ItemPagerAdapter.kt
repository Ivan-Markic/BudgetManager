package hr.markic.budgetmanager

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.markic.budgetmanager.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemPagerAdapter(private val context: Context, private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val ivRead: ImageView = itemView.findViewById(R.id.ivRead)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)

        fun bind(item: Item) {
            Picasso.get()
                .load(File(item.picturePath))
                .error(R.drawable.product)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            val priceString = item.price.toString() + " €"
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvCategory.text = item.category
            tvPrice.text = priceString

            ivRead.setImageResource(if (item.read) R.drawable.green_flag else R.drawable.red_flag)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(itemView = LayoutInflater
        .from(parent.context).inflate(R.layout.item_pager, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.ivRead.setOnClickListener {
            item.read = !item.read
            val uri = ContentUris.withAppendedId(PRODUCT_PROVIDER_URI, item._id!!)
            val values = ContentValues().apply {
                put(Item::read.name, item.read)
            }
            context.contentResolver.update(
                uri,
                values,
                null,
                null
            )
            notifyItemChanged(position)
        }
        holder.bind(item)
    }

    override fun getItemCount() = items.size
}