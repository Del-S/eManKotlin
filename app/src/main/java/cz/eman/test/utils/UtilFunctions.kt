package cz.eman.test.utils

import android.os.Build
import android.text.Html
import cz.eman.test.BaseActivity
import java.util.*

/**
 * Functions that are utility and did not fit anywhere else.
 */
class UtilFunctions {
    // All these functions are static using companion object
    companion object{
        /**
         * Decode HTML text into String text translating HTML characters.
         */
        fun decodeHtmlString(text: String): String {
            return if (Build.VERSION.SDK_INT >= 24)
                Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString().trim()
            else
                Html.fromHtml(text).toString().trim()
        }

        /**
         * Translates milliseconds into String using SimpleDateFormatter
         */
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