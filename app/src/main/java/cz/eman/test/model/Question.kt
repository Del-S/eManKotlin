package cz.eman.test.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import cz.eman.test.database.AppDatabase

/**
 * Data class containing question information.
 */
@Table(name = Question.TABLE_NAME, database = AppDatabase::class)
data class Question(
        @JsonProperty("question_id")
        @PrimaryKey
        var id: Long = -1,

        @JsonProperty("answer_count") @Column var answerCount: Int = 0,
        @JsonProperty("body") @Column var body: String = "",
        @JsonProperty("creation_date") @Column var creationDate: Long = 0,
        @JsonProperty("last_edit_date") @Column var lastEditDate: Long = 0,
        @JsonProperty("closed_date") @Column var closedDate: Long = 0,
        @JsonProperty("is_answered") @Column var isAnswered: Boolean = false,
        @JsonProperty("last_activity_date") @Column var lastActivityDate: Long = 0,
        @JsonProperty("closed_reason") @Column var closedReason: String? = "",
        @JsonProperty("link") @Column var link: String = "",
        @JsonProperty("score") @Column var score: Int = 0,
        @JsonProperty("title") @Column var title: String = "",
        @JsonProperty("view_count") @Column var viewCount: Int = 0,
        @JsonProperty("owner")
        @Column
        @ForeignKey(saveForeignKeyModel = true)
        var owner: Owner? = null
) {
        companion object {
                const val TABLE_NAME = "Question"
                private const val SEPARATE_SYMBOL = ", "
        }

        /**
         * Setting tags and tagsDB to be saved in the database.
         * Reason for this is MutableList could not be saved into the database.
         * DBFlow supports custom TypeConverters but gradle did not pass the
         * tests when this feature was used.
         *
         * That is why MutableList<String> is converted to String and saved.
         */
        @JsonProperty("tags")
        var tags: MutableList<String>? = null
                get() = field
                set(value) {
                        field = value
                        if(value != null && tagsDb == null) {
                            tagsDb = value.joinToString()
                        }
                }

        /**
         * Variable used to save tags in the database.
         * Contains List parsed to a String.
         *
         * When this variable is loaded from the database, tags are parsed
         * into a MutableList and saved into the tags variable.
         */
        @Column var tagsDb: String? = null
                set(value) {
                        field = value
                        if(value != null && tags == null)
                                tags = value.split(SEPARATE_SYMBOL).toMutableList()
                }


        /**
         * Convert object to indented string to enable easy reading in logging.
         */
        override fun toString(): String {
                return "class Question {\n" +
                        "    id: " + toIndentedString(id) + "\n" +
                        "    answerCount: " + toIndentedString(answerCount) + "\n" +
                        "    body: " + toIndentedString(body) + "\n" +
                        "    creationDate: " + toIndentedString(creationDate) + "\n" +
                        "    lastEditDate: " + toIndentedString(lastEditDate) + "\n" +
                        "    isAnswered: " + toIndentedString(isAnswered) + "\n" +
                        "    lastActivityDate: " + toIndentedString(lastActivityDate) + "\n" +
                        "    link: " + toIndentedString(link) + "\n" +
                        "    score: " + toIndentedString(score) + "\n" +
                        "    title: " + toIndentedString(title) + "\n" +
                        "    viewCount: " + toIndentedString(viewCount) + "\n" +
                        "    owner: " + toIndentedString(owner) + "\n" +
                        "    tags: " + toIndentedString(tags) + "\n" +
                        "    tagsDb: " + toIndentedString(tagsDb) + "\n" +
                        "}"
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private fun toIndentedString(o: Any?): String {
                return o?.toString()?.replace("\n", "\n    ") ?: "null"
        }
}