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

data class Agenda(val days: List<Day>) {
   companion object {
       val NULL_OBJECT = Agenda(emptyList())
   }
}

data class Day(val id: Long, val talkPanels: List<TalkPanel>)

data class TalkPanel(val start: String, val end: String, val talks: List<Talk>)

data class Talk(val title: String, val speakers: List<Speaker>, val session: Session?)