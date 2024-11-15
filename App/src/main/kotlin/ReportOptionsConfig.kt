package org.example


enum class ReportType {
    CSV, TXT, PDF, EXCEL
}

data class ReportOptionsConfig(
    val supportsHeader: Boolean,
    val supportsCalculations: Boolean,
    val supportsRowNumbers: Boolean,
    val supportsTitle: Boolean,
    val supportsSummary: Boolean,
    val supportsFormatting: Boolean
)

val reportConfigurations = mapOf(
    ReportType.CSV to ReportOptionsConfig(
        supportsHeader = true,
        supportsCalculations = true,
        supportsRowNumbers = true,
        supportsTitle = false,
        supportsSummary = false,
        supportsFormatting = false
    ),
    ReportType.TXT to ReportOptionsConfig(
        supportsHeader = true,
        supportsCalculations = true,
        supportsRowNumbers = true,
        supportsTitle = true,
        supportsSummary = true,
        supportsFormatting = false
    ),
    ReportType.PDF to ReportOptionsConfig(
        supportsHeader = true,
        supportsCalculations = true,
        supportsRowNumbers = true,
        supportsTitle = true,
        supportsSummary = true,
        supportsFormatting = true
    ),
    ReportType.EXCEL to ReportOptionsConfig(
        supportsHeader = true,
        supportsCalculations = true,
        supportsRowNumbers = true,
        supportsTitle = true,
        supportsSummary = true,
        supportsFormatting = true
    )
)
