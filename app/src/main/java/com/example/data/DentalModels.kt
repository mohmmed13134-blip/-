package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AbscessAlertRed
import com.example.ui.theme.BoneColor
import com.example.ui.theme.CariesDecayColor
import com.example.ui.theme.DentinColor
import com.example.ui.theme.EnamelColor
import com.example.ui.theme.GumPink
import com.example.ui.theme.PulpColor
import com.example.ui.theme.TealPrimary

/**
 * طبقات وأجزاء السن التشريحية
 */
enum class ToothLayerId {
    ENAMEL,
    DENTIN,
    PULP,
    ROOT_CANAL,
    CEMENTUM,
    PERIODONTAL_LIGAMENT,
    ALVEOLAR_BONE,
    GINGIVA
}

data class ToothPart(
    val id: ToothLayerId,
    val arabicName: String,
    val englishName: String,
    val summary: String,
    val anatomicalRole: String,
    val vulnerability: String,
    val clinicalImportance: String,
    val careAdvice: String,
    val color: Color
)

/**
 * أسنان الفم في المخطط السني (32 سن للبالغين)
 */
enum class ToothType(val arabicName: String) {
    INCISOR("قاطع"),
    CANINE("ناب"),
    PREMOLAR("ضاحك"),
    MOLAR("ضرس"),
    WISDOM("ضرس عقل")
}

data class DentalArchTooth(
    val fdiNumber: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val type: ToothType,
    val isUpperArch: Boolean,
    val isLeft: Boolean,
    val rootsCount: Int,
    val primaryFunction: String
)

/**
 * درجات الخطورة للحالات
 */
enum class ConditionSeverity(val labelArabic: String, val colorHex: Long) {
    EARLY("مبكرة - قابلة للعكس", 0xFF16A34A),
    MODERATE("متوسطة - تحتاج حشوة", 0xFFD97706),
    ADVANCED("متقدمة - علاج عصب", 0xFFE27D60),
    CRITICAL_EMERGENCY("حرجة - خطر انتشار وخراج", 0xFFDC2626)
}

/**
 * مراحل تطور الحالة
 */
data class ConditionStage(
    val stageNumber: Int,
    val titleArabic: String,
    val depthDescription: String,
    val symptomsArabic: String,
    val visualClue: String,
    val recommendedTreatment: String
)

/**
 * الحالة المرضية السنية
 */
data class DentalCondition(
    val id: String,
    val arabicTitle: String,
    val englishTitle: String,
    val briefDescription: String,
    val severity: ConditionSeverity,
    val affectedLayers: List<ToothLayerId>,
    val symptoms: List<String>,
    val causes: List<String>,
    val risksIfNotTreated: List<String>,
    val clinicalSteps: List<String>,
    val realToolsNeeded: List<String>,
    val firstAidHomeCare: List<String>,
    val whatToAvoidUrgent: List<String>,
    val stages: List<ConditionStage>
)

/**
 * تصنيفات الأدوات السريرية الحقيقية
 */
enum class ToolCategory(val arabicTitle: String) {
    EXAMINATION("الفحص والتشخيص السريري"),
    RESTORATIVE("العلاج التحفظي وحفر الأسنان"),
    ENDODONTICS("علاج الجذور وسحب العصب"),
    PERIODONTICS("تنظيف الجير وصحة اللثة"),
    ANESTHESIA_SURGERY("التخدير والجراحة والخلع")
}

/**
 * أداة سنية حقيقية
 */
data class DentalTool(
    val id: String,
    val arabicName: String,
    val englishName: String,
    val category: ToolCategory,
    val primaryUsage: String,
    val howItWorksInClinic: String,
    val patientExperience: String, // ما يشعر به المريض
    val sterilizationAndCare: String,
    val iconKey: String,
    val funFactOrTip: String
)

/**
 * محاكاة إجراء علاجي في العيادة
 */
data class ProcedureStep(
    val stepIndex: Int,
    val titleArabic: String,
    val descriptionArabic: String,
    val requiredToolId: String,
    val actionVerbArabic: String,
    val completedMessageArabic: String
)

data class TreatmentCase(
    val id: String,
    val conditionId: String,
    val patientScenarioArabic: String,
    val targetToothFdi: Int,
    val steps: List<ProcedureStep>
)

/**
 * أسئلة اختبار المفاهيم وتصحيح الخرافات
 */
data class DentalQuizItem(
    val id: Int,
    val questionArabic: String,
    val optionsArabic: List<String>,
    val correctIndex: Int,
    val explanationArabic: String,
    val isMythBuster: Boolean = false
)
