package di
import dagger.Component
import data.repositories.*
import data.source.DatabaseManager
import ui.screens.classes.all.viewmodel.ClassesViewModel
import ui.screens.login.viewmodel.LoginViewModel
import ui.screens.register.viewmodel.RegisterFormViewModel
import ui.screens.reports.viewmodel.ReportsViewModel
import ui.screens.splash.viewmodel.SplashViewModel
import ui.screens.subjects.all.viewmodel.SubjectsViewModel
import ui.screens.teachers.all.viewmodel.TeachersViewModel
import javax.inject.Singleton

@Singleton @Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun getDatabaseManager(): DatabaseManager

    fun getRegisterFormViewModel(): RegisterFormViewModel

    fun getLoginViewModel(): LoginViewModel

    fun getSubjectsViewModel(): SubjectsViewModel

    fun getSplashViewModel(): SplashViewModel

    fun getClassesViewModel(): ClassesViewModel

    fun getTeachersViewModel(): TeachersViewModel

    fun getReportsViewModel(): ReportsViewModel

    fun getUserRepository(): UserRepository

    fun getSubjectRepository(): SubjectRepository

    fun getClassRepository(): ClassRepository

    fun getReviewRepository(): ReviewRepository

    fun getTeacherRepository(): TeacherRepository

    fun getReportRepository(): ReportRepository
}