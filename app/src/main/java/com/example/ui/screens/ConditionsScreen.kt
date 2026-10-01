package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DentalCondition
import com.example.data.DentalDataProvider
import com.example.ui.components.ToothCrossSectionCanvas
import com.example.ui.theme.AbscessAlertRed
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight

@Composable
fun ConditionsScreen(
    initialConditionId: String = "caries",
    onNavigateToTool: (toolId: String) -> Unit,
    onNavigateToSimulator: (conditionId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedConditionId by remember { mutableStateOf(initialConditionId) }
    var currentStage by remember { mutableIntStateOf(2) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: الطبيب والأدوات, 1: الإسعاف المنزلي والمحاذير, 2: الأعراض والمخاطر

    val currentCondition = remember(selectedConditionId) {
        DentalDataProvider.dentalConditions.firstOrNull { it.id == selectedConditionId }
            ?: DentalDataProvider.dentalConditions.first()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("conditions_screen_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Condition Selector Chips
        item {
            Column {
                Text(
                    text = "مختار الحالة ومخاطر الأسنان",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "اختر الحالة المرضية لمشاهدة تأثيرها وكيفية التعامل معها طبياً",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DentalDataProvider.dentalConditions.forEach { condition ->
                        val isSelected = condition.id == selectedConditionId
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedConditionId = condition.id
                                currentStage = 1
                            },
                            label = {
                                Text(
                                    text = condition.arabicTitle.substringBefore("(").substringBefore("و").trim(),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (condition.id == "abscess") AbscessAlertRed else TealPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("condition_chip_${condition.id}")
                        )
                    }
                }
            }
        }

        // Pathology Visual Simulation Banner (Interactive Cross Section with active condition)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("condition_visual_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentCondition.arabicTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = currentCondition.englishTitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Severity Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(currentCondition.severity.colorHex).copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(currentCondition.severity.colorHex))
                        ) {
                            Text(
                                text = currentCondition.severity.labelArabic,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(currentCondition.severity.colorHex),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Live Anatomy Canvas with Condition
                    ToothCrossSectionCanvas(
                        selectedLayer = null,
                        activeConditionId = currentCondition.id,
                        conditionStage = currentStage,
                        onLayerSelected = {}
                    )

                    // Stage Slider if multiple stages exist
                    if (currentCondition.stages.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "مرحلة التطور: المرحلة $currentStage من ${currentCondition.stages.size}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Slider(
                            value = currentStage.toFloat(),
                            onValueChange = { currentStage = it.toInt() },
                            valueRange = 1f..currentCondition.stages.size.toFloat(),
                            steps = currentCondition.stages.size - 2,
                            colors = SliderDefaults.colors(
                                thumbColor = TealPrimary,
                                activeTrackColor = TealPrimary
                            ),
                            modifier = Modifier.testTag("stage_slider")
                        )

                        val stageObj = currentCondition.stages.getOrNull(currentStage - 1)
                        if (stageObj != null) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = stageObj.titleArabic,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = stageObj.depthDescription,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "الأعراض: ${stageObj.symptomsArabic}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Educational Stages Diagram Photo (if caries or abscess)
        if (currentCondition.id == "caries" || currentCondition.id == "abscess") {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .testTag("caries_progression_image_card"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_caries_stages),
                            contentDescription = "مراحل تطور التسوس والخراج",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "مخطط طبي لمراحل نخر السن وتكون الخراج عند قمة الجذر",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(6.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Tab Row for Deep Medical Guidance
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .testTag("condition_tabs")
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("علاج الطبيب والأدوات", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("الإسعاف والمحاذير", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("الأعراض والمخاطر", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }
        }

        // Content of Selected Tab
        when (selectedTab) {
            0 -> {
                // علاج طبيب الأسنان والأدوات الحقيقية
                item {
                    DentistTreatmentCard(
                        condition = currentCondition,
                        onNavigateToSimulator = { onNavigateToSimulator(currentCondition.id) },
                        onNavigateToTool = onNavigateToTool
                    )
                }
            }
            1 -> {
                // الإسعاف المنزلي والمحاذير الخطيرة
                item {
                    FirstAidAndWarningsCard(condition = currentCondition)
                }
            }
            2 -> {
                // الأعراض السريرية والأسباب والمخاطر
                item {
                    SymptomsAndRisksCard(condition = currentCondition)
                }
            }
        }
    }
}

@Composable
private fun DentistTreatmentCard(
    condition: DentalCondition,
    onNavigateToSimulator: () -> Unit,
    onNavigateToTool: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dentist_treatment_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "بروتوكول العلاج في عيادة الأسنان خطوة بخطوة",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            condition.clinicalSteps.forEach { stepText ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(7.dp)
                            .background(TealPrimary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stepText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "الأدوات السريرية الحقيقية المستخدمة:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                condition.realToolsNeeded.forEach { toolName ->
                    Surface(
                        color = TealPrimaryLight.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Build,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = toolName,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onNavigateToSimulator,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("launch_treatment_sim_button"),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تجربة محاكاة علاج هذه الحالة بالأدوات",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FirstAidAndWarningsCard(condition: DentalCondition) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("first_aid_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // First Aid
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Healing,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "الإسعافات الأولية والتصرف المنزلي الصحيح",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF16A34A)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            condition.firstAidHomeCare.forEach { advice ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "✓",
                        color = Color(0xFF16A34A),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = advice,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Urgent Warnings (Aspirin, Popping abscess, etc.)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Dangerous,
                    contentDescription = null,
                    tint = AbscessAlertRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "محاذير خطيرة وأخطاء شائعة يجب تجنبها!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AbscessAlertRed
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            condition.whatToAvoidUrgent.forEach { warning ->
                Surface(
                    color = AbscessAlertRed.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AbscessAlertRed.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = AbscessAlertRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = warning,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SymptomsAndRisksCard(condition: DentalCondition) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("symptoms_risks_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Symptoms
            Text(
                text = "الأعراض السريرية الشائعة",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            condition.symptoms.forEach { symptom ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("•", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 6.dp))
                    Text(text = symptom, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Causes
            Text(
                text = "الأسباب وعوامل الخطورة",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD97706)
            )
            Spacer(modifier = Modifier.height(8.dp))

            condition.causes.forEach { cause ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("•", color = Color(0xFFD97706), fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 6.dp))
                    Text(text = cause, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Risks
            Text(
                text = "المخاطر والمضاعفات في حال إهمال العلاج",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AbscessAlertRed
            )
            Spacer(modifier = Modifier.height(8.dp))

            condition.risksIfNotTreated.forEach { risk ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.ReportProblem,
                        contentDescription = null,
                        tint = AbscessAlertRed,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = risk,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
