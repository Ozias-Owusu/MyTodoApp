package com.persol.mytodoapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.persol.mytodoapp.ViewModelProvider
import com.persol.mytodoapp.viewModels.PinScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PinMainScreen(
    navController: NavHostController,
) {
    var pin by remember{ mutableStateOf("") }
    var isPinSaved by rememberSaveable { mutableStateOf(false) }
    var isPinWrong by rememberSaveable { mutableStateOf(false) }
    val viewModel : PinScreenViewModel = viewModel(factory = ViewModelProvider.Factory)
    PinEntry(
        onSubmit = {
            if(!isPinSaved && pin.length == 4 && viewModel.checkPin(pin)) {
                navController.navigate("homePage")
                println("correct")
            } else if (!viewModel.checkPin(pin)) {
                println("incorrect")
            } else if (isPinSaved && pin.length == 4) {
                viewModel.savePin(pin)
                isPinSaved = true
                println("saved")
                navController.navigate("homePage")
            }
                   },
        pin = pin,
        onPinChange = { pin = it },
        navController,
        viewModel,
        onReset = {
            if (pin.isNotEmpty() && pin.length == 4) {
                viewModel.resetPin(pin)
                isPinSaved = false
                pin = ""
            }
        }
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PinEntry(
    onSubmit:()-> Unit,
    pin: String,
    onPinChange: (String) -> Unit,
    navController: NavHostController,
    viewModel: PinScreenViewModel,
    onReset: () -> Unit
) {
    var onNoPin by rememberSaveable { mutableStateOf(false) }
    var isPinWrong by rememberSaveable { mutableStateOf(false) }
    var isReset by rememberSaveable { mutableStateOf(false) }
    var showPin by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Welcome",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(10.dp),
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp
            )
            Text(
                text = "Enter Pin",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(10.dp))
            TextField(
                value = pin.take(4),
                onValueChange = {onPinChange(it.take(4)); isPinWrong = false},
                label = {Text(if (!isPinWrong)"Enter 4-digit Pin" else "Wrong Pin")},
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                trailingIcon = {
                    if (pin.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                showPin = !showPin
                            }
                        ) { Icon(
                            imageVector = if (!showPin) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = if (!showPin) "Hide Pin" else "Show Pin"
                        ) }
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedLabelColor = if (isPinWrong) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface,
                    focusedLabelColor = if (isPinWrong) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface,
                    unfocusedIndicatorColor = if (isPinWrong) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = if (isPinWrong) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface,
                ),
                visualTransformation  = if (showPin) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                )
            TextButton(
                onClick = {
                    if (!isReset) {
                        if (!viewModel.checkPin(pin) && pin.isNotEmpty()){
                           isPinWrong = true
                        } else if (pin.isEmpty()) {
                            onNoPin = true
                        } else {
                            onSubmit()
                        }
                    } else {
                        onReset()
                        isReset = false
                    }
                },
                enabled = pin.length == 4,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceBright,
                    disabledContentColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .padding(top = 10.dp, start = 50.dp, end = 50.dp)
                    .fillMaxWidth()
            ){
                Text(if (isReset) "Reset" else "Enter",
                    style = MaterialTheme.typography.titleLarge)
            }
            if (isPinWrong) {
                Text(
                    text = "Pin is incorrect",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            TextButton(
                onClick = {
                    isReset = !isReset
                },
                enabled = viewModel.pin != ""
            ) {
                Text(if (isReset) "Enter Pin" else "Reset Pin",
                    )
            }
        }
    }
}
