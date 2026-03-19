package com.example.calculadorajuros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadorajuros.ui.theme.CalculadoraJurosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraJurosTheme {
                App()
            }
        }
    }
}

enum class NivelAmizade(val nome: String, val juros: Int) {
    MelhorAmigo(nome = "Melhor Amigo", juros = 0),
    Amigo(nome = "Amigo",juros = 5),
    Colega(nome = "Colega", juros = 10),
    Desconhecido(nome = "Desconhecido", juros = 25)
}

@Composable
fun Juros(modifier: Modifier) {
    var valorInput by remember { mutableStateOf("") }
    var valorSlider by remember { mutableFloatStateOf(0f) }
    var arredondar by remember { mutableStateOf(true) }
    var nivelSelecionado by remember { mutableStateOf(NivelAmizade.MelhorAmigo) }

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calculadora de Juros",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = valorInput,
            onValueChange = { novoTexto ->
                valorInput = novoTexto
                val num = novoTexto.replace(",", ".").toFloatOrNull() ?: 0f
                valorSlider = num.coerceIn(0f, 1000f)
            },
            label = { Text("Dinheiro emprestado") },
            singleLine = true,
            prefix = { Text("R$ ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Slider(
            value = valorSlider,
            onValueChange = { newValue ->
                valorSlider = newValue
                valorInput = newValue.toInt().toString()
            },
            valueRange = 0f..1000f,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        )

        Text(
            text = "Quanto a pessoa é sua amiga?",
            modifier = Modifier.padding(20.dp)
        )

        NivelAmizade.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = ( nivelSelecionado == option ),
                        onClick = { nivelSelecionado = option },
                        role = Role.RadioButton
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = ( nivelSelecionado == option ),
                    onClick = null
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${option.nome} (${option.juros}%)",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }


        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Arredondar?")
            Switch(
                checked = arredondar,
                onCheckedChange = { arredondar = it },
                thumbContent =
                    if (arredondar) {
                        {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    } else null
            )

        }

        val baseCalculo = valorInput.replace(",", ".").toFloatOrNull() ?: 0f
        val valorFinal = baseCalculo * (1 + nivelSelecionado.juros / 100f)

        val resultado = if (arredondar) {
            kotlin.math.round(valorFinal).toInt()
        } else {
            valorFinal
        }

        Text("A pessoa te deve:")
        Spacer(Modifier.height(12.dp))
        Text(
            text = "R$$resultado",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun App(){
    Juros(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .wrapContentSize(Alignment.Center)
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CalculadoraJurosTheme {
        App()
    }
}