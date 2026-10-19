package com.tecsup.cafecampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Usuario

data class PedidoConDetalle(
    @Embedded val pedido: Pedido,
    @Relation(
        entity = DetallePedido::class,
        parentColumn = "id",
        entityColumn = "pedidoId"
    )
    val detalles: List<DetalleConProducto>,
    @Relation(
        parentColumn = "clienteId",
        entityColumn = "id"
    )
    val cliente: Usuario
)