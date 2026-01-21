package org.tues.tudy.data.repository

import org.tues.tudy.data.model.StudyTimeRequest
import org.tues.tudy.data.remote.ApiService
import java.time.LocalDate

class StudyRepository(
    private val api: ApiService
) {
    suspend fun getStudyStats() = api.getStudyStats()

    suspend fun saveStudyTime(seconds: Int) {
        println("saveStudyTime() called with seconds = $seconds")
        if (seconds <= 0){
            println("No study time to save (seconds <= 0)")
            return
        }

        val minutes = ((seconds + 59) / 60)
        println("Calculated minutes = $minutes")

        val today = LocalDate.now().toString()
        println("Date = $today")

        try {
            val response = api.addRealStudyTime(
                StudyTimeRequest(
                    minutes = minutes,
                    date = today
                )
            )
            println("API Response: $response")
        } catch (e: Exception) {
            println("ERROR in saveStudyTime: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
