package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.DentalQuizItem
import com.example.ui.theme.AbscessAlertRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight
import kotlinx.coroutines.delay

@Composable
fun CareAndQuizScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: موقت التفريش, 1: اختبار الخرافات, 2: طوارئ الأسنان

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("care_quiz_screen_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "العناية اليومية والاختبار الطبي",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "مؤقت تفريش الأسنان المعتمد من أطباء الأسنان واختبار تصحيح المفاهيم",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .testTag("care_tab_row")
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("مؤقت التفريش (دقيقتان)", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("اختبار الخرافات", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("طوارئ وإسعافات", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Brushing Timer
                item {
                    BrushingTimerCard()
                }
            }
            1 -> {
                // Dental Quiz
                item {
                    DentalQuizSection()
                }
            }
            2 -> {
                // Dental Emergencies Protocol
                item {
                    DentalEmergencySection()
                }
            }
        }
    }
}

@Composable
private fun BrushingTimerCard() {
    var isTimerRunning by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableIntStateOf(120) } // 2 minutes total

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining--
        }
        if (secondsRemaining <= 0) {
            isTimerRunning = false
        }
    }

    val elapsedSeconds = 120 - secondsRemaining
    val currentQuadrant = when {
        elapsedSeconds < 30 -> "الربع العلوي الأيمن (الأسنان العلوية جهة اليمين)"
        elapsedSeconds < 60 -> "الربع العلوي الأيسر (الأسنان العلوية جهة اليسار)"
        elapsedSeconds < 90 -> "الربع السفلي الأيمن (الأسنان السفلية جهة اليمين)"
        else -> "الربع السفلي الأيسر (الأسنان السفلية جهة اليسار)"
    }

    val minutes = secondsRemaining / 60
    val secs = secondsRemaining % 60
    val formattedTime = "%02d:%02d".format(minutes, secs)
    val progress = (120 - secondsRemaining) / 120f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("brushing_timer_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "مؤقت تفريش الأسنان الذكي (2 Minutes)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
            )
            Text(
                text = "توصي الجمعية الأمريكية لطب الأسنان (ADA) بالتفريش لمدة دقيقتين كاملتين (30 ثانية لكل ربع من الفك)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Timer Circular Display
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(170.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(170.dp),
                    color = TealPrimary,
                    strokeWidth = 10.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedTime,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (secondsRemaining == 0) "أحسنت! ابتسامة نظيفة" else "الوقت المتبقي",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Current Quadrant Indicator
            Surface(
                color = TealPrimaryLight,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "المنطقة النشطة الآن للتفريش:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TealPrimary
                    )
                    Text(
                        text = currentQuadrant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004D40),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Control Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (secondsRemaining == 0) secondsRemaining = 120
                        isTimerRunning = !isTimerRunning
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTimerRunning) Color(0xFFD97706) else TealPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("timer_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isTimerRunning) "إيقاف مؤقت" else "ابدأ التفريش")
                }

                IconButton(
                    onClick = {
                        isTimerRunning = false
                        secondsRemaining = 120
                    },
                    modifier = Modifier.testTag("timer_reset_button")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "إعادة ضبط", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun DentalQuizSection() {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val question = DentalDataProvider.dentalQuizzes[currentQuestionIndex]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (question.isMythBuster) "تصحيح خرافة شائعة" else "معلومة سريرية",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "سؤال ${currentQuestionIndex + 1} من ${DentalDataProvider.dentalQuizzes.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = question.questionArabic,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Options
            question.optionsArabic.forEachIndexed { idx, optionText ->
                val isSelected = selectedOptionIndex == idx
                val isCorrect = idx == question.correctIndex

                val borderColor = when {
                    !isSubmitted && isSelected -> TealPrimary
                    isSubmitted && isCorrect -> SuccessGreen
                    isSubmitted && isSelected && !isCorrect -> AbscessAlertRed
                    else -> MaterialTheme.colorScheme.outlineVariant
                }

                val bgColor = when {
                    !isSubmitted && isSelected -> TealPrimaryLight.copy(alpha = 0.5f)
                    isSubmitted && isCorrect -> Color(0xFFDCFCE7)
                    isSubmitted && isSelected && !isCorrect -> Color(0xFFFEE2E2)
                    else -> MaterialTheme.colorScheme.surface
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = bgColor,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable(enabled = !isSubmitted) {
                            selectedOptionIndex = idx
                        }
                        .testTag("quiz_option_$idx")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${idx + 1}.",
                            fontWeight = FontWeight.Bold,
                            color = borderColor,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted && isCorrect) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen)
                        } else if (isSubmitted && isSelected && !isCorrect) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = AbscessAlertRed)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Submit or Next Button
            if (!isSubmitted) {
                Button(
                    onClick = {
                        if (selectedOptionIndex != null) {
                            isSubmitted = true
                            if (selectedOptionIndex == question.correctIndex) {
                                score++
                            }
                        }
                    },
                    enabled = selectedOptionIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_quiz_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("تحقق من الإجابة")
                }
            } else {
                // Explanation Box
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "الشرح الطبي التوضيحي:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = question.explanationArabic,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Button(
                    onClick = {
                        if (currentQuestionIndex + 1 < DentalDataProvider.dentalQuizzes.size) {
                            currentQuestionIndex++
                            selectedOptionIndex = null
                            isSubmitted = false
                        } else {
                            // Loop or restart
                            currentQuestionIndex = 0
                            selectedOptionIndex = null
                            isSubmitted = false
                            score = 0
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("next_quiz_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text(
                        if (currentQuestionIndex + 1 < DentalDataProvider.dentalQuizzes.size) "السؤال التالي" else "إعادة الاختبار من البداية"
                    )
                }
            }
        }
    }
}

@Composable
private fun DentalEmergencySection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        EmergencyCard(
            title = "سقوط السن بالكامل نتيجة صدمة (Tooth Avulsion)",
            urgency = "تدخل طارئ خلال 60 دقيقة!",
            steps = listOf(
                "1. امسك السن من التاج الأبيض فقط ولا تلمس الجذر نهائياً لحماية الخلايا الحية.",
                "2. لا تغسل السن بالصابون أو تفركه بفرشاة؛ إذا كان متسخاً اشطفه بماء بارد برفق لثانية واحدة.",
                "3. ضع السن فوراً في كوب حليب سائل بارد، أو محلول ملحي، أو داخل فم المصاب بجانب خده.",
                "4. توجه فوراً لأقرب عيادة أسنان؛ إعادة غرس السن خلال أقل من ساعة تنجح بنسبة تفوق 90%!"
            ),
            color = AbscessAlertRed
        )

        EmergencyCard(
            title = "ألم أسنان حاد ونابض في منتصف الليل",
            urgency = "إسعاف أولي منزلي آمن",
            steps = listOf(
                "1. تمضمض بماء دافئ لإزالة أي بقايا طعام محصورة بين الأسنان.",
                "2. مرر خيط الأسنان برفق للتأكد من خلو الفراغات من العوالق الصلبة.",
                "3. تناول مسكن ألم فموي مثل الإيبوبروفين (400 مجم) أو الباراسيتامول حسب الجرعات الدوائية.",
                "4. تحذير قاطع: لا تضع أبداً حبة أسبرين على اللثة! فهذا يسبب حرقاً كيميائياً شديداً دون علاج العصب.",
                "5. ضع كمادة باردة على الخد من الخارج لتقليل تدفق الدم والنبض، وتوجه للعيادة صباحاً."
            ),
            color = Color(0xFFD97706)
        )

        EmergencyCard(
            title = "انكسار جزء من السن أو الحشوة",
            urgency = "حماية اللسان والعصب",
            steps = listOf(
                "1. اشطف الفم بماء دافئ واحفظ الجزء المكسور في علبة نظيفة.",
                "2. إذا كانت الحافة حادة وتجرح لسانك، ضع عليها قطعة شمع تقويم أو علكة خالية من السكر مؤقتاً.",
                "3. تجنب الأطعمة الباردة والساخنة لأن العاج يكون مكشوفاً وحساساً.",
                "4. لا تستخدم أي لاصق منزلي على الإطلاق."
            ),
            color = TealPrimary
        )
    }
}

@Composable
private fun EmergencyCard(
    title: String,
    urgency: String,
    steps: List<String>,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = color.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = urgency,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = color,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            steps.forEach { step ->
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 2.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
