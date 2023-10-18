@file:OptIn(ExperimentalMaterial3Api::class)

package net.eknath.jot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import net.eknath.jot.ui.theme.JOTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JOTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold {
                        LazyColumn(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxWidth()
                        ) {
                            item {
                                Card {
                                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                        Column(verticalArrangement = Arrangement.SpaceBetween) {
                                            Text(text = "Income: Rs. 5000/-")
                                            Text(text = "Expense: Rs. 9999/-")
                                        }
                                        Column(verticalArrangement = Arrangement.SpaceEvenly) {
                                            Text(text = "Due Today Task: 0")
                                            Text(text = "Over Due Task: 0")
                                            Text(text = "Logs: 0")
                                            Text(text = "Events: 0")
                                        }
                                    }
                                }
                            }

                            item {

                            }
                        }
                    }

                }
            }
        }
    }
}
