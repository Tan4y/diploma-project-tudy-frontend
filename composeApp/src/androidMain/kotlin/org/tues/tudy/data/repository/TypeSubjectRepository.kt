package org.tues.tudy.data.repository

import org.tues.tudy.data.model.TypeSubjectRequest
import org.tues.tudy.data.remote.ApiService

class TypeSubjectRepository(private val api: ApiService) {

    suspend fun getItems(userId: String, type: String) =
        api.getItems(userId, type)

    suspend fun addItem(request: TypeSubjectRequest) =
        api.addItem(request)
}