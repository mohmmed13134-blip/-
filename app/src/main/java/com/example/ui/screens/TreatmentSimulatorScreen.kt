package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DentalDataProvider
import com.example.data.DentalTool
import com.example.data.ProcedureStep
import com.example.data.TreatmentCase
import com.example.ui.components.ToothCrossSectionCanvas
import com.example.ui.theme.AbscessAlertRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TreatmentSimulatorScreen(
    initialCaseId: String? = null,
    modifier: Modifier = Modifier
) {
    var selectedCaseId by remember {
        mutableStateOf(initialCaseId ?: DentalDataProvider.treatmentCases.first().id)
    }

    val currentCase = remember(selectedCaseId) {
        DentalDataProvider.treatmentCases.firstOrNull { it.id == selectedCaseId }
            ?: DentalDataProvider.treatmentCases.first()
    }

    var currentStepIndex by remember(selectedCaseId) { mutableIntStateOf(0) }
    var feedbackMessage by remember(selectedCaseId) { mutableStateOf<String?>(null) }
    var isSuccessFeedback by remember(selectedCaseId) { mutableStateOf(false) }
    var isCaseCompleted by remember(selectedCaseId) { mutableStateOf(false) }

    val activeStep: ProcedureStep? = if (!isCaseCompleted) {
        currentCase.steps.getOrNull(currentStepIndex)
    } else null

    val progress = if (isCaseCompleted) 1f else (currentStepIndex.toFloat() / currentCase.steps.size)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("simulator_screen_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Case Selector Chips
        item {
            Column {
                Text(
                    text = "محاكي علاج الأسنان السريري",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "تقمص دور طبيب الأسنان واختر الأداة الحقيقية المناسبة لكل خطوة",
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
                    DentalDataProvider.treatmentCases.forEach { tc ->
                        val isSelected = tc.id == selectedCaseId
                        val title = when (tc.conditionId) {
                            "caries" -> "علاج تسوس وحشوة"
                            "abscess" -> "علاج خراج وتطهير عصب"
                            "gingivitis" -> "تنظيف جير عميق"
                            else -> "حالة سريرية"
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCaseId = tc.id
                                currentStepIndex = 0
                                feedbackMessage = null
                                isCaseCompleted = false
                            },
                            label = { Text(title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("case_chip_${tc.id}")
                        )
                    }
                }
            }
        }

        // Scenario & Progress Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sim_scenario_card"),
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
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TealPrimaryLight
                        ) {
                            Text(
                                text = "السن المستهدف: رقم ${currentCase.targetToothFdi}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = if (isCaseCompleted) "اكتمل العلاج بنجاح!" else "الخطوة ${currentStepIndex + 1} من ${currentCase.steps.size}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isCaseCompleted) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentCase.patientScenarioArabic,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (isCaseCompleted) SuccessGreen else TealPrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        // Dynamic Tooth Anatomy State View
        item {
            val visualCondition = if (isCaseCompleted) null else currentCase.conditionId
            val visualStage = if (isCaseCompleted) 1 else (currentCase.steps.size - currentStepIndex).coerceAtLeast(1)

            ToothCrossSectionCanvas(
                selectedLayer = null,
                activeConditionId = visualCondition,
                conditionStage = visualStage,
                onLayerSelected = {}
            )
        }

        // Active Step Task or Completion Celebration
        if (!isCaseCompleted && activeStep != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_step_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(TealPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${activeStep.stepIndex}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = activeStep.titleArabic,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = activeStep.descriptionArabic,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Real Instrument Tray Selector
            item {
                Column {
                    Text(
                        text = "صينية الأدوات المعقمة - اختر الأداة الصحيحة:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DentalDataProvider.dentalTools.forEach { tool ->
                            ToolInstrumentPill(
                                tool = tool,
                                onClick = {
                                    if (tool.id == activeStep.requiredToolId) {
                                        // Correct Tool!
                                        isSuccessFeedback = true
                                        feedbackMessage = "أحسنت! ${activeStep.completedMessageArabic}"

                                        if (currentStepIndex + 1 < currentCase.steps.size) {
                                            currentStepIndex++
                                        } else {
                                            isCaseCompleted = true
                                        }
                                    } else {
                                        // Incorrect Tool
                                        isSuccessFeedback = false
                                        feedbackMessage = "أداة غير مناسبة لهذه الخطوة! ${tool.arabicName} تُستخدم لـ: ${tool.primaryUsage.substringBefore("،")}. راجع المطلوب في الخطوة."
                                    }
                                }
                            )
                        }
                    }
                }
            }
        } else {
            // Case Completed Screen
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("case_completed_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SuccessGreen)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "تم علاج الحالة بنجاح وبأعلى معايير الأمان!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF14532D),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "لقد استخدمت الأدوات الحقيقية في الترتيب السريري الصحيح، وتم إنقاذ السن وتسكين ألم المريض.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF166534),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                currentStepIndex = 0
                                isCaseCompleted = false
                                feedbackMessage = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            modifier = Modifier.testTag("restart_case_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("إعادة تطبيق الحالة أو تجربة أخرى")
                        }
                    }
                }
            }
        }

        // Live Feedback Box
        if (feedbackMessage != null) {
            item {
                Surface(
                    color = if (isSuccessFeedback) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSuccessFeedback) SuccessGreen else AbscessAlertRed
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("feedback_surface")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSuccessFeedback) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (isSuccessFeedback) SuccessGreen else AbscessAlertRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = feedbackMessage!!,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSuccessFeedback) Color(0xFF14532D) else Color(0xFF991B1B),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ToolInstrumentPill(
    tool: DentalTool,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
        modifier = Modifier
            .clickable { onClick() }
            .testTag("instrument_pick_${tool.id}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                tint = TealPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = tool.arabicName.substringBefore("(").trim(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
