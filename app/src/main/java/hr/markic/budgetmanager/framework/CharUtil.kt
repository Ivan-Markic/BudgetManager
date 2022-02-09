package hr.markic.budgetmanager.framework

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import hr.markic.budgetmanager.repository.bills
import hr.markic.budgetmanager.repository.locations
import java.time.LocalDateTime

class CharUtil(var bcCosts: BarChart) {

    init {
        bcCosts.description.text = ""
    }

    fun fillGraphWithData(labels: Array<String>, categories: Array<String>, range: Int) {

        val clothesGroup: ArrayList<BarEntry> = ArrayList()

        val homeGroup: ArrayList<BarEntry> = ArrayList()

        val funGroup: ArrayList<BarEntry> = ArrayList()

        //Creating labels which we use on xAxis

        when (range) {


            23 -> {

                for (i in 0..range) {

                    var sumClothes = 0f
                    var sumHome = 0f
                    var sumFun = 0f

                    for (bill in bills) {

                        if (bill.time.dayOfMonth == LocalDateTime.now().dayOfMonth
                            && bill.time.hour == i
                        ) {

                            val location = locations.filter { location ->
                                location.billID == bill.billID
                            }[0]

                            when (location.colorName) {
                                categories[0] -> {
                                    sumClothes += bill.amount.toFloat()
                                }
                                categories[1] -> {
                                    sumHome += bill.amount.toFloat()
                                }
                                categories[2] -> {
                                    sumFun += bill.amount.toFloat()
                                }
                            }


                        }
                    }
                    clothesGroup.add(BarEntry(i.toFloat(), sumClothes))

                    homeGroup.add(BarEntry(i.toFloat(), sumHome))

                    funGroup.add(BarEntry(i.toFloat(), sumFun))
                }
            }

            7 -> {
                for (i in 0 until range) {

                    var sumClothes = 0f
                    var sumHome = 0f
                    var sumFun = 0f

                    for (bill in bills) {

                        if (6 - (LocalDateTime.now().dayOfMonth - bill.time.dayOfMonth) == i) {

                            val location = locations.filter { location ->
                                location.billID == bill.billID
                            }[0]

                            when (location.colorName) {
                                categories[0] -> {
                                    sumClothes += bill.amount.toFloat()
                                }
                                categories[1] -> {

                                    sumHome += bill.amount.toFloat()
                                }
                                categories[2] -> {
                                    sumFun += bill.amount.toFloat()
                                }
                            }
                        }
                    }
                    clothesGroup.add(BarEntry(i.toFloat(), sumClothes))

                    homeGroup.add(BarEntry(i.toFloat(), sumHome))

                    funGroup.add(BarEntry(i.toFloat(), sumFun))
                }
            }

            12 -> {

                for (i in 1..range) {


                    var sumClothes = 0f
                    var sumHome = 0f
                    var sumFun = 0f

                    for (bill in bills) {

                        if (bill.time.monthValue == i
                            && bill.time.year == LocalDateTime.now().year
                        ) {

                            val location = locations.filter { location ->
                                location.billID == bill.billID
                            }[0]

                            when (location.colorName) {
                                categories[0] -> {
                                    sumClothes += bill.amount.toFloat()
                                }
                                categories[1] -> {
                                    sumHome += bill.amount.toFloat()
                                }
                                categories[2] -> {
                                    sumFun += bill.amount.toFloat()
                                }
                            }
                        }

                    }
                    clothesGroup.add(BarEntry(i.toFloat(), sumClothes))

                    homeGroup.add(BarEntry(i.toFloat(), sumHome))

                    funGroup.add(BarEntry(i.toFloat(), sumFun))
                }
            }

            else -> {

                for (i in 1..range) {

                    var sumClothes = 0f
                    var sumHome = 0f
                    var sumFun = 0f


                    for (bill in bills) {

                        if (bill.time.dayOfMonth == i) {

                            val location = locations.filter { location ->
                                location.billID == bill.billID
                            }[0]

                            when (location.colorName) {
                                categories[0] -> {
                                    sumClothes += bill.amount.toFloat()
                                }
                                categories[1] -> {
                                    sumHome += bill.amount.toFloat()
                                }
                                categories[2] -> {
                                    sumFun += bill.amount.toFloat()
                                }
                            }
                        }

                    }
                    clothesGroup.add(BarEntry(i.toFloat(), sumClothes))

                    homeGroup.add(BarEntry(i.toFloat(), sumHome))

                    funGroup.add(BarEntry(i.toFloat(), sumFun))
                }
            }
        }

        // creating dataset for group1
        //Which bind data to present and label which data represent

        // creating dataset for group1

        val barDataSet2 = BarDataSet(homeGroup, categories[1])
        barDataSet2.setColors(Color.GREEN)
        barDataSet2.setDrawValues(false)

        val barDataSet3 = BarDataSet(funGroup, categories[2])
        barDataSet3.setColors(Color.RED)
        barDataSet3.setDrawValues(false)

        val barDataSet1 = BarDataSet(clothesGroup, categories[0])
        barDataSet1.setColors(Color.BLUE)
        barDataSet1.setDrawValues(false)


        //Merging datasets in one data
        //Giving size of each bar on graph width
        val data = BarData(barDataSet1, barDataSet2, barDataSet3)
        //data.barWidth = 0.22f

        //Setting our data to graph
        bcCosts.data = data

        //Formatting label for xAxis
        //Setting position where label are going to be
        //Centering labels that label is in center of all Bars on graph
        //Giving size of each label on xAxis which default is 1
        //Enabling granularity that we can use it
        bcCosts.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        bcCosts.xAxis.position = XAxis.XAxisPosition.BOTTOM
        bcCosts.xAxis.setCenterAxisLabels(true)
        //bcCosts.xAxis.granularity = 1f
        //bcCosts.xAxis.isGranularityEnabled = true

        //Setting where graph on xAxis starting
        //Setting where is maximum of xAxis
        //We Calculate it using all parameters and adding it together to
        // get full width of graph that we neeed

        bcCosts.xAxis.axisMinimum = 0f

        bcCosts.axisLeft.axisMinimum = 0f
        bcCosts.axisRight.axisMinimum = 0f


        //We setting where graph is starting
        //Setting how much space between groups we have
        //And space between bars inside group
        //bcCosts.groupBars(0f, 0.10f, 0.05f)

        //Redrawing graph with new values
        //Using if we made some changes
        bcCosts.invalidate()
    }
}