package learn.cm.latestnewsapp.database.remote.model


data class NewsResponse(
    val statusCode: Int,
    val articles: List<Article>
)