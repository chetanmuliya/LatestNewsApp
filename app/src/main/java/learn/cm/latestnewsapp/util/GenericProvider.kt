package learn.cm.latestnewsapp.util

object GenericProvider {

    init {
        System.loadLibrary("native-lib")
    }

    external fun getRapidApiKey(): String
}