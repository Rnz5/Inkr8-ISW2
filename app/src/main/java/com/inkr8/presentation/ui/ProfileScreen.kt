package com.inkr8.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inkr8.domain.model.User
import com.inkr8.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(user: User, viewModel: ProfileViewModel, logout: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var name by rememberSaveable(user.id) { mutableStateOf(user.name) }
    Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionCard {
            Text("Perfil", style = MaterialTheme.typography.titleLarge)
            user.email?.let { Text(it) }
            Text("Liga ${user.league} · Rating ${user.rating}")
            Text("Merit disponible: ${user.merit}")
            OutlinedTextField(value = name, onValueChange = { name = it.take(20) }, label = { Text("Nombre") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            ErrorMessage(state.error)
            if (state.saved) Text("Nombre actualizado.")
            Button(onClick = { viewModel.save(name) }, enabled = !state.saving) { Text(if (state.saving) "Guardando…" else "Guardar nombre") }
            OutlinedButton(onClick = logout) { Text("Cerrar sesión") }
        }
    }
}
