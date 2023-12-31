package ui.components.cards

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.models.TeacherModel
import utils.Utils
import utils.resources.Colors
import utils.resources.Paths
import utils.resources.Strings

@Composable
fun TeacherCard(
    teacherModel: TeacherModel,
    backgroundColor: Color = Colors.White,
    rippleColor: Color = Colors.Gray,
    fieldNameTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    fieldTextStyle: TextStyle = MaterialTheme.typography.body1,
    showScore: Boolean = true,
    clickable: Boolean = false,
    onClick: (TeacherModel) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(14.dp)
            .background(backgroundColor)
            .then(
                if (clickable) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = rippleColor)
                    )
                    { onClick(teacherModel) }
                } else Modifier
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = teacherModel.profilePicture ?: Utils.getDefaultProfilePicture(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(145.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                // Teacher Name
                CardInformation(
                    fieldName = Strings.FIELD_PREFIX_NAME,
                    fieldNameTextStyle = MaterialTheme.typography.subtitle1,
                    fieldText = teacherModel.name,
                    fieldTextStyle = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                )

                // Teacher department name
                CardInformation(
                    fieldName = Strings.FIELD_PREFIX_DEPARTMENT,
                    fieldNameTextStyle = fieldNameTextStyle,
                    fieldText = teacherModel.departmentName,
                    fieldTextStyle = fieldTextStyle,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                )

                // Teacher semester
                CardInformation(
                    fieldName = Strings.FIELD_PREFIX_SEMESTER,
                    fieldNameTextStyle = fieldNameTextStyle,
                    fieldText = teacherModel.semester,
                    fieldTextStyle = fieldTextStyle,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(end = 6.dp)
            ) {
                if (showScore) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(14.dp)
                    ) {
                        Icon(
                            painter = painterResource(Paths.Images.GRADE),
                            contentDescription = null,
                            tint = if (teacherModel.score == null) Colors.LightGray else Colors.AmericanOrange,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(end = 12.dp, bottom = 5.dp)
                        )
                        if (teacherModel.score == null) {
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
                                    text = String.format("%.1f", teacherModel.score),
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                Text(
                                    text = "${teacherModel.numOfReviews} análises",
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
    }
}