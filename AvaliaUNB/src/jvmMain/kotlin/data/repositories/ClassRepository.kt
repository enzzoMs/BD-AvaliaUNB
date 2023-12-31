package data.repositories

import data.models.ClassModel
import data.models.ClassReviewModel
import data.models.TeacherModel
import data.source.ClassDAO
import data.source.ReviewDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRepository @Inject constructor(
    private val classDAO: ClassDAO,
    private val reviewDAO: ReviewDAO
) {

    fun getClassTeacher(classModel: ClassModel): TeacherModel = classDAO.getClassTeacher(classModel)

    suspend fun getClassReviews(classModel: ClassModel): List<ClassReviewModel> {
        val allReviewsDeferred = CoroutineScope(Dispatchers.IO).async {
            reviewDAO.getClassReviews(classModel.id)
        }
        return allReviewsDeferred.await()
    }

    suspend fun getAllClasses(): List<ClassModel> {
        val allClassesDeferred = CoroutineScope(Dispatchers.IO).async {
            classDAO.getAllClasses()
        }
        return allClassesDeferred.await()
    }

    fun getClassScore(classId: Int): Double? = classDAO.getClassScore(classId)
}