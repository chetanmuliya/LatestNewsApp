package learn.cm.latestnewsapp.util

class DRK {
    companion object{
        fun dec(encData: String):String{
            var strChar = android.util.Base64.decode(encData , android.util.Base64.DEFAULT)
            //strChar = android.util.Base64.decode(strChar , android.util.Base64.DEFAULT)
            //strChar = android.util.Base64.decode(strChar , android.util.Base64.DEFAULT)
            return String(strChar)
        }

        //external fun getRapidApiKey(): String
    }
}