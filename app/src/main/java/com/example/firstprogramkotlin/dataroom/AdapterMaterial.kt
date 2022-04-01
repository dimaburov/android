package com.example.firstprogramkotlin.dataroom

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.firstprogramkotlin.R
import com.example.firstprogramkotlin.database.Apartment
import com.example.firstprogramkotlin.database.Material

class MaterialViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val res: Resources = itemView.context.resources
    private val nameMaterial: TextView = itemView.findViewById(R.id.textNameMaterial)
    private val utilsAndCountMaterial: TextView = itemView.findViewById(R.id.textCountUtilsMaterial)
    val buttonDeleteItem: ImageButton = itemView.findViewById(R.id.deleteMaterialItemButton)
    fun bind(item: Material) {
        nameMaterial.text = item.materialName.toString()
        utilsAndCountMaterial.text = "Еденицы измерения " + item.materialUtils.toString() + " кол-во " + item.materialCount.toString()
    }

    companion object {
        fun from(parent: ViewGroup): MaterialViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.fragment_material_item, parent, false)
            return MaterialViewHolder(view)
        }
    }
}

class AdapterMaterial(
    private val deleteOnClickListener: (Material) -> Unit
): RecyclerView.Adapter<MaterialViewHolder>() {

    var data = listOf<Material>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        var item = data[position]
        holder.bind(item)

        //test delete
        holder.buttonDeleteItem.setOnClickListener{deleteOnClickListener(item)}

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        return MaterialViewHolder.from(parent)
    }
}