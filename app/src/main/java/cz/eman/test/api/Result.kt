package cz.eman.test.api

import com.fasterxml.jackson.annotation.JsonProperty
import cz.eman.test.model.Question

/**
 * Data class containing result api information.
 */
data class Result(
        @JsonProperty("items") val items: List<Question>,
        @JsonProperty("has_more") val hasMore: Boolean,
        @JsonProperty("quota_max") val quotaMax: Int,
        @JsonProperty("quota_remaining") val quotaRemaining: Int
)