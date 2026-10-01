package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DentalArchTooth
import com.example.data.DentalDataProvider
import com.example.data.ToothType
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DentalArchChart(
    selectedToothFdi: Int?,
    onToothSelected: (DentalArchTooth) -> Unit,
    modifier: Modifier = Modifier
) {
    val upperTeeth = DentalDataProvider.dentalArchTeeth.filter { it.isUpperArch }
    val lowerTeeth = DentalDataProvider.dentalArchTeeth.filter { !it.isUpperArch }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dental_arch_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "مخطط الأسنان السريري الكامل (FDI Dental Chart)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "اضغط على أي سن لفحصه واختيار حالته (32 سن دائم)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // الفك العلوي
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "الفك العلوي (Maxilla)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        maxItemsInEachRow = 8
                    ) {
                        upperTeeth.forEach { tooth ->
                            ToothPill(
                                tooth = tooth,
                                isSelected = tooth.fdiNumber == selectedToothFdi,
                                onClick = { onToothSelected(tooth) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // الفك السفلي
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "الفك السفلي (Mandible)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        maxItemsInEachRow = 8
                    ) {
                        lowerTeeth.forEach { tooth ->
                            ToothPill(
                                tooth = tooth,
                                isSelected = tooth.fdiNumber == selectedToothFdi,
                                onClick = { onToothSelected(tooth) }
                            )
                        }
                    }
                }
            }

            // الدليل اللوني لأنواع الأسنان
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ToothTypeIndicator("قواطع", Color(0xFF38BDF8))
                ToothTypeIndicator("أنياب", Color(0xFFFBBF24))
                ToothTypeIndicator("ضواحك", Color(0xFF34D399))
                ToothTypeIndicator("أضراس", Color(0xFFA78BFA))
                ToothTypeIndicator("عقل", Color(0xFFF87171))
            }
        }
    }
}

@Composable
private fun ToothPill(
    tooth: DentalArchTooth,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val toothColor = when (tooth.type) {
        ToothType.INCISOR -> Color(0xFF38BDF8)
        ToothType.CANINE -> Color(0xFFFBBF24)
        ToothType.PREMOLAR -> Color(0xFF34D399)
        ToothType.MOLAR -> Color(0xFFA78BFA)
        ToothType.WISDOM -> Color(0xFFF87171)
    }

    Box(
        modifier = Modifier
            .padding(2.5.dp)
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) TealPrimary else toothColor.copy(alpha = 0.18f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) TealPrimary else toothColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .testTag("tooth_button_${tooth.fdiNumber}"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tooth.fdiNumber.toString(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ToothTypeIndicator(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
