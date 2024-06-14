/*
 *  MIT License
 *
 *  Copyright (c) 2023 Denis Kholodenin, hi.cosmonaut@gmail.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package hi.cosmonaut.hourly.ui.compose.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hi.cosmonaut.hourly.R

object Common {
    @Composable
    fun NoticeCard(
        imageVector: ImageVector,
        text: String,
        containerColor: Color,
        contentColor: Color,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = imageVector,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = contentColor
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = text,
                    color = contentColor
                )
            }
        }
    }

    @Composable
    fun PermissionCard(
        imageVector: ImageVector,
        text: String,
        containerColor: Color,
        contentColor: Color,
        onGrantClicked: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = imageVector,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = contentColor
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = text,
                    color = contentColor,
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )
                TextButton(onClick = onGrantClicked) {
                    Text(
                        text = stringResource(id = R.string.label_grant),
                        color = contentColor
                    )
                }
            }
        }
    }

    @Composable
    fun TimeColumn(modifier: Modifier = Modifier, titleResId: Int, hours: Int, minutes: Int) {
        Column(
            modifier = modifier,
        ) {
            Text(
                stringResource(id = titleResId),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
            Text(
                stringResource(
                    id = R.string.label_HH_mm,
                    hours,
                    minutes
                ),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
            )
        }
    }

    @Composable
    fun VerticalSpacer(value: Dp){
        Spacer(modifier = Modifier.height(value))
    }

    @Composable
    fun HorizontalSpacer(value: Dp){
        Spacer(modifier = Modifier.width(value))
    }

    @Composable
    fun TimeOutlinedTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit = {},
        enabled: Boolean = false,
        @StringRes labelResId: Int,
        @DrawableRes leadingIconResId: Int,
        @DrawableRes trailingIconResId: Int,
        colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick: () -> Unit,
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick()
                },
            enabled = enabled,
            label = {
                Text(
                    text = stringResource(id = labelResId),
                )
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = leadingIconResId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            },
            trailingIcon = {
                Image(
                    painter = painterResource(id = trailingIconResId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            },
            colors = colors,
            value = value,
            onValueChange = onValueChange
        )
    }

    @Composable
    fun ApplyButton(
        onClick: () -> Unit
    ){
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(16.dp, 0.dp, 24.dp, 0.dp)
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Filled.Done,
                contentDescription = ""
            )
            HorizontalSpacer(8.dp)
            Text(
                text = stringResource(id = R.string.label_apply),
            )
        }
    }

    @Composable
    fun DiscardButton(
        onClick: () -> Unit
    ){
        TextButton(
            onClick = onClick
        ) {
            Text(
                text = stringResource(id = R.string.label_discard),
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun PermissionCardPreview() {
    Common.PermissionCard(
        imageVector = Icons.Filled.Warning,
        text = stringResource(id = R.string.text_grant_permission_notification),
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.error,
        onGrantClicked = {}
    )
}