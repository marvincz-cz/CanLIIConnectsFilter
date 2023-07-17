package cz.marvincz.canlii.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import cz.marvincz.canlii.Link
import cz.marvincz.canlii.Summary

@Preview
@Composable
private fun ItemPreview() {
    AppTheme {
        Item(
            summary = Summary(
                title = Link("COURT OF APPEAL SUMMARIES (July 3- July 7, 2023)", ""),
                case = Link("Ahluwalia v. Ahluwalia, 2023 ONCA 476 (CanLII)", ""),
                author = Link("John Polyzogopoulos and Ines Ferreira", ""),
                publisher = Link("Blaney McMurtry LLP", ""),
                concurs = 0,
                date = "Jul 11, 2023",
            ),
            onBlock = {},
        )
    }
}