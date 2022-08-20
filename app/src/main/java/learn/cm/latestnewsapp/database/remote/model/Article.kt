package learn.cm.latestnewsapp.database.remote.model


import com.google.gson.annotations.SerializedName

data class Article(
    val title: String,
    val link: String,
    @SerializedName("published_date")
    val publishedDate: String,
    val source: Source
)