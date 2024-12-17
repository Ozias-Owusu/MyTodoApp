package com.persol.mytodoapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AccountDetailsPage(
                     navController: NavController,
                     modifier: Modifier = Modifier
)
{
    var userName by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    //var isError by rememberSaveable { mutableStateOf(false) }

    Column (
        modifier.fillMaxSize().padding(40.dp),
        verticalArrangement = Arrangement.Center,
        Alignment.CenterHorizontally
    )
    {
        Text(
            text = "User Details",
            modifier = modifier,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier.padding(16.dp).height(300.dp).fillMaxWidth()) {
            OutlinedTextField(
                value = userName.take(35),
                onValueChange = { userName = it.take(35) },
                label = { Text("Name") },
                modifier = modifier.padding(10.dp),
                prefix = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Black
                    )
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = modifier.padding(10.dp),
                suffix = {
                    if (showPassword) {
                        IconButton(
                            onClick = {
                                showPassword = !showPassword
                            }
                        ) {
                            Icon(
                                imageVector = if (!showPassword) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                contentDescription = if (!showPassword) "Hide Pin" else "Show Pin"
                            )
                        }
                    }
                },
                readOnly = true,
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("homePage")
                },
                enabled = userName.isNotEmpty(),
                modifier = modifier.padding(10.dp).align(Alignment.CenterHorizontally),
            ) {
                Text(text = "Save")
            }

        }





    }



}