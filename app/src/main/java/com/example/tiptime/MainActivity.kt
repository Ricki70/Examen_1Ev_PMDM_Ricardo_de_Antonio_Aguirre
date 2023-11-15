package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.SumadorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SumadorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    SumadorLayout()
                }
            }
        }
    }
}

@Composable
fun SumadorLayout() {
    var amountInput1 by remember { mutableStateOf("") }
    var amountInput2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var history by remember { mutableStateOf(emptyList<String>()) } // Nuevo estado para el historial
    var isHistoryEmpty by remember { mutableStateOf(true) }
    val n1 = amountInput1.toDoubleOrNull() ?: 0.0
    val n2 = amountInput2.toDoubleOrNull() ?: 0.0
    calculateTip(n1, n2) // Calcular el resultado al inicio

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.titulo),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.n1,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput1,
            onValueChanged = { amountInput1 = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
        )
        EditNumberField(
            label = R.string.n2,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = amountInput2,
            onValueChanged = { amountInput2 = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
        )

        // Mostrar el botón y actualizar el resultado al hacer clic
        Button(onClick = {
            result = calculateTip(n1, n2)
            history = history + listOf("$n1 + $n2 =  $result") // Actualizar el historial
        }) {
            Text("Calcular y Mostrar Resultado")
        }

        // Mostrar el resultado solo cuando result no esté vacío
        if (result.isNotEmpty()) {
            Text(
                text = stringResource(R.string.resultado, result),
                style = MaterialTheme.typography.displaySmall
            )
        }


        // Mostrar el historial solo cuando no esté vacío
        if (history.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
            ) {
                Button(
                    onClick = {
                        history = emptyList() // Limpiar el historial
                        isHistoryEmpty = true // Marcar el historial como vacío
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Limpiar Historial")
                }
                Text(
                    "Historial:",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
                )
                Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                history.forEach { item ->
                    Text(
                        item,
                        modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions
    )
}

private fun calculateTip(n1: Double, n2: Double): String {
    var resul = n2 + n1
    return NumberFormat.getCurrencyInstance().format(resul)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    SumadorTheme {
        SumadorLayout()
    }
}
