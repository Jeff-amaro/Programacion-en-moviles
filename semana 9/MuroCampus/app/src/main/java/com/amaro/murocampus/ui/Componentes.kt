
package com.amaro.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaro.murocampus.model.Post

@Composable
fun TarjetaPost(
    post: Post,
    onClick: () -> Unit = {}
) {
    val colorUsuario = when (post.userId % 5) {
        1 -> Color(0xFF287BC1)
        2 -> Color(0xFFFFB300)
        3 -> Color(0xFF388E3C)
        4 -> Color(0xFF8E24AA)
        else -> Color(0xFFE65100)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDF0F5)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = colorUsuario,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 2.dp
                    )
            ) {
                Text(
                    text = "Usuario ${post.userId}",
                    color = if (post.userId % 5 == 2)
                        Color.Black else Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = post.title,
                color = Color(0xFF202124),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = post.body.replace(Regex("\\s+"), " "),
                color = Color(0xFF626873),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun Cargando(
    mensaje: String = "Cargando publicaciones..."
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color(0xFF1565A5)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = mensaje)
        }
    }
}

@Composable
fun ErrorConReintento(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFDAD6)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mensaje,
                    color = Color(0xFF690005)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onReintentar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1565A5),
                        contentColor = Color.White
                    )
                ) {
                    Text("Reintentar")
                }
            }
        }
    }
}
