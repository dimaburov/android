package com.example.firstprogramkotlin.dataroom

class RoomData(_length: Double, _wight: Double, _height:Double,
               _floorF: Boolean, _size: String, _count: Int,
               _utilsMSm: Boolean) {
     val length: Double = _length
     val wight: Double = _wight
     val height: Double = _height
     val floorF: Boolean = _floorF
     val size: String = _size
     val count_board: Int = _count
     val utilsMSm: Boolean = _utilsMSm

    //Количество рулонов обоев - один рулом 10м
    //Выбирается ширина обоев в 50см или 1м
    //Переменная utilsMSmF - true 1м false 50см
    //Считваем, что комната имеет простой вид
    fun number_of_wallpaper_rolls(): Int{
        var count_rools: Int = 0
        var utils_wallpaper:Double = 0.0

        if (utilsMSm) utils_wallpaper =  1.0
        else utils_wallpaper = 0.5
        count_rools = (Math.ceil((height * (2*length + 2*wight) / utils_wallpaper)/10)).toInt()
        return count_rools
    }

    //Расчитываем количество ламината
    fun get_count_lam(): Int{
        var square_room: Double = 0.0
        var square_board: Double = 0.0

        val l_board:Double = size.split("*")[0].toDouble()
        val w_board:Double = size.split("*")[1].toDouble()
        square_board = l_board*0.01 * w_board*0.01
        square_room = length * wight

        return (Math.ceil(Math.ceil(square_room/square_board)/count_board)).toInt()
    }
    //Расчитваем линолеума
    fun get_count_lin(): Int{
        return (Math.ceil(length * wight)).toInt()
    }
}