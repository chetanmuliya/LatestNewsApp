package learn.cm.latestnewsapp.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import learn.cm.latestnewsapp.database.remote.model.Article
import learn.cm.latestnewsapp.databinding.ArticleItemLayBinding
import learn.cm.latestnewsapp.R
import learn.cm.latestnewsapp.presentation.home.HomeActivity


class NewsAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {


    private var lists: List<Article> = listOf()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding: ArticleItemLayBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.article_item_lay,
            parent,
            false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = lists[position]
        holder.binding.model = model
        holder.binding.cl.setOnClickListener {
            listener.onItemClick(model)
        }
    }

    fun updateItems(items: List<Article>?) {
        lists = items ?: emptyList()
        notifyDataSetChanged()
    }


    class MyViewHolder(val binding: ArticleItemLayBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
         interface OnItemClickListener {
             fun onItemClick(model: Article)
         }
     }

}

