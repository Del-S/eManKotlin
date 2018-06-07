package utils

import android.os.Build
import android.text.Html
import cz.eman.test.BaseActivity
import java.util.*

class UtilFunctions {
    companion object{
        fun decodeHtmlString(text: String): String {
            return if (Build.VERSION.SDK_INT >= 24)
                Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString().trim()
            else
                Html.fromHtml(text).toString().trim()
        }

        fun createDateString(millis: Long, default: String?): String? {
            var dateString: String? = default
            if(millis > 0) {
                val createdDate = Date(millis)
                dateString = BaseActivity.mSimpleDateFormatter.format(createdDate)
            }

            return dateString
        }
    }
}