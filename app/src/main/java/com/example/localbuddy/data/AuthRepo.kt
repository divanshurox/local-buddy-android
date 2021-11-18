package com.example.localbuddy.data

import com.example.api.LocalBuddyClient
import com.example.api.models.entity.Address
import com.example.api.models.request.SigninRequest
import com.example.api.models.request.SignupSellerRequest
import com.example.api.models.request.SignupUserRequest
import com.example.api.services.LocalBuddyAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

object AuthRepo {
    object BaseRepo {
        suspend fun <T> safeApiCall(
            apiCall: suspend () -> T
        ): Resource<T> {
            return withContext(Dispatchers.IO) {
                try {
                    Resource.Success(apiCall.invoke())
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is HttpException -> {
                            Resource.Faliure(
                                false,
                                throwable.code(),
                                throwable.response()?.errorBody()
                            )
                        }
                        else -> {
                            Resource.Faliure(true, null, null)
                        }
                    }
                }
            }
        }
    }


    private val api: LocalBuddyAPI = LocalBuddyClient.publicApi

    suspend fun login(username: String, password: String) = BaseRepo.safeApiCall {
        api.signinUser(
            SigninRequest(username, password)
        )
    }

    suspend fun registerUser(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        address: Address,
        username: String,
        isSeller: Boolean = false
    ) = BaseRepo.safeApiCall {
        api.registerUser(
            SignupUserRequest(
                firstname,
                lastname,
                email,
                password,
                phone,
                address,
                username,
                isSeller
            )
        )
    }

    suspend fun registerSeller(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        phone: String,
        address: Address,
        username: String,
        gstno: String,
        shopname: String,
        isSeller: Boolean = true
    ) = BaseRepo.safeApiCall {
        api.registerSeller(
            SignupSellerRequest(
                firstname,
                lastname,
                email,
                password,
                phone,
                address,
                username,
                gstno,
                shopname,
                isSeller
            )
        )
    }

//    suspend fun login(username: String, password: String): User? {
//        val res = api.signinUser(SigninRequest(username, password))
//        LocalBuddyClient.authToken = res.body()?.token
//        return res.body()
//    }

//    suspend fun registerUser(
//        firstname: String,
//        lastname: String,
//        email: String,
//        password: String,
//        phone: String,
//        address: Address,
//        username: String,
//        isSeller: Boolean = false
//    ): User? {
//        val res = api.registerUser(
//            SignupUserRequest(
//                firstname,
//                lastname,
//                email,
//                password,
//                phone,
//                address,
//                username,
//                isSeller
//            )
//        )
//        LocalBuddyClient.authToken = res.body()?.token
//        return res.body()
//    }

//    suspend fun registerSeller(
//        firstname: String,
//        lastname: String,
//        email: String,
//        password: String,
//        phone: String,
//        address: Address,
//        username: String,
//        gstno: String,
//        shopname: String,
//        isSeller: Boolean = true
//    ): User? {
//        val res = api.registerSeller(
//            SignupSellerRequest(
//                firstname,
//                lastname,
//                email,
//                password,
//                phone,
//                address,
//                username,
//                gstno,
//                shopname,
//                isSeller
//            )
//        )
//        LocalBuddyClient.authToken = res.body()?.token
//        return res.body()
//    }

}