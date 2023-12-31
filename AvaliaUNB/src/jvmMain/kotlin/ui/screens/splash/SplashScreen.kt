package ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.source.loading.LoadingStatus
import data.source.loading.SemesterLoadingStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.components.splash.AnimatedSplashScreen
import ui.screens.splash.viewmodel.SplashViewModel
import utils.database.PrePopulatedSemester
import utils.resources.Colors
import utils.resources.Strings

const val SPLASH_ANIMATION_DURATION_MS = 2000
const val POST_DATABASE_INITIALIZATION_DELAY_MS = 700L


@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    onSplashEnd: () -> Unit
) {
    val splashUiState by splashViewModel.splashUiState.collectAsState()
    val databaseDataLoadingStatus by splashUiState.databaseLoadingStatus.collectAsState()

    var loadDatabaseVisibility by remember { mutableStateOf(false) }

    if (databaseDataLoadingStatus.finishedLoading && splashUiState.reloadDatabase) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(POST_DATABASE_INITIALIZATION_DELAY_MS)
            onSplashEnd()
        }
    }

    AnimatedSplashScreen(
        splashAnimationDurationMs = SPLASH_ANIMATION_DURATION_MS,
        targetValue = 0.9f,
        onSplashEnd = {
            if (splashUiState.reloadDatabase) {
                loadDatabaseVisibility = true
            } else {
                onSplashEnd()
            }
        },
        modifier = Modifier
            .background(Color.White),
        splashContent = { scaleModifier ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = scaleModifier
            ) {
                Image (
                    painter = painterResource("images/logo_avalia_unb.svg"),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(180.dp)
                )
                AnimatedVisibility(visible = !loadDatabaseVisibility) {
                    Text(
                        modifier = Modifier,
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Colors.UnbBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 56.sp
                                )
                            ) {
                                append(Strings.APP_TITLE_FIRST_PART)
                            }

                            withStyle(
                                style = SpanStyle(
                                    color = Colors.UnbGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 56.sp
                                )
                            ) {
                                append(Strings.APP_TITLE_SECOND_PART)
                            }
                        }
                    )
                }
                AnimatedVisibility(visible = !databaseDataLoadingStatus.finishedLoading && loadDatabaseVisibility) {
                    LoadDatabaseStatus(
                        isSchemaLoading = databaseDataLoadingStatus.schemaStatus == LoadingStatus.LOADING,
                        initializeDatabaseData = splashUiState.initializeData,
                        semestersLoadingStatus = databaseDataLoadingStatus.semestersLoadingStatus
                    )
                }
            }
        }
    )
}

@Composable
private fun LoadDatabaseStatus(
    isSchemaLoading: Boolean = false,
    initializeDatabaseData: Boolean = false,
    semestersLoadingStatus: List<SemesterLoadingStatus> = listOf()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 45.dp)
    ) {
        // Initializing database schema
        Text(
            text = Strings.DATABASE_INITIALIZING,
            style = MaterialTheme.typography.h4,
            fontSize = 26.sp,
            color = Colors.DimGray
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 20.dp)
        ) {
            Text(
                text = Strings.DATABASE_CREATING_SCHEMA,
                style = MaterialTheme.typography.h6,
                color = Colors.Gray
            )
            if (isSchemaLoading) {
                CircularProgressIndicator(
                    color = Colors.UnbGreen,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Colors.UnbGreen,
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
            }
        }

        // Initializing database data
        if (initializeDatabaseData) {
            Text(
                text = Strings.DATABASE_INITIALIZING_DATA,
                style = MaterialTheme.typography.h4,
                fontSize = 26.sp,
                color = Colors.DimGray,
                modifier = Modifier
                    .padding(top = 30.dp)
            )
            Row {
                for (loadingStatus in semestersLoadingStatus) {
                    SemesterDataLoadingStatus(
                        prePopulatedSemester = loadingStatus.prePopulatedSemester,
                        departmentStatus = loadingStatus.departmentsStatus,
                        subjectsStatus = loadingStatus.subjectsStatus,
                        classesStatus = loadingStatus.classesStatus
                    )
                }
            }
        }
    }
}

@Composable
private fun SemesterDataLoadingStatus(
    prePopulatedSemester: PrePopulatedSemester,
    departmentStatus: LoadingStatus,
    subjectsStatus: LoadingStatus,
    classesStatus: LoadingStatus
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = "${prePopulatedSemester.year}-${prePopulatedSemester.semester_number}",
            style = MaterialTheme.typography.h4,
            fontSize = 26.sp,
            color = Colors.DimGray
        )

        ItemLoadingStatus(
            itemLabel = Strings.DEPARTMENTS,
            itemStatus = departmentStatus
        )
        ItemLoadingStatus(
            itemLabel = Strings.SUBJECTS,
            itemStatus = subjectsStatus
        )
        ItemLoadingStatus(
            itemLabel = Strings.CLASSES,
            itemStatus = classesStatus
        )
    }
}

@Composable
private fun ItemLoadingStatus(
    itemLabel: String,
    itemStatus: LoadingStatus
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 20.dp)
    ) {
        Text(
            text = itemLabel,
            style = MaterialTheme.typography.h6,
            color = Colors.Gray
        )
        if (itemStatus == LoadingStatus.LOADING) {
            CircularProgressIndicator(
                color = Colors.UnbGreen,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(24.dp)
            )
        } else {
            Icon(
                imageVector = if (itemStatus == LoadingStatus.COMPLETED) Icons.Filled.Check else Icons.Outlined.Schedule,
                contentDescription = null,
                tint = if (itemStatus == LoadingStatus.COMPLETED) Colors.UnbGreen else Colors.Gray,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
        }
    }
}





