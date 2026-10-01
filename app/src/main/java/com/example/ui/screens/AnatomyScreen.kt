package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DentalArchTooth
import com.example.data.DentalDataProvider
import com.example.data.ToothLayerId
import com.example.data.ToothPart
import com.example.ui.components.DentalArchChart
import com.example.ui.components.ToothCrossSectionCanvas
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnatomyScreen(
    onNavigateToCondition: (conditionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLayerId by remember { mutableStateOf(ToothLayerId.ENAMEL) }
    var selectedTooth by remember { mutableStateOf<DentalArchTooth?>(null) }
    var showToothSheet by remember { mutableStateOf(false) }

    val activeLayer = remember(selectedLayerId) {
        DentalDataProvider.toothParts.firstOrNull { it.id == selectedLayerId }
            ?: DentalDataProvider.toothParts.first()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("anatomy_screen_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card with Image
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .testTag("anatomy_hero_card"),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_dental_hero),
                        contentDescription = "هيكل وتشريح السن",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.75f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "تشريح هيكل الأسنان البشري",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "استكشف طبقات السن التفاعلية ومخطط الأسنان 32 سناً",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Tooth Cross Section Interactive Canvas
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مقطع عرضي تفاعلي في الضرس",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "اضغط على الطبقات للفحص",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                ToothCrossSectionCanvas(
                    selectedLayer = selectedLayerId,
                    activeConditionId = null,
                    conditionStage = 1,
                    onLayerSelected = { selectedLayerId = it }
                )
            }
        }

        // Layer Selection Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DentalDataProvider.toothParts.forEach { part ->
                    val isSelected = part.id == selectedLayerId
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedLayerId = part.id },
                        label = {
                            Text(
                                text = part.arabicName.substringBefore("(").trim(),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TealPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("layer_chip_${part.id.name}")
                    )
                }
            }
        }

        // Active Layer Details Card
        item {
            LayerDetailCard(
                part = activeLayer,
                onExploreConditions = {
                    when (activeLayer.id) {
                        ToothLayerId.ENAMEL, ToothLayerId.DENTIN -> onNavigateToCondition("caries")
                        ToothLayerId.PULP, ToothLayerId.ROOT_CANAL, ToothLayerId.ALVEOLAR_BONE -> onNavigateToCondition("abscess")
                        ToothLayerId.GINGIVA, ToothLayerId.PERIODONTAL_LIGAMENT -> onNavigateToCondition("gingivitis")
                        ToothLayerId.CEMENTUM -> onNavigateToCondition("enamel_erosion")
                    }
                }
            )
        }

        // Dental Arch Chart (32 Teeth)
        item {
            DentalArchChart(
                selectedToothFdi = selectedTooth?.fdiNumber,
                onToothSelected = { tooth ->
                    selectedTooth = tooth
                    showToothSheet = true
                }
            )
        }
    }

    // Modal Sheet for Selected Tooth
    if (showToothSheet && selectedTooth != null) {
        val tooth = selectedTooth!!
        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = { showToothSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .testTag("tooth_info_sheet")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(TealPrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tooth.fdiNumber.toString(),
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = tooth.nameArabic,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = tooth.nameEnglish,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "الوظيفة التشريحية: ${tooth.primaryFunction}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "عدد الجذور: ${tooth.rootsCount} • النوع: ${tooth.type.arabicName} • الفك: ${if (tooth.isUpperArch) "العلوي" else "السفلي"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            showToothSheet = false
                            if (tooth.type == com.example.data.ToothType.WISDOM) {
                                onNavigateToCondition("impacted_wisdom")
                            } else {
                                onNavigateToCondition("caries")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("simulate_condition_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                    ) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("محاكاة تسوس أو خراج")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun LayerDetailCard(
    part: ToothPart,
    onExploreConditions: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("layer_detail_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = part.arabicName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = part.englishName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(part.color, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = part.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Role
            InfoSection(
                icon = Icons.Default.Info,
                title = "الدور التشريحي في الفم",
                content = part.anatomicalRole,
                tint = TealPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Vulnerability
            InfoSection(
                icon = Icons.Default.Warning,
                title = "نقاط الضعف وسرعة التلف",
                content = part.vulnerability,
                tint = Color(0xFFD97706)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Clinical Importance
            InfoSection(
                icon = Icons.Default.MedicalServices,
                title = "الأهمية السريرية في العيادة",
                content = part.clinicalImportance,
                tint = Color(0xFF2563EB)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Care Advice
            InfoSection(
                icon = Icons.Default.Shield,
                title = "نصيحة الحماية والوقاية",
                content = part.careAdvice,
                tint = Color(0xFF16A34A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onExploreConditions,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("explore_layer_conditions_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "عرض الحالات والمخاطر التي تهدد هذا الجزء",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun InfoSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    tint: Color
) {
    Surface(
        color = tint.copy(alpha = 0.08f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = tint
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
