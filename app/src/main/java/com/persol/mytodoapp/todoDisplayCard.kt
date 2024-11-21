package com.persol.mytodoapp

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun TodoItemCard(
    todo: TodoItem,
    onLongPress: () -> Unit,
    onSwipeRight: () -> Unit,
    onChecked: () -> Unit,
    viewModel: TodoViewModel) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var checked by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offsetX.dp)
            .draggable(
                state = rememberDraggableState { delta ->
                    offsetX += delta
                },
                orientation = Orientation.Horizontal,
                onDragStopped = { velocity ->
                    if (velocity > 1000 || offsetX > 100) {
                        onSwipeRight()
                    }
                    offsetX = 0f
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress() }
                )
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Todo: ${todo.text}",
                    fontSize = 18.sp,
                    textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(text = "Due: ${todo.dateTime}",
                    fontSize = 14.sp,
                    textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = {
                    onChecked()
                    checked = it
                    viewModel.toggleTodo(todo)
                }
            )
        }
    }
}