package space.bunniesin.crescent.ui.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import space.bunniesin.crescent.R
import space.bunniesin.crescent.ui.composables.PeopleListItem
import space.bunniesin.crescent.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartConversationPage(goBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.start_conversation_header)) },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(
                                R.string.ui_go_back
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        val range = 1..100
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = it,
        ) {
            items(range.count()) {
                PeopleListItem (
                    callback = {},
                    disableBottomSheet = true
                )
            }
        }
    }
}

@Preview
@Composable
fun StartConversationPagePreview() {
    RevoltTheme {
        StartConversationPage {}
    }
}
