package com.example.weatherapp2.ui.inputCities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp2.databinding.FragmentInputCitiesBinding
import com.example.weatherapp2.model.db.DataBase
import com.example.weatherapp2.model.repository.LocalRepoImpl

class InputCitiesFragment : Fragment() {

    private lateinit var fragmentInputCitiesBinding: FragmentInputCitiesBinding
    private lateinit var inputCitiesModel: InputCitiesModel
    private lateinit var inputCitiesModelFactory: InputCitiesModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentInputCitiesBinding = FragmentInputCitiesBinding.inflate(inflater, container, false)
        val dataBase = DataBase.getDataBase(this.context!!)!!
        val localDao = dataBase.localDao()
        val localRepo = LocalRepoImpl(localDao)
        inputCitiesModelFactory = InputCitiesModelFactory(localRepo)
        inputCitiesModel = ViewModelProvider(this, inputCitiesModelFactory).get(
            InputCitiesModel::class.java
        )
        return fragmentInputCitiesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentInputCitiesBinding.testButton.setOnClickListener {
            inputCitiesModel.addTwoCities()
        }
    }
}
