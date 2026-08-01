package space.bunniesin.crescent.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.bunniesin.crescent.R
import space.bunniesin.crescent.ui.composables.ProfileImage
import space.bunniesin.crescent.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilePage(
    goBack: () -> Unit
) {
    //TODO: Implement profile texts and images (replace these placeholders please)

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("User Profile") }, navigationIcon = {
            IconButton(onClick = { goBack() }) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.ui_go_back)
                )
            }
        })
    }) {
        Column(Modifier.padding(it)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.padding(12.dp)) {
                    Column(
                        Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .fillMaxWidth()
                            .padding(12.dp)
                            .height(128.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProfileImage(
                                url = "https://images.unsplash.com/photo-1712135596173-2bb522bcfd88?q=80&w=1538&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                                fallback = "Placeholder",
                                size = 64.dp
                            )
                            Text("Username#1234", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }

                Row(
                    Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 12.dp, bottomStart = 12.dp, topEnd = 7.dp, bottomEnd = 7.dp
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = {/* TODO */ },
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.AutoMirrored.Default.Message, "Message")
                            Text("Message")
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(7.dp),
                        onClick = {/* TODO */ },
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(painterResource(R.drawable.material_symbols_block), "Message")
                            Text("Block")
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(7.dp),
                        onClick = {/* TODO */ },
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(painterResource(R.drawable.material_symbols_person_add), "Message")
                            Text("Friend")
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 7.dp, bottomStart = 7.dp, topEnd = 12.dp, bottomEnd = 12.dp
                        ),
                        onClick = {/* TODO */ },
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(painterResource(R.drawable.material_symbols_more_horiz), "More")
                            Text("More")
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Column(
                        Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text("Bio", style = MaterialTheme.typography.headlineSmall)
                        Text("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                    }
                }
            }
        }

    }
}

@Composable
@Preview
fun UserProfilePagePreview() {
    RevoltTheme {
        UserProfilePage({})
    }
}
