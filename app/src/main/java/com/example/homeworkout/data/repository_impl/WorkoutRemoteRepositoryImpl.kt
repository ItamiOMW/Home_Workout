package com.example.homeworkout.data.repository_impl

import android.app.Application
import androidx.core.net.toUri
import com.example.homeworkout.*
import com.example.homeworkout.domain.models.PlannedWorkoutModel
import com.example.homeworkout.domain.models.Response
import com.example.homeworkout.domain.models.UserInfoModel
import com.example.homeworkout.domain.models.WorkoutModel
import com.example.homeworkout.domain.repository.WorkoutRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WorkoutRemoteRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val application: Application,
) : WorkoutRepository {


    override fun getPlannedWorkoutsByDate(date: Long) =
        callbackFlow<Response<List<PlannedWorkoutModel>>> {
            send(Response.loading())

            //SNAPSHOT LISTENER FOR PLANNED WORKOUT REFERENCE
            auth.currentUser?.uid?.let {
                db.collection(USERS_PATH).document(it)
                    .collection(PLANNED_WORKOUT_PATH)
                    .addSnapshotListener { snapshot, error ->
                        trySend(Response.loading())
                        //IF SNAPSHOT IS NULL THEN HERE IS AN ERROR OCCURRED
                        if (snapshot != null) {
                            val listPlannedWorkouts = mutableListOf<PlannedWorkoutModel>()
                            snapshot.forEach { document ->
                                val plannedWorkout = document.toObject(
                                    PlannedWorkoutModel::class.java
                                )
                                //SAVE DOCUMENT ID TO THE MODEL FOR FURTHER POSSIBLE CHANGES(DELETE OR EDIT)
                                plannedWorkout.firebaseId = document.id
                                listPlannedWorkouts.add(plannedWorkout)
                            }
                            trySend(Response.success(listPlannedWorkouts.filter { model ->
                                model.date == date
                            }))
                        } else {
                            //HANDLE ERROR WHEN SNAPSHOT IS NULL
                            trySend(Response.failed(error?.message.toString()))
                        }
                    }
            } ?: send(Response.failed(application.getString(R.string.user_isnt_signed)))

            awaitClose { this.cancel() }

        }.catch {
            emit(Response.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    override fun addPlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) = flow {
        emit(Response.loading())

        //PLANNED WORKOUT REFERENCE
        val reference = auth.currentUser?.uid?.let {
            db.collection(USERS_PATH)
                .document(it)
                .collection(PLANNED_WORKOUT_PATH)
        }

        reference?.let {

            it.add(plannedWorkoutModel).await()
            emit(Response.success(true))

        } ?: emit(Response.failed(application.getString(R.string.user_isnt_signed)))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun deletePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) =
        flow {
            emit(Response.loading())

            //PLANNED WORKOUT REFERENCE
            val reference = auth.currentUser?.uid?.let {
                db.collection(USERS_PATH)
                    .document(it)
                    .collection(PLANNED_WORKOUT_PATH)
            }

            reference?.let {

                //DELETING THE DOCUMENT
                it.document(plannedWorkoutModel.firebaseId).delete().await()
                emit(Response.success(true))

            } ?: emit(Response.failed(application.getString(R.string.user_isnt_signed)))

        }.catch {
            emit(Response.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    override fun completePlannedWorkout(plannedWorkoutModel: PlannedWorkoutModel) =
        flow {
            emit(Response.loading())

            //PLANNED WORKOUT REFERENCE
            val reference = auth.currentUser?.uid?.let {
                db.collection(USERS_PATH).document(it)
                    .collection(PLANNED_WORKOUT_PATH)
            }

            reference?.let {

                //THIS METHOD INCREASES COUNT OF COMPLETED WORKOUTS
                completeWorkout(plannedWorkoutModel.workoutModel)

                //SET FIELD isCompleted to true
                it.document(plannedWorkoutModel.firebaseId)
                    .update(IS_COMPLETED_FIELD, WORKOUT_COMPLETED)
                    .await()

                emit(Response.success(true))

            } ?: emit(Response.failed(application.getString(R.string.user_isnt_signed)))


        }.catch {
            emit(Response.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    override fun getAllWorkouts() = callbackFlow<Response<List<WorkoutModel>>> {
        send(Response.loading())

        //SNAPSHOT LISTENER FOR WORKOUT REFERENCE
        db.collection(WORKOUT_PATH).addSnapshotListener { snapshot, e ->
            trySend(Response.loading())
            //IF SNAPSHOT IS NULL THEN HERE IS AN ERROR OCCURRED
            if (snapshot != null) {
                val workouts = snapshot.toObjects(WorkoutModel::class.java)
                trySend(Response.success(workouts))
            } else {
                //HANDLE ERROR WHEN SNAPSHOT IS NULL
                trySend(Response.failed(e?.message.toString()))
            }
        }

        awaitClose { this.cancel() }

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun addWorkout(workoutModel: WorkoutModel) = flow {
        emit(Response.loading())

        //WORKOUT REFERENCE
        val reference = db.collection(WORKOUT_PATH)

        //ID
        val pushId = db.collection(WORKOUT_PATH).document().id

        //SAVING IMAGE
        val imageUpload = storage.getReference(IMAGES)
            .child(pushId)
            .putFile(workoutModel.image.toUri())
            .await()

        //GETTING URL
        val url = imageUpload.metadata?.reference?.downloadUrl?.await().toString()

        //SAVING WORKOUT MODEL
        reference.add(workoutModel.copy(image = url))

        emit(Response.success(true))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun completeWorkout(workoutModel: WorkoutModel) = flow {
        emit(Response.loading())

        //COUNT OF COMPLETED WORKOUTS REFERENCE
        val reference = auth.currentUser?.uid?.let {
            db.collection(USERS_PATH).document(it)
                .collection(COUNT_OF_COMPLETED_WORKOUTS_PATH)
        }

        reference?.let {

            //GETTING CURRENT COUNT
            val count = it.document(COUNT_OF_COMPLETED_WORKOUTS_DOCUMENT).get().await()
                .get(COUNT_FIELD)
                .toString()
                .toIntOrNull() ?: ZERO_WORKOUTS_COMPLETED

            //WE COMPLETED THE WORKOUT, SO UPDATE THE COUNT BY 1
            val newCount = count + 1

            //SAVING NEW VALUE
            it.document(COUNT_OF_COMPLETED_WORKOUTS_DOCUMENT)
                .set(mapOf(COUNT_FIELD to newCount.toString())).await()

            emit(Response.success(true))

        } ?: emit(Response.failed(application.getString(R.string.user_isnt_signed)))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun getListUserInfo() = callbackFlow<Response<List<UserInfoModel>>> {
        send(Response.loading())

        //SNAPSHOT LISTENER FOR USER INFO REFERENCE
        auth.uid?.let {
            db.collection(USERS_PATH).document(it)
                .collection(USER_INF0_PATH)
                .orderBy(DATE_MILLIS)
                .addSnapshotListener { snapshot, error ->
                    trySend(Response.loading())
                    //IF SNAPSHOT IS NULL THEN HERE IS AN ERROR OCCURRED
                    if (snapshot != null) {
                        val listUserInfo = mutableListOf<UserInfoModel>()
                        snapshot.forEach { document ->
                            val userInfo = document.toObject(UserInfoModel::class.java)
                            //SAVE DOCUMENT ID TO THE MODEL FOR FURTHER POSSIBLE CHANGES(DELETE OR EDIT)
                            userInfo.firebaseId = document.id
                            listUserInfo.add(userInfo)
                        }
                        trySend(Response.success(listUserInfo))
                    } else {
                        //HANDLE ERROR WHEN SNAPSHOT IS NULL
                        trySend(Response.failed(error?.message.toString()))
                    }
                }
        } ?: send(Response.failed(application.getString(R.string.user_isnt_signed)))

        awaitClose { this.cancel() }

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun updateUserInfo(userInfoModel: UserInfoModel) = flow<Response<Boolean>> {
        emit(Response.loading())

        //USER INFO REFERENCE
        val reference = auth.currentUser?.uid?.let {
            db.collection(USERS_PATH)
                .document(it)
                .collection(USER_INF0_PATH)
        }

        reference?.let {

            //GETTING OBJECT BY DOCUMENT ID
            val oldObject = it.document(userInfoModel.firebaseId).get().await()
                .toObject(UserInfoModel::class.java)

            if (oldObject == userInfoModel) {
                emit(Response.failed(application.getString(R.string.changes_not_found)))
            } else if (oldObject?.photo != userInfoModel.photo) {
                //IF THE PHOTOS ARE NOT THE SAME THEN DELETE OLD PHOTO AND UPLOAD NEW ONE
                val imageUpload = storage.getReference(IMAGES)
                    .putFile(userInfoModel.photo.toUri()).await()

                //GETTING A NEW URL
                val newUrl = imageUpload?.metadata?.reference?.downloadUrl?.await().toString()

                //INSERT NEW OBJECT
                it.document(userInfoModel.firebaseId).update(
                    DATE_FIELD, userInfoModel.date,
                    FIREBASE_ID_FIELD, userInfoModel.firebaseId,
                    PHOTO_FIELD, newUrl,
                    WEIGHT_FIELD, userInfoModel.weight
                )
            } else {
                //IF THE PHOTO'S URLS ARE THE SAME THEN JUST SET OBJECT WITHOUT UPLOADING NEW PHOTO
                it.document(userInfoModel.firebaseId).update(
                    DATE_FIELD, userInfoModel.date,
                    FIREBASE_ID_FIELD, userInfoModel.firebaseId,
                    PHOTO_FIELD, userInfoModel.photo,
                    WEIGHT_FIELD, userInfoModel.weight
                )
            }

        } ?: emit(Response.failed(application.getString(R.string.user_isnt_signed)))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun addUserInfo(userInfoModel: UserInfoModel) = flow {
        emit(Response.loading())

        //USER INFO REFERENCE
        val reference = auth.currentUser?.uid?.let {
            db.collection(USERS_PATH)
                .document(it)
                .collection(USER_INF0_PATH)
        }

        reference?.let {

            //GETTING GENERATED ID FROM FIREBASE
            val pushId = db.collection(USERS_PATH).document().id

            //SAVE IMAGE TO FIRESTORE STORAGE
            val imageUpload = storage.getReference(IMAGES)
                .child(pushId)
                .putFile(userInfoModel.photo.toUri())
                .await()

            //GET URL AFTER UPLOADING TO FIREBASE
            val urlOfUploadedImage = imageUpload.metadata?.reference?.downloadUrl?.await()
                .toString()

            //MODEL TO BE SAVED
            val modelToSave = userInfoModel.copy(photo = urlOfUploadedImage)

            //SAVING MODEL
            it.add(modelToSave).await()
            emit(Response.success(true))

        } ?: emit(Response.failed(application.getString(R.string.user_isnt_signed)))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun deleteUserInfo(userInfoModel: UserInfoModel) = flow {
        emit(Response.loading())

        //USER INFO REFERENCE
        val reference = auth.currentUser?.uid?.let {
            db.collection(USERS_PATH)
                .document(it)
                .collection(USER_INF0_PATH)
        }

        reference?.let {

            //DELETE DOCUMENT FROM FIRESTORE (FIREBASE ID IS OBJECT'S DOCUMENT ID)
            it.document(userInfoModel.firebaseId).delete().await()

            //DELETE FILE BY URL ONLY AFTER SUCCESSFUL DELETION OF THE DOCUMENT
            storage.getReferenceFromUrl(userInfoModel.photo).delete().await()

            emit(Response.success(true))

        } ?: emit(Response.failed(application.getString(R.string.user_isnt_signed)))

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override fun getCountOfCompletedWorkouts() = callbackFlow<Response<Int>> {
        send(Response.loading())

        //SNAPSHOT LISTENER FOR COUNT OF COMPLETED WORKOUTS REFERENCE
        auth.currentUser?.uid?.let {
            db.collection(USERS_PATH).document(it)
                .collection(COUNT_OF_COMPLETED_WORKOUTS_PATH)
                .document(COUNT_OF_COMPLETED_WORKOUTS_DOCUMENT)
                .addSnapshotListener { snapshot, error ->
                    //IF SNAPSHOT IS NULL THEN HERE IS AN ERROR OCCURRED
                    if (snapshot != null) {
                        //I KEEP THE ONLY 1 DOCUMENT TO SAVE COUNT OF COMPLETED WORKOUTS
                        val count = snapshot.get(COUNT_FIELD).toString().toIntOrNull()
                            ?: ZERO_WORKOUTS_COMPLETED
                        trySend(Response.success(count))
                    } else {
                        //HANDLE ERROR WHEN SNAPSHOT IS NULL
                        trySend(Response.failed(error?.message.toString()))
                    }
                }

        } ?: trySend(Response.failed(application.getString(R.string.user_isnt_signed)))

        awaitClose { this.cancel() }

    }.catch {
        emit(Response.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}