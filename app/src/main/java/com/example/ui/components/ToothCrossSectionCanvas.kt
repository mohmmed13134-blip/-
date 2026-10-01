package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.ToothLayerId
import com.example.ui.theme.AbscessAlertRed
import com.example.ui.theme.BoneColor
import com.example.ui.theme.CariesDecayColor
import com.example.ui.theme.DentinColor
import com.example.ui.theme.EnamelColor
import com.example.ui.theme.GumPink
import com.example.ui.theme.PulpColor
import com.example.ui.theme.TealPrimary

/**
 * رسم بياني تفاعلي لمقطع عرضي في ضرس الإنسان
 * يوضح طبقات المينا، العاج، اللب، الجذر، عظم الفك، واللثة
 */
@Composable
fun ToothCrossSectionCanvas(
    selectedLayer: ToothLayerId?,
    activeConditionId: String?,
    conditionStage: Int,
    onLayerSelected: (ToothLayerId) -> Unit,
    modifier: Modifier = Modifier
) {
    val enamelHighlight by animateColorAsState(
        targetValue = if (selectedLayer == ToothLayerId.ENAMEL) TealPrimary else Color(0xFF475569),
        animationSpec = tween(300),
        label = "enamelHighlight"
    )

    val dentinHighlight by animateColorAsState(
        targetValue = if (selectedLayer == ToothLayerId.DENTIN) TealPrimary else Color(0xFFB45309),
        animationSpec = tween(300),
        label = "dentinHighlight"
    )

    val pulpHighlight by animateColorAsState(
        targetValue = if (selectedLayer == ToothLayerId.PULP || selectedLayer == ToothLayerId.ROOT_CANAL) TealPrimary else Color(0xFF991B1B),
        animationSpec = tween(300),
        label = "pulpHighlight"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
            .testTag("tooth_cross_section_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F7F8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        // Click defaults toggle to enamel or pulp
                        if (selectedLayer == null) onLayerSelected(ToothLayerId.ENAMEL)
                    }
                    .testTag("tooth_anatomy_canvas")
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val centerX = canvasWidth / 2f

                // 1. Draw Alveolar Bone (Background bottom)
                val boneTopY = canvasHeight * 0.48f
                val bonePath = Path().apply {
                    moveTo(0f, boneTopY)
                    cubicTo(
                        canvasWidth * 0.25f, boneTopY + 15f,
                        canvasWidth * 0.75f, boneTopY + 15f,
                        canvasWidth, boneTopY
                    )
                    lineTo(canvasWidth, canvasHeight)
                    lineTo(0f, canvasHeight)
                    close()
                }
                drawPath(
                    path = bonePath,
                    color = if (selectedLayer == ToothLayerId.ALVEOLAR_BONE) BoneColor.copy(alpha = 0.9f) else Color(0xFFE2E8F0)
                )

                // 2. Draw Gum (Gingiva) on sides of tooth neck
                val gumPathLeft = Path().apply {
                    moveTo(0f, boneTopY - 20f)
                    cubicTo(
                        centerX * 0.4f, boneTopY - 25f,
                        centerX * 0.65f, boneTopY - 5f,
                        centerX * 0.70f, boneTopY + 30f
                    )
                    lineTo(0f, boneTopY + 30f)
                    close()
                }
                val gumPathRight = Path().apply {
                    moveTo(canvasWidth, boneTopY - 20f)
                    cubicTo(
                        canvasWidth - (centerX * 0.4f), boneTopY - 25f,
                        canvasWidth - (centerX * 0.65f), boneTopY - 5f,
                        canvasWidth - (centerX * 0.70f), boneTopY + 30f
                    )
                    lineTo(canvasWidth, boneTopY + 30f)
                    close()
                }
                val gumColor = if (selectedLayer == ToothLayerId.GINGIVA) GumPink else Color(0xFFF9A8D4)
                drawPath(gumPathLeft, gumColor)
                drawPath(gumPathRight, gumColor)

                // 3. Draw Tooth Root Outlines (Dentin Roots extending into bone)
                val crownTopY = canvasHeight * 0.12f
                val crownBottomY = canvasHeight * 0.46f
                val rootApexLeftY = canvasHeight * 0.90f
                val rootApexRightY = canvasHeight * 0.90f

                val rootsPath = Path().apply {
                    // Left root
                    moveTo(centerX * 0.72f, crownBottomY)
                    cubicTo(
                        centerX * 0.60f, canvasHeight * 0.62f,
                        centerX * 0.45f, canvasHeight * 0.75f,
                        centerX * 0.50f, rootApexLeftY
                    )
                    // Left apex curve
                    cubicTo(
                        centerX * 0.55f, rootApexLeftY + 8f,
                        centerX * 0.65f, rootApexLeftY + 8f,
                        centerX * 0.68f, rootApexLeftY
                    )
                    // Inner bifurcation between roots
                    cubicTo(
                        centerX * 0.75f, canvasHeight * 0.72f,
                        centerX * 0.85f, canvasHeight * 0.58f,
                        centerX, canvasHeight * 0.56f
                    )
                    // Inner right root to apex
                    cubicTo(
                        centerX * 1.15f, canvasHeight * 0.58f,
                        centerX * 1.25f, canvasHeight * 0.72f,
                        centerX * 1.32f, rootApexRightY
                    )
                    // Right apex curve
                    cubicTo(
                        centerX * 1.35f, rootApexRightY + 8f,
                        centerX * 1.45f, rootApexRightY + 8f,
                        centerX * 1.50f, rootApexRightY
                    )
                    // Right root outer edge up to crown
                    cubicTo(
                        centerX * 1.55f, canvasHeight * 0.75f,
                        centerX * 1.40f, canvasHeight * 0.62f,
                        centerX * 1.28f, crownBottomY
                    )
                    close()
                }

                // Fill Roots Dentin
                drawPath(
                    path = rootsPath,
                    color = if (selectedLayer == ToothLayerId.DENTIN) DentinColor else Color(0xFFFEF3C7)
                )
                drawPath(
                    path = rootsPath,
                    color = dentinHighlight,
                    style = Stroke(width = if (selectedLayer == ToothLayerId.DENTIN) 3f else 1.5f)
                )

                // 4. Draw Tooth Crown Enamel Outer Cap
                val crownPath = Path().apply {
                    moveTo(centerX * 0.72f, crownBottomY)
                    // Left cusp
                    cubicTo(
                        centerX * 0.48f, crownBottomY - 15f,
                        centerX * 0.46f, crownTopY + 25f,
                        centerX * 0.60f, crownTopY
                    )
                    // Left cusp tip to central groove
                    cubicTo(
                        centerX * 0.75f, crownTopY + 12f,
                        centerX * 0.88f, crownTopY + 18f,
                        centerX, crownTopY + 16f
                    )
                    // Central groove to right cusp
                    cubicTo(
                        centerX * 1.12f, crownTopY + 18f,
                        centerX * 1.25f, crownTopY + 12f,
                        centerX * 1.40f, crownTopY
                    )
                    // Right cusp to neck
                    cubicTo(
                        centerX * 1.54f, crownTopY + 25f,
                        centerX * 1.52f, crownBottomY - 15f,
                        centerX * 1.28f, crownBottomY
                    )
                    close()
                }

                // Fill Enamel
                drawPath(
                    path = crownPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            if (selectedLayer == ToothLayerId.ENAMEL) EnamelColor else Color(0xFFF1F5F9),
                            Color(0xFFE2E8F0)
                        )
                    )
                )
                drawPath(
                    path = crownPath,
                    color = enamelHighlight,
                    style = Stroke(width = if (selectedLayer == ToothLayerId.ENAMEL) 3.5f else 1.5f)
                )

                // 5. Crown Dentin Layer (Under Enamel)
                val crownDentinPath = Path().apply {
                    moveTo(centerX * 0.76f, crownBottomY)
                    cubicTo(
                        centerX * 0.58f, crownBottomY - 10f,
                        centerX * 0.56f, crownTopY + 36f,
                        centerX * 0.68f, crownTopY + 18f
                    )
                    cubicTo(
                        centerX * 0.80f, crownTopY + 28f,
                        centerX * 0.90f, crownTopY + 32f,
                        centerX, crownTopY + 30f
                    )
                    cubicTo(
                        centerX * 1.10f, crownTopY + 32f,
                        centerX * 1.20f, crownTopY + 28f,
                        centerX * 1.32f, crownTopY + 18f
                    )
                    cubicTo(
                        centerX * 1.44f, crownTopY + 36f,
                        centerX * 1.42f, crownBottomY - 10f,
                        centerX * 1.24f, crownBottomY
                    )
                    close()
                }
                drawPath(
                    path = crownDentinPath,
                    color = if (selectedLayer == ToothLayerId.DENTIN) DentinColor else Color(0xFFFDE68A)
                )

                // 6. Draw Pulp Chamber and Root Canals (Living Core)
                val pulpChamberPath = Path().apply {
                    // Pulp Horns under cusps
                    moveTo(centerX * 0.88f, crownBottomY)
                    cubicTo(
                        centerX * 0.78f, crownBottomY - 8f,
                        centerX * 0.74f, crownTopY + 50f,
                        centerX * 0.78f, crownTopY + 38f // Left pulp horn
                    )
                    cubicTo(
                        centerX * 0.84f, crownTopY + 45f,
                        centerX * 0.92f, crownTopY + 48f,
                        centerX, crownTopY + 46f
                    )
                    cubicTo(
                        centerX * 1.08f, crownTopY + 48f,
                        centerX * 1.16f, crownTopY + 45f,
                        centerX * 1.22f, crownTopY + 38f // Right pulp horn
                    )
                    cubicTo(
                        centerX * 1.26f, crownTopY + 50f,
                        centerX * 1.22f, crownBottomY - 8f,
                        centerX * 1.12f, crownBottomY
                    )
                    close()
                }

                // Root canals going down to apices
                val leftCanalPath = Path().apply {
                    moveTo(centerX * 0.88f, crownBottomY)
                    cubicTo(
                        centerX * 0.80f, canvasHeight * 0.65f,
                        centerX * 0.65f, canvasHeight * 0.78f,
                        centerX * 0.58f, rootApexLeftY
                    )
                    lineTo(centerX * 0.62f, rootApexLeftY)
                    cubicTo(
                        centerX * 0.68f, canvasHeight * 0.78f,
                        centerX * 0.83f, canvasHeight * 0.65f,
                        centerX * 0.92f, crownBottomY
                    )
                    close()
                }

                val rightCanalPath = Path().apply {
                    moveTo(centerX * 1.08f, crownBottomY)
                    cubicTo(
                        centerX * 1.17f, canvasHeight * 0.65f,
                        centerX * 1.32f, canvasHeight * 0.78f,
                        centerX * 1.38f, rootApexRightY
                    )
                    lineTo(centerX * 1.42f, rootApexRightY)
                    cubicTo(
                        centerX * 1.35f, canvasHeight * 0.78f,
                        centerX * 1.20f, canvasHeight * 0.65f,
                        centerX * 1.12f, crownBottomY
                    )
                    close()
                }

                val pulpFill = if (selectedLayer == ToothLayerId.PULP) PulpColor else Color(0xFFEF4444)
                drawPath(pulpChamberPath, pulpFill)
                drawPath(leftCanalPath, pulpFill)
                drawPath(rightCanalPath, pulpFill)
                drawPath(pulpChamberPath, pulpHighlight, style = Stroke(width = 1.5f))

                // Nerve & Blood vessel central lines
                val leftNervePath = Path().apply {
                    moveTo(centerX * 0.80f, crownTopY + 45f)
                    cubicTo(
                        centerX * 0.90f, canvasHeight * 0.52f,
                        centerX * 0.72f, canvasHeight * 0.70f,
                        centerX * 0.60f, rootApexLeftY
                    )
                }
                drawPath(leftNervePath, Color(0xFFFDE047), style = Stroke(width = 2.5f, cap = StrokeCap.Round))

                val rightNervePath = Path().apply {
                    moveTo(centerX * 1.20f, crownTopY + 45f)
                    cubicTo(
                        centerX * 1.10f, canvasHeight * 0.52f,
                        centerX * 1.28f, canvasHeight * 0.70f,
                        centerX * 1.40f, rootApexRightY
                    )
                }
                drawPath(rightNervePath, Color(0xFFFDE047), style = Stroke(width = 2.5f, cap = StrokeCap.Round))

                // 7. Active Pathology Overlays (Carries, Abscess, Gingivitis, Fracture)
                when (activeConditionId) {
                    "caries" -> {
                        // Draw cavity expanding with stage
                        val cavityRadius = when (conditionStage) {
                            1 -> 8f
                            2 -> 16f
                            3 -> 26f
                            else -> 38f
                        }
                        val cavityCenter = Offset(centerX, crownTopY + 17f)
                        drawCircle(
                            color = CariesDecayColor,
                            radius = cavityRadius,
                            center = cavityCenter
                        )
                        drawCircle(
                            color = Color(0xFF451A03),
                            radius = cavityRadius * 0.65f,
                            center = cavityCenter
                        )
                    }

                    "abscess" -> {
                        // Draw pus halo at left & right root apices
                        val abscessRadius = when (conditionStage) {
                            1 -> 14f
                            2 -> 24f
                            3 -> 34f
                            else -> 46f
                        }
                        // Left apex abscess
                        drawCircle(
                            color = AbscessAlertRed.copy(alpha = 0.55f),
                            radius = abscessRadius + 6f,
                            center = Offset(centerX * 0.60f, rootApexLeftY + 4f)
                        )
                        drawCircle(
                            color = Color(0xFFEAB308), // Pus core
                            radius = abscessRadius * 0.6f,
                            center = Offset(centerX * 0.60f, rootApexLeftY + 4f)
                        )
                        // Right apex abscess
                        drawCircle(
                            color = AbscessAlertRed.copy(alpha = 0.4f),
                            radius = abscessRadius,
                            center = Offset(centerX * 1.40f, rootApexRightY + 4f)
                        )
                    }

                    "gingivitis" -> {
                        // Inflamed red swollen gums & calculus at tooth neck
                        val calculusColor = Color(0xFF78350F)
                        drawRoundRect(
                            color = calculusColor,
                            topLeft = Offset(centerX * 0.68f, crownBottomY - 4f),
                            size = Size(14f, 10f),
                            cornerRadius = CornerRadius(4f)
                        )
                        drawRoundRect(
                            color = calculusColor,
                            topLeft = Offset(centerX * 1.24f, crownBottomY - 4f),
                            size = Size(14f, 10f),
                            cornerRadius = CornerRadius(4f)
                        )
                        // Red inflamed gum margin
                        drawCircle(
                            color = Color(0xFFDC2626).copy(alpha = 0.8f),
                            radius = 16f,
                            center = Offset(centerX * 0.65f, crownBottomY + 4f)
                        )
                        drawCircle(
                            color = Color(0xFFDC2626).copy(alpha = 0.8f),
                            radius = 16f,
                            center = Offset(centerX * 1.30f, crownBottomY + 4f)
                        )
                    }

                    "cracked_tooth" -> {
                        // Draw crack line cutting through crown
                        val crackPath = Path().apply {
                            moveTo(centerX * 0.65f, crownTopY + 2f)
                            lineTo(centerX * 0.72f, crownTopY + 25f)
                            lineTo(centerX * 0.78f, crownTopY + 45f)
                            lineTo(centerX * 0.82f, crownBottomY)
                            if (conditionStage >= 3) {
                                lineTo(centerX * 0.70f, canvasHeight * 0.70f)
                            }
                        }
                        drawPath(
                            path = crackPath,
                            color = Color(0xFF1E293B),
                            style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }
                }
            }
        }
    }
}
