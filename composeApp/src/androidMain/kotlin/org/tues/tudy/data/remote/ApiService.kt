package org.tues.tudy.data.remote

import okhttp3.ResponseBody
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.data.model.CreateAccountRequest
import org.tues.tudy.data.model.CreateEventRequest
import org.tues.tudy.data.model.CreateEventResponse
import org.tues.tudy.data.model.Event
import org.tues.tudy.data.model.GetUserResponse
import org.tues.tudy.data.model.LogInRequest
import org.tues.tudy.data.model.LoginResponse
import org.tues.tudy.data.model.RequestResetPasswordRequest
import org.tues.tudy.data.model.ResetPasswordRequest
import org.tues.tudy.data.model.StudyPlanResponse
import org.tues.tudy.data.model.StudyStatsResponse
import org.tues.tudy.data.model.StudyTimeRequest
import org.tues.tudy.data.model.StudyTimeResponse
import org.tues.tudy.data.model.TypeSubjectRequest
import org.tues.tudy.data.model.TypeSubjectResponse
import org.tues.tudy.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: CreateAccountRequest
    ): Response<ResponseBody>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LogInRequest
    ): Response<LoginResponse>

    @GET("verify-email")
    suspend fun verifyEmail(
        @Query("token") token: String
    ): Response<Unit>

    @POST("api/auth/request-reset")
    suspend fun requestResetPassword(
        @Body request: RequestResetPasswordRequest
    ): Response<ResponseBody>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResponseBody>

    @PATCH("api/users/update-username")
    suspend fun updateUsername(
        @Query("userId") userId: String,
        @Query("newUsername") newUsername: String
    ): Response<GetUserResponse>

    @GET("api/events")
    suspend fun getEvents(): Response<List<Event>>

    @GET("api/type-subject/{userId}")
    suspend fun getItems(
        @Path("userId") userId: String,
        @Query("type") type: String
    ): Response<List<TypeSubjectResponse>>

    @POST("api/type-subject")
    suspend fun addItem(
        @Body request: TypeSubjectRequest
    ): Response<TypeSubjectResponse>

    @DELETE("api/type-subject/{userId}/{id}")
    suspend fun deleteTypeSubject(
        @Path("userId") userId: String,
        @Path("id") itemId: String
    ): Response<ResponseBody>

    @POST("api/events")
    suspend fun createEvent(
        @Body request: CreateEventRequest
    ): Response<CreateEventResponse>

    @DELETE("api/events/{id}")
    suspend fun deleteEvent(
        @Path("id") eventId: String
    ): Response<ResponseBody>

    @GET("api/calendar")
    suspend fun getCalendarItems(): Response<List<CalendarItem>>

    @GET("api/study/study-plans")
    suspend fun getAllStudyPlans(): Response<List<StudyPlanResponse>>

    @GET("api/users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String
    ): Response<GetUserResponse>

    @DELETE("api/users/{id}")
    suspend fun deleteUser(
        @Path("id") userId: String
    ): Response<ResponseBody>

    @POST("api/auth/logout")
    suspend fun logout()

    @POST("api/users/study-time")
    suspend fun addRealStudyTime(
        @Body request: StudyTimeRequest
    ): StudyTimeResponse

    @GET("api/users/stats")
    suspend fun getStudyStats(): StudyStatsResponse
}