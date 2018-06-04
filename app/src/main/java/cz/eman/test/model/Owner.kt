package cz.eman.test.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import cz.eman.test.database.AppDatabase

/**
 * Data class for owner of a question.
 */
@Table(name = Owner.TABLE_NAME, database = AppDatabase::class)
data class Owner(
        @PrimaryKey
        @JsonProperty("user_id")
        var id: Long = -1,

        @JsonProperty("accept_rate") @Column var acceptRate: Int = 0,
        @JsonProperty("display_name") @Column var displayName: String? = "",
        @JsonProperty("link") @Column var link: String? = "",
        @JsonProperty("profile_image") @Column var profileImage: String? = "",
        @JsonProperty("user_type") @Column var userType: String = "",
        @JsonProperty("reputation") @Column var reputation: Int = 0
) {
    companion object {
        const val TABLE_NAME = "Owner"
    }

    /**
     * Convert object to indented string to enable easy reading in logging.
     */
    override fun toString(): String {
        return "class Owner {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    acceptRate: " + toIndentedString(acceptRate) + "\n" +
                "    displayName: " + toIndentedString(displayName) + "\n" +
                "    link: " + toIndentedString(link) + "\n" +
                "    profileImage: " + toIndentedString(profileImage) + "\n" +
                "    userType: " + toIndentedString(userType) + "\n" +
                "    reputation: " + toIndentedString(reputation) + "\n" +
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