package uz.ttpu.weatherpatterns.mvp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import uz.ttpu.weatherpatterns.data.model.Weather
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.mvp.WeatherContract
import uz.ttpu.weatherpatterns.mvp.presenter.WeatherPresenter
import uz.ttpu.weatherpatterns.ui.WeatherScreen

class MvpActivity : ComponentActivity(), WeatherContract.View {
    private var status by mutableStateOf("Pick a city")
    private val presenter = WeatherPresenter(view = this, repository = WeatherModule.repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                WeatherScreen(status = status, onCitySelected = presenter::onCitySelected)
            }
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun showLoading() { status = "Loading…" }
    override fun showWeather(weather: Weather) { status = "${weather.city}: ${weather.temperatureC}°C, ${weather.condition}" }
    override fun showError(message: String) { status = "Error: $message" }
}
