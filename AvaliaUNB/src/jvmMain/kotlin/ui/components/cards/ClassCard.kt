package ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.ClassModel
import utils.resources.Colors
import utils.resources.Paths
import utils.resources.Strings

@Composable
fun ClassCard(
    classModel: ClassModel,
    backgroundColor: Color = Colors.White,
    rippleColor: Color = Colors.Gray,
    subjectTitleTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    fieldNameTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    fieldTextStyle: TextStyle = MaterialTheme.typography.body1,
    showScore: Boolean = true,
    clickable: Boolean = false,
    onClick: (ClassModel) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(bottom = 14.dp)
            .clip(RoundedCornerShape(10.dp))
            .then(
                if (clickable) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = rippleColor)
                    )
                    { onClick(classModel) }
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .padding(14.dp)
        ) {
            // Class name, subject code and number of hours
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = classModel.subjectName,
                    style = subjectTitleTextStyle,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 50))
                        .background(classModel.departmentColor)
                ) {
                    Text(
                        text = "Turma ${classModel.code}",
                        style = subjectTitleTextStyle,
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }

            }

            // Class department name
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_DEPARTMENT,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = classModel.departmentName,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class schedule
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_SCHEDULE,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = classModel.schedule ?: Strings.DEFAULT_CLASS_SCHEDULE,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class location
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_LOCATION,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = classModel.location,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class teacher
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_TEACHER,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = classModel.teacherName,
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class filled seats
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_FILLED_SEATS,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = "${classModel.filledSeats}/${classModel.totalSeats}",
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )

            // Class semester
            CardInformation(
                fieldName = Strings.FIELD_PREFIX_SEMESTER,
                fieldNameTextStyle = fieldNameTextStyle,
                fieldText = "${classModel.semester.year}.${classModel.semester.semesterNumber}",
                fieldTextStyle = fieldTextStyle,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
        }

        if (showScore) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(14.dp)
            ) {
                Icon(
                    painter = painterResource(Paths.Images.GRADE),
                    contentDescription = null,
                    tint = if (classModel.score == null) Colors.LightGray else Colors.AmericanOrange,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 12.dp, bottom = 5.dp)
                )
                if (classModel.score == null) {
                    Text(
                        text = Strings.NO_REVIEW_MULTILINE,
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.subtitle2.fontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = Colors.Gray
                        )
                    )
                } else {
                    Column {
                        Text(
                            text = String.format("%.1f", classModel.score),
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                        )
                        Text(
                            text = "${classModel.numOfReviews} análises",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }

    }
}