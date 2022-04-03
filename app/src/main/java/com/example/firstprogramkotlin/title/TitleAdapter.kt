package com.example.firstprogramkotlin.title

import android.app.Application
import com.example.firstprogramkotlin.R
import com.example.firstprogramkotlin.database.Apartment
import android.content.res.Resources
import android.graphics.Path
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.firstprogramkotlin.database.RoomDao
import com.example.firstprogramkotlin.dataroom.DataRoomFragment
import com.example.firstprogramkotlin.dataroom.DataRoomViewModel
import com.example.firstprogramkotlin.dataroom.DataRoomViewModelFactory
import com.example.firstprogramkotlin.dataroom.RoomData

class TitleViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val res: Resources = itemView.context.resources
        private val widthLaminateLinol: TextView = itemView.findViewById(R.id.lamin_lin)
        private val lengthCountWallpaper: TextView = itemView.findViewById(R.id.length_material)
        private val WallpaperMaterial: TextView = itemView.findViewById(R.id.length_count_wallpaper)
        val deleteItemButton:ImageButton = itemView.findViewById(R.id.deleteItemButton)
        val updateItemButton:ImageButton = itemView.findViewById(R.id.updateItemButton)
        val countMaterial:TextView = itemView.findViewById(R.id.textCountMaterial)
        //Записываем в title данные
        fun bind(item: Apartment) {
            var roomObj = RoomData(item.length, item.wight, item.height, item.floorF,
                                    item.size, item.countBoard, item.utilsMSm)
            var sm_or_m = ""
            if (item.utilsMSm) sm_or_m = "м"
            else sm_or_m = "см"
            lengthCountWallpaper.text = "Высота " + item.height.toString() + " Длина " + item.length.toString() + " Ширина " + item.wight.toString()
            WallpaperMaterial.text = "Кол-во рулонов " + roomObj.number_of_wallpaper_rolls() + " ширина обоев в " + sm_or_m
            if (item.floorF) {
                widthLaminateLinol.text = "Кол-во ламината " + roomObj.get_count_lam()
            }
            else widthLaminateLinol.text = "Кол-во линолеума " + roomObj.get_count_lin()
        }

        companion object {
            fun from(parent: ViewGroup): TitleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.room_item_fragment, parent, false)
                return TitleViewHolder(view)
            }
        }
    }

    class TitleAdapter(
        private val deleteOnClickListener: (Apartment) -> Unit,
        private val editOnClickListener: (Apartment) -> Unit,
        private val moveDataMaterialBasicInMaterial: (Apartment) -> Unit
    ): RecyclerView.Adapter<TitleViewHolder>() {

        var data = listOf<Apartment>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
            var item = data[position]
            holder.bind(item)

            //test delete
            holder.deleteItemButton.setOnClickListener{deleteOnClickListener(item)}

            holder.countMaterial.text = "Кол-во материалов: " + item.countMaterial
            holder.updateItemButton.setOnClickListener {
                //Вывод редактируемых материалов
                moveDataMaterialBasicInMaterial(item)
                //Переход на страницу редактирования
                val action = R.id.action_titleFragment_to_dataRoomFragment
                it.findNavController().navigate(action)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
            return TitleViewHolder.from(parent)
        }
    }
