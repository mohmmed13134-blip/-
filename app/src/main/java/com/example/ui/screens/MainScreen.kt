package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.TealPrimaryLight

enum class DentalNavDestination(val labelArabic: String) {
    ANATOMY("هيكل الأسنان"),
    CONDITIONS("مختار الحالات"),
    TOOLS("الأدوات الحقيقية"),
    SIMULATOR("محاكي العلاج"),
    CARE_QUIZ("العناية والاختبار")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var currentDestination by remember { mutableStateOf(DentalNavDestination.ANATOMY) }
    var selectedConditionParam by remember { mutableStateOf("caries") }
    var selectedSimCaseParam by remember { mutableStateOf<String?>("case_caries") }

    // Support hardware/gesture Back button
    BackHandler(enabled = currentDestination != DentalNavDestination.ANATOMY) {
        currentDestination = DentalNavDestination.ANATOMY
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_screen_scaffold"),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(TealPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "دليل تشريح وعلاج الأسنان",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Dental Anatomy & Clinical Care",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.testTag("app_top_bar")
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = currentDestination == DentalNavDestination.ANATOMY,
                    onClick = { currentDestination = DentalNavDestination.ANATOMY },
                    icon = {
                        Icon(Icons.Default.MedicalInformation, contentDescription = "هيكل الأسنان")
                    },
                    label = { Text("الهيكل", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TealPrimary,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimaryLight
                    ),
                    modifier = Modifier.testTag("nav_anatomy")
                )

                NavigationBarItem(
                    selected = currentDestination == DentalNavDestination.CONDITIONS,
                    onClick = { currentDestination = DentalNavDestination.CONDITIONS },
                    icon = {
                        Icon(Icons.Default.Healing, contentDescription = "مختار الحالات")
                    },
                    label = { Text("الحالات", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TealPrimary,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimaryLight
                    ),
                    modifier = Modifier.testTag("nav_conditions")
                )

                NavigationBarItem(
                    selected = currentDestination == DentalNavDestination.TOOLS,
                    onClick = { currentDestination = DentalNavDestination.TOOLS },
                    icon = {
                        Icon(Icons.Default.Build, contentDescription = "الأدوات الحقيقية")
                    },
                    label = { Text("الأدوات", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TealPrimary,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimaryLight
                    ),
                    modifier = Modifier.testTag("nav_tools")
                )

                NavigationBarItem(
                    selected = currentDestination == DentalNavDestination.SIMULATOR,
                    onClick = { currentDestination = DentalNavDestination.SIMULATOR },
                    icon = {
                        Icon(Icons.Default.PlayCircle, contentDescription = "محاكي العلاج")
                    },
                    label = { Text("المحاكي", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TealPrimary,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimaryLight
                    ),
                    modifier = Modifier.testTag("nav_simulator")
                )

                NavigationBarItem(
                    selected = currentDestination == DentalNavDestination.CARE_QUIZ,
                    onClick = { currentDestination = DentalNavDestination.CARE_QUIZ },
                    icon = {
                        Icon(Icons.Default.HealthAndSafety, contentDescription = "العناية والاختبار")
                    },
                    label = { Text("العناية", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TealPrimary,
                        selectedTextColor = TealPrimary,
                        indicatorColor = TealPrimaryLight
                    ),
                    modifier = Modifier.testTag("nav_care")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentDestination) {
                DentalNavDestination.ANATOMY -> {
                    AnatomyScreen(
                        onNavigateToCondition = { condId ->
                            selectedConditionParam = condId
                            currentDestination = DentalNavDestination.CONDITIONS
                        }
                    )
                }
                DentalNavDestination.CONDITIONS -> {
                    ConditionsScreen(
                        initialConditionId = selectedConditionParam,
                        onNavigateToTool = {
                            currentDestination = DentalNavDestination.TOOLS
                        },
                        onNavigateToSimulator = { condId ->
                            selectedSimCaseParam = when (condId) {
                                "abscess" -> "case_abscess"
                                "gingivitis" -> "case_scaling"
                                else -> "case_caries"
                            }
                            currentDestination = DentalNavDestination.SIMULATOR
                        }
                    )
                }
                DentalNavDestination.TOOLS -> {
                    RealToolsScreen(
                        onToolSelectedForSim = {
                            currentDestination = DentalNavDestination.SIMULATOR
                        }
                    )
                }
                DentalNavDestination.SIMULATOR -> {
                    TreatmentSimulatorScreen(
                        initialCaseId = selectedSimCaseParam
                    )
                }
                DentalNavDestination.CARE_QUIZ -> {
                    CareAndQuizScreen()
                }
            }
        }
    }
}
