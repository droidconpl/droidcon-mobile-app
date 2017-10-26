package pl.droidcon.app.domain

data class Speaker(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val title: String,
        val description: String,
        val websiteUrl: String,
        val facebookUrl: String,
        val twitterUrl: String,
        val githubUrl: String,
        val linkedinUrl: String,
        val googlePlusUrl: String,
        val imageUrl: String
)

data class Session(
        val sessionId: Long,
        val sessionType: SessionType,
        val sessionTitle: String,
        val sessionDescription: String,
        val speakers: List<Speaker>,
        val sessionLength: Double,
        val workshopCapacity: Int
)

enum class SessionType {
    TALK,
    WORKSHOP
}