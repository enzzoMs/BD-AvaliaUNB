package ui.screens.teachers.single.viewmodel

import data.models.*
import data.repositories.ReviewRepository
import data.repositories.TeacherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class SingleTeacherViewModel(
    private val user: UserModel,
    private val teacherModel: TeacherModel,
    private val teacherRepository: TeacherRepository,
    private val reviewRepository: ReviewRepository
) {
    private val _singleTeacherUiState = MutableStateFlow(
        SingleTeacherUiState(
            teacherModel = teacherModel,
            reviews = listOf(),
            isReviewsLoading = true
        )
    )

    val singleTeacherUiState = _singleTeacherUiState.asStateFlow()

    init {
        loadAllReviews()
    }

    private fun loadAllReviews() {
        CoroutineScope(Dispatchers.IO).launch {
            val allReviews = teacherRepository.getTeacherReviews(teacherModel).reversed()

            _singleTeacherUiState.update { singleTeacherUiState ->
                singleTeacherUiState.copy(
                    reviews = allReviews,
                    isReviewsLoading = false
                )
            }
        }
    }

    fun reviewBelongsToUser(review: ReviewModel): Boolean = review.userRegistrationNumber == user.registrationNumber

    fun updateReviewComment(newComment: String) {
        _singleTeacherUiState.update { singleTeacherUiState ->
            singleTeacherUiState.copy(
                reviewComment = newComment,
                userAlreadyMadeReview = false
            )
        }
    }

    fun publishReview(comment: String, rating: Int) {
        val reviewModel = TeacherReviewModel(
            id = UUID.randomUUID().hashCode(),
            comment = comment,
            rating = rating,
            userProfilePicture = user.profilePicture,
            userName = user.name,
            userRegistrationNumber = user.registrationNumber,
            teacherName = teacherModel.name,
            departmentCode = teacherModel.departmentCode
        )

        val insertionResult = reviewRepository.insertReview(reviewModel)

        when (insertionResult) {
            ReviewRepository.ReviewInsertionResult.ReviewAlreadyMade -> {
                _singleTeacherUiState.update { singleTeacherUiState ->
                    singleTeacherUiState.copy(
                        userAlreadyMadeReview = true
                    )
                }
            }
            ReviewRepository.ReviewInsertionResult.Success -> {
                _singleTeacherUiState.update { singleTeacherUiState ->
                    singleTeacherUiState.copy(
                        reviews = listOf(reviewModel) + singleTeacherUiState.reviews,
                        teacherModel = teacherModel.copy(
                            score = teacherRepository.getTeacherScore(
                                teacherModel.name,
                                teacherModel.departmentCode
                            )
                        ),
                        reviewComment = ""
                    )
                }
            }
        }
    }

    fun editReview(oldReviewModel: ReviewModel, newRating: Int, newComment: String) {
        val newReview = (oldReviewModel as TeacherReviewModel).copy(
            rating = newRating,
            comment = newComment
        )

        reviewRepository.updateReview(newReview)

        _singleTeacherUiState.update { singleTeacherUiState ->
            singleTeacherUiState.copy(
                reviews = singleTeacherUiState.reviews.map { if (it == oldReviewModel) newReview else it },
                teacherModel = teacherModel.copy(
                    score = teacherRepository.getTeacherScore(
                        teacherModel.name,
                        teacherModel.departmentCode
                    )
                )
            )
        }
    }

    fun deleteReview(review: TeacherReviewModel) {
        reviewRepository.deleteReview(review.id)

        _singleTeacherUiState.update { singleTeacherUiState ->
            singleTeacherUiState.copy(
                reviews = singleTeacherUiState.reviews.filter { it != review },
                teacherModel = teacherModel.copy(
                    score = teacherRepository.getTeacherScore(
                        teacherModel.name,
                        teacherModel.departmentCode
                    )
                ),
                userAlreadyMadeReview = false
            )
        }
    }

}