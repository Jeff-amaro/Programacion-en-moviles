package com.tecsup.cafecampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Producto

data class DetalleConProducto(
    @Embedded val detalle: DetallePedido,
    @Relation(
        parentColumn = "productoId",
        entityColumn = "id"
    )
    val producto: Producto
)